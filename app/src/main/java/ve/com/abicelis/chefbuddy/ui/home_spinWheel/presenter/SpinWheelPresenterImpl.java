package ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.view.SpinWheelView;

/**
 * Created by abicelis on 27/7/2017.
 */

public class SpinWheelPresenterImpl implements SpinWheelPresenter {

    //DATA
    private SpinWheelView mView;
    private ChefBuddyDAO mDao;
    private List<Recipe> mWheelRecipes;

    public SpinWheelPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }



    @Override
    public void attachView(SpinWheelView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getWheelRecipes() {
        //Check if the user already selected recipes to go on the wheel
        mWheelRecipes = new ArrayList<>();

        // TODO: 27/7/2017 implement this
        //mWheelRecipes = mDao.getWheelRecipes();
        // TODO: 27/7/2017 make sure the recipes exist in the db and the user did not just delete a recipe
        // TODO: In this dao method grab the ids of the recipes, grab recipes and if not exist dont send them here to this presenter

        if(mWheelRecipes.size() > 0) {
            mView.refreshView(mWheelRecipes);
        } else {

            //Check if there are recipes in the app
            try {
                mWheelRecipes = mDao.getRecipes();
            } catch (CouldNotGetDataException e) {
                mView.showErrorMessage(Message.ERROR_LOADING_RECIPES);
            }

            if(mWheelRecipes.size() > 0) {

                //Grab the first 6 recipes if list is too long
                if(mWheelRecipes.size() > Constants.DEFAULT_SPIN_WHEEL_RECIPE_AMOUNT)
                    mWheelRecipes = new ArrayList<>(mWheelRecipes.subList(0, Constants.DEFAULT_SPIN_WHEEL_RECIPE_AMOUNT-1));

                //Save the wheel recipes and populate wheel
                // TODO: 27/7/2017 implement this
                //mWheelRecipes = mDao.saveWheelRecipes(mWheelRecipes);
                mView.refreshView(mWheelRecipes);
            } else {
                mView.noRecipesAvailable();
            }
        }
    }

    @Override
    public void wheelSettledAtPosition(int position) {
        mView.updateBottomRecipe(mWheelRecipes.get(position));
    }
}
