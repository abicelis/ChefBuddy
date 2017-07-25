package ve.com.abicelis.chefbuddy.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import retrofit2.Response;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.model.PreparationTime;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.model.Servings;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamHit;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamIngredient;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamRecipe;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;
import ve.com.abicelis.chefbuddy.model.food2fork.Food2ForkRecipe;
import ve.com.abicelis.chefbuddy.model.food2fork.Food2ForkResponse;

/**
 * Created by abicelis on 22/7/2017.
 */

public class RecipeUtil {
    private static int ACCEPTABLE_MIN_LEVENSHTEIN_DISTANCE = 1;


    /* Edamam helper methods */
    public static List<Recipe> getRecipesFromEdamamResponse(Response<EdamamResponse> response) {
        List<Recipe> recipes = new ArrayList<>();

        for(EdamamHit hit : response.body().getHits()) {
            EdamamRecipe er = hit.getRecipe();
            String linkToPreparation = String.format(Locale.getDefault(), ChefBuddyApplication.getContext().getString(R.string.edamam_perparation), er.getUrl());
            Recipe newRecipe = new Recipe(er.getLabel(), Servings.getServingsForInt(er.getYield()), PreparationTime.HOUR_1,
                    linkToPreparation, new ArrayList<>(Arrays.asList(er.getImage())));

            for (EdamamIngredient ei : er.getIngredients()) {
                newRecipe.getRecipeIngredients().add(getRecipeIngredientFromEdamamIngredient(ei));
            }
            recipes.add(newRecipe);
        }
        return recipes;
    }

    private static RecipeIngredient getRecipeIngredientFromEdamamIngredient(EdamamIngredient ei) {
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        String amount = df.format(ei.getQuantity());
        amount = (amount.equals("0") ? "" : amount);

        return new RecipeIngredient(amount, getMeasurementFromString(ei.getMeasure()), new Ingredient(StringUtil.startWithUppercase(ei.getFood())));
    }







    /* Food2Fork helper methods */
    public static List<Recipe> getRecipesFromFood2ForkResponse(Response<Food2ForkResponse> response) {
        List<Recipe> recipes = new ArrayList<>();

        for(Food2ForkRecipe r : response.body().getRecipes()) {

            String linkToPreparation = String.format(Locale.getDefault(), ChefBuddyApplication.getContext().getString(R.string.edamam_perparation), r.getSource_url());
            Recipe newRecipe = new Recipe(r.getTitle(), Servings.SERVINGS_1, PreparationTime.HOUR_1,
                    linkToPreparation, new ArrayList<>(Arrays.asList(r.getImage_url())));
            newRecipe.setExtraId(r.getRecipe_id());

            recipes.add(newRecipe);
        }
        return recipes;
    }

    public static List<RecipeIngredient> getRecipeIngredientFromFood2ForkRecipe(Food2ForkRecipe fr) {
        List<RecipeIngredient> ingredients = new ArrayList<>();

        for (String ingredientStr : fr.getIngredients()) {

            List<String> amountTokens = new ArrayList<>();
            List<String> ingredientNameTokens = new ArrayList<>();
            Measurement measurement = Measurement.NONE;
            String ingredientName = "";
            boolean gotAmount = false;

            StringTokenizer tokenizer = new StringTokenizer(ingredientStr, " ", false);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                if(!gotAmount && isAValidFood2ForkAmount(token)) {
                    amountTokens.add(token);
                } else {
                    gotAmount = true;
                    measurement = getMeasurementFromString(token);
                    if (measurement.equals(Measurement.NONE))       //Token not a measurement, store it as ingredientName
                        ingredientNameTokens.add(token);
                    else                                            //Found measurement, build ingredient name with ingredientNameTokens and remaining tokens
                        ingredientName = getStringFromListOfString(ingredientNameTokens) + " " + getStringFromRemainingTokens(tokenizer);
                }
            }

            //Never found Measurement, build ingredient name with stored ingredientNameTokens
            if(ingredientName.isEmpty())
                ingredientName = getStringFromListOfString(ingredientNameTokens);

            String amountStr = getStringFromListOfString(amountTokens);
            ingredients.add(new RecipeIngredient(amountStr, measurement, new Ingredient(StringUtil.startWithUppercase(ingredientName))));
        }


        return ingredients;
    }

    private static String getStringFromListOfString(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for(String s : strings) {
            sb.append(s);
            sb.append(" ");
        }
        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    private static String getStringFromRemainingTokens(StringTokenizer tokenizer) {
        if(!tokenizer.hasMoreTokens())
            return "";

        StringBuilder sb = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            sb.append(tokenizer.nextToken());
            sb.append(" ");
        }
        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    private static boolean isAValidFood2ForkAmount(String amount) {
        Pattern pattern = Pattern.compile("[0-9]/[0-9]|[0-9]+"); //Matches either a fraction 1/2 or a number
        return pattern.matcher(amount).matches();
    }







    /* Common */
    private static Measurement getMeasurementFromString(String measurementStr) {
        if(measurementStr == null)
            return Measurement.NONE;


        Pattern pattern = Pattern.compile("[0-9]+\\.?[0-9]*|.*\\)|\\(.*"); //Matches numbers like '1', '1.5' or '(blah' or 'blah)'
        if(pattern.matcher(measurementStr).matches())
            return Measurement.NONE;

        //Lower case
        measurementStr = measurementStr.toLowerCase();

        double minScore = Integer.MAX_VALUE;
        Measurement closestMeasurement = Measurement.NONE;
        double score;
        for(int i = 0; i< Measurement.values().length; i++) {
            Measurement m = Measurement.values()[i];

            score = StringUtil.levenshteinDistance(m.getAbbreviation().toLowerCase(), measurementStr);
            if(score < minScore) {
                minScore = score;
                closestMeasurement = m;
            }

            score = StringUtil.levenshteinDistance(m.getFriendlyName().toLowerCase(), measurementStr);
            if(score < minScore) {
                minScore = score;
                closestMeasurement = m;
            }
        }

        return (minScore <= ACCEPTABLE_MIN_LEVENSHTEIN_DISTANCE ? closestMeasurement : Measurement.NONE);
    }

}
