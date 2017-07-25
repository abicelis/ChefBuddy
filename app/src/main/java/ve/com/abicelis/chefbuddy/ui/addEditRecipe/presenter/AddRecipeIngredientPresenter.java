package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddRecipeIngredientView;

/**
 * Created by abicelis on 16/7/2017.
 */

public interface AddRecipeIngredientPresenter {
    void attachView(AddRecipeIngredientView view);
    void detachView();

    void getIngredients();
    void setSelectedIngredient(int position);
    void setSelectedMeasurement(Measurement measurement);

    void checkRecipeIngredientValues(String amount, String ingredientName);
}
