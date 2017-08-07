package ve.com.abicelis.chefbuddy.ui.backup.view;

import android.support.annotation.Nullable;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupConnectionType;
import ve.com.abicelis.chefbuddy.model.BackupFrequencyType;
import ve.com.abicelis.chefbuddy.model.BackupInfo;

/**
 * Created by abicelis on 4/8/2017.
 */

public interface BackupView {

    void startGoogleApiClient();
    void disconnectGoogleApiClient();
    List<String> getGoogleDriveFileList();

    //View updaters
    void updateBackupFrequencyType(BackupFrequencyType backupFrequencyType);
    void updateLastBackupSection(LastBackupState state, @Nullable BackupInfo backupInfo);
    void updateGoogleDriveSection(GoogleDriveBackupState state, @Nullable String account, @Nullable BackupConnectionType backupConnectionType);


    void triggerBackupServiceStarter();

    void showInfo(String info);
    void showErrorMessage(Message message);


    enum LastBackupState { LOADING, NO_BACKUPS, SHOW_BACKUP_INFO, BACKING_UP }
    enum GoogleDriveBackupState { LOADING, DISABLED, ENABLED}
}
