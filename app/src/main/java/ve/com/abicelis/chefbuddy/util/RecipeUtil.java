package ve.com.abicelis.chefbuddy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

/**
 * Created by abicelis on 22/7/2017.
 */

public class RecipeUtil {

    public static List<Recipe> getRecipesFromEdamamResponse(Response<EdamamResponse> response) {
        List<Recipe> recipes = new ArrayList<>();

        for(EdamamHit hit : response.body().getHits()) {
            EdamamRecipe er = hit.getRecipe();
            String linkToPreparation = String.format(Locale.getDefault(), ChefBuddyApplication.getContext().getString(R.string.edamam_perparation), er.getUrl());
            Recipe newRecipe = new Recipe(er.getLabel(), Servings.getServingsForInt(er.getYield()), PreparationTime.HOUR_1,
                    linkToPreparation, new ArrayList<>(Arrays.asList(er.getImage())));

            for (EdamamIngredient ei : er.getIngredients()) {
                newRecipe.getRecipeIngredients().add(new RecipeIngredient(String.valueOf(ei.getQuantity()), Measurement.NONE, new Ingredient(ei.getFood())));
            }
            recipes.add(newRecipe);
        }
        return recipes;
    }

}
