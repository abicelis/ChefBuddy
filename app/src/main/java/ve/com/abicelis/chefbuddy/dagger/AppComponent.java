package ve.com.abicelis.chefbuddy.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ve.com.abicelis.chefbuddy.ui.home.HomeActivity;
import ve.com.abicelis.chefbuddy.ui.home.presenter.HomePresenterImpl;
import ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList.RecipeListFragment;
import ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList.presenter.RecipeListPresenterImpl;
import ve.com.abicelis.chefbuddy.ui.main.MainActivity;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeDetailActivity;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenterImpl;

/**
 * Created by abicelis on 5/7/2017.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class, DatabaseModule.class})
public interface AppComponent {
    void inject(MainActivity target);

    void inject(HomeActivity target);
    void inject(HomePresenterImpl target);

    void inject(RecipeListFragment target);
    void inject(RecipeListPresenterImpl target);

    void inject(RecipeDetailActivity target);
    void inject(RecipeDetailPresenterImpl target);

}
