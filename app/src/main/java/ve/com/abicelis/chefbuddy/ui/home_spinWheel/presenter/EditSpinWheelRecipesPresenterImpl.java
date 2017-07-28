package ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.CheckedRecipe;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.view.EditSpinWheelRecipesView;

/**
 * Created by abicelis on 27/7/2017.
 */

public class EditSpinWheelRecipesPresenterImpl implements EditSpinWheelRecipesPresenter {

    //DATA
    EditSpinWheelRecipesView mView;
    ChefBuddyDAO mDao;

    public EditSpinWheelRecipesPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }


    @Override
    public void attachView(EditSpinWheelRecipesView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getRecipes() {
        try {
            List<CheckedRecipe> checkedRecipes = new ArrayList<>();
            List<Recipe> recipes = mDao.getRecipes();
            long[] wheelRecipeIds = mDao.getWheelRecipeIds();

            for (Recipe r : recipes)
                checkedRecipes.add(new CheckedRecipe(contains(wheelRecipeIds, r.getId()), r));

            mView.showRecipes(checkedRecipes);
        }catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.ERROR_LOADING_RECIPES);
        }
    }

    @Override
    public void setWheelRecipes(List<CheckedRecipe> checkedRecipes) {

        List<Recipe> wheelRecipes = new ArrayList<>();
        for(CheckedRecipe r : checkedRecipes) {
            if(r.getChecked())
                wheelRecipes.add(r.getRecipe());
        }

        //Check if between min and max
        if(wheelRecipes.size() > Constants.MAX_SPIN_WHEEL_RECIPE_AMOUNT) {
            mView.showErrorMessage(String.format(Locale.getDefault(), Message.INVALID_WHEEL_RECIPE_AMOUNT_TOO_MANY.getFriendlyName(), Constants.MAX_SPIN_WHEEL_RECIPE_AMOUNT));
        } else if(wheelRecipes.size() < Constants.MIN_SPIN_WHEEL_RECIPE_AMOUNT) {
            mView.showErrorMessage(String.format(Locale.getDefault(), Message.INVALID_WHEEL_RECIPE_AMOUNT_TOO_FEW.getFriendlyName(), Constants.MIN_SPIN_WHEEL_RECIPE_AMOUNT));
        } else {
            mDao.updateWheelRecipes(wheelRecipes);
            mView.wheelRecipesSetSoDismiss();
        }
    }


    private boolean contains(long[] values, long value) {
        for (long v : values) {
            if(value == v)
                return true;
        }
        return false;
    }
}
