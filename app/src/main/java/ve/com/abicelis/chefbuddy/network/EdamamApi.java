package ve.com.abicelis.chefbuddy.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;

/**
 * Created by abicelis on 21/7/2017.
 */

public interface EdamamApi {

    @Headers({
            "X-Mashape-Key: " + Constants.EDAMAM_API_KEY,
            "Accept: application/json"
    })
    @GET("search")
    Call<EdamamResponse> searchRecipesByName(@Query("q") String query);
}
