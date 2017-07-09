package ve.com.abicelis.chefbuddy.ui.home.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 8/7/2017.
 */

public class RecipeListFragment extends Fragment implements RecipeListView {

    //CONST
    private static final String TAG = RecipeListFragment.class.getSimpleName();

    //DATA
    @Inject
    RecipeListPresenter presenter;

    //UI
    //@BindView(R.id.fragment_recipe_list_recyclerview)
    // TODO: 8/7/2017 Reciclerview...

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);

        presenter.attachView(this);
        presenter.getRecipes();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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
                Log.d(TAG, Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES.getFriendlyName());
            default:
                Log.d(TAG, "Unexpected unknown error.");
        }
    }
}
