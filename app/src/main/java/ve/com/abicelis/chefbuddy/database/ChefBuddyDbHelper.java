package ve.com.abicelis.chefbuddy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import ve.com.abicelis.chefbuddy.util.CalendarUtil;
import ve.com.abicelis.chefbuddy.util.FileUtil;


/**
 * Created by abicelis on 3/7/2017.
 */
public class ChefBuddyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ChefBuddy.db";
    private static final int DATABASE_VERSION = 1;                               // If you change the database schema, you must increment the database version.
    private static final String COMMA_SEP = ", ";

    private String mAppDbFilepath;
    private String mDbExternalBackupFilepath;


    public ChefBuddyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mAppDbFilepath =  context.getDatabasePath(DATABASE_NAME).getPath();
        mDbExternalBackupFilepath = Environment.getExternalStorageDirectory().getPath() + "/" + DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDatabase(sqLiteDatabase);
        insertMockData(sqLiteDatabase);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        deleteDatabase(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }


    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean exportDatabase() throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File appDatabase = new File(mAppDbFilepath);
        File backupDatabase = new File(mDbExternalBackupFilepath);

        if (appDatabase.exists()) {
            FileUtil.copyFile(new FileInputStream(appDatabase), new FileOutputStream(backupDatabase));
            return true;
        }
        return false;
    }

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase() throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File appDatabase = new File(mAppDbFilepath);
        File backupDatabase = new File(mDbExternalBackupFilepath);

        if (backupDatabase.exists()) {
            FileUtil.copyFile(new FileInputStream(backupDatabase), new FileOutputStream(appDatabase));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    private void insertMockData(SQLiteDatabase sqLiteDatabase) {
        String statement;

        //Insert mock Ingredients
        statement = "INSERT INTO " + ChefBuddyContract.IngredientTable.TABLE_NAME + " (" +
                ChefBuddyContract.IngredientTable._ID + COMMA_SEP +
                ChefBuddyContract.IngredientTable.COL_NAME_NAME.getName() +
                ") VALUES " +
                "(0, 'Mozarella Cheese')," +
                "(1, 'Flour')," +
                "(2, 'Oil')," +
                "(3, 'Ham')," +
                "(4, 'Chickpea')," +
                "(5, 'Lemon')," +
                "(6, 'Tahini')" +
                ";";
        sqLiteDatabase.execSQL(statement);


        //Insert mock Recipes
        statement = "INSERT INTO " + ChefBuddyContract.RecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeTable._ID + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_NAME.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_SERVINGS.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME_TYPE.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_DIRECTIONS.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_FEATURED_IMAGE.getName() +
                ") VALUES " +
                "(0, 'Pizza Gloria', 24, '3', 'HOUR', 'Preheat oven and a baking sheet to 220C/fan 200C. Mix strong bread flour, salt and fast-action yeast together in a large bowl. Quickly stir in lukewarm water and olive oil and bring together to a rough dough.', '')," +
                "(1, 'Hummus', 99, '90', 'MINUTE', '1. Put everything except the parsley in a food processor and begin to process; add the chickpea liquid or water as needed to allow the machine to produce a smooth puree.', '')" +
                ";";
        sqLiteDatabase.execSQL(statement);


        //Insert mock Ingredients
        statement = "INSERT INTO " + ChefBuddyContract.RecipeIngredientTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeIngredientTable._ID + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_RECIPE_FK.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_INGREDIENT_FK.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_AMOUNT.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_MEASUREMENT.getName() +
                ") VALUES " +
                "(0, 0, 0, '800', 'GRAM')," +
                "(1, 0, 1, '2', 'CUP')," +
                "(2, 0, 2, '1', 'TABLESPOON')," +
                "(3, 0, 3, '250', 'GRAM')," +
                "(4, 1, 4, '1', 'KILOGRAM')," +
                "(5, 1, 5, '5', 'NONE')," +
                "(6, 1, 6, '500', 'MILILITER')," +
                "(7, 1, 2, '200', 'CUBIC_CENTIMITER')" +
                ";";
        sqLiteDatabase.execSQL(statement);


        //Mock dates for Daily Recipes
        Calendar cal = CalendarUtil.getNewInstanceZeroedCalendar();
        Calendar today = Calendar.getInstance();
        Calendar todayPlusOne = Calendar.getInstance();
        Calendar todayPlusTwo = Calendar.getInstance();
        Calendar todayPlusThree = Calendar.getInstance();
        Calendar todayPlusFour = Calendar.getInstance();
        Calendar todayPlusFive = Calendar.getInstance();
        Calendar todayPlusSix = Calendar.getInstance();

        CalendarUtil.copyCalendar(cal, today);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        CalendarUtil.copyCalendar(cal, todayPlusOne);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        CalendarUtil.copyCalendar(cal, todayPlusTwo);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        CalendarUtil.copyCalendar(cal, todayPlusThree);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        CalendarUtil.copyCalendar(cal, todayPlusFour);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        CalendarUtil.copyCalendar(cal, todayPlusFive);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        CalendarUtil.copyCalendar(cal, todayPlusSix);


        //Insert mock Daily Recipes
        statement = "INSERT INTO " + ChefBuddyContract.DailyRecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.DailyRecipeTable._ID + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_YEAR.getName() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_MONTH.getName() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_DAY.getName() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_RECIPE_FK.getName() +
                ") VALUES " +
                "(0, " + today.get(Calendar.YEAR) + ", " + today.get(Calendar.MONTH) + ", " + today.get(Calendar.DAY_OF_MONTH) + ", 0)," +
                "(1, " + todayPlusOne.get(Calendar.YEAR) + ", " + todayPlusOne.get(Calendar.MONTH) + ", " + todayPlusOne.get(Calendar.DAY_OF_MONTH) + ", 1)," +
                "(2, " + todayPlusTwo.get(Calendar.YEAR) + ", " + todayPlusTwo.get(Calendar.MONTH) + ", " + todayPlusTwo.get(Calendar.DAY_OF_MONTH) + ", 0)," +
                "(3, " + todayPlusThree.get(Calendar.YEAR) + ", " + todayPlusThree.get(Calendar.MONTH) + ", " + todayPlusThree.get(Calendar.DAY_OF_MONTH) + ", 1)," +
                "(4, " + todayPlusFour.get(Calendar.YEAR) + ", " + todayPlusFour.get(Calendar.MONTH) + ", " + todayPlusFour.get(Calendar.DAY_OF_MONTH) + ", 0)," +
                "(5, " + todayPlusFive.get(Calendar.YEAR) + ", " + todayPlusFive.get(Calendar.MONTH) + ", " + todayPlusFive.get(Calendar.DAY_OF_MONTH) + ", 1)," +
                "(6, " + todayPlusSix.get(Calendar.YEAR) + ", " + todayPlusSix.get(Calendar.MONTH) + ", " + todayPlusSix.get(Calendar.DAY_OF_MONTH) + ", 0)" +
                ";";
        sqLiteDatabase.execSQL(statement);

    }

    private void createDatabase(SQLiteDatabase sqLiteDatabase) {
        String statement;

        statement = "CREATE TABLE " + ChefBuddyContract.IngredientTable.TABLE_NAME + " (" +
                ChefBuddyContract.IngredientTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.IngredientTable.COL_NAME_NAME.getName() + " " + ChefBuddyContract.IngredientTable.COL_NAME_NAME.getDataType() +
                " ); " ;
        sqLiteDatabase.execSQL(statement);

        statement = "CREATE TABLE " + ChefBuddyContract.RecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.RecipeTable.COL_NAME_NAME.getName() + " " + ChefBuddyContract.RecipeTable.COL_NAME_NAME.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_SERVINGS.getName() + " " + ChefBuddyContract.RecipeTable.COL_NAME_SERVINGS.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME.getName() + " " + ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME_TYPE.getName() + " " + ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME_TYPE.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_DIRECTIONS.getName() + " " + ChefBuddyContract.RecipeTable.COL_NAME_DIRECTIONS.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COL_NAME_FEATURED_IMAGE.getName() + " " + ChefBuddyContract.RecipeTable.COL_NAME_FEATURED_IMAGE.getDataType() +
                " ); " ;
        sqLiteDatabase.execSQL(statement);

        statement = "CREATE TABLE " + ChefBuddyContract.RecipeIngredientTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeIngredientTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_RECIPE_FK.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COL_NAME_RECIPE_FK.getDataType() +
                " REFERENCES " + ChefBuddyContract.RecipeTable.TABLE_NAME + "(" + ChefBuddyContract.RecipeTable._ID + ") " + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_INGREDIENT_FK.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COL_NAME_INGREDIENT_FK.getDataType() +
                " REFERENCES " + ChefBuddyContract.IngredientTable.TABLE_NAME + "(" + ChefBuddyContract.IngredientTable._ID + ") " + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_AMOUNT.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COL_NAME_AMOUNT.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COL_NAME_MEASUREMENT.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COL_NAME_MEASUREMENT.getDataType() +
                " ); " ;
        sqLiteDatabase.execSQL(statement);


        statement = "CREATE TABLE " + ChefBuddyContract.DailyRecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.DailyRecipeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_YEAR.getName() + " " + ChefBuddyContract.DailyRecipeTable.COL_NAME_YEAR.getDataType() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_MONTH.getName() + " " + ChefBuddyContract.DailyRecipeTable.COL_NAME_MONTH.getDataType() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_DAY.getName() + " " + ChefBuddyContract.DailyRecipeTable.COL_NAME_DAY.getDataType() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COL_NAME_RECIPE_FK.getName() + " " + ChefBuddyContract.DailyRecipeTable.COL_NAME_RECIPE_FK.getDataType() +
                " REFERENCES " + ChefBuddyContract.RecipeTable.TABLE_NAME + "(" + ChefBuddyContract.RecipeTable._ID + ") " +
                " ); " ;
        sqLiteDatabase.execSQL(statement);
    }

    private void deleteDatabase(SQLiteDatabase sqLiteDatabase) {
        String statement ;

        statement = "DROP TABLE IF EXISTS " + ChefBuddyContract.IngredientTable.TABLE_NAME + "; ";
        sqLiteDatabase.execSQL(statement);

        statement = "DROP TABLE IF EXISTS " + ChefBuddyContract.DailyRecipeTable.TABLE_NAME + "; ";
        sqLiteDatabase.execSQL(statement);

        statement = "DROP TABLE IF EXISTS " + ChefBuddyContract.RecipeIngredientTable.TABLE_NAME + "; ";
        sqLiteDatabase.execSQL(statement);

        statement = "DROP TABLE IF EXISTS " + ChefBuddyContract.RecipeTable.TABLE_NAME + "; ";
        sqLiteDatabase.execSQL(statement);

    }
}
