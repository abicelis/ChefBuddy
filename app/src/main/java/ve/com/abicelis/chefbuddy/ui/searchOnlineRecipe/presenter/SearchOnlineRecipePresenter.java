package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter;

import ve.com.abicelis.chefbuddy.model.OnlineSourceApi;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view.SearchOnlineRecipeView;

/**
 * Created by abicelis on 21/7/2017.
 */

public interface SearchOnlineRecipePresenter {
    void attachView(SearchOnlineRecipeView view);
    void detachView();

    void setSourceApi(OnlineSourceApi onlineSourceApi);
    void searchRecipes(String query);
    void getRecipeDetail(int position);
}
