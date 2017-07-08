package ve.com.abicelis.chefbuddy.ui.home;

import java.util.List;

import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 8/7/2017.
 */

public interface HomePresenter {
    void setView(HomeView view);

    void getRecipes();
}
