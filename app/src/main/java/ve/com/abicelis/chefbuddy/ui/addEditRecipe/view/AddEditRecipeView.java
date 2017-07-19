package ve.com.abicelis.chefbuddy.ui.addEditRecipe.view;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;

/**
 * Created by abicelis on 16/7/2017.
 */

public interface AddEditRecipeView {
    void showRecipe(Recipe recipe);
    void showErrorMessage(Message message);
    void recipeSavedSoFinish();
}
