package ve.com.abicelis.chefbuddy.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;

import static ve.com.abicelis.chefbuddy.app.Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES;

/**
 * Created by abicelis on 8/7/2017.
 */

public class HomeActivity extends AppCompatActivity implements HomeView {

    //CONST
    private static final String TAG = HomeActivity.class.getSimpleName();

    //DATA
    @Inject
    HomePresenter presenter;

    //UI


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);

        presenter.setView(this);
        presenter.getRecipes();
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            Log.d(TAG, "Found recipe in DB:\n\r" + recipe.toString() + "\n\r\n\r");
        }

        // TODO: 8/7/2017 call refresh on recyclerview with new recipes
    }

    @Override
    public void showErrorMessage(Message message) {
        switch (message) {
            case HOME_ACTIVITY_ERROR_LOADING_RECIPES:
                Log.d(TAG, HOME_ACTIVITY_ERROR_LOADING_RECIPES.getFriendlyName());
            default:
                Log.d(TAG, "Unexpected unknown error.");

        }
    }
}
