package ve.com.abicelis.chefbuddy.ui.home_history;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.model.DailyRecipe;

/**
 * Created by abicelis on 1/8/2017.
 */

public class RecipeDecorator implements DayViewDecorator {

    //DATA
    private List<DailyRecipe> dailyRecipes = new ArrayList<>();


    public void setDailyRecipes(List<DailyRecipe> recipes) {
        dailyRecipes.clear();
        dailyRecipes.addAll(recipes);
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        for (DailyRecipe dr : dailyRecipes)
            if(CalendarDay.from(dr.getDate()).equals(day))
                return true;
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan(new DotSpan(7, Color.RED));
    }
}
