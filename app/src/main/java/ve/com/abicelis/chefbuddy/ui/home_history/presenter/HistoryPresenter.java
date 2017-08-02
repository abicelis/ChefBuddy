package ve.com.abicelis.chefbuddy.ui.home_history.presenter;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

import ve.com.abicelis.chefbuddy.ui.home_history.view.HistoryView;

/**
 * Created by abicelis on 1/8/2017.
 */

public interface HistoryPresenter {
    void attachView(HistoryView view);
    void detachView();

    void getDailyRecipes();
    void daySelected(CalendarDay calendar);

    void dailyRecipeUpdated(Calendar calendar);
}
