package ve.com.abicelis.chefbuddy.database;

import android.provider.BaseColumns;

/**
 * Created by abicelis on 3/7/2017.
 */
final class ChefBuddyContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    ChefBuddyContract() { }

    /* RecipeIngredient Table  */
    static abstract class IngredientTable {
        static final String TABLE_NAME = "ingredient";
        static final TableColumn COLUMN_ID = new TableColumn(DataType.INTEGER, "ingredient_id");
        static final TableColumn COLUMN_NAME = new TableColumn(DataType.TEXT, "name");
    }

    /* Recipe Table  */
    static abstract class RecipeTable {
        static final String TABLE_NAME = "recipe";

        static final TableColumn COLUMN_ID = new TableColumn(DataType.INTEGER, "recipe_id");
        static final TableColumn COLUMN_NAME = new TableColumn(DataType.TEXT, "name");
        static final TableColumn COLUMN_SERVINGS = new TableColumn(DataType.INTEGER, "servings");
        static final TableColumn COLUMN_PREPARATION_TIME = new TableColumn(DataType.TEXT, "preparation_time");
        static final TableColumn COLUMN_DIRECTIONS = new TableColumn(DataType.TEXT, "directions");
        static final TableColumn COLUMN_IMAGE_FILENAMES = new TableColumn(DataType.TEXT, "image_filenames");
    }

    /* RecipeIngredient Table  */
    static abstract class RecipeIngredientTable {
        static final String TABLE_NAME = "recipe_ingredient";

        static final TableColumn COLUMN_ID = new TableColumn(DataType.INTEGER, "recipe_ingredient_id");
        static final TableColumn COLUMN_RECIPE_FK = new TableColumn(DataType.TEXT, "fk_recipe");
        static final TableColumn COLUMN_INGREDIENT_FK = new TableColumn(DataType.TEXT, "fk_ingredient");
        static final TableColumn COLUMN_AMOUNT = new TableColumn(DataType.TEXT, "amount");
        static final TableColumn COLUMN_MEASUREMENT = new TableColumn(DataType.TEXT, "measurement");
    }

    /* Daily Recipe Table */
    static abstract class DailyRecipeTable {
        static final String TABLE_NAME = "daily_recipe";

        static final TableColumn COLUMN_ID = new TableColumn(DataType.INTEGER, "daily_recipe_id");
        static final TableColumn COLUMN_YEAR = new TableColumn(DataType.INTEGER, "year");
        static final TableColumn COLUMN_MONTH = new TableColumn(DataType.INTEGER, "month");
        static final TableColumn COLUMN_DAY = new TableColumn(DataType.INTEGER, "date");
        static final TableColumn COLUMN_RECIPE_FK = new TableColumn(DataType.INTEGER, "fk_recipe");
    }

    /* Wheel Recipe Table */
    static abstract class WheelRecipeTable {
        static final String TABLE_NAME = "wheel_recipe";

        static final TableColumn COLUMN_ID = new TableColumn(DataType.INTEGER, "wheel_recipe_id");
        static final TableColumn COLUMN_RECIPE = new TableColumn(DataType.INTEGER, "recipe_fk");
    }

}
