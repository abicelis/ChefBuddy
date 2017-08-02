package ve.com.abicelis.chefbuddy.ui.home_history.presenter;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.home_history.view.EditDailyRecipeView;
import ve.com.abicelis.chefbuddy.ui.home_history.view.HistoryView;

/**
 * Created by abicelis on 1/8/2017.
 */

public class EditDailyRecipePresenterImpl implements EditDailyRecipePresenter {

    //DATA
    private ChefBuddyDAO mDao;
    private EditDailyRecipeView mView;
    private Calendar mDate;
    private List<Recipe> mRecipes = new ArrayList<>();
    private long mSelectedRecipeId = -1; //Default to first recipe

    public EditDailyRecipePresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }


    @Override
    public void attachView(EditDailyRecipeView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void setDailyRecipeData(Calendar calendar, long recipeId) {
        mDate = calendar;
        if(recipeId != -1)
            mSelectedRecipeId = recipeId;
    }


    @Override
    public void getRecipes() {
        try {
            mRecipes.clear();
            mRecipes.add(new Recipe());
            mRecipes.addAll(mDao.getRecipes());
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.ERROR_LOADING_RECIPES);
        }

        int position = 0;
        if(mSelectedRecipeId != -1) {
            for (int i = 0; i < mRecipes.size(); i++) {
                if (mSelectedRecipeId == mRecipes.get(i).getId()) {
                    position = i;
                    break;
                }
            }
            if(position == 0) {
                mSelectedRecipeId = mRecipes.get(0).getId();
            }
        }


        mView.populateRecipeSpinner(mRecipes, position);
    }


@Override
    public void setRecipe(int position) {
        mSelectedRecipeId = mRecipes.get(position).getId();
    }

    @Override
    public void saveAndDismiss() {
        mDao.updateDailyRecipeForDate(mDate, mSelectedRecipeId);
        mView.recipeSavedSoDismiss(mDate);
    }

}
