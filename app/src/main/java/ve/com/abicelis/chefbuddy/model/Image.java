package ve.com.abicelis.chefbuddy.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 15/7/2017.
 */

public class Image {

    private String filename;
    private Bitmap image;

    public Image(@NonNull String filename, boolean preLoadImage) {
        this.filename = filename;
        if(preLoadImage) loadImage();
    }

    public void loadImage() {
        try {
            this.image = ImageUtil.getBitmap(FileUtil.getImageFilesDir(), filename);
        }catch (Exception e ) { /* Do nothing */ }
    }

    public String getFilename() {
        return filename;
    }

    public Bitmap getImage() {
        return image;
    }
}
