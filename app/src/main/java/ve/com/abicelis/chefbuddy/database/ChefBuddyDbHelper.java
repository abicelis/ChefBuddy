package ve.com.abicelis.chefbuddy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.util.CalendarUtil;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;


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
        saveImageFiles();
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
                ChefBuddyContract.IngredientTable.COLUMN_ID.getName() + COMMA_SEP +
                ChefBuddyContract.IngredientTable.COLUMN_NAME.getName() +
                ") VALUES " +
                "(0, 'Mozarella Cheese')," +
                "(1, 'Flour')," +
                "(2, 'Oil')," +
                "(3, 'Ham')," +
                "(4, 'Chickpea')," +
                "(5, 'Lemon')," +
                "(6, 'Tahini')," +
                "(7, 'Bread')," +
                "(8, 'Ketchup')," +
                "(9, 'Meat')," +
                "(10, 'Lettuce')," +
                "(11, 'Croutons')," +
                "(12, 'Chicken')," +
                "(13, 'Pasta')," +
                "(14, 'Bacon')," +
                "(15, 'Garlic')" +
                ";";
        sqLiteDatabase.execSQL(statement);

        //Insert mock Recipes
        statement = "INSERT INTO " + ChefBuddyContract.RecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeTable.COLUMN_ID.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_NAME.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_SERVINGS.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_PREPARATION_TIME.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_DIRECTIONS.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_IMAGE_FILENAMES.getName() +
                ") VALUES " +
                "(0, 'Pizza Gloria', 'SERVINGS_40', 'HOUR_2', 'Preheat oven and a baking sheet to 220C/fan 200C. Mix strong bread flour, salt and fast-action yeast together in a large bowl. Quickly stir in lukewarm water and olive oil and bring together to a rough dough.', '1.jpg')," +
                "(1, 'Hummus', 'SERVINGS_50', 'MINUTE_45', '1. Put everything except the parsley in a food processor and begin to process; add the chickpea liquid or water as needed to allow the machine to produce a smooth puree.', '2.jpg|1.jpg')," +
                "(2, 'Burgers', 'SERVINGS_3', 'HOUR_2', '1 Preheat grill for high heat. 2 In a large bowl, mix the ground beef, egg, bread crumbs, evaporated milk, Worcestershire sauce, cayenne pepper, and garlic using your hands. Form the mixture into 8 hamburger patties. 3 Lightly oil the grill grate. Grill patties 5 minutes per side, or until well done.', '|')," +
                "(3, 'Caesar Salad', 'SERVINGS_1', 'MINUTE_30', 'Mince 3 cloves of garlic, and combine in a small bowl with mayonnaise, anchovies, 2 tablespoons of the Parmesan cheese, Worcestershire sauce, mustard, and lemon juice. Season to taste with salt and black pepper. Heat oil in a large skillet over medium heat. Place lettuce in a large bowl.', '4.jpg|2.jpg|3.jpg|1.jpg|')," +
                "(4, 'Carbonara Pasta', 'SERVINGS_2', 'HOUR_1', '1 Heat pasta water. 2 Saute pancetta/bacon and garlic. 3 Beat eggs and half of the cheese. 4 Cook pasta. 5 Toss pasta with pancetta/bacon. 6 Add the beaten egg mixture', '|5.jpg|2.jpg|3.jpg|4.jpg|1.jpg')" +
                ";";
        sqLiteDatabase.execSQL(statement);


        //Insert mock Ingredients
        statement = "INSERT INTO " + ChefBuddyContract.RecipeIngredientTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_ID.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_RECIPE_FK.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_INGREDIENT_FK.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_AMOUNT.getName() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_MEASUREMENT.getName() +
                ") VALUES " +
                "(0, 0, 0, '800', 'GRAM')," +
                "(1, 0, 1, '2', 'CUP')," +
                "(2, 0, 2, '1', 'TABLESPOON')," +
                "(3, 0, 3, '250', 'GRAM')," +
                "(4, 1, 4, '1', 'KILOGRAM')," +
                "(5, 1, 5, '5', 'NONE')," +
                "(6, 1, 6, '500', 'MILILITER')," +
                "(7, 1, 2, '200', 'CUBIC_CENTIMITER')," +
                "(8, 2, 7, '4', 'NONE')," +
                "(9, 2, 8, '', 'NONE')," +
                "(10, 2, 9, '1', 'KILOGRAM')," +
                "(11, 3, 10, '200', 'CUBIC_CENTIMITER')," +
                "(12, 3, 11, '200', 'CUBIC_CENTIMITER')," +
                "(13, 3, 12, '200', 'CUBIC_CENTIMITER')," +
                "(14, 4, 13, '1', 'TABLESPOON')," +
                "(15, 4, 14, '2', 'TEASPOON')," +
                "(16, 4, 15, '3', 'INCH')," +
                "(17, 4, 15, '4', 'TABLESPOON')," +
                "(18, 4, 15, '5', 'TEASPOON')," +
                "(19, 4, 15, '6', 'CUP')," +
                "(20, 4, 15, '7', 'PINT')," +
                "(21, 4, 15, '200', 'QUART')," +
                "(22, 4, 15, '200', 'GALLON')," +
                "(23, 4, 15, '200', 'OUNCE')," +
                "(24, 4, 15, '200', 'FLUID_OUNCE')," +
                "(25, 4, 15, '200', 'CENTIMETER')," +
                "(26, 4, 15, '200', 'METER')," +
                "(27, 4, 15, '200', 'CUBIC_CENTIMITER')," +
                "(28, 4, 15, '200', 'MILILITER')," +
                "(29, 4, 15, '200', 'LITER')," +
                "(30, 4, 15, '200', 'GRAM')," +
                "(31, 4, 15, '200', 'KILOGRAM')" +
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
                ChefBuddyContract.DailyRecipeTable.COLUMN_ID.getName() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COLUMN_YEAR.getName() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COLUMN_MONTH.getName() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COLUMN_DAY.getName() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COLUMN_RECIPE_FK.getName() +
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
                ChefBuddyContract.IngredientTable.COLUMN_ID.getName() + " " + ChefBuddyContract.IngredientTable.COLUMN_ID.getDataType() + " PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.IngredientTable.COLUMN_NAME.getName() + " " + ChefBuddyContract.IngredientTable.COLUMN_NAME.getDataType() +
                " ); " ;
        sqLiteDatabase.execSQL(statement);

        statement = "CREATE TABLE " + ChefBuddyContract.RecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeTable.COLUMN_ID.getName() + " " + ChefBuddyContract.RecipeTable.COLUMN_ID.getDataType() + " PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.RecipeTable.COLUMN_NAME.getName() + " " + ChefBuddyContract.RecipeTable.COLUMN_NAME.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_SERVINGS.getName() + " " + ChefBuddyContract.RecipeTable.COLUMN_SERVINGS.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_PREPARATION_TIME.getName() + " " + ChefBuddyContract.RecipeTable.COLUMN_PREPARATION_TIME.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_DIRECTIONS.getName() + " " + ChefBuddyContract.RecipeTable.COLUMN_DIRECTIONS.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeTable.COLUMN_IMAGE_FILENAMES.getName() + " " + ChefBuddyContract.RecipeTable.COLUMN_IMAGE_FILENAMES.getDataType() +
                " ); " ;
        sqLiteDatabase.execSQL(statement);

        statement = "CREATE TABLE " + ChefBuddyContract.RecipeIngredientTable.TABLE_NAME + " (" +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_ID.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COLUMN_ID.getDataType() + " PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_RECIPE_FK.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COLUMN_RECIPE_FK.getDataType() +
                " REFERENCES " + ChefBuddyContract.RecipeTable.TABLE_NAME + "(" + ChefBuddyContract.RecipeTable.COLUMN_ID.getName() + ") " + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_INGREDIENT_FK.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COLUMN_INGREDIENT_FK.getDataType() +
                " REFERENCES " + ChefBuddyContract.IngredientTable.TABLE_NAME + "(" + ChefBuddyContract.IngredientTable.COLUMN_ID.getName() + ") " + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_AMOUNT.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COLUMN_AMOUNT.getDataType() + COMMA_SEP +
                ChefBuddyContract.RecipeIngredientTable.COLUMN_MEASUREMENT.getName() + " " + ChefBuddyContract.RecipeIngredientTable.COLUMN_MEASUREMENT.getDataType() +
                " ); " ;
        sqLiteDatabase.execSQL(statement);


        statement = "CREATE TABLE " + ChefBuddyContract.DailyRecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.DailyRecipeTable.COLUMN_ID.getName() + " " + ChefBuddyContract.DailyRecipeTable.COLUMN_ID.getDataType() + " PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.DailyRecipeTable.COLUMN_YEAR.getName() + " " + ChefBuddyContract.DailyRecipeTable.COLUMN_YEAR.getDataType() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COLUMN_MONTH.getName() + " " + ChefBuddyContract.DailyRecipeTable.COLUMN_MONTH.getDataType() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COLUMN_DAY.getName() + " " + ChefBuddyContract.DailyRecipeTable.COLUMN_DAY.getDataType() + COMMA_SEP +
                ChefBuddyContract.DailyRecipeTable.COLUMN_RECIPE_FK.getName() + " " + ChefBuddyContract.DailyRecipeTable.COLUMN_RECIPE_FK.getDataType() +
                " REFERENCES " + ChefBuddyContract.RecipeTable.TABLE_NAME + "(" + ChefBuddyContract.RecipeTable.COLUMN_ID.getName() + ") " +
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


    private void saveImageFiles() {
        try {
            File imageDir = FileUtil.getImageFilesDir();
            FileUtil.createDirIfNotExists(imageDir);
            saveDrawableAsImage(imageDir, "1.jpg", R.drawable.pizza);
            saveDrawableAsImage(imageDir, "2.jpg", R.drawable.hummus);
            saveDrawableAsImage(imageDir, "3.jpg", R.drawable.burger);
            saveDrawableAsImage(imageDir, "4.jpg", R.drawable.salad);
            saveDrawableAsImage(imageDir, "5.jpg", R.drawable.pasta);
        } catch (Exception e) {
            Toast.makeText(ChefBuddyApplication.getContext(), "Error saving image files", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDrawableAsImage(File imageDir, String filename, @DrawableRes int drawable) throws IOException {
        Drawable d = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), drawable);
        Bitmap b = ImageUtil.getBitmap(d);
        ImageUtil.saveBitmapAsJpeg(new File(imageDir, filename), b, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);
    }
}
