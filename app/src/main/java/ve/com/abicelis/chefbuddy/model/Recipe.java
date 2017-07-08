package ve.com.abicelis.chefbuddy.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.enums.PreparationTimeType;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Recipe {
    private int id;
    private String name;
    private int servings;
    private String preparationTime;
    private PreparationTimeType preparationTimeType;
    private List<Ingredient> ingredients = new ArrayList<>();
    private String directions;

    private byte[] featuredImageBytes;
    private Bitmap featuredImage = null;

    private List<String> pathsOfImages;
    private List<Bitmap> images = new ArrayList<>();



    public Recipe() {}

    public Recipe(int id, @NonNull String name, int servings, @NonNull String preparationTime, @NonNull PreparationTimeType preparationTimeType, @NonNull String directions, byte[] featuredImageBytes) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.preparationTimeType = preparationTimeType;
        this.directions = directions;

        this.featuredImageBytes = featuredImageBytes;
        this.featuredImage = ImageUtil.getBitmap(featuredImageBytes);
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

    public Bitmap getFeaturedImage() {
        return featuredImage;
    }
    public byte[] getFeaturedImageBytes() {
        return featuredImageBytes;
    }

    public List<String> getPathsOfImages() {
        return pathsOfImages;
    }
    public List<Bitmap> getImages() {
        return images;
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

    public void setFeaturedImageBytes(byte[] featuredImageBytes) {
        this.featuredImageBytes = featuredImageBytes;
        this.featuredImage = ImageUtil.getBitmap(featuredImageBytes);
    }


    @Override
    public String toString() {
        return  "ID=" + id + "\r\n" +
                " name=" + name + "\r\n" +
                " servings=" + servings + "\r\n" +
                " preparation=" + preparationTime + (preparationTimeType != null ? preparationTimeType.getFriendlyName(preparationTime) : "") + "\r\n" +
                " ingredients=" + TextUtils.join(", ", ingredients) + "\r\n" +
                " directions=" + directions;
    }

}
