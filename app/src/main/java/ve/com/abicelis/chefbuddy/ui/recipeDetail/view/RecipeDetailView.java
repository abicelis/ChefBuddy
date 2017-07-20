package ve.com.abicelis.chefbuddy.ui.recipeDetail.view;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 14/7/2017.
 */

public interface RecipeDetailView {
    void showRecipe(Recipe recipe);
    void recipeDeletedSoFinish();
    void showErrorMessage(Message message);
}
