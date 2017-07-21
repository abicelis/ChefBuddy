package ve.com.abicelis.chefbuddy.ui.editImageActivity.view;

import java.io.File;

import ve.com.abicelis.chefbuddy.app.Message;

/**
 * Created by abicelis on 20/7/2017.
 */

public interface EditImageView {
    void showSelectImageSourceDialog();
    void showErrorMessage(Message message);
    void imageSavedSoFinish();

}
