package ve.com.abicelis.chefbuddy.ui.editImageActivity.presenter;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.ui.editImageActivity.view.EditImageView;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 20/7/2017.
 */

public class EditImagePresenterImpl implements EditImagePresenter {

    //DATA
    private String mImageFilename;
    private Bitmap mImage;
    private EditImageView mView;


    public EditImagePresenterImpl() {
        //Nothing for now
    }

    @Override
    public void attachView(EditImageView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void startImageCapture() {
        File imageDir = FileUtil.getImageFilesDir();
        try {
            FileUtil.createDirIfNotExists(imageDir);
            mImageFilename = UUID.randomUUID().toString() + Constants.IMAGE_FILE_EXTENSION;
            FileUtil.createNewFileIfNotExistsInDir(imageDir, mImageFilename);
        } catch (IOException e) {
            if (mView != null)
                mView.showErrorMessage(Message.ERROR_CREATING_IMAGE);
            return;
        }

        if(mView != null)
            mView.showSelectImageSourceDialog();
    }

    @Override
    public String getImageFilename() {
        return mImageFilename;
    }



    @Override
    public Bitmap getTempImage() {
        return mImage;
    }

    @Override
    public void setTempImage(Bitmap image) {
        mImage = image;
    }

    @Override
    public void saveImage() {
        try {
            File file = new File(FileUtil.getImageFilesDir(), mImageFilename);
            FileUtil.saveBitmapAsJpeg(file, mImage, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);
            if(mView != null)
                mView.imageSavedSoFinish();
        }catch (IOException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_SAVING_IMAGE);
        }
    }

}
