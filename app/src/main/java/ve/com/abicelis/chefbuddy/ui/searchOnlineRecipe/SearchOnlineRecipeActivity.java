package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter.SearchOnlineRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view.SearchOnlineRecipeView;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 21/7/2017.
 */

public class SearchOnlineRecipeActivity extends AppCompatActivity implements SearchOnlineRecipeView {

    @Inject
    SearchOnlineRecipePresenter mPresenter;


    @BindView(R.id.activity_search_online_recipe_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.activity_search_online_recipe_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_search_online_recipe_no_items_container)
    RelativeLayout mNoItemsContainer;

    private LinearLayoutManager mLayoutManager;
    private SearchOnlineRecipeListAdapter mSearchOnlineRecipeListAdapter;

    @BindView(R.id.activity_search_online_recipe_container)
    LinearLayout mContainer;

    @BindView(R.id.activity_search_online_recipe_text)
    TextView mText;

    @BindView(R.id.activity_search_online_recipe_loading)
    TextView mLoading;

    @BindView(R.id.activity_search_online_recipe_query)
    EditText mQuery;

    @BindView(R.id.activity_search_online_recipe_go)
    Button mSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_online_recipe);
        ButterKnife.bind(this);

        setUpRecyclerView();
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getRecipes(mQuery.getText().toString());
            }
        });

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

    }

    private void setUpRecyclerView() {

        mLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mSearchOnlineRecipeListAdapter = new SearchOnlineRecipeListAdapter(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mSearchOnlineRecipeListAdapter);
        mSearchOnlineRecipeListAdapter.setOnRecyclerViewClickListener(new SearchOnlineRecipeListAdapter.RecyclerViewClickListener() {
            @Override
            public void onRecyclerViewClicked() {
                // TODO: 22/7/2017 hide search or something
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_green, R.color.swipe_refresh_red, R.color.swipe_refresh_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    /* SearchOnlineRecipeView interface implementation */

    @Override
    public void showLoading() {
        mLoading.setText("Loading");
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mLoading.setText("Done Loading");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mSearchOnlineRecipeListAdapter.getItems().clear();
        mSearchOnlineRecipeListAdapter.getItems().addAll(recipes);
        mSearchOnlineRecipeListAdapter.notifyDataSetChanged();
        mText.setText("Results done");
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }
}
