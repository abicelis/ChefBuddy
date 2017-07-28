package ve.com.abicelis.chefbuddy.model;

/**
 * Created by abicelis on 27/7/2017.
 */

public class CheckedRecipe  {

    private boolean checked;
    private Recipe recipe;

    public CheckedRecipe(boolean checked, Recipe recipe) {
        this.checked = checked;
        this.recipe = recipe;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean getChecked(){
        return checked;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
    public Recipe getRecipe() {
        return recipe;
    }
}
