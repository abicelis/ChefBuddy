package ve.com.abicelis.chefbuddy.ui.imageGallery.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Image;

/**
 * Created by abicelis on 19/7/2017.
 */

public interface ImageGalleryView {
    void showImages(List<Image> images);
    void showErrorMessage(Message message);
}
