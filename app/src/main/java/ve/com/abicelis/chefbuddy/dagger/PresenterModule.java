package ve.com.abicelis.chefbuddy.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.ui.home.HomePresenter;
import ve.com.abicelis.chefbuddy.ui.home.HomePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home.fragments.RecipeListPresenter;
import ve.com.abicelis.chefbuddy.ui.home.fragments.RecipeListPresenterImpl;

/**
 * Created by abicelis on 5/7/2017.
 */

@Module
public class PresenterModule {

    @Provides
    @Singleton
    HomePresenter provideHomePresenter(ChefBuddyDAO dao) {
        return new HomePresenterImpl(dao);
    }

    @Provides
    @Singleton
    RecipeListPresenter provideRecipeListPresenter(ChefBuddyDAO dao) {
        return new RecipeListPresenterImpl(dao);
    }

}
