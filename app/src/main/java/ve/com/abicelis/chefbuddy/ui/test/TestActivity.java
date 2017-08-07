package ve.com.abicelis.chefbuddy.ui.test;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.util.FileUtil;

/**
 * Created by abicelis on 14/7/2017.
 */

public class TestActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    GoogleApiClient mGoogleApiClient;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 192;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Poop");
        getSupportActionBar().setLogo(R.drawable.pizza);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);


    }

    // [START drive_contents_callback]
    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Toast.makeText(TestActivity.this, "Error while trying to create new file contents", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    OutputStream outputStream = result.getDriveContents().getOutputStream();

                    File backupDir = new File(Constants.BACKUP_SERVICE_BACKUP_DIR);
                    File[] backupFiles = backupDir.listFiles();

                    if (backupFiles != null && backupFiles.length > 0) {
                        File backupFile = backupFiles[0];
                        String extension = backupFile.getName().substring(backupFile.getName().lastIndexOf('.') + 1).toLowerCase();
                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                        try {
                            FileInputStream fileInputStream = new FileInputStream(backupFile);
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        } catch (IOException e ) {
                            Toast.makeText(TestActivity.this, "Unable to write file contents", Toast.LENGTH_SHORT).show();
                        }


                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(backupFile.getName())
                                .setMimeType(mimeType)
                                .build();

                        //Drive.DriveApi.getAppFolder(mGoogleApiClient)
                        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                .createFile(mGoogleApiClient, changeSet, result.getDriveContents())
                                .setResultCallback(fileCallback);

                    }
                }
            };
    // [END drive_contents_callback]

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Toast.makeText(TestActivity.this, "Error while trying to create the file", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(TestActivity.this, "Created a file in App Folder: " + result.getDriveFile().getDriveId(), Toast.LENGTH_SHORT).show();

                    //Drive.DriveApi.getAppFolder(mGoogleApiClient)
                    Drive.DriveApi.getRootFolder(mGoogleApiClient)
                            .listChildren(mGoogleApiClient)
                            .setResultCallback(listFilesResult);
                }
            };


    final private ResultCallback<DriveApi.MetadataBufferResult> listFilesResult = new ResultCallback<DriveApi.MetadataBufferResult>() {
        @Override
        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
            if (!metadataBufferResult.getStatus().isSuccess()) {
                Toast.makeText(TestActivity.this, "Error while trying to list the drive files", Toast.LENGTH_SHORT).show();
                return;
            }

            for(Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                Toast.makeText(TestActivity.this, "Title=" + metadata.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onConnectionSuspended(int i) {
        Log.d("", "onConnectionSuspended()");
        //Called when for instance Google Play services is Force Stopped, in which case reconnection is automatic
        //Or if user uninstalls Google Play Services (WHY U DO DIS?), in which case onConnectionFailed() gets called seconds after
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }


}
