package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.EditImageAdapter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.EditRecipeIngredientAdapter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;

/**
 * Created by abicelis on 16/7/2017.
 */

public interface AddEditRecipePresenter {
    void attachViews(AddEditRecipeView view, EditRecipeIngredientAdapter editRecipeIngredientAdapter, EditImageAdapter editImageAdapter);
    void detachViews();


    void setExistingRecipe(long recipeId);
    void creatingNewRecipe();

    void saveRecipe(String name, String preparation);


    boolean isEditingExistingRecipe();

    int getServingsSelection();
    void setServingsSelection(int position);
    int getPreparationTimeSelection();
    void setPreparationTimeSelection(int position);

    void addRecipeIngredient(RecipeIngredient recipeIngredient);
    void addImage(String imageFilename);
}
