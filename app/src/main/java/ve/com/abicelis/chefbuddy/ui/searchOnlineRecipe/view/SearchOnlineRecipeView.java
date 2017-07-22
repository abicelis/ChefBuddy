package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view;

import retrofit2.Response;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;

/**
 * Created by abicelis on 21/7/2017.
 */

public interface SearchOnlineRecipeView {
    void showLoading();
    void hideLoading();
    void showQueryRawResults(Response<EdamamResponse> response);
    void showErrorMessage(Message message);
}
