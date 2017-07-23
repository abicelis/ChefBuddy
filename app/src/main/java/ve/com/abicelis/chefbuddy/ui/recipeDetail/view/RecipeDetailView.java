package ve.com.abicelis.chefbuddy.ui.recipeDetail.view;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;

/**
 * Created by abicelis on 14/7/2017.
 */

public interface RecipeDetailView {

    void initViews(RecipeSource recipeSource);
    void showRecipe(Recipe recipe, RecipeSource recipeSource);
    void recipeDeletedSoFinish();
    void recipeDownloadedSoFinish();
    void showErrorMessage(Message message);
}
