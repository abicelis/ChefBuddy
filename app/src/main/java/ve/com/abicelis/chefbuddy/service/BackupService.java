package ve.com.abicelis.chefbuddy.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.util.BackupUtil;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;

/**
 * Created by abicelis on 2/8/2017.
 */

public class BackupService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //CONSTS
    private static final String TAG = BackupService.class.getSimpleName();


    //DATA
    GoogleApiClient mGoogleApiClient;
    private String mZipFilePath;

    @Inject
    ChefBuddyDAO mDao;


    @Override
    public void onCreate() {
        super.onCreate();

        //Check for permissions
        if(!(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            publishResult(Message.PERMISSIONS_WRITE_STORAGE_NOT_GRANTED);
           Log.e(TAG, Message.PERMISSIONS_WRITE_STORAGE_NOT_GRANTED.getFriendlyName());
            stopSelf();
        } else {
            //Code called only once during lifecycle of service
            ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);

            handleLocalBackupCreation();
        }



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;

        //Code called on every onStartService() call.
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // stop GoogleApiClient
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }




    /* Local backup handling */

    private void handleLocalBackupCreation() {

        //Handle all processes in a different thread, since this service runs on the main UI thread
        new Thread(){
            @Override
            public void run() {


                try {

                    //Get the backup dir
                    File backupDir = BackupUtil.getBackupDirCreateIfNotExists();
                    if (backupDir == null) {
                        publishResult(Message.ERROR_CREATING_BACKUP_DIRECTORY);
                       Log.e(TAG, Message.ERROR_CREATING_BACKUP_DIRECTORY.getFriendlyName());
                        stopSelf();
                        return;
                    }


                    //Get the amount of recipes stored in DB
                    int recipeCount;
                    try {
                        recipeCount = mDao.getRecipeCount();
                    }catch (CouldNotGetDataException e) {
                        publishResult(Message.ERROR_LOADING_RECIPES);
                       Log.e(TAG, Message.ERROR_LOADING_RECIPES.getFriendlyName() + ". Exception: "+ e.getMessage() + ". StackTrace: " + Log.getStackTraceString(e));
                        stopSelf();
                        return;
                    }

                    //Count the list of images in app
                    int imageCount = FileUtil.getImageFilesDir().listFiles().length;

                    //Generate zipFilePath
                    mZipFilePath = BackupUtil.generateBackupFilePath(recipeCount, imageCount);

                    //Create the backup zip file
                    try {
                        BackupUtil.createZipBackup(mZipFilePath);
                    } catch (IOException e) {
                        publishResult(Message.ERROR_CREATING_ZIP_FILE);
                       Log.e(TAG, Message.ERROR_CREATING_ZIP_FILE.getFriendlyName() + ". Exception: "+ e.getMessage() + ". StackTrace: " + Log.getStackTraceString(e));
                        stopSelf();
                        return;
                    }

                    //Delete old local backups if exceeding BACKUP_SERVICE_MAXIMUM_BACKUPS
                    BackupUtil.deleteOldLocalBackups();


                } catch (Exception e) {
                    publishResult(Message.ERROR_UNKNOWN_CREATING_BACKUP);
                   Log.e(TAG, Message.ERROR_UNKNOWN_CREATING_BACKUP.getFriendlyName() + ". Exception: "+ e.getMessage() + ". StackTrace: " + Log.getStackTraceString(e));
                    stopSelf();
                    return;
                }




                //Check if GoogleDrive backup is enabled
                if(SharedPreferenceUtil.getGoogleDriveBackupEnabled())
                    handleGoogleDriveBackupCreation();
                else {
                    //All good!
                    publishResult(Message.SUCCESS_CREATING_LOCAL_BACKUP);
                    Log.d(TAG, Message.SUCCESS_CREATING_LOCAL_BACKUP.getFriendlyName());
                    //We're done here
                    stopSelf();
                }


            }
        }.start();

    }










    /* Google drive backup handling */

    private void handleGoogleDriveBackupCreation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        publishResult(Message.ERROR_DRIVE_CREATING_FILE_CONTENTS);
                       Log.e(TAG, Message.ERROR_DRIVE_CREATING_FILE_CONTENTS.getFriendlyName() + ". Result message: "+ result.getStatus().getStatusMessage());
                        stopSelf();
                        return;
                    }

                    //Open an output stream from the google drive contents
                    OutputStream outputStream = result.getDriveContents().getOutputStream();

                    //Get backup file, extension and mimeType
                    File backupFile = new File(mZipFilePath);
                    String extension = backupFile.getName().substring(backupFile.getName().lastIndexOf('.') + 1).toLowerCase();
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                    //Write the backupFile into the google drive stream
                    try {
                        FileInputStream fileInputStream = new FileInputStream(backupFile);
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e ) {
                        publishResult(Message.ERROR_DRIVE_WRITING_FILE_CONTENTS);
                       Log.e(TAG, Message.ERROR_DRIVE_WRITING_FILE_CONTENTS.getFriendlyName() + ". Exception: "+ e.getMessage() + ". StackTrace: " + Log.getStackTraceString(e));

                        stopSelf();
                        return;
                    }


                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(backupFile.getName())
                            .setMimeType(mimeType)
                            .build();

                    Drive.DriveApi.getAppFolder(mGoogleApiClient)
                    //Drive.DriveApi.getRootFolder(mGoogleApiClient)
                            .createFile(mGoogleApiClient, changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        publishResult(Message.ERROR_DRIVE_UPLOADING_BACKUP);
                       Log.e(TAG, Message.ERROR_DRIVE_UPLOADING_BACKUP.getFriendlyName() + ". Result message: "+ result.getStatus().getStatusMessage());

                        stopSelf();
                        return;
                    }


                    //Drive.DriveApi.getAppFolder(mGoogleApiClient)
//                    Drive.DriveApi.getRootFolder(mGoogleApiClient)
//                            .listChildren(mGoogleApiClient)
//                            .setResultCallback(listSortedBackupFilesResult);


                    //Order backups by modified date, so oldest first.
                    SortOrder sortOrder = new SortOrder.Builder()
                            .addSortAscending(SortableField.MODIFIED_DATE).build();

                    //Build a query, with no filters and just a sortOrder
                    Query query = new Query.Builder().setSortOrder(sortOrder).build();

                    Drive.DriveApi.getAppFolder(mGoogleApiClient)
                    //Drive.DriveApi.getRootFolder(mGoogleApiClient)
                            .queryChildren(mGoogleApiClient, query)
                            .setResultCallback(listSortedBackupFilesResult);

                }
            };

    final private ResultCallback<DriveApi.MetadataBufferResult> listSortedBackupFilesResult = new ResultCallback<DriveApi.MetadataBufferResult>() {
        @Override
        public void onResult(final @NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
            if (!metadataBufferResult.getStatus().isSuccess()) {
                publishResult(Message.ERROR_DRIVE_LISTING_FILES);
               Log.e(TAG, Message.ERROR_DRIVE_LISTING_FILES.getFriendlyName() + ". Result message: "+ metadataBufferResult.getStatus().getStatusMessage());
                stopSelf();
                return;
            }


            new Thread() {
                @Override
                public void run() {
                    super.run();

                    int fileCount = metadataBufferResult.getMetadataBuffer().getCount();
                    int aux = fileCount - Constants.BACKUP_SERVICE_MAXIMUM_BACKUPS;
                    for(int i = 0 ; i < aux ; i++ ) {
                        Metadata metadata = metadataBufferResult.getMetadataBuffer().get(i);
                        Status deleteStatus = metadata.getDriveId().asDriveResource().delete(mGoogleApiClient).await();

                        if(!deleteStatus.isSuccess()) {
                            publishResult(Message.ERROR_DRIVE_DELETING_OLD_BACKUPS);
                           Log.e(TAG, Message.ERROR_DRIVE_DELETING_OLD_BACKUPS.getFriendlyName() + ". Result message: "+ metadataBufferResult.getStatus().getStatusMessage());
                            stopSelf();
                            return;
                        }
                    }

                    //All good!
                    publishResult(Message.SUCCESS_CREATING_LOCAL_AND_GOOGLE_DRIVE_BACKUP);
                    Log.d(TAG, Message.SUCCESS_CREATING_LOCAL_AND_GOOGLE_DRIVE_BACKUP.getFriendlyName());
                    //We're done here
                    stopSelf();
                }
            }.start();


        }
    };







    /* Google API callbacks */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended()");
        //Called when for instance Google Play services is Force Stopped, in which case reconnection is automatic
        //Or if user uninstalls Google Play Services (WHY U DO DIS?), in which case onConnectionFailed() gets called seconds after
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        publishResult(Message.ERROR_CONNECTING_GOOGLE_API);
        Log.d(TAG, Message.ERROR_CONNECTING_GOOGLE_API.getFriendlyName() + ". Result message: "+ connectionResult.getErrorMessage());
        stopSelf();
        return;
    }








    private void publishResult(Message message) {
        Intent intent = new Intent(Constants.BACKUP_SERVICE_BROADCAST_BACKUP_PROGRESS);
        intent.putExtra(Constants.BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_MESSAGE, message);


        //Using LocalBroadcastManager instead of sendBroadcast() because...
        //LocalBroadcastManager is a helper class to register for and send broadcasts of Intents to local objects
        // within your process. This approach improves security as the broadcast events are only
        // visible within your process and is faster than using standard events.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        //sendBroadcast(intent);
    }






    /* Zip related methods */


}
