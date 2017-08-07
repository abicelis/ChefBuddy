package ve.com.abicelis.chefbuddy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;

import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;

/**
 * Created by abicelis on 2/8/2017.
 */

public class BackupServiceV2 extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //CONSTS
    private static final String TAG = BackupServiceV2.class.getSimpleName();
    private static final int BUFFER_SIZE = 8192;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 193;


    //DATA
    GoogleApiClient mGoogleApiClient;
    private String mZipFilePath;

    @Inject
    ChefBuddyDAO mDao;


    @Override
    public void onCreate() {
        super.onCreate();

        //Code called only once during lifecycle of service
        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);

        handleLocalBackupCreation();
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
                    File backupDir = getBackupDirCreateIfNotExists();
                    if (backupDir == null) {
                        publishResult(Message.ERROR_CREATING_BACKUP_DIRECTORY);
                        stopSelf();
                        return;
                    }


                    //Get the amount of recipes stored in DB
                    int recipeCount;
                    try {
                        recipeCount = mDao.getRecipeCount();
                    }catch (CouldNotGetDataException e) {
                        publishResult(Message.ERROR_LOADING_RECIPES);
                        stopSelf();
                        return;
                    }

                    //Get and count the list of images in app
                    File recipeImages = new File(FileUtil.getImageFilesDir().getPath());
                    int imageCount = recipeImages.listFiles().length;


                    //Get local paths
                    mZipFilePath = String.format(Locale.getDefault(),
                            Constants.BACKUP_SERVICE_BACKUP_FILE_FORMAT, Constants.BACKUP_SERVICE_BACKUP_DIR,
                            Calendar.getInstance().getTimeInMillis(), recipeCount, imageCount);
                    String databasePath = BackupServiceV2.this.getDatabasePath(Constants.DATABASE_NAME).getPath();
                    String zipFolderForImages = Constants.BACKUP_SERVICE_ZIP_IMAGES_DIR;

                    //Create the backup zip file
                    try {
                        createZipBackup(mZipFilePath, databasePath, recipeImages.listFiles(), zipFolderForImages);
                    } catch (IOException e) {
                        publishResult(Message.ERROR_CREATING_ZIP_FILE);
                        stopSelf();
                        return;
                    }

                    //Delete old local backups if exceeding BACKUP_SERVICE_MAXIMUM_BACKUPS
                    deleteOldLocalBackups(backupDir);


                } catch (Exception e) {
                    publishResult(Message.ERROR_UNKNOWN_CREATING_BACKUP);
                    stopSelf();
                    return;
                }




                //Check if GoogleDrive backup is enabled
                if(SharedPreferenceUtil.getGoogleDriveBackupEnabled())
                    handleGoogleDriveBackupCreation();
                else {
                    //All good!
                    publishResult(Message.SUCCESS_CREATING_LOCAL_BACKUP);
                    //We're done here
                    stopSelf();
                }


            }
        }.start();

    }

    @Nullable
    private File getBackupDirCreateIfNotExists() {
        File backupDir = new File(Constants.BACKUP_SERVICE_BACKUP_DIR);
        if(!backupDir.exists()) {
            backupDir.mkdir();

            if(!backupDir.exists())
                return null;
        }
        return backupDir;
    }

    private void deleteOldLocalBackups(File backupDir) {
        File[] backupZipFiles = backupDir.listFiles();
        Arrays.sort(backupZipFiles);
        int aux = backupDir.listFiles().length - Constants.BACKUP_SERVICE_MAXIMUM_BACKUPS;
        for(int i = 0 ; i < aux ; i++ ) {
            backupZipFiles[i].delete();
        }
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
                        publishResult(Message.ERROR_UNKNOWN_CREATING_BACKUP);
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
                            stopSelf();
                            return;
                        }
                    }

                    //All good!
                    publishResult(Message.SUCCESS_CREATING_LOCAL_AND_GOOGLE_DRIVE_BACKUP);
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

    /**
     * Creates a new Chef Buddy zip backup file containing the SQL database and the recipe images
     * @param zipFilePath The path to the zip file where the backup will be stored
     * @param databasePath The path to the database file used by the app
     * @param imagePaths Array of paths to the images to back up
     */
    private static void createZipBackup(String zipFilePath, String databasePath, File[] imagePaths, String zipFolderForImages) throws IOException {

        //Create zip file
        File zipFile = new File(zipFilePath);
        zipFile.createNewFile();

        //Create a zipOutputStream
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));

        //Add database to zip
        addFileToZip(databasePath, null, out);

        //Add 'images' dir entry to zip
        addFolderToZip(zipFolderForImages, null, out);

        //Add images to zip
        for (File image : imagePaths) {
            addFileToZip(image.getPath(), zipFolderForImages, out);
        }

        out.close();
    }


    /**
     * Add a ZipEntry to a ZipOutputStream. This entry can only be a FILE, it can be inside a folder or in the root of the zip.
     * @param filePath the path to the file to be zipped
     * @param inFolder Specify a folder where to save {@code filePath}. Parameter must be in the form of 'firstFolder/', 'firstFolder/secondFolder/' or NULL if savint to root of zip
     * @param out A ZipOutputStream where the zipEntry will be added to
     * @throws IOException For errors regarding putNextEntry(), read() or write() on {@code out}
     */
    private static void addFileToZip(String filePath, @Nullable String inFolder, ZipOutputStream out) throws IOException {
        BufferedInputStream origin;
        byte data[] = new byte[BUFFER_SIZE];

        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String zipEntryFile = (inFolder != null ? inFolder : "" ) + fileName;


        FileInputStream fi = new FileInputStream(filePath);
        origin = new BufferedInputStream(fi, BUFFER_SIZE);
        try {
            ZipEntry entry = new ZipEntry(zipEntryFile);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            }
        }
        finally {
            fi.close();
            origin.close();
        }
    }


    /**
     * Add a FOLDER ZipEntry to a ZipOutputStream. This entry can be inside another folder or in the root of the zip.
     * @param folderName the name of the folder to be created. E.g.: 'folderName/'
     * @param inFolder Specify a folder where to save {@code folderName}. Parameter must be in the form of 'firstFolder/', 'firstFolder/secondFolder/' or NULL if savint to root of zip
     * @param out A ZipOutputStream where the zipEntry will be added to
     * @throws IOException For errors regarding putNextEntry(), read() or write() on {@code out}
     */
    private static void addFolderToZip(String folderName, @Nullable String inFolder, ZipOutputStream out) throws IOException {
        String zipEntryFile = (inFolder != null ? inFolder : "" ) + folderName;
        ZipEntry entry = new ZipEntry(zipEntryFile);
        out.putNextEntry(entry);
    }


}
