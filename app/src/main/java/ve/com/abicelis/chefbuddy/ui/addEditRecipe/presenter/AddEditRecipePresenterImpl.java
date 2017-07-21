package ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter;

import android.graphics.Bitmap;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotInsertDataException;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotUpdateDataException;
import ve.com.abicelis.chefbuddy.model.Image;
import ve.com.abicelis.chefbuddy.model.PreparationTime;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.model.Servings;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.EditImageAdapter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.EditRecipeIngredientAdapter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 16/7/2017.
 */

public class AddEditRecipePresenterImpl implements AddEditRecipePresenter {

    //DATA
    private boolean mEditingExistingRecipe = false;
    private Recipe mRecipe;
    private ChefBuddyDAO mDao;

    private AddEditRecipeView mView;
    private EditRecipeIngredientAdapter mEditRecipeIngredientAdapter;
    private EditImageAdapter mEditImageAdapter;

    public AddEditRecipePresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
        mRecipe = new Recipe();
    }

    @Override
    public void attachViews(AddEditRecipeView view, EditRecipeIngredientAdapter editRecipeIngredientAdapter, EditImageAdapter editImageAdapter) {
        mView = view;
        mEditRecipeIngredientAdapter = editRecipeIngredientAdapter;
        mEditImageAdapter = editImageAdapter;
    }

    @Override
    public void detachViews() {
        mView = null;
        mEditRecipeIngredientAdapter = null;
        mEditImageAdapter = null;
    }



    @Override
    public void setExistingRecipe(long recipeId) {
        mEditingExistingRecipe = true;
        try {
            mRecipe = mDao.getRecipe(recipeId);
            attachRecipeToAdapters();
            if(mView != null)
                mView.showRecipe(mRecipe);
        } catch (CouldNotGetDataException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_LOADING_RECIPE);
        }
    }

    @Override
    public void creatingNewRecipe() {
        attachRecipeToAdapters();
        if(mView != null)
            mView.showRecipe(mRecipe);
    }

    private void attachRecipeToAdapters() {
        mEditRecipeIngredientAdapter.setItems(mRecipe.getRecipeIngredients());
        mEditImageAdapter.setItems(mRecipe.getImages());
    }

    @Override
    public void saveRecipe(String name, String preparation) {
        mRecipe.setName(name);
        mRecipe.setDirections(preparation);

        updateFeaturedImage();

        Message valid = mRecipe.checkIfValid();
        if(valid == null) {
            try {
                if(isEditingExistingRecipe())
                    mDao.updateRecipe(mRecipe);
                else
                    mDao.insertRecipe(mRecipe);

                if(mView != null)
                    mView.recipeSavedSoFinish();
            } catch (CouldNotInsertDataException | CouldNotUpdateDataException e) {
                if(mView != null)
                    mView.showErrorMessage(Message.ERROR_SAVING_RECIPE);
            }
        } else {
            if(mView != null)
                mView.showErrorMessage(valid);
        }
    }

    private void updateFeaturedImage() {

        if(mRecipe.getImages().size() > 0) {

            try {
                Bitmap thumbnail = ImageUtil.scaleBitmap(mRecipe.getImages().get(0).getImage(), Constants.THUMBNAIL_LARGER_DIMENSION);
                byte[] thumbnailBytes = ImageUtil.toCompressedByteArray(thumbnail, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);
                mRecipe.setFeaturedImageBytes(thumbnailBytes);
            } catch (Exception e) {
                if(mView != null)
                    mView.showErrorMessage(Message.ERROR_SAVING_RECIPE);
            }
        }

    }

    @Override
    public boolean isEditingExistingRecipe() {
        return mEditingExistingRecipe;
    }



    @Override
    public int getServingsSelection() {
        return mRecipe.getServings().ordinal();
    }

    @Override
    public void setServingsSelection(int position) {
        mRecipe.setServings(Servings.values()[position]);
    }

    @Override
    public int getPreparationTimeSelection() {
        return mRecipe.getPreparationTime().ordinal();
    }

    @Override
    public void setPreparationTimeSelection(int position) {
        mRecipe.setPreparationTime(PreparationTime.values()[position]);
    }



    @Override
    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        mEditRecipeIngredientAdapter.addItem(recipeIngredient);
    }
    @Override
    public void addImage(Image image) {
        mEditImageAdapter.addItem(image);
    }


}
