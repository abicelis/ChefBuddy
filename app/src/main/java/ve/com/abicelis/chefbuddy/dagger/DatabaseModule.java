package ve.com.abicelis.chefbuddy.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDbHelper;

/**
 * Created by abicelis on 5/7/2017.
 */

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    ChefBuddyDbHelper provideDbHelper(Context context) {
        return new ChefBuddyDbHelper(context);
    }

    @Provides
    @Singleton
    ChefBuddyDAO provideDAO(ChefBuddyDbHelper chefBuddyDbHelper) {
        return new ChefBuddyDAO(chefBuddyDbHelper);
    }
}
