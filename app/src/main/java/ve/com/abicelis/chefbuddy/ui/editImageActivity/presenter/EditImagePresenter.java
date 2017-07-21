package ve.com.abicelis.chefbuddy.ui.editImageActivity.presenter;

import android.graphics.Bitmap;

import java.io.File;

import ve.com.abicelis.chefbuddy.ui.editImageActivity.view.EditImageView;

/**
 * Created by abicelis on 20/7/2017.
 */

public interface EditImagePresenter {
    void attachView(EditImageView view);
    void detachView();

    void startImageCapture();
    String getImageFilename();
    Bitmap getTempImage();
    void setTempImage(Bitmap image);
    void saveImage();
}
