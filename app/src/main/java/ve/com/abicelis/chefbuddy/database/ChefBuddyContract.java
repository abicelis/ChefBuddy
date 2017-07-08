package ve.com.abicelis.chefbuddy.database;

import android.provider.BaseColumns;

/**
 * Created by abicelis on 3/7/2017.
 */
final class ChefBuddyContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    ChefBuddyContract() { }

    /* Ingredient Table  */
    static abstract class IngredientTable implements BaseColumns {
        static final String TABLE_NAME = "ingredient";
        static final TableColumn COL_NAME_NAME = new TableColumn(DataType.TEXT, "name");
    }

    /* Recipe Table  */
    static abstract class RecipeTable implements BaseColumns {
        static final String TABLE_NAME = "recipe";

        static final TableColumn COL_NAME_NAME = new TableColumn(DataType.TEXT, "name");
        static final TableColumn COL_NAME_SERVINGS = new TableColumn(DataType.INTEGER, "servings");
        static final TableColumn COL_NAME_PREPARATION_TIME = new TableColumn(DataType.TEXT, "preparation_time");
        static final TableColumn COL_NAME_PREPARATION_TIME_TYPE = new TableColumn(DataType.TEXT, "preparation_time_type");
        static final TableColumn COL_NAME_DIRECTIONS = new TableColumn(DataType.TEXT, "directions");
        static final TableColumn COL_NAME_FEATURED_IMAGE = new TableColumn(DataType.BLOB, "featured_image");
    }

    /* RecipeIngredient Table  */
    static abstract class RecipeIngredientTable implements BaseColumns {
        static final String TABLE_NAME = "recipe_ingredient";

        static final TableColumn COL_NAME_RECIPE_FK = new TableColumn(DataType.TEXT, "fk_recipe");
        static final TableColumn COL_NAME_INGREDIENT_FK = new TableColumn(DataType.TEXT, "fk_ingredient");
        static final TableColumn COL_NAME_AMOUNT = new TableColumn(DataType.TEXT, "amount");
        static final TableColumn COL_NAME_MEASUREMENT = new TableColumn(DataType.TEXT, "measurement");
    }

    /* Daily Recipe Table */
    static abstract class DailyRecipeTable implements BaseColumns {
        static final String TABLE_NAME = "daily_recipe";

        static final TableColumn COL_NAME_YEAR = new TableColumn(DataType.INTEGER, "year");
        static final TableColumn COL_NAME_MONTH = new TableColumn(DataType.INTEGER, "month");
        static final TableColumn COL_NAME_DAY = new TableColumn(DataType.INTEGER, "date");
        static final TableColumn COL_NAME_RECIPE_FK = new TableColumn(DataType.INTEGER, "fk_recipe");
    }

}
