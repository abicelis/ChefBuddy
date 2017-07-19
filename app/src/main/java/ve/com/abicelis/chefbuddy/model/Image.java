package ve.com.abicelis.chefbuddy.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 15/7/2017.
 */
/* Parcelable made with http://www.parcelabler.com/ */
public class Image implements Parcelable {

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
        if(image == null)
            loadImage();

        return image;
    }




    protected Image(Parcel in) {
        filename = in.readString();
        image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filename);
        dest.writeValue(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
