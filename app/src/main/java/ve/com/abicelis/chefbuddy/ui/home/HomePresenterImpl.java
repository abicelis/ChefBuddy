package ve.com.abicelis.chefbuddy.ui.home;

import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;

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
