package ve.com.abicelis.chefbuddy.ui.imageGallery;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Image;
import ve.com.abicelis.chefbuddy.ui.imageGallery.presenter.ImageGalleryPresenter;
import ve.com.abicelis.chefbuddy.ui.imageGallery.view.ImageGalleryView;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 19/7/2017.
 */

public class ImageGalleryActivity extends AppCompatActivity implements ImageGalleryView {


    @Inject
    ImageGalleryPresenter mPresenter;

    //DATA
    List<Image> mImages;
    private int mPosition;

    @BindView(R.id.activity_image_gallery_view)
    ScrollGalleryView mGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

        //Set translucent things
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));`
        }

        //Get list of images from intent
        if(getIntent().hasExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_RECIPE_ID) &&
                getIntent().hasExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_IMAGE_POSITION)) {
            mPosition = getIntent().getIntExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_IMAGE_POSITION, -1);
            mPresenter.getImages(getIntent().getLongExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_RECIPE_ID, -1));
        } else
            showErrorMessage(Message.ERROR_LOADING_IMAGES);

    }



    /* ImageGalleryView interface implementation */

    @Override
    public void showImages(List<Image> images) {
        mGallery
                .setThumbnailSize(300)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager());

        for (Image i : images)
            mGallery.addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(i.getImage())));

        mGallery.setCurrentItem(mPosition);
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mGallery, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }
}
