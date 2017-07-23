package ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotDeleteDataException;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotInsertDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.view.RecipeDetailView;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 14/7/2017.
 */

public class RecipeDetailPresenterImpl implements RecipeDetailPresenter {

    private RecipeSource mRecipeSource;
    private Recipe mRecipe = null;
    private List<Bitmap> mCachedImages = new ArrayList<>();

    private RecipeDetailView mView;
    private ChefBuddyDAO mDao;

    public RecipeDetailPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }


    @Override
    public void attachView(RecipeDetailView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void setSourceData(RecipeSource recipeSource, Recipe recipe) {
        mRecipeSource = recipeSource;
        mRecipe = recipe;
        mView.initViews(mRecipeSource);
    }

    @Override
    public void downloadRecipe() {

        //Clear previous image URLs
        mRecipe.getImages().clear();

        File imageDir = FileUtil.getImageFilesDir();

        try {
            FileUtil.createDirIfNotExists(imageDir);

            //Save bitmaps into files
            for (Bitmap b : mCachedImages) {
                String imageFilename = UUID.randomUUID().toString() + Constants.IMAGE_FILE_EXTENSION;
                File file = new File(imageDir, imageFilename);
                ImageUtil.saveBitmapAsJpeg(file, b, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);

                //Save the filename into the recipe
                mRecipe.getImages().add(imageFilename);
            }

            mDao.insertRecipe(mRecipe);
        } catch (CouldNotInsertDataException e) {
            mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);
        } catch (IOException e) {
            mView.showErrorMessage(Message.ERROR_SAVING_IMAGE);
        }

        mView.recipeDownloadedSoFinish();
    }


    @Override
    public void reloadRecipe() {
        switch (mRecipeSource) {
            case DATABASE:
                try {
                    mRecipe = mDao.getRecipe(mRecipe.getId());
                    mView.showRecipe(mRecipe, mRecipeSource);
                } catch (CouldNotGetDataException e) {
                    mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);
                }
                break;
            case ONLINE:
                mView.showRecipe(mRecipe, mRecipeSource);
                break;
        }
    }

    @Override
    public void deleteRecipe() {
        switch (mRecipeSource) {
            case DATABASE:
                try {
                    mDao.deleteRecipe(mRecipe.getId());
                    mView.showRecipe(mRecipe, mRecipeSource);
                } catch (CouldNotDeleteDataException e) {
                    mView.showErrorMessage(Message.ERROR_DELETING_RECIPE);
                    mView.recipeDeletedSoFinish();
                }
                break;
            case ONLINE:
                mView.showErrorMessage(Message.ERROR_DELETING_RECIPE);
                break;
        }
    }

    @Override
    public Recipe getLoadedRecipe() {
        return mRecipe;
    }

    @Override
    public void clearCachedBitmaps() {
        mCachedImages.clear();
    }

    @Override
    public void cacheBitmap(Bitmap bitmap) {
        mCachedImages.add(bitmap);
    }

}
