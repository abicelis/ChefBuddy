package ve.com.abicelis.chefbuddy.ui.imageGallery.view;

import java.util.List;

import ve.com.abicelis.chefbuddy.app.Message;

/**
 * Created by abicelis on 19/7/2017.
 */

public interface ImageGalleryView {
    void showImages(List<String> imageFilenames);
    void showErrorMessage(Message message);
}
