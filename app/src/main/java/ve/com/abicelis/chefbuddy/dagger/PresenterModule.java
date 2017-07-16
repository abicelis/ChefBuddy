package ve.com.abicelis.chefbuddy.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddEditRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddEditRecipePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home.presenter.HomePresenter;
import ve.com.abicelis.chefbuddy.ui.home.presenter.HomePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList.presenter.RecipeListPresenter;
import ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList.presenter.RecipeListPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenter;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenterImpl;

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

    @Provides
    @Singleton
    RecipeDetailPresenter provideRecipeDetailPresenter(ChefBuddyDAO dao){
        return new RecipeDetailPresenterImpl(dao);
    }

    @Provides
    @Singleton
    AddEditRecipePresenter provideAddEditRecipeDetailPresenter(ChefBuddyDAO dao) {
        return new AddEditRecipePresenterImpl(dao);
    }

}
