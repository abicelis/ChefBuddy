package ve.com.abicelis.chefbuddy.ui.home;

import java.util.List;

import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.app.Message;

/**
 * Created by abicelis on 8/7/2017.
 */

public interface HomeView {
    void showRecipes(List<Recipe> recipes);
    void showErrorMessage(Message message);
}
