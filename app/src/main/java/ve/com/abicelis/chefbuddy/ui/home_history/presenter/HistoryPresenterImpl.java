package ve.com.abicelis.chefbuddy.ui.home_history.presenter;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.DailyRecipe;
import ve.com.abicelis.chefbuddy.ui.home_history.view.HistoryView;
import ve.com.abicelis.chefbuddy.util.CalendarUtil;

/**
 * Created by abicelis on 1/8/2017.
 */

public class HistoryPresenterImpl implements HistoryPresenter {

    //DATA
    private HistoryView mView;
    private ChefBuddyDAO mDao;
    private List<DailyRecipe> mDailyRecipes;

    public HistoryPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }


    @Override
    public void attachView(HistoryView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getDailyRecipes() {
        try {
            mDailyRecipes = mDao.getDailyRecipes();
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.ERROR_LOADING_RECIPES);
            return;
        }
        mView.setCalendarDecorations(mDailyRecipes);
    }

    @Override
    public void daySelected(CalendarDay calendar) {

        for (DailyRecipe dr : mDailyRecipes) {
            CalendarDay cd = CalendarDay.from(dr.getDate());
            if (calendar.equals(cd)) {
                mView.displayDailyRecipeDetails(dr);
                return;
            }
        }

        mView.displayNoDailyRecipe(CalendarUtil.getZeroedCalendarFromYearMonthDay(calendar.getYear(), calendar.getMonth(), calendar.getDay()));
    }

    @Override
    public void dailyRecipeUpdated(Calendar calendar) {
        //Refresh recipes, refresh decorations
        getDailyRecipes();

        DailyRecipe dailyRecipe;
        try {
            dailyRecipe = mDao.getDailyRecipeForDate(calendar);
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);
            return;
        }

        if(dailyRecipe == null)
            mView.displayNoDailyRecipe(calendar);
        else
            mView.displayDailyRecipeDetails(dailyRecipe);
    }


}
