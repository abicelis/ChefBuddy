package ve.com.abicelis.chefbuddy.ui.intro.presenter;

import ve.com.abicelis.chefbuddy.model.BackupInfo;
import ve.com.abicelis.chefbuddy.ui.intro.view.AppIntroRestoreBackupView;

/**
 * Created by abicelis on 7/8/2017.
 */

public interface AppIntroRestoreBackupPresenter {
    void attachView(AppIntroRestoreBackupView view);
    void detachView();

    void fragmentIsVisible(boolean storagePermissions);
    void fragmentLostVisibility();

    void restoreTypeChosen(boolean localOnly);
    void restoreBackup(BackupInfo backupInfo);

    void googleApiClientConnected();
    void googleApiClientNotConnected();


}
