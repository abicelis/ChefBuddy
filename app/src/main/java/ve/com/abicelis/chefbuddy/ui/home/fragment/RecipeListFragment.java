package ve.com.abicelis.chefbuddy.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
    @BindView(R.id.fragment_recipe_list_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_recipe_list_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_recipe_list_no_items_container)
    RelativeLayout mNoItemsContainer;
    private LinearLayoutManager mLayoutManager;
    private RecipeListAdapter mRecipeListAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        setUpRecyclerView();

        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);
        presenter.attachView(this);
        presenter.getRecipes();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Show search menu item
        menu.findItem(R.id.menu_home_search).setVisible(true);
    }


    private void setUpRecyclerView() {

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecipeListAdapter = new RecipeListAdapter(getActivity());

        //DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), mLayoutManager.getOrientation());
        //itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.item_decoration_complete_line));
        //mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecipeListAdapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_green, R.color.swipe_refresh_red, R.color.swipe_refresh_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         presenter.getRecipes();
                                                     }
                                                 }
        );
    }


    /*
     * RecipeListView interface
     */

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mSwipeRefreshLayout.setRefreshing(false);

        mRecipeListAdapter.getItems().clear();
        mRecipeListAdapter.getItems().addAll(recipes);
        mRecipeListAdapter.notifyDataSetChanged();

        if(mRecipeListAdapter.getItems().size() == 0) {
            mNoItemsContainer.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mNoItemsContainer.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showErrorMessage(Message message) {
        mSwipeRefreshLayout.setRefreshing(false);

        switch (message) {
            case HOME_ACTIVITY_ERROR_LOADING_RECIPES:
                Log.d(TAG, Message.HOME_ACTIVITY_ERROR_LOADING_RECIPES.getFriendlyName());
            default:
                Log.d(TAG, "Unexpected unknown error.");
        }
    }
}
