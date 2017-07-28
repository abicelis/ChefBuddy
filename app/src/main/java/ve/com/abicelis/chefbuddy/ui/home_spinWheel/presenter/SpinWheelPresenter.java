package ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter;

import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.view.SpinWheelView;

/**
 * Created by abicelis on 27/7/2017.
 */

public interface SpinWheelPresenter {
    void attachView(SpinWheelView view);
    void detachView();

    void getWheelRecipes();
    void wheelSettledAtPosition(int position);

}
