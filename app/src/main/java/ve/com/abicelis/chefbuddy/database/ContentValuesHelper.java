package ve.com.abicelis.chefbuddy.database;

import android.content.ContentValues;

import java.util.Calendar;

import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;

/**
 * Created by abicelis on 30/7/2017.
 */

public class ContentValuesHelper {

    public static ContentValues getValuesForRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(ChefBuddyContract.RecipeTable.COLUMN_NAME.getName(), recipe.getName());
        values.put(ChefBuddyContract.RecipeTable.COLUMN_SERVINGS.getName(), recipe.getServings().name());
        values.put(ChefBuddyContract.RecipeTable.COLUMN_PREPARATION_TIME.getName(), recipe.getPreparationTime().name());
        values.put(ChefBuddyContract.RecipeTable.COLUMN_DIRECTIONS.getName(), recipe.getDirections());
        values.put(ChefBuddyContract.RecipeTable.COLUMN_IMAGE_FILENAMES.getName(), recipe.getImageFilenamesStr());
        return values;
    }

    public static ContentValues getWheelRecipeValuesForRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(ChefBuddyContract.WheelRecipeTable.COLUMN_RECIPE.getName(), recipe.getId());
        return values;
    }

    public static ContentValues getDailyRecipeValues(Calendar date, long recipeId) {
        ContentValues values = new ContentValues();
        values.put(ChefBuddyContract.DailyRecipeTable.COLUMN_DAY.getName(), date.get(Calendar.DAY_OF_MONTH));
        values.put(ChefBuddyContract.DailyRecipeTable.COLUMN_MONTH.getName(), date.get(Calendar.MONTH));
        values.put(ChefBuddyContract.DailyRecipeTable.COLUMN_YEAR.getName(), date.get(Calendar.YEAR));
        values.put(ChefBuddyContract.DailyRecipeTable.COLUMN_RECIPE_FK.getName(), recipeId);
        return values;
    }


    public static ContentValues getValuesForRecipeIngredient(long recipeId, RecipeIngredient recipeIngredient) {
        ContentValues values = new ContentValues();
        values.put(ChefBuddyContract.RecipeIngredientTable.COLUMN_RECIPE_FK.getName(), recipeId);
        values.put(ChefBuddyContract.RecipeIngredientTable.COLUMN_INGREDIENT_FK.getName(), recipeIngredient.getIngredient().getId());
        values.put(ChefBuddyContract.RecipeIngredientTable.COLUMN_AMOUNT.getName(), recipeIngredient.getAmount());
        values.put(ChefBuddyContract.RecipeIngredientTable.COLUMN_MEASUREMENT.getName(), recipeIngredient.getMeasurement().name());
        return values;
    }

    public static ContentValues getValuesForIngredient(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put(ChefBuddyContract.IngredientTable.COLUMN_NAME.getName(), ingredient.getName());
        return values;
    }

}
