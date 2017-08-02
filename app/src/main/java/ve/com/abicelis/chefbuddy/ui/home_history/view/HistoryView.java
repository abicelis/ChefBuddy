package ve.com.abicelis.chefbuddy.ui.home_history.view;


import java.util.Calendar;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.DailyRecipe;

/**
 * Created by abicelis on 1/8/2017.
 */

public interface HistoryView {
    void setCalendarDecorations(List<DailyRecipe> dailyRecipes);

    void displayDailyRecipeDetails(DailyRecipe dailyRecipe);
    void displayNoDailyRecipe(Calendar date);

    void showErrorMessage(Message message);
}
