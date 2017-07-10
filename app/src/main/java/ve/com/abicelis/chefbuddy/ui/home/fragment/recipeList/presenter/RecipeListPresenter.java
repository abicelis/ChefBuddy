package ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList.presenter;

import ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList.view.RecipeListView;

/**
 * Created by abicelis on 8/7/2017.
 */

public interface RecipeListPresenter {
    void attachView(RecipeListView view);
    void detachView();

    void getRecipes();
}
