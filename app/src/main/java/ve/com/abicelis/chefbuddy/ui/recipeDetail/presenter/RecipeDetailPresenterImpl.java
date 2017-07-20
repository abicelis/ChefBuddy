package ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotDeleteDataException;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.view.RecipeDetailView;

/**
 * Created by abicelis on 14/7/2017.
 */

public class RecipeDetailPresenterImpl implements RecipeDetailPresenter {

    private long mRecipeId = -1;
    private Recipe mLoadedRecipe;
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
    public void setRecipeId(long recipeId) {
        mRecipeId = recipeId;
        mLoadedRecipe = null;
    }

    @Override
    public void reloadRecipe() {
        if(mRecipeId == -1)
            mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);

        try {
            mLoadedRecipe = mDao.getRecipe(mRecipeId);
            if(mView != null)
                mView.showRecipe(mLoadedRecipe);
        } catch (CouldNotGetDataException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);
        }
    }

    @Override
    public void deleteRecipe() {
        if(mRecipeId == -1) {
            if (mView != null) {
                mView.showErrorMessage(Message.ERROR_DELETING_RECIPE);
                return;
            }
        }

        try {
            mDao.deleteRecipe(mRecipeId);
            mView.recipeDeletedSoFinish();
        } catch (CouldNotDeleteDataException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_DELETING_RECIPE);
        }
    }

    @Override
    public Recipe getLoadedRecipe() {
        return mLoadedRecipe;
    }

}
