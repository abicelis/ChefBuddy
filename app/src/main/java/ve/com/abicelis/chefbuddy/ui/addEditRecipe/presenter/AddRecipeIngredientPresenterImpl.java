package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddRecipeIngredientView;
import ve.com.abicelis.chefbuddy.util.StringUtil;

/**
 * Created by abicelis on 16/7/2017.
 */

public class AddRecipeIngredientPresenterImpl implements AddRecipeIngredientPresenter {

    //DATA
    private List<Ingredient> mIngredients;
    private Ingredient mSelectedIngredient;
    private Measurement mSelectedMeasurement;


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
            mIngredients = mDao.getIngredients();
            if(mView != null)
                mView.populateIngredientSpinner(mIngredients);
        } catch (CouldNotGetDataException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_LOADING_INGREDIENTS);
        }
    }

    @Override
    public void setSelectedIngredient(int position) {
        mSelectedIngredient = mIngredients.get(position);
    }

    @Override
    public void setSelectedMeasurement(Measurement measurement) {
        mSelectedMeasurement = measurement;
    }

    @Override
    public void checkRecipeIngredientValues(String amount, String ingredientName) {
        ingredientName = ingredientName.trim();
        amount = amount.trim();

        if(ingredientName.isEmpty()) {
            mView.showErrorMessage(Message.INVALID_RECIPE_INGREDIENT_NAME);
        } else {
            if(mSelectedIngredient == null || !mSelectedIngredient.getName().equals(ingredientName) ) { //Ingredient was typed manually

                //Check if it really is new
                mSelectedIngredient = null;
                for (Ingredient i : mIngredients) {
                    if(i.getName().toLowerCase().equals(ingredientName.toLowerCase())) {
                        mSelectedIngredient = i;
                        break;
                    }
                }

                if(mSelectedIngredient != null)     //Found manually typed ingredient in DB
                    mView.recipeIngredientSelectedSoDismiss(new RecipeIngredient(amount, mSelectedMeasurement, mSelectedIngredient));
                else                                //Ingredient is truly new
                    mView.recipeIngredientSelectedSoDismiss(new RecipeIngredient(amount, mSelectedMeasurement, new Ingredient(StringUtil.startWithUppercase(ingredientName))));
            } else {        //Ingredient was selected
                mView.recipeIngredientSelectedSoDismiss(new RecipeIngredient(amount, mSelectedMeasurement, mSelectedIngredient));
            }
        }


    }


}
