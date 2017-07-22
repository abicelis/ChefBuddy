package ve.com.abicelis.chefbuddy.model.edamam;

import java.util.List;

/**
 * Created by abicelis on 21/7/2017.
 */

public class EdamamRecipe {

    private String label;
    private String image;
    private String url;
    private int yield;
    private List<EdamamIngredient> ingredients;

    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public int getYield() {
        return yield;
    }

    public List<EdamamIngredient> getIngredients() {
        return ingredients;
    }
}
