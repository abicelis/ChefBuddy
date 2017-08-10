package ve.com.abicelis.chefbuddy.ui.intro.presenter;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.BackupInfo;
import ve.com.abicelis.chefbuddy.model.BackupType;
import ve.com.abicelis.chefbuddy.ui.intro.view.AppIntroRestoreBackupView;
import ve.com.abicelis.chefbuddy.util.BackupUtil;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;

/**
 * Created by abicelis on 7/8/2017.
 */

public class AppIntroRestoreBackupPresenterImpl implements AppIntroRestoreBackupPresenter {

    //DATA
    private AppIntroRestoreBackupView mView;
    private ChefBuddyDAO mDao;

    public AppIntroRestoreBackupPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void attachView(AppIntroRestoreBackupView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void fragmentIsVisible(boolean storagePermissions) {

        if(storagePermissions)
            mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.CHOOSE_RESTORE_SOURCE);
        else
            mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.NO_PERMISSIONS);

    }

    @Override
    public void fragmentLostVisibility() {
        mView.disconnectGoogleApiClient();
    }

    @Override
    public void restoreTypeChosen(boolean localOnly) {
        if(localOnly) {
            //Get local list
            List<String> localBackupList = new ArrayList<>();
            for (File f : FileUtil.getLocalBackupList(false))
                localBackupList.add(f.getName());

            if(localBackupList.size() == 0) {
                mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.NO_BACKUPS_FOUND);
            } else {
                List<BackupInfo> backupInfoList = new ArrayList<>();
                for (String localFile : localBackupList)
                    backupInfoList.add(new BackupInfo(localFile, BackupType.LOCAL));

                mView.setUpBackupsList(backupInfoList);
                mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.CHOOSE_BACKUP);
            }
        } else {
            mView.startGoogleApiClient();
        }
    }

    @Override
    public void restoreBackup(BackupInfo backupInfo) {
        if (backupInfo == null) {
            mView.showErrorMessage(Message.ERROR_INVALID_BACKUP_SELECTED);
        } else {
            try {
                //Force SQLite to create database.
                try {
                    int fakeRecipeCount = mDao.getRecipeCount();
                } catch (CouldNotGetDataException e) {
                    mView.showErrorMessage(Message.ERROR_RESTORING_BACKUP);
                    return;
                }

                //If backup is in google only, download zip first
                if(backupInfo.getBackupType().equals(BackupType.GOOGLE_DRIVE)) {
                    mView.showErrorMessage(Message.ERROR_CREATING_ZIP_FILE);
                    // TODO
                    // mView.downloadGoogleDriveBackupFile(backupInfo);
                }

                BackupUtil.restoreZipBackup(new File(FileUtil.getBackupDir(), backupInfo.getFilename()).getPath());
                mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.RESTORE_DONE);

            } catch (IOException e) {
                mView.showErrorMessage(Message.ERROR_RESTORING_BACKUP);
                mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.RESTORE_CANCELED);
            }
        }
    }

    @Override
    public void googleApiClientConnected() {
        SharedPreferenceUtil.setGoogleDriveBackupEnabled(true);
        //TODO maybe init more things here? idk

        new AsyncTask<String, String, List<BackupInfo>>(){
            @Override
            protected List<BackupInfo> doInBackground(String... params) {
                //Get local list
                List<String> localBackupList = new ArrayList<>();
                for (File f : FileUtil.getLocalBackupList(false))
                    localBackupList.add(f.getName());

                //Get google drive list
                List<String> googleDriveBackupList = mView.getGoogleDriveFileList();

                //Merge
                List<BackupInfo> mergedBackupList = getMergedBackupInfoList(localBackupList, googleDriveBackupList);
                return mergedBackupList;
            }

            @Override
            protected void onPostExecute(List<BackupInfo> backupInfos) {
                if(backupInfos.size() == 0) {
                    mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.NO_BACKUPS_FOUND);
                } else {
                    mView.setUpBackupsList(backupInfos);
                    mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.CHOOSE_BACKUP);
                }
            }
        }.execute();
    }

    private List<BackupInfo> getMergedBackupInfoList(List<String> localBackupList, List<String> googleDriveBackupList) {
        List<BackupInfo> backupInfoList = new ArrayList<>();
        if(localBackupList.size() > 0 && googleDriveBackupList.size() > 0) {
            for (String localFile : localBackupList) {
                int i = googleDriveBackupList.indexOf(localFile);
                if(i != -1) {
                    backupInfoList.add(new BackupInfo(localFile, BackupType.BOTH));
                    googleDriveBackupList.remove(i);
                } else {
                    backupInfoList.add(new BackupInfo(localFile, BackupType.LOCAL));
                }
            }

            for (String googleDriveFile : googleDriveBackupList)
                backupInfoList.add(new BackupInfo(googleDriveFile, BackupType.GOOGLE_DRIVE));

            // TODO: 7/8/2017 SORT by name!


        } else if(localBackupList.size() > 0) {
            for (String localFile : localBackupList)
                backupInfoList.add(new BackupInfo(localFile, BackupType.LOCAL));

        } else if (googleDriveBackupList.size() > 0) {
            for (String googleDriveFile : googleDriveBackupList)
                backupInfoList.add(new BackupInfo(googleDriveFile, BackupType.GOOGLE_DRIVE));

        }

        return backupInfoList;
    }


    @Override
    public void googleApiClientNotConnected() {
        SharedPreferenceUtil.setGoogleDriveBackupEnabled(false);
        mView.showErrorMessage(Message.ERROR_CONNECTING_GOOGLE_API);
    }

}
