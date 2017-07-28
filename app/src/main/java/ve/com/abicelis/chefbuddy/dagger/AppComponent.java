package ve.com.abicelis.chefbuddy.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.AddEditRecipeActivity;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.AddRecipeIngredientDialogFragment;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddEditRecipePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.editImageActivity.EditImageActivity;
import ve.com.abicelis.chefbuddy.ui.editImageActivity.presenter.EditImagePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home.HomeActivity;
import ve.com.abicelis.chefbuddy.ui.home.presenter.HomePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home_recipeList.RecipeListFragment;
import ve.com.abicelis.chefbuddy.ui.home_recipeList.presenter.RecipeListPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.SpinWheelFragment;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter.SpinWheelPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.imageGallery.ImageGalleryActivity;
import ve.com.abicelis.chefbuddy.ui.imageGallery.presenter.ImageGalleryPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.main.MainActivity;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeDetailActivity;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.SearchOnlineRecipeActivity;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter.SearchOnlineRecipePresenterImpl;

/**
 * Created by abicelis on 5/7/2017.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class, DatabaseModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(MainActivity target);

    void inject(HomeActivity target);
    void inject(HomePresenterImpl target);

    void inject(RecipeListFragment target);
    void inject(RecipeListPresenterImpl target);

    void inject(SpinWheelFragment target);
    void inject(SpinWheelPresenterImpl target);

    void inject(RecipeDetailActivity target);
    void inject(RecipeDetailPresenterImpl target);

    void inject(AddEditRecipeActivity target);
    void inject(AddRecipeIngredientDialogFragment target);
    void inject(AddEditRecipePresenterImpl target);

    void inject(ImageGalleryActivity target);
    void inject(ImageGalleryPresenterImpl target);

    void inject(EditImageActivity target);
    void inject(EditImagePresenterImpl target);

    void inject(SearchOnlineRecipeActivity target);
    void inject(SearchOnlineRecipePresenterImpl target);
}
