package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 21/7/2017.
 */

public interface SearchOnlineRecipeView {
    void showLoading();
    void hideLoading();
    void showRecipes(List<Recipe> recipes);
    void showErrorMessage(Message message);
}
