package ve.com.abicelis.chefbuddy.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.model.food2fork.Food2ForkGetResponse;
import ve.com.abicelis.chefbuddy.model.food2fork.Food2ForkResponse;

/**
 * Created by abicelis on 21/7/2017.
 */

public interface Food2ForkApi {

    @GET("search?key=" + Constants.FOOD2FORK_API_KEY)
    Call<Food2ForkResponse> searchRecipesByName(@Query("q") String query);

    @GET("get?key=" + Constants.FOOD2FORK_API_KEY)
    Call<Food2ForkGetResponse> getRecipeById(@Query("rId") String recipeId);
}
