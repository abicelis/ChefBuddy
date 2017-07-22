package ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotDeleteDataException;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.view.RecipeDetailView;

/**
 * Created by abicelis on 14/7/2017.
 */

public class RecipeDetailPresenterImpl implements RecipeDetailPresenter {

    private RecipeSource mRecipeSource;
    private Recipe mRecipe = null;

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
    public void setSourceData(RecipeSource recipeSource, Recipe recipe) {
        mRecipeSource = recipeSource;
        mRecipe = recipe;
        mView.initViews(mRecipeSource);
    }


    @Override
    public void reloadRecipe() {
        switch (mRecipeSource) {
            case DATABASE:
                try {
                    mRecipe = mDao.getRecipe(mRecipe.getId());
                    mView.showRecipe(mRecipe, mRecipeSource);
                } catch (CouldNotGetDataException e) {
                    mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);
                }
                break;
            case ONLINE:
                mView.showRecipe(mRecipe, mRecipeSource);
                break;
        }
    }

    @Override
    public void deleteRecipe() {
        switch (mRecipeSource) {
            case DATABASE:
                try {
                    mDao.deleteRecipe(mRecipe.getId());
                    mView.showRecipe(mRecipe, mRecipeSource);
                } catch (CouldNotDeleteDataException e) {
                    mView.showErrorMessage(Message.ERROR_DELETING_RECIPE);
                    mView.recipeDeletedSoFinish();
                }
                break;
            case ONLINE:
                mView.showErrorMessage(Message.ERROR_DELETING_RECIPE);
                break;
        }
    }

    @Override
    public Recipe getLoadedRecipe() {
        return mRecipe;
    }

}
