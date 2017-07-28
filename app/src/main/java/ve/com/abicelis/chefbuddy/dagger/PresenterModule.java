package ve.com.abicelis.chefbuddy.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.network.EdamamApi;
import ve.com.abicelis.chefbuddy.network.Food2ForkApi;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddEditRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddEditRecipePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddRecipeIngredientPresenter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddRecipeIngredientPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.editImageActivity.presenter.EditImagePresenter;
import ve.com.abicelis.chefbuddy.ui.editImageActivity.presenter.EditImagePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home.presenter.HomePresenter;
import ve.com.abicelis.chefbuddy.ui.home.presenter.HomePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home_recipeList.presenter.RecipeListPresenter;
import ve.com.abicelis.chefbuddy.ui.home_recipeList.presenter.RecipeListPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter.SpinWheelPresenter;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter.SpinWheelPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.imageGallery.presenter.ImageGalleryPresenter;
import ve.com.abicelis.chefbuddy.ui.imageGallery.presenter.ImageGalleryPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenter;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter.SearchOnlineRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter.SearchOnlineRecipePresenterImpl;

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
    SpinWheelPresenter provideSpinwheelPresenter(ChefBuddyDAO dao) {
        return new SpinWheelPresenterImpl(dao);
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

    @Provides
    @Singleton
    AddRecipeIngredientPresenter provideAddRecipeIngredientPresenter(ChefBuddyDAO dao) {
        return new AddRecipeIngredientPresenterImpl(dao);
    }

    @Provides
    @Singleton
    ImageGalleryPresenter provideImageGalleryPresenter(ChefBuddyDAO dao) {
        return new ImageGalleryPresenterImpl(dao);
    }

    @Provides
    @Singleton
    EditImagePresenter provideEditImagePresenter() {
        return new EditImagePresenterImpl();
    }

    @Provides
    @Singleton
    SearchOnlineRecipePresenter provideSearchOnlineRecipePresenter(EdamamApi edamamApi, Food2ForkApi food2ForkApi, ChefBuddyDAO dao) {
        return new SearchOnlineRecipePresenterImpl(edamamApi, food2ForkApi, dao);
    }
}
