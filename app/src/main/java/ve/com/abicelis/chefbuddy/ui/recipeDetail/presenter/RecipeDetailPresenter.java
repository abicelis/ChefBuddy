package ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter;

import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.view.RecipeDetailView;

/**
 * Created by abicelis on 14/7/2017.
 */

public interface RecipeDetailPresenter {
    void attachView(RecipeDetailView view);
    void detachView();

    void setSourceData(RecipeSource recipeSource, Recipe recipe);

    void reloadRecipe();
    void deleteRecipe();
    Recipe getLoadedRecipe();

}
