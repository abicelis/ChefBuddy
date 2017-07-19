package ve.com.abicelis.chefbuddy.ui.imageGallery.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.imageGallery.view.ImageGalleryView;

/**
 * Created by abicelis on 19/7/2017.
 */

public class ImageGalleryPresenterImpl implements ImageGalleryPresenter {

    //DATA
    private ChefBuddyDAO mDao;
    private ImageGalleryView mView;
    private Recipe mRecipe;

    public ImageGalleryPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void attachView(ImageGalleryView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getImages(long recipeId) {
        try{
            mRecipe = mDao.getRecipe(recipeId);
            if(mView != null)
                mView.showImages(mRecipe.getImages());
        } catch (CouldNotGetDataException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_LOADING_IMAGES);
        }
    }
}
