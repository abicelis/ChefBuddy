package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.model.OnlineSourceApi;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;
import ve.com.abicelis.chefbuddy.network.EdamamApi;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view.SearchOnlineRecipeView;
import ve.com.abicelis.chefbuddy.util.RecipeUtil;

/**
 * Created by abicelis on 21/7/2017.
 */

public class SearchOnlineRecipePresenterImpl implements SearchOnlineRecipePresenter {

    //DATA
    SearchOnlineRecipeView mView;
    OnlineSourceApi mOnlineSourceApi = OnlineSourceApi.EDAMAM;

    //Injected
    EdamamApi mEdamamApi;
    ChefBuddyDAO mDao;

    public SearchOnlineRecipePresenterImpl(EdamamApi edamamApi, ChefBuddyDAO dao) {
        mEdamamApi = edamamApi;
        mDao = dao;
    }


    @Override
    public void attachView(SearchOnlineRecipeView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getRecipes(String query) {
        if(mView != null)
            mView.showLoading();

        //TODO implement some kind of interface for source apis based also on mOnlineSourceApi enum.
        mEdamamApi.searchRecipesByName(query).enqueue(new Callback<EdamamResponse>() {
            @Override
            public void onResponse(Call<EdamamResponse> call, Response<EdamamResponse> response) {

                if (response.code() != 200) {
                    if(mView != null)
                        mView.showErrorMessage(Message.ERROR_LOADING_ONLINE_RECIPES);
                } else {
                    List<Recipe> recipes = RecipeUtil.getRecipesFromEdamamResponse(response);

                    if( mView != null)
                        mView.showRecipes(recipes);
                }
                if(mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onFailure(Call<EdamamResponse> call, Throwable t) {
                if(mView != null)
                    mView.showErrorMessage(Message.ERROR_LOADING_ONLINE_RECIPES);
            }
        });
    }

    @Override
    public void setSourceApi(OnlineSourceApi onlineSourceApi) {
        mOnlineSourceApi = onlineSourceApi;
        mView.clearRecipes();
    }
}
