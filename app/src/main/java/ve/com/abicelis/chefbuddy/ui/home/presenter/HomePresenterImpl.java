package ve.com.abicelis.chefbuddy.ui.home.presenter;

import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.ui.home.view.HomeView;

/**
 * Created by abicelis on 8/7/2017.
 */

public class HomePresenterImpl implements HomePresenter {

    ChefBuddyDAO mDao;
    private HomeView view;

    public HomePresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void attachView(HomeView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

}
