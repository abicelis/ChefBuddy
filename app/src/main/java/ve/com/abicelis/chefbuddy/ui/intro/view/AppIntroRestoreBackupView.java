package ve.com.abicelis.chefbuddy.ui.intro.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;

/**
 * Created by abicelis on 7/8/2017.
 */

public interface AppIntroRestoreBackupView {

    void changeStatus(RestoreBackupState restoreBackupState);

    void startGoogleApiClient();
    void disconnectGoogleApiClient();
    List<String> getGoogleDriveFileList();

    void showErrorMessage(Message message);


    enum RestoreBackupState {NO_PERMISSIONS, CHOOSING_LOCAL_AND_OR_GOOGLE, NO_BACKUPS_FOUND, CHOOSING_BACKUP_TO_RESTORE }
}
