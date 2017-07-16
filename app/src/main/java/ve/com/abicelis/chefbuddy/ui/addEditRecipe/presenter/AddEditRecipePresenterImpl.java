package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;

/**
 * Created by abicelis on 16/7/2017.
 */

public class AddEditRecipePresenterImpl implements AddEditRecipePresenter {

    private AddEditRecipeView mView;
    private ChefBuddyDAO mDao;

    public AddEditRecipePresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void attachView(AddEditRecipeView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getRecipe(long recipeId) {
        try {
            Recipe recipe = mDao.getRecipe(recipeId);
            mView.showRecipe(recipe);
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.RECIPE_DETAIL_ACTIVITY_ERROR_LOADING_RECIPE);
        }
    }
}
