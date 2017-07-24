package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Recipe implements Serializable {
    private long id;
    private String name;
    private Servings servings;
    private PreparationTime preparationTime;
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    private String directions;
    private List<String> images = new ArrayList<>();

    private String extraId;     //This id is used in some cases for some online Apis when fetching recipes.


    /* When creating a new recipe */
    public Recipe() {
        id = -1;
        name = "";
        servings = Servings.SERVINGS_2;
        preparationTime = PreparationTime.HOUR_1;
        directions = "";
    }

    /* When fetching a recipe from db */
    public Recipe(long id, @NonNull String name, Servings servings, @NonNull PreparationTime preparationTime, @NonNull String directions, String imageFilenamesStr) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.directions = directions;

        if(imageFilenamesStr != null && !imageFilenamesStr.isEmpty()) {
            for( String filename : imageFilenamesStr.split("[" + Constants.IMAGE_FILENAMES_SEPARATOR + "]")) {
                if(!filename.isEmpty())
                    this.images.add(filename);
            }
        }
    }

    /* When fetching recipes online */
    public Recipe(@NonNull String name, Servings servings, @NonNull PreparationTime preparationTime, @NonNull String directions, List<String> imageUrls) {
        id = -1;
        this.name = name;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.directions = directions;

        if(imageUrls != null) {
            this.images = imageUrls;
        }
    }


    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Servings getServings() {
        return servings;
    }
    public PreparationTime getPreparationTime() {
        return preparationTime;
    }
    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }
    public String getSimpleIngredientsString() {
        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient i: recipeIngredients) {
            sb.append(i.getIngredient().getName());
            sb.append(", ");
        }

        //Remove last ", "
        if(sb.length() > 0)
            sb.setLength(sb.length() - 2);

        return sb.toString();
    }
    public String getIngredientsString() {
        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient i: recipeIngredients) {
            if(Measurement.NONE == i.getMeasurement())
                sb.append(i.getIngredient().getName());
            else
                sb.append(String.format(Locale.getDefault(), Constants.RECIPE_INGREDIENT_STRING_FORMAT, i.getAmount(), i.getMeasurement().getAbbreviation(), i.getIngredient().getName()));

            sb.append("\n");
        }

        //Remove last ", "
        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }
    public String getDirections() {
        return directions;
    }
    public String getFeaturedImage() {
        if(images.size() > 0)
            return images.get(0);
        else return "NOFEATUREDIMAGE";
    }
    public List<String> getImages() {
        return images;
    }
    public String getImageFilenamesStr() {
        StringBuilder sb = new StringBuilder();
        for (String s : images) {
            sb.append(s);
            sb.append(Constants.IMAGE_FILENAMES_SEPARATOR);
        }

        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);  //Remove last separator

        return sb.toString();
    }
    public String getExtraId() {
        return extraId;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }
    public void setServings(@NonNull Servings servings) {
        this.servings = servings;
    }
    public void setPreparationTime(@NonNull PreparationTime preparationTime) {
        this.preparationTime = preparationTime;
    }
    public void setRecipeIngredients(@NonNull List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
    public void setDirections(@NonNull String directions) {
        this.directions = directions;
    }

    public Message checkIfValid() {
        if(name.trim().isEmpty())
            return Message.INVALID_RECIPE_NAME;

        if(servings == null)
            return Message.INVALID_RECIPE_SERVINGS;

        if(preparationTime == null)
            return Message.INVALID_RECIPE_PREPARATION_TIME;

        if(directions.trim().isEmpty())
            return Message.INVALID_RECIPE_DIRECTIONS;

        return null;
    }
    public void setExtraId(String extraId) {
        this.extraId = extraId;
    }

    @Override
    public String toString() {
        return  "ID=" + id + "\r\n" +
                " name=" + name + "\r\n" +
                " servings=" + servings.getFriendlyName() + "\r\n" +
                " preparationTime=" + preparationTime.getFriendlyName() + "\r\n" +
                " recipeIngredients=" + TextUtils.join(", ", recipeIngredients) + "\r\n" +
                " directions=" + directions;
    }

}
