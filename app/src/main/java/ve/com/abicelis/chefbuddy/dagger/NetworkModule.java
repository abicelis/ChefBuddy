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


/**
 * Created by abicelis on 21/7/2017.
 */

@Module
public class NetworkModule {
    private static final String EDAMAM_BASE_URL = "EDAMAM_BASE_URL";

    @Provides
    @Named(EDAMAM_BASE_URL)
    String provideBaseUrlString() {
        return Constants.EDAMAM_BASE_URL;
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, @Named(EDAMAM_BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build();
    }

    @Provides
    @Singleton
    EdamamApi providesEdamamApi(Retrofit retrofit) {
        return retrofit.create(EdamamApi.class);
    }

}
