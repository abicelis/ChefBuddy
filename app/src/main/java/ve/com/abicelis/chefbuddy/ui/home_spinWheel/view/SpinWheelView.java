package ve.com.abicelis.chefbuddy.ui.home_spinWheel.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 27/7/2017.
 */

public interface SpinWheelView {
    void refreshView(List<Recipe> recipes);
    void noRecipesAvailable();
    void updateBottomRecipe(Recipe recipe);

    void showErrorMessage(Message message);
}
