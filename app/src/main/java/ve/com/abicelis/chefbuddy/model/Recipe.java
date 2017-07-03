package ve.com.abicelis.chefbuddy.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.List;

import ve.com.abicelis.chefbuddy.enums.PreparationTimeType;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Recipe {
    private int id;
    private String name;
    private int servings;
    private String preparationTime;
    private PreparationTimeType preparationTimeType;
    private List<Ingredient> ingredients;
    private String directions;
    private List<String> images;
    private Bitmap featuredImage;
    // TODO: 28/6/2017 List of images?


    public Recipe() {}

    public Recipe(int id, @NonNull String name, int servings, @NonNull String preparationTime, @NonNull PreparationTimeType preparationTimeType,@NonNull List<Ingredient> ingredients, @NonNull String directions) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.preparationTimeType = preparationTimeType;
        this.ingredients = ingredients;
        this.directions = directions;
    }


    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getServings() {
        return servings;
    }
    public String getPreparationTime() {
        return preparationTime;
    }
    public PreparationTimeType getPreparationTimeType() {
        return preparationTimeType;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public String getDirections() {
        return directions;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setServings(int servings) {
        this.servings = servings;
    }
    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }
    public void setPreparationTimeType(PreparationTimeType preparationTimeType) {
        this.preparationTimeType = preparationTimeType;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void setDirections(String directions) {
        this.directions = directions;
    }

}
