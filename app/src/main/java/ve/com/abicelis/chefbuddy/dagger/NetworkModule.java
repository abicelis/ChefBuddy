package ve.com.abicelis.chefbuddy.dagger;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.network.EdamamApi;
import ve.com.abicelis.chefbuddy.network.Food2ForkApi;


/**
 * Created by abicelis on 21/7/2017.
 */

@Module
public class NetworkModule {
    private static final String EDAMAM_BASE_URL = "EDAMAM_BASE_URL";
    private static final String FOOD_2_FORK_BASE_URL = "FOOD_2_FORK_BASE_URL";

    private static final String EDAMAM_RETROFIT = "EDAMAM_RETROFIT";
    private static final String FOOD_2_FORK_RETROFIT = "FOOD_2_FORK_RETROFIT";

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }


    /**
     * INJECTION GRAPH FOR EDAMAM API
     */

    @Provides
    @Named(EDAMAM_BASE_URL)
    String provideEdamamBaseUrlString() {
        return Constants.EDAMAM_BASE_URL;
    }

    @Provides
    @Singleton
    @Named(EDAMAM_RETROFIT)
    Retrofit provideEdamamRetrofit(Converter.Factory converter, @Named(EDAMAM_BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build();
    }

    @Provides
    @Singleton
    EdamamApi providesEdamamApi(@Named(EDAMAM_RETROFIT) Retrofit retrofit) {
        return retrofit.create(EdamamApi.class);
    }


    /**
     * INJECTION GRAPH FOR FOOD_2_FORK API
     */

    @Provides
    @Named(FOOD_2_FORK_BASE_URL)
    String provideFood2ForkBaseUrlString() {
        return Constants.FOOD2FORK_BASE_URL;
    }


    @Provides
    @Singleton
    @Named(FOOD_2_FORK_RETROFIT)
    Retrofit provideRetrofit(Converter.Factory converter, @Named(FOOD_2_FORK_BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build();
    }



    @Provides
    @Singleton
    Food2ForkApi providesFood2ForkApi(@Named(FOOD_2_FORK_RETROFIT) Retrofit retrofit) {
        return retrofit.create(Food2ForkApi.class);
    }

}
