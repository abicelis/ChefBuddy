package ve.com.abicelis.chefbuddy.ui.home;

import javax.inject.Inject;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;

/**
 * Created by abicelis on 8/7/2017.
 */

public class HomePresenterImpl implements HomePresenter {

    @Inject
    ChefBuddyDAO mDao;
    private HomeView view;

    public HomePresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void setView(HomeView view) {
        this.view = view;
    }

    @Override
    public void getRecipes() {
        try {
            view.showRecipes(mDao.getRecipes());
        } catch (CouldNotGetDataException e) {
            view.showErrorMessage(Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES);
        }
    }
}
