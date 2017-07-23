package ve.com.abicelis.chefbuddy.ui.imageGallery.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.RecipeSource;

/**
 * Created by abicelis on 19/7/2017.
 */

public interface ImageGalleryView {
    void showImages(RecipeSource recipeSource, List<String> images, int position);
    void showErrorMessage(Message message);
}
