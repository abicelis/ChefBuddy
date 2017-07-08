package ve.com.abicelis.chefbuddy.app;

import android.app.Application;
import android.content.Context;

import ve.com.abicelis.chefbuddy.dagger.AppComponent;
import ve.com.abicelis.chefbuddy.dagger.AppModule;
import ve.com.abicelis.chefbuddy.dagger.DaggerAppComponent;

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
