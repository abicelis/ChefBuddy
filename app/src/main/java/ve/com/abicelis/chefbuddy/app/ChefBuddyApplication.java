package ve.com.abicelis.chefbuddy.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import ve.com.abicelis.chefbuddy.dagger.AppComponent;
import ve.com.abicelis.chefbuddy.dagger.AppModule;
import ve.com.abicelis.chefbuddy.dagger.DaggerAppComponent;
import ve.com.abicelis.chefbuddy.model.BackupFrequencyType;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;

/**
 * Created by abicelis on 5/7/2017.
 */

public class ChefBuddyApplication extends Application {

    private AppComponent appComponent;
    private static Context appContext;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
        appContext = this;

        SharedPreferenceUtil.setBackupFrequencyType(BackupFrequencyType.MINUTES_5);

        //Trigger BackupServiceStarter
        Intent triggerBackupServiceStarter = new Intent(Constants.APP_STARTED_ACTION);
        sendBroadcast(triggerBackupServiceStarter);
    }

    protected AppComponent initDagger(ChefBuddyApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

    public static Context getContext(){
        return appContext;
    }
}
