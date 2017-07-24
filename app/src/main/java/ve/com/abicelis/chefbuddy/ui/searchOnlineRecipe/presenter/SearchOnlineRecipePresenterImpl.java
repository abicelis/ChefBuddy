package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.model.OnlineSourceApi;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;
import ve.com.abicelis.chefbuddy.model.food2fork.Food2ForkGetResponse;
import ve.com.abicelis.chefbuddy.model.food2fork.Food2ForkResponse;
import ve.com.abicelis.chefbuddy.network.EdamamApi;
import ve.com.abicelis.chefbuddy.network.Food2ForkApi;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view.SearchOnlineRecipeView;
import ve.com.abicelis.chefbuddy.util.RecipeUtil;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 21/7/2017.
 */

public class SearchOnlineRecipePresenterImpl implements SearchOnlineRecipePresenter {

    //DATA
    List<Recipe> mRecipes;
    SearchOnlineRecipeView mView;
    OnlineSourceApi mOnlineSourceApi = OnlineSourceApi.EDAMAM;

    //Injected
    EdamamApi mEdamamApi;
    Food2ForkApi mFood2ForkApi;
    ChefBuddyDAO mDao;

    public SearchOnlineRecipePresenterImpl(EdamamApi edamamApi, Food2ForkApi food2ForkApi, ChefBuddyDAO dao) {
        mEdamamApi = edamamApi;
        mFood2ForkApi = food2ForkApi;
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
    public void setSourceApi(@NonNull OnlineSourceApi onlineSourceApi) {
        mOnlineSourceApi = onlineSourceApi;
        mView.clearRecipes();
    }

    @Override
    public void searchRecipes(@NonNull String query) {
        if(mView != null)
            mView.showLoading();

        switch (mOnlineSourceApi) {
            case EDAMAM:
                mEdamamApi.searchRecipesByName(query).enqueue(new EdamamCallback());
                break;
            case FOOD2FORK:
                mFood2ForkApi.searchRecipesByName(query).enqueue(new Food2ForkCallback());
                break;
        }
    }

    @Override
    public void getRecipeDetail(final int position) {
        switch (mOnlineSourceApi) {
            case EDAMAM:
                mView.showRecipeDetail(position, mRecipes.get(position));
                break;
            case FOOD2FORK:
                mView.showLoading();
                mView.showMessage(Message.NOTICE_LOADING_RECIPE_DETAILS, SnackbarUtil.SnackbarType.NOTICE);
                mFood2ForkApi.getRecipeById(mRecipes.get(position).getExtraId()).enqueue(new Callback<Food2ForkGetResponse>() {
                    @Override
                    public void onResponse(Call<Food2ForkGetResponse> call, Response<Food2ForkGetResponse> response) {
                        mView.hideLoading();
                        mRecipes.get(position).getRecipeIngredients().addAll(RecipeUtil.getRecipeIngredientFromFood2ForkRecipe(response.body().getRecipe()));
                        mView.showRecipeDetail(position, mRecipes.get(position));
                    }

                    @Override
                    public void onFailure(Call<Food2ForkGetResponse> call, Throwable t) {
                        mView.hideLoading();
                        mView.showMessage(Message.ERROR_LOADING_RECIPE, SnackbarUtil.SnackbarType.ERROR);
                    }
                });
                break;
        }
    }


    private class EdamamCallback implements Callback<EdamamResponse> {

        @Override
        public void onResponse(Call<EdamamResponse> call, Response<EdamamResponse> response) {

            if (response.code() != 200) {
                if(mView != null)
                    mView.showMessage(Message.ERROR_LOADING_ONLINE_RECIPES, SnackbarUtil.SnackbarType.ERROR);
            } else {
                mRecipes = RecipeUtil.getRecipesFromEdamamResponse(response);

                if( mView != null)
                    mView.showRecipes(mRecipes);
            }
            if(mView != null)
                mView.hideLoading();
        }

        @Override
        public void onFailure(Call<EdamamResponse> call, Throwable t) {
            mView.hideLoading();
            mView.showMessage(Message.ERROR_LOADING_ONLINE_RECIPES, SnackbarUtil.SnackbarType.ERROR);
        }
    }

    private class Food2ForkCallback implements Callback<Food2ForkResponse> {

        @Override
        public void onResponse(Call<Food2ForkResponse> call, Response<Food2ForkResponse> response) {

            if (response.code() != 200) {
                if(mView != null)
                    mView.showMessage(Message.ERROR_LOADING_ONLINE_RECIPES, SnackbarUtil.SnackbarType.ERROR);
            } else {
                mRecipes = RecipeUtil.getRecipesFromFood2ForkResponse(response);

                if( mView != null)
                    mView.showRecipes(mRecipes);
            }
            if(mView != null)
                mView.hideLoading();
        }

        @Override
        public void onFailure(Call<Food2ForkResponse> call, Throwable t) {
            mView.hideLoading();
            mView.showMessage(Message.ERROR_LOADING_ONLINE_RECIPES, SnackbarUtil.SnackbarType.ERROR);
        }
    }
}
