package ve.com.abicelis.chefbuddy.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ve.com.abicelis.chefbuddy.service.BackupService;
import ve.com.abicelis.chefbuddy.service.BackupServiceV2;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.AddEditRecipeActivity;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.AddRecipeIngredientDialogFragment;
import ve.com.abicelis.chefbuddy.ui.backup.BackupActivity;
import ve.com.abicelis.chefbuddy.ui.editImageActivity.EditImageActivity;
import ve.com.abicelis.chefbuddy.ui.home.HomeActivity;
import ve.com.abicelis.chefbuddy.ui.home_history.EditDailyRecipeDialogFragment;
import ve.com.abicelis.chefbuddy.ui.home_history.HistoryFragment;
import ve.com.abicelis.chefbuddy.ui.home_recipeList.RecipeListFragment;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.EditSpinWheelRecipesDialogFragment;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.SpinWheelFragment;
import ve.com.abicelis.chefbuddy.ui.imageGallery.ImageGalleryActivity;
import ve.com.abicelis.chefbuddy.ui.intro.AppIntroRestoreBackupFragment;
import ve.com.abicelis.chefbuddy.ui.main.MainActivity;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeDetailActivity;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.SearchOnlineRecipeActivity;

/**
 * Created by abicelis on 5/7/2017.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class, DatabaseModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(MainActivity target);

    void inject(AppIntroRestoreBackupFragment target);



    void inject(HomeActivity target);
    void inject(RecipeListFragment target);
    void inject(SpinWheelFragment target);
    void inject(EditSpinWheelRecipesDialogFragment target);
    void inject(HistoryFragment target);
    void inject(EditDailyRecipeDialogFragment target);



    void inject(RecipeDetailActivity target);



    void inject(AddEditRecipeActivity target);
    void inject(AddRecipeIngredientDialogFragment target);



    void inject(ImageGalleryActivity target);



    void inject(EditImageActivity target);



    void inject(SearchOnlineRecipeActivity target);



    void inject(BackupActivity target);
    void inject(BackupService target);
    void inject(BackupServiceV2 target);
}
