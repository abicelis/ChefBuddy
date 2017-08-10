package ve.com.abicelis.chefbuddy.ui.intro.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupInfo;

/**
 * Created by abicelis on 7/8/2017.
 */

public interface AppIntroRestoreBackupView {

    void changeStatus(RestoreBackupState restoreBackupState);
    void setUpBackupsList(List<BackupInfo> backupInfos);

    void startGoogleApiClient();
    void disconnectGoogleApiClient();
    List<String> getGoogleDriveFileList();
    void downloadGoogleDriveBackupFile(BackupInfo backupInfo);

    void showErrorMessage(Message message);


    enum RestoreBackupState {NO_PERMISSIONS, CHOOSE_RESTORE_SOURCE, NO_BACKUPS_FOUND, CHOOSE_BACKUP, RESTORE_DONE, RESTORE_CANCELED }
}
