package ve.com.abicelis.chefbuddy.ui.home.presenter;

import ve.com.abicelis.chefbuddy.ui.home.view.HomeView;

/**
 * Created by abicelis on 8/7/2017.
 */

public interface HomePresenter {
    void attachView(HomeView view);
    void detachView();

    //void getRecipes();
}
