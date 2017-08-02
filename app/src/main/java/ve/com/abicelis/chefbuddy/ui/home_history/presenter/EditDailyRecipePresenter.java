package ve.com.abicelis.chefbuddy.ui.home_history.presenter;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

import ve.com.abicelis.chefbuddy.ui.home_history.view.EditDailyRecipeView;
import ve.com.abicelis.chefbuddy.ui.home_history.view.HistoryView;

/**
 * Created by abicelis on 1/8/2017.
 */

public interface EditDailyRecipePresenter {
    void attachView(EditDailyRecipeView view);
    void detachView();

    void setDailyRecipeData(Calendar calendar, long recipeId);
    void getRecipes();
    void setRecipe(int position);
    void saveAndDismiss();

}
