package ve.com.abicelis.chefbuddy.ui.home_spinWheel.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.CheckedRecipe;

/**
 * Created by abicelis on 27/7/2017.
 */

public interface EditSpinWheelRecipesView {
    void showRecipes(List<CheckedRecipe> checkedRecipes);
    void wheelRecipesSetSoDismiss();
    void showErrorMessage(Message messsage);
    void showErrorMessage(String string);
}
