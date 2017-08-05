package ve.com.abicelis.chefbuddy.ui.backup.view;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupConnectionType;
import ve.com.abicelis.chefbuddy.model.BackupFrequencyType;
import ve.com.abicelis.chefbuddy.model.BackupInfo;

/**
 * Created by abicelis on 4/8/2017.
 */

public interface BackupView {

    void updateLastBackupInfo(BackupInfo backupInfo);

    void updateBackupFrequencyType(BackupFrequencyType backupFrequencyType);
    void updateBackupConnectionType(BackupConnectionType backupConnectionType);

    void showErrorMessage(Message message);
}
