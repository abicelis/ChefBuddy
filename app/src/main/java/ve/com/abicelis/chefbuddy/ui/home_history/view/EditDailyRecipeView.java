package ve.com.abicelis.chefbuddy.ui.home_history.view;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.DailyRecipe;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 1/8/2017.
 */

public interface EditDailyRecipeView {

    void populateRecipeSpinner(List<Recipe> recipes, int selection);

    void recipeSavedSoDismiss(Calendar date);

    void showErrorMessage(Message message);
}
