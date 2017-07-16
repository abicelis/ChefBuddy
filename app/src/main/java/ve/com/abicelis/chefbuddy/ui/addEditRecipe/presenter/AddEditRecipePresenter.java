package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;

/**
 * Created by abicelis on 16/7/2017.
 */

public interface AddEditRecipePresenter {
    void attachView(AddEditRecipeView view);
    void detachView();

    void getRecipe(long recipeId);
}
