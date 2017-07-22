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
        mView.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if(mView != null)
                        mView.showRecipes(mDao.getRecipes());
                } catch (CouldNotGetDataException e) {
                    if(mView != null)
                        mView.showErrorMessage(Message.ERROR_LOADING_RECIPES);
                }
            }
        }, 1000);
    }


    @Override
    public void cancelFilteredRecipes() {
        try {
            if(mView != null)
                mView.showRecipes(mDao.getRecipes());
        } catch (CouldNotGetDataException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_LOADING_RECIPES);
        }
    }

    @Override
    public void getFilteredRecipes(@NonNull String query) {
        try {
            if(mView != null)
                mView.showRecipes(mDao.getFilteredRecipes(query));
        } catch (CouldNotGetDataException e) {
            if(mView != null)
                mView.showErrorMessage(Message.ERROR_LOADING_RECIPES);
        }
    }
}
