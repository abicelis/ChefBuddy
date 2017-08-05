package ve.com.abicelis.chefbuddy.ui.backup.presenter;

import java.io.File;
import java.util.Arrays;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupInfo;
import ve.com.abicelis.chefbuddy.model.BackupType;
import ve.com.abicelis.chefbuddy.ui.backup.view.BackupView;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;

/**
 * Created by abicelis on 4/8/2017.
 */

public class BackupPresenterImpl implements BackupPresenter {

    //DATA
    BackupView mView;
    File mLastBackupFile = null;
    BackupInfo mLastBackupInfo = null;


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
        updateLastBackupFileAndInfo();
        mView.updateLastBackupInfo(mLastBackupInfo);
        mView.updateBackupFrequencyType(SharedPreferenceUtil.getBackupFrequencyType());
        mView.updateBackupConnectionType(SharedPreferenceUtil.getBackupConnectionType());
    }

    @Override
    public void manualBackupComplete(boolean result, Message message) {
        if(!result)
            mView.showErrorMessage(message);

        updateLastBackupFileAndInfo();
        mView.updateLastBackupInfo(mLastBackupInfo);
    }


    private void updateLastBackupFileAndInfo() {
        File backupDir = new File(Constants.BACKUP_SERVICE_BACKUP_DIR);
        File[] backupZipFiles = backupDir.listFiles();

        if(backupZipFiles != null && backupZipFiles.length > 0) {
            Arrays.sort(backupZipFiles);
            mLastBackupFile = backupZipFiles[backupZipFiles.length-1];
            mLastBackupInfo = new BackupInfo(mLastBackupFile.getName(), BackupType.LOCAL);
        } else {
            mLastBackupFile = null;
            mLastBackupInfo = null;
        }
    }
}
