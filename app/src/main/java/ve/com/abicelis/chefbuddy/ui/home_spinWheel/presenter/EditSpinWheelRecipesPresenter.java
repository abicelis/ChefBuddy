package ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter;

import java.util.List;

import ve.com.abicelis.chefbuddy.model.CheckedRecipe;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.view.EditSpinWheelRecipesView;

/**
 * Created by abicelis on 27/7/2017.
 */

public interface EditSpinWheelRecipesPresenter {
    void attachView(EditSpinWheelRecipesView view);
    void detachView();

    void getRecipes();
    void setWheelRecipes(List<CheckedRecipe> checkedRecipes);
}
