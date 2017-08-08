package ve.com.abicelis.chefbuddy.ui.intro.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.model.BackupInfo;
import ve.com.abicelis.chefbuddy.model.BackupType;
import ve.com.abicelis.chefbuddy.ui.intro.view.AppIntroRestoreBackupView;

/**
 * Created by abicelis on 7/8/2017.
 */

public class AppIntroRestoreBackupPresenterImpl implements AppIntroRestoreBackupPresenter {

    //DATA
    private AppIntroRestoreBackupView mView;

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

        // TODO: 7/8/2017 maybe reset a few things ?

        if(storagePermissions)
            mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.NO_PERMISSIONS);
        else
            mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.CHOOSING_LOCAL_AND_OR_GOOGLE);

    }

    @Override
    public void fragmentLostVisibility() {

    }

    @Override
    public void restoreTypeChosen(boolean localOnly) {
        if(localOnly) {

        } else {
            mView.startGoogleApiClient();
        }
    }

    @Override
    public void googleApiClientConnected() {

        new Thread() {
            @Override
            public void run() {
                super.run();

                //Get local list
                List<String> localBackupList = getLocalBackupList();

                //Get google drive list
                List<String> googleDriveBackupList = mView.getGoogleDriveFileList();


                List<BackupInfo> mergedBackupList = getMergedBackupInfoList(localBackupList, googleDriveBackupList);
                if(mergedBackupList.size() == 0) {
                    mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.NO_BACKUPS_FOUND);
                } else {
                    mView.changeStatus(AppIntroRestoreBackupView.RestoreBackupState.CHOOSING_BACKUP_TO_RESTORE);
                    //// TODO: 7/8/2017 mView.populaterecyclerview thing
                }
            }
        }.start();
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

    private List<String> getLocalBackupList() {
        List<String> localBackupList = new ArrayList<>();
        File backupDir = new File(Constants.BACKUP_SERVICE_BACKUP_DIR);
        if(!backupDir.exists()) {
            File [] files = backupDir.listFiles();
            Arrays.sort(files);
            for(File f : files)
            localBackupList.add(f.getName());
        }

        return localBackupList;
    }

    @Override
    public void googleApiClientNotConnected() {

    }


}
