package ve.com.abicelis.chefbuddy.ui.home.fragment;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 8/7/2017.
 */

public interface RecipeListView {
    void showRecipes(List<Recipe> recipes);
    void showLoading();
    void showErrorMessage(Message message);
}
