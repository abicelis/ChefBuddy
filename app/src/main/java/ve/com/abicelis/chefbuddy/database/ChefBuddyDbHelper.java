package ve.com.abicelis.chefbuddy.database;

import android.content.ContentValues;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.model.PreparationTime;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.model.Servings;
import ve.com.abicelis.chefbuddy.util.CalendarUtil;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

import static ve.com.abicelis.chefbuddy.app.Constants.DATABASE_NAME;


/**
 * Created by abicelis on 3/7/2017.
 */
public class ChefBuddyDbHelper extends SQLiteOpenHelper {

    //CONSTS
    private static final int DATABASE_VERSION = 16;    // If you change the database schema, you must increment the database version.
    private static final String COMMA_SEP = ", ";


    public ChefBuddyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    private void insertMockData(SQLiteDatabase sqLiteDatabase) {
        String statement;

        //Insert mock Ingredients
        List<Ingredient> ingredients = new ArrayList<>();

        //Burgers
        ingredients.add( new Ingredient(1, "Egg"));
        ingredients.add( new Ingredient(2, "Mustard"));
        ingredients.add( new Ingredient(3, "Worcestershire sauce"));
        ingredients.add( new Ingredient(4, "Onion"));
        ingredients.add( new Ingredient(5, "Garlic, minced"));
        ingredients.add( new Ingredient(6, "Salt"));
        ingredients.add( new Ingredient(7, "Pepper"));
        ingredients.add( new Ingredient(8, "Ground Beef"));

        //Pizza
        ingredients.add( new Ingredient(9, "All-purpose flour"));
        ingredients.add( new Ingredient(10, "Active dry yeast"));
            //Salt
        ingredients.add( new Ingredient(11, "Warm water"));
        ingredients.add( new Ingredient(12, "Olive oil"));
        ingredients.add( new Ingredient(13, "Cornmeal"));

        //Caesar Salad
        ingredients.add( new Ingredient(14, "Romaine lettuce"));
        ingredients.add( new Ingredient(15, "Garlic croutons"));
        ingredients.add( new Ingredient(16, "Parmesan cheese"));
        ingredients.add( new Ingredient(17, "Black Pepper"));
        ingredients.add( new Ingredient(18, "Anchovy fillet"));
            //Garlic
        ingredients.add( new Ingredient(19, "Mayonnaise"));
        ingredients.add( new Ingredient(20, "Milk"));
        ingredients.add( new Ingredient(21, "Lemon juice"));
        ingredients.add( new Ingredient(22, "Dijon mustard"));


        //Pasta
        ingredients.add( new Ingredient(23, "Penne"));
        ingredients.add( new Ingredient(24, "Margarine"));
            //Garlic
            //Flour
        ingredients.add( new Ingredient(25, "Chicken broth"));
            //Milk
        ingredients.add( new Ingredient(26, "Parsley flakes"));
            //Salt
            //Pepper
            //Parmesan


        for (Ingredient i : ingredients) {
            ContentValues cv = ContentValuesHelper.getValuesForIngredient(i);
            sqLiteDatabase.insert(ChefBuddyContract.IngredientTable.TABLE_NAME, null, cv);
        }





        //Insert mock Recipes
        List<Recipe> recipes = new ArrayList<>();
        recipes.add( new Recipe("The Perfect Burger", Servings.SERVINGS_4, PreparationTime.MINUTE_30, "Lightly oil grill and heat BBQ to medium.\n\nWhisk egg in a bowl and add next 6 ingredients.\n\nAdd any of the “stir-ins” that appeal to you.\n\nCrumble in beef and using your hands or a fork, gently mix together.\n\nHandle the meat as little as possible – the more you work it, the tougher it gets.\n\nGently shape (don’t firmly press) mixture into burgers about ¾ inch thick.\n\nUsing your thumb, make a shallow depression in the centre of each burger to prevent puffing up during cooking.\n\nPlace burgers on the grill, close lid and BBQ until NO LONGER PINK INSIDE, turning once, about 6 – 8 minutes per side.\n\nAn instant read thermometer should read 160F.\n\nDon't abuse your burgers by pressing with a spatula, pricking with a fork or turning frequently as precious juices will be lost!\n\nTuck into a warm crusty bun and add your favourite toppings. \n\n\nMore info at: http://www.food.com/recipe/the-perfect-burger-92021", new ArrayList<>(Arrays.asList("burger.jpg"))));
        recipes.add( new Recipe("Thin Pizza dough", Servings.SERVINGS_2, PreparationTime.HOUR_1, "Mix a little sugar into the warm water.\n\nSprinkle yeast on top.\n\nWait for 10 minutes or until it gets all foamy.\n\nPour into a large bowl.\n\nAdd flour, salt, olive oil.\n\nCombine.\n\nKnead for 6-8 minutes until you have a moderately stiff dough that is smooth and elastic (add a bit more flour if you need to).\n\nCover and let rest for 20-30 minutes.\n\nLightly grease two 12-inch pizza pans.\n\nSprinkle with a little bit of cornmeal.\n\nDivide dough in half.\n\nPlace each half on a pizza pan and pat it with your fingers until it stretches over the whole pan.\n\nTry to make it thicker around the edge.\n\nIf desired, pre-bake at 425 F for 10 minutes (I don't always do this).\n\nThen spread with pizza sauce and use the toppings of your choice.\n\nBake at 425 F for 10-20 minutes longer or until bubbly and hot.\n\nMakes 2 12-inch pizzas.\n\nIf you don't want to use all the dough, you can freeze it.\n\nTake a portion of dough, form into a ball, rub olive oil over it and place it in a freezer bag (the oil makes it easier to take out of the bag).\n\nWhen you want to make a pizza, take dough out of freezer and allow to thaw before using. \n\n\nMore info at: http://www.food.com/recipe/pizza-dough-for-thin-crust-pizza-70165", new ArrayList<>(Arrays.asList("pizza.jpg"))));
        recipes.add( new Recipe("Caesar Salad", Servings.SERVINGS_6, PreparationTime.MINUTE_20, "In a small 2-cup mini food processor, process/mince the anchovy fillets and garlic together until finely minced (do this together and firstly, otherwise it will not get minced properly with the other ingredients).\n\nAdd in the remaining ingredients, and process for 30 seconds, or more until well mixed.\n\nAdjust seasonings to taste.\n\nThin with buttermilk (or milk) for a thinner consistency if desired.\n\nStore in fridge, covered in a glass container for 3 or more hours before using (the flavors become stronger when left in the fridge for a longer time).\n\nToss desired amount of dressing with Romaine lettuce and croutons.\n\nSprinkle with more grated Parmesan cheese if desired. \n\n\n More info at: http://www.food.com/recipe/kittencals-famous-caesar-salad-116849", new ArrayList<>(Arrays.asList("salad.jpg"))));
        recipes.add( new Recipe("Creamy Garlic Pasta", Servings.SERVINGS_8, PreparationTime.MINUTE_20, "Melt butter and add garlic in a medium sauce pan.\n\nCook over medium for 1 minute.\n\nAdd flour and cook 1 minute, stirring constantly.\n\nStir in broth and milk and cook, stirring frequently, until sauce boils and thickens.\n\nAdd parsley, salt, pepper and cheese.\n\nStir until cheese is melted.\n\nToss hot pasta with sauce and serve immediately. \n\n\n More info at: http://www.food.com/recipe/creamy-garlic-penne-pasta-43023", new ArrayList<>(Arrays.asList("pasta.jpg"))));


        for (Recipe r : recipes) {
            ContentValues cv = ContentValuesHelper.getValuesForRecipe(r);
            sqLiteDatabase.insert(ChefBuddyContract.RecipeTable.TABLE_NAME, null, cv);
        }




        //Insert mock Ingredients
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        //Burgers
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.NONE,           new Ingredient(1, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.TEASPOON,       new Ingredient(2, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.TEASPOON,       new Ingredient(3, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.NONE,           new Ingredient(4, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.CLOVE,          new Ingredient(5, "")));
        recipeIngredients.add( new RecipeIngredient("1/2",      Measurement.TEASPOON,       new Ingredient(6, "")));
        recipeIngredients.add( new RecipeIngredient("1/2",      Measurement.TEASPOON,       new Ingredient(7, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.POUND,          new Ingredient(8, "")));

        //Pizza
        recipeIngredients.add( new RecipeIngredient("2 1/2",    Measurement.CUP,            new Ingredient(9, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.OUNCE,          new Ingredient(10, "")));
        recipeIngredients.add( new RecipeIngredient("1/4",      Measurement.TEASPOON,       new Ingredient(6, "")));    //Salt
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.CUP,            new Ingredient(11, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.TABLESPOON,     new Ingredient(12, "")));
        recipeIngredients.add( new RecipeIngredient("",         Measurement.NONE,           new Ingredient(13, "")));

        //Salad
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.NONE,           new Ingredient(14, "")));
        recipeIngredients.add( new RecipeIngredient("1 1/2",    Measurement.CUP,            new Ingredient(15, "")));
        recipeIngredients.add( new RecipeIngredient("",         Measurement.NONE,           new Ingredient(16, "")));
        recipeIngredients.add( new RecipeIngredient("",         Measurement.NONE,           new Ingredient(17, "")));
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.NONE,           new Ingredient(18, "")));
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.CLOVE,          new Ingredient(5, "")));    //Garlic
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.CUP,            new Ingredient(19, "")));
        recipeIngredients.add( new RecipeIngredient("1/4",      Measurement.CUP,            new Ingredient(20, "")));
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.TABLESPOON,     new Ingredient(21, "")));
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.TABLESPOON,     new Ingredient(22, "")));
        recipeIngredients.add( new RecipeIngredient("",         Measurement.NONE,           new Ingredient(6, "")));    //Salt
        recipeIngredients.add( new RecipeIngredient("",         Measurement.NONE,           new Ingredient(7, "")));    //Pepper
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.TEASPOON,       new Ingredient(3, "")));    //Worcestershire

        //Pasta
        recipeIngredients.add( new RecipeIngredient("1",        Measurement.POUND,          new Ingredient(23, "")));
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.TABLESPOON,     new Ingredient(24, "")));
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.CLOVE,          new Ingredient(5, "")));    //Garlic
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.TABLESPOON,     new Ingredient(9, "")));    //Flour
        recipeIngredients.add( new RecipeIngredient("3/4",      Measurement.CUP,            new Ingredient(25, "")));
        recipeIngredients.add( new RecipeIngredient("3/4",      Measurement.CUP,            new Ingredient(20, "")));   //Milk
        recipeIngredients.add( new RecipeIngredient("2",        Measurement.TEASPOON,       new Ingredient(26, "")));
        recipeIngredients.add( new RecipeIngredient("",         Measurement.NONE,           new Ingredient(6, "")));    //Salt
        recipeIngredients.add( new RecipeIngredient("",         Measurement.NONE,           new Ingredient(7, "")));    //Pepper
        recipeIngredients.add( new RecipeIngredient("1/3",      Measurement.CUP,            new Ingredient(16, "")));   //Parmesan


        for(int i = 0; i < recipeIngredients.size(); i++) {

            long recipeID;
            if(i < 8)
                recipeID = 1;       //8 ingredients
            else if(i < 14)
                recipeID = 2;       //6 ingredients
            else if(i < 27)
                recipeID = 3;       //13 ingredients
            else if(i < 37)
                recipeID = 4;       //10 ingredients
            else
                throw new IndexOutOfBoundsException("No index for recipe!");

            ContentValues cv = ContentValuesHelper.getValuesForRecipeIngredient(recipeID, recipeIngredients.get(i));
            sqLiteDatabase.insert(ChefBuddyContract.RecipeIngredientTable.TABLE_NAME, null, cv);
        }







        //Mock dates for Daily Recipes
        // TODO: 30/7/2017 change these raw db statements and use insert() like above
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
                "(0, " + today.get(Calendar.YEAR) + ", " + today.get(Calendar.MONTH) + ", " + today.get(Calendar.DAY_OF_MONTH) + ", 1)," +
                "(1, " + todayPlusOne.get(Calendar.YEAR) + ", " + todayPlusOne.get(Calendar.MONTH) + ", " + todayPlusOne.get(Calendar.DAY_OF_MONTH) + ", 2)," +
                "(2, " + todayPlusTwo.get(Calendar.YEAR) + ", " + todayPlusTwo.get(Calendar.MONTH) + ", " + todayPlusTwo.get(Calendar.DAY_OF_MONTH) + ", 3)," +
                "(3, " + todayPlusThree.get(Calendar.YEAR) + ", " + todayPlusThree.get(Calendar.MONTH) + ", " + todayPlusThree.get(Calendar.DAY_OF_MONTH) + ", 4)," +
                "(4, " + todayPlusFour.get(Calendar.YEAR) + ", " + todayPlusFour.get(Calendar.MONTH) + ", " + todayPlusFour.get(Calendar.DAY_OF_MONTH) + ", 4)," +
                "(5, " + todayPlusFive.get(Calendar.YEAR) + ", " + todayPlusFive.get(Calendar.MONTH) + ", " + todayPlusFive.get(Calendar.DAY_OF_MONTH) + ", 3)," +
                "(6, " + todayPlusSix.get(Calendar.YEAR) + ", " + todayPlusSix.get(Calendar.MONTH) + ", " + todayPlusSix.get(Calendar.DAY_OF_MONTH) + ", 3)" +
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


        statement = "CREATE TABLE " + ChefBuddyContract.WheelRecipeTable.TABLE_NAME + " (" +
                ChefBuddyContract.WheelRecipeTable.COLUMN_ID.getName() + " " + ChefBuddyContract.WheelRecipeTable.COLUMN_ID.getDataType() + " PRIMARY KEY AUTOINCREMENT, " +
                ChefBuddyContract.WheelRecipeTable.COLUMN_RECIPE.getName() + " " + ChefBuddyContract.WheelRecipeTable.COLUMN_RECIPE.getDataType() +
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

        statement = "DROP TABLE IF EXISTS " + ChefBuddyContract.WheelRecipeTable.TABLE_NAME + "; ";
        sqLiteDatabase.execSQL(statement);

        statement = "DROP TABLE IF EXISTS " + ChefBuddyContract.RecipeTable.TABLE_NAME + "; ";
        sqLiteDatabase.execSQL(statement);

    }


    private void saveImageFiles() {
        try {
            File imageDir = FileUtil.getImageFilesDir();
            FileUtil.createDirIfNotExists(imageDir);
            saveDrawableAsImage(imageDir, "pizza.jpg", R.drawable.pizza);
            saveDrawableAsImage(imageDir, "burger.jpg", R.drawable.burger);
            saveDrawableAsImage(imageDir, "salad.jpg", R.drawable.salad);
            saveDrawableAsImage(imageDir, "pasta.jpg", R.drawable.pasta);
        } catch (Exception e) {
            Toast.makeText(ChefBuddyApplication.getContext(), "Error saving image files", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDrawableAsImage(File imageDir, String filename, @DrawableRes int drawable) throws IOException {
        Drawable d = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), drawable);
        Bitmap b = ImageUtil.getBitmap(d);
        FileUtil.saveBitmapAsJpeg(new File(imageDir, filename), b, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);
    }
}
