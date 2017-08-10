package ve.com.abicelis.chefbuddy.ui.backup.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupFrequencyType;
import ve.com.abicelis.chefbuddy.model.BackupInfo;
import ve.com.abicelis.chefbuddy.model.BackupType;
import ve.com.abicelis.chefbuddy.ui.backup.view.BackupView;
import ve.com.abicelis.chefbuddy.util.BackupUtil;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;

/**
 * Created by abicelis on 4/8/2017.
 */

public class BackupPresenterImpl implements BackupPresenter {

    //DATA
    BackupView mView;
    BackupInfo mLastBackupFileInfo = null;
    boolean mGoogleApiClientEnabled = false;



    @Override
    public void attachView(BackupView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void start() {
        mView.updateBackupFrequencyType(SharedPreferenceUtil.getBackupFrequencyType());

        if(SharedPreferenceUtil.getGoogleDriveBackupEnabled()) {
            mView.updateLastBackupSection(BackupView.LastBackupState.LOADING, null);
            mView.updateGoogleDriveSection(BackupView.GoogleDriveBackupState.LOADING, null, null);
            mView.startGoogleApiClient();
        } else {
            mGoogleApiClientEnabled = false;
            mView.updateGoogleDriveSection(BackupView.GoogleDriveBackupState.DISABLED, null, null);
            updateLastBackupFileAndInfo();
        }



//        updateLastBackupFileAndInfo();
//        mView.updateLastBackupSection(mLastBackupFileInfo);
//        mView.updateBackupFrequencyType(SharedPreferenceUtil.getBackupFrequencyType());
//        mView.updateBackupConnectionType(SharedPreferenceUtil.getBackupConnectionType());
//
//        if(SharedPreferenceUtil.getGoogleDriveBackupEnabled()) {
//            mView.setGoogleDriveSwitch(true);
//        }
    }

    @Override
    public void backupServiceProgressReport(Message message) {
        switch (message.getMessageType()) {
            case SUCCESS:
                mView.showInfo(message.getFriendlyName());
                updateLastBackupFileAndInfo();
                break;
            case ERROR:
                mView.showErrorMessage(message);
                updateLastBackupFileAndInfo();
                break;
            case NOTICE:
                //TODO maybe update "backing up" text with notice message?
                break;
        }
    }


    @Override
    public void backupFrequencyUpdated(BackupFrequencyType backupFrequencyType) {
        //Save value, notify user
        SharedPreferenceUtil.setBackupFrequencyType(backupFrequencyType);
        mView.updateBackupFrequencyType(backupFrequencyType);

        String message = String.format(Locale.getDefault(), ChefBuddyApplication.getContext().getString(R.string.dialog_set_backup_frequency_success), backupFrequencyType.getFriendlyName());
        mView.showInfo(message);

        mView.triggerBackupServiceStarter();
    }

    @Override
    public void googleDriveSwitchToggled(boolean isChecked) {
        mGoogleApiClientEnabled = false;

        if(isChecked) {
            mView.updateGoogleDriveSection(BackupView.GoogleDriveBackupState.LOADING, null, null);
            mView.startGoogleApiClient();
        } else {
            SharedPreferenceUtil.setGoogleDriveBackupEnabled(false);
            mView.updateGoogleDriveSection(BackupView.GoogleDriveBackupState.DISABLED, null, null);
        }
    }

    @Override
    public void googleApiClientConnected() {
        mGoogleApiClientEnabled = true;
        SharedPreferenceUtil.setGoogleDriveBackupEnabled(true);

        //TODO
        SharedPreferenceUtil.setGoogleDriveBackupAccount("example@email.com");

        mView.updateGoogleDriveSection(BackupView.GoogleDriveBackupState.ENABLED, SharedPreferenceUtil.getGoogleDriveBackupAccount(), SharedPreferenceUtil.getBackupConnectionType());
        updateLastBackupFileAndInfo();
    }

    @Override
    public void googleApiClientNotConnected() {
        mGoogleApiClientEnabled = false;
        mView.updateGoogleDriveSection(BackupView.GoogleDriveBackupState.DISABLED, null, null);
        updateLastBackupFileAndInfo();
    }


    private void updateLastBackupFileAndInfo() {
        //Get local files
        final List<File> backupZipFiles = FileUtil.getLocalBackupList(false);

        if(mGoogleApiClientEnabled) {

            new Thread() {
                @Override
                public void run() {
                    super.run();

                    //Get files from remote
                    List<String> remoteFiles = mView.getGoogleDriveFileList();

                    if(remoteFiles.size() > 0 && backupZipFiles.size() > 0) {
                        String localFile = backupZipFiles.get(0).getName();
                        String remoteFile = remoteFiles.get(remoteFiles.size()-1);

                        int result = localFile.compareTo(remoteFile);

                        if(result == 0)
                            mLastBackupFileInfo = new BackupInfo(remoteFile, BackupType.BOTH);
                        else if (result > 0)
                            mLastBackupFileInfo = new BackupInfo(localFile, BackupType.LOCAL);
                        else
                            mLastBackupFileInfo = new BackupInfo(localFile, BackupType.GOOGLE_DRIVE);


                    } else if(backupZipFiles.size() > 0) {
                        mLastBackupFileInfo = new BackupInfo(backupZipFiles.get(0).getName(), BackupType.LOCAL);

                    } else if (remoteFiles.size() > 0) {
                        mLastBackupFileInfo = new BackupInfo(remoteFiles.get(0), BackupType.GOOGLE_DRIVE);

                    } else {
                        mLastBackupFileInfo = null;
                    }



                    if(mLastBackupFileInfo == null)
                        mView.updateLastBackupSection(BackupView.LastBackupState.NO_BACKUPS, null);
                    else
                        mView.updateLastBackupSection(BackupView.LastBackupState.SHOW_BACKUP_INFO, mLastBackupFileInfo);
                }
            }.start();

        } else {

            if(backupZipFiles.size() > 0) {
                mLastBackupFileInfo = new BackupInfo(backupZipFiles.get(0).getName(), BackupType.LOCAL);
                mView.updateLastBackupSection(BackupView.LastBackupState.SHOW_BACKUP_INFO, mLastBackupFileInfo);
            } else {
                mLastBackupFileInfo = null;
                mView.updateLastBackupSection(BackupView.LastBackupState.NO_BACKUPS, null);
            }
        }
    }




}
