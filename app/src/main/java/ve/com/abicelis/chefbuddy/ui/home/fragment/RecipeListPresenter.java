package ve.com.abicelis.chefbuddy.ui.home.fragment;

/**
 * Created by abicelis on 8/7/2017.
 */

public interface RecipeListPresenter {
    void attachView(RecipeListView view);
    void detachView();

    void getRecipes();
}
