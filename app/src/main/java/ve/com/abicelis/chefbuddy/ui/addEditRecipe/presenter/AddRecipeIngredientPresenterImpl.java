package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddRecipeIngredientView;

/**
 * Created by abicelis on 16/7/2017.
 */

public class AddRecipeIngredientPresenterImpl implements AddRecipeIngredientPresenter {

    private AddRecipeIngredientView mView;
    private ChefBuddyDAO mDao;

    public AddRecipeIngredientPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void attachView(AddRecipeIngredientView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getIngredients() {
        try {
            List<Ingredient> ingredients = mDao.getIngredients();
            mView.populateIngredientSpinner(ingredients);
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.ERROR_LOADING_INGREDIENTS);
        }
    }

}
