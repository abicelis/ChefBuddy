package ve.com.abicelis.chefbuddy.model.food2fork;

import java.util.List;


/**
 * Created by abicelis on 21/7/2017.
 */

public class Food2ForkRecipe {

    private String title;
    private String source_url;
    private String recipe_id;
    private String image_url;
    private List<String> ingredients;

    public String getTitle() {
        return title;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
