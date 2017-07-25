package ve.com.abicelis.chefbuddy.ui.addEditRecipe.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;

/**
 * Created by abicelis on 16/7/2017.
 */

public interface AddRecipeIngredientView {
    void populateIngredientSpinner(List<Ingredient> ingredients);
    void showErrorMessage(Message message);
    void recipeIngredientSelectedSoDismiss(RecipeIngredient recipeIngredient);
}
