package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamRecipe;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;
import ve.com.abicelis.chefbuddy.network.EdamamApi;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view.SearchOnlineRecipeView;

/**
 * Created by abicelis on 21/7/2017.
 */

public class SearchOnlineRecipePresenterImpl implements SearchOnlineRecipePresenter {

    //DATA
    //EdamamResponse edamamResponse;
    SearchOnlineRecipeView mView;

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
    public void getQueryRawResults(String query) {
        if(mView != null)
            mView.showLoading();

        mEdamamApi.searchRecipesByName(query).enqueue(new Callback<EdamamResponse>() {
            @Override
            public void onResponse(Call<EdamamResponse> call, Response<EdamamResponse> response) {

                if (response.code() != 200) {
                    if(mView != null)
                        mView.showErrorMessage(Message.ERROR_LOADING_ONLINE_RECIPES);
                } else {

//                    List<FoodzItem> foodzItemList = Stream.of(response.body().getList().getItem())
//                            .filter(foodzItem -> !foodzItem.getName().contains("ERROR"))
//                            .collect(Collectors.toList());

                    if( mView != null)
                        mView.showQueryRawResults(response);
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
}
