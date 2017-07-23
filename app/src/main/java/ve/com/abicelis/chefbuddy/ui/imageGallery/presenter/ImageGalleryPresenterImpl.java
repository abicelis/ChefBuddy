package ve.com.abicelis.chefbuddy.ui.imageGallery.presenter;

import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.imageGallery.view.ImageGalleryView;

/**
 * Created by abicelis on 19/7/2017.
 */

public class ImageGalleryPresenterImpl implements ImageGalleryPresenter {

    //DATA
    private ChefBuddyDAO mDao;
    private ImageGalleryView mView;
    private RecipeSource mRecipeSource;
    private Recipe mRecipe;
    private int mPosition;

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
    public void setSourceData(RecipeSource recipeSource, Recipe recipe, int position) {
        mRecipeSource = recipeSource;
        mRecipe = recipe;
        mPosition = position;

        switch (mRecipeSource) {
            case DATABASE:
                try{
                    mRecipe = mDao.getRecipe(mRecipe.getId());
                } catch (CouldNotGetDataException e) {
                        mView.showErrorMessage(Message.ERROR_LOADING_IMAGES);
                }
                break;
            case ONLINE:
                break;
        }
        mView.showImages(mRecipeSource, mRecipe.getImageFilenames(), mPosition);
    }

}
