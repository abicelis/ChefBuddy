package ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.view.RecipeDetailView;

/**
 * Created by abicelis on 14/7/2017.
 */

public class RecipeDetailPresenterImpl implements RecipeDetailPresenter {

    private RecipeDetailView mView;
    private ChefBuddyDAO mDao;

    public RecipeDetailPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }


    @Override
    public void attachView(RecipeDetailView view) {
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
