package ve.com.abicelis.chefbuddy.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Recipe {
    private long id;
    private String name;
    private Servings servings;
    private PreparationTime preparationTime;
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    private String directions;

    private byte[] featuredImageBytes;
    private Bitmap featuredImage = null;

    private List<Image> images = new ArrayList<>();


    public Recipe() {
        id = -1;
        name = "";
        servings = Servings.SERVINGS_2;
        preparationTime = PreparationTime.HOUR_1;
        directions = "";
    }

    public Recipe(long id, @NonNull String name, Servings servings, @NonNull PreparationTime preparationTime, @NonNull String directions, byte[] featuredImageBytes, String imageFilenames, boolean preloadImages) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.directions = directions;

        this.featuredImageBytes = featuredImageBytes;
        if(featuredImageBytes != null)
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
    public void setFeaturedImageBytes(@NonNull byte[] featuredImageBytes) {
        this.featuredImageBytes = featuredImageBytes;
        this.featuredImage = ImageUtil.getBitmap(featuredImageBytes);
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
