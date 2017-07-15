package ve.com.abicelis.chefbuddy.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Recipe {
    private long id;
    private String name;
    private int servings;
    private String preparationTime;
    private PreparationTimeType preparationTimeType;
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    private String directions;

    private byte[] featuredImageBytes;
    private Bitmap featuredImage = null;

    private List<Image> images = new ArrayList<>();



    public Recipe() {}

    public Recipe(long id, @NonNull String name, int servings, @NonNull String preparationTime, @NonNull PreparationTimeType preparationTimeType, @NonNull String directions, byte[] featuredImageBytes, String imageFilenames, boolean preloadImages) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.preparationTimeType = preparationTimeType;
        this.directions = directions;

        this.featuredImageBytes = featuredImageBytes;
        this.featuredImage = ImageUtil.getBitmap(featuredImageBytes);

        if(imageFilenames != null && !imageFilenames.isEmpty()) {
            for( String filename : imageFilenames.split("[" + Constants.IMAGE_FILENAMES_SEPARATOR + "]")) {
                if(!filename.isEmpty())
                    images.add(new Image(filename, preloadImages));
            }
        }
    }

    public void reloadImages() {
        for (Image i : images)
            i.loadImage();
    }

    public long getId() {
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

    public String getDirections() {
        return directions;
    }

    public Bitmap getFeaturedImage() {
        return featuredImage;
    }
    public byte[] getFeaturedImageBytes() {
        return featuredImageBytes;
    }

    public List<Image> getImages() {
        return images;
    }
    public String getImageFilenames() {
        StringBuilder sb = new StringBuilder();
        for (Image i : images) {
            sb.append(i.getFilename());
            sb.append(Constants.IMAGE_FILENAMES_SEPARATOR);
        }

        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);  //Remove last separator

        return sb.toString();
    }


    public void setId(long id) {
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
    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
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
                " preparation=" + preparationTime + " " + (preparationTimeType != null ? preparationTimeType.getFriendlyName(preparationTime) : "") + "\r\n" +
                " recipeIngredients=" + TextUtils.join(", ", recipeIngredients) + "\r\n" +
                " directions=" + directions;
    }

}
