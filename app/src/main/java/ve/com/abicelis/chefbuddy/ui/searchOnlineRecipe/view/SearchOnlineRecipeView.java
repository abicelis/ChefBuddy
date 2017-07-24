package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 21/7/2017.
 */

public interface SearchOnlineRecipeView {
    void showLoading();
    void hideLoading();
    void clearRecipes();
    void showRecipes(List<Recipe> recipes);
    void showRecipeDetail(int position, Recipe recipe);
    void showMessage(Message message, SnackbarUtil.SnackbarType snackbarType);

}
