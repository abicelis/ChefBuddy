package ve.com.abicelis.chefbuddy.ui.home.fragment;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;

/**
 * Created by abicelis on 8/7/2017.
 */

public class RecipeListPresenterImpl implements RecipeListPresenter {

    ChefBuddyDAO mDao;
    private RecipeListView mView;

    public RecipeListPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void attachView(RecipeListView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getRecipes() {
        try {
            mView.showRecipes(mDao.getRecipes());
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES);
        }
    }
}
