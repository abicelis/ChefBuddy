package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.Servings;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;

/**
 * Created by abicelis on 16/7/2017.
 */

public class AddEditRecipePresenterImpl implements AddEditRecipePresenter {

    //DATA
    private boolean mEditingExistingRecipe = false;
    private Recipe mExistingRecipe;
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
    public void editingExistingRecipe(long recipeId) {
        mEditingExistingRecipe = true;
        try {
            mExistingRecipe = mDao.getRecipe(recipeId);
            mView.showRecipe(mExistingRecipe);
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);
        }
    }

    @Override
    public int getServingsSelection() {
        if(!mEditingExistingRecipe)
            return -1;
        return mExistingRecipe.getServings().ordinal();
    }

    @Override
    public int getPreparationTimeSelection() {
        if(!mEditingExistingRecipe)
            return -1;
        return mExistingRecipe.getPreparationTime().ordinal();
    }
}
