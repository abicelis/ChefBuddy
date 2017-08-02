package ve.com.abicelis.chefbuddy.model;


import java.util.Calendar;

/**
 * Created by abicelis on 27/7/2017.
 */

public class DailyRecipe {

    private Calendar date;
    private Recipe recipe;

    public DailyRecipe(Calendar date, Recipe recipe) {
        this.date = date;
        this.recipe = recipe;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
    public Calendar getDate() {
        return date;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
    public Recipe getRecipe() {
        return recipe;
    }
}
