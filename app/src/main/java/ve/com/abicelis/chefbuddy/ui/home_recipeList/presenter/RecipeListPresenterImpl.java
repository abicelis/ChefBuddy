package ve.com.abicelis.chefbuddy.ui.home_recipeList.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.home_recipeList.view.RecipeListView;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;

/**
 * Created by abicelis on 8/7/2017.
 */

public class RecipeListPresenterImpl implements RecipeListPresenter {

    ChefBuddyDAO mDao;
    private RecipeListView mView;

    public RecipeListPresenterImpl(ChefBuddyDAO dao) {
        mDao = dao;
    }

    @Override
    public void attachView(RecipeListView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getRecipes() {

        // TODO: 10/7/2017 THIS IS A HORRIBLE HACK, DELETE THIS
        //See if recipes need pics
        try {
            List<Recipe> recipes = mDao.getRecipes();

            if(recipes.size() > 0 && recipes.get(0).getFeaturedImage() == null) {
                //Recipes don't have images.

                //Create image dir
                File imageDir = FileUtil.getImageFilesDir();
                FileUtil.createDirIfNotExists(imageDir);
                saveDrawableAsImage(imageDir, "1.jpg", R.drawable.pizza);
                saveDrawableAsImage(imageDir, "2.jpg", R.drawable.hummus);
                saveDrawableAsImage(imageDir, "3.jpg", R.drawable.burger);
                saveDrawableAsImage(imageDir, "4.jpg", R.drawable.salad);
                saveDrawableAsImage(imageDir, "5.jpg", R.drawable.pasta);

                for (Recipe r: recipes) {

                    Drawable dp = null;
                    switch (r.getName()) {
                        case "Pizza Gloria":
                            dp = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), R.drawable.pizza);
                            break;

                        case "Hummus":
                            dp = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), R.drawable.hummus);
                            break;


                        case "Burgers":
                            dp = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), R.drawable.burger);
                            break;


                        case "Caesar Salad":
                            dp = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), R.drawable.salad);
                            break;


                        case "Carbonara Pasta":
                            dp = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), R.drawable.pasta);
                            break;

                        default:
                            dp = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), R.drawable.pasta);
                            break;

                    }

                    Bitmap bp = ImageUtil.getBitmap(dp);
                    byte[] btp = ImageUtil.toCompressedByteArray(bp, 30);


                    mDao.deleteRecipe(r.getId());
                    r.setFeaturedImageBytes(btp);
                    mDao.insertRecipe(r);

                }

            }
        } catch (Exception e) {
            Exception poop = e;
            String message = e.getMessage();
            message += "asd";

        }




        mView.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mView.showRecipes(mDao.getRecipes());
                } catch (CouldNotGetDataException e) {
                    mView.showErrorMessage(Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES);
                }
            }
        }, 1000);


    }

    //TODO Kill this eventually
    private void saveDrawableAsImage(File imageDir, String filename, @DrawableRes int drawable) throws IOException {
        Drawable d = ContextCompat.getDrawable(ChefBuddyApplication.getContext(), drawable);
        Bitmap b = ImageUtil.getBitmap(d);
        ImageUtil.saveBitmapAsJpeg(new File(imageDir, filename), b, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);
    }


    @Override
    public void cancelFilteredRecipes() {
        try {
            mView.showRecipes(mDao.getRecipes());
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES);
        }
    }

    @Override
    public void getFilteredRecipes(@NonNull String query) {
        try {
            mView.showRecipes(mDao.getFilteredRecipes(query));
        } catch (CouldNotGetDataException e) {
            mView.showErrorMessage(Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES);
        }
    }
}
