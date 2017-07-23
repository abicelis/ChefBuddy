package ve.com.abicelis.chefbuddy.ui.imageGallery.presenter;

import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.imageGallery.view.ImageGalleryView;

/**
 * Created by abicelis on 19/7/2017.
 */

public interface ImageGalleryPresenter {
    void attachView(ImageGalleryView view);
    void detachView();

    void setSourceData(RecipeSource recipeSource, Recipe recipe, int position);
}
