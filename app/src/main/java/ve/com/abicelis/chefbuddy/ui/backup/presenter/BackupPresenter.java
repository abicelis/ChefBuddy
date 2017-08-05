package ve.com.abicelis.chefbuddy.ui.backup.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupFrequencyType;
import ve.com.abicelis.chefbuddy.ui.backup.view.BackupView;

/**
 * Created by abicelis on 4/8/2017.
 */

public interface BackupPresenter {
    void attachView(BackupView view);
    void detachView();

    void start();
    void manualBackupComplete(boolean result, Message message);

    void backupFrequencyUpdated(BackupFrequencyType backupFrequencyType);
}
