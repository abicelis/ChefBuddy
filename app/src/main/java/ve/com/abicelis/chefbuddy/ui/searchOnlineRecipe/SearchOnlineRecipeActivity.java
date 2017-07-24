package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.OnlineSourceApi;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeDetailActivity;
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

    @BindView(R.id.activity_search_online_recipe_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_search_online_recipe_query)
    EditText mQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_online_recipe);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpRecyclerView();
        mQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {     //Handle soft keyboard search action
                    handleSearch();
                    return true;
                }
                return false;
            }
        });

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

    }

    private void setUpToolbar() {
        //Setup toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.icon_back_material));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView() {

        mLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mSearchOnlineRecipeListAdapter = new SearchOnlineRecipeListAdapter(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mSearchOnlineRecipeListAdapter);
        mSearchOnlineRecipeListAdapter.setOnRecyclerViewClickListener(new SearchOnlineRecipeListAdapter.RecyclerViewClickListener() {
            @Override
            public void onRecyclerViewClicked(int position) {
                mPresenter.getRecipeDetail(position);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_online_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String message;
        switch (id) {
            case R.id.menu_search_online_recipe_search:
                handleSearch();
                break;
            case R.id.menu_search_online_recipe_source_edamam:
                mPresenter.setSourceApi(OnlineSourceApi.EDAMAM);
                message = String.format(Locale.getDefault(), getString(R.string.activity_search_online_recipe_api_changed), getString(R.string.activity_search_online_recipe_api_name_edamam));
                SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.SUCCESS, message, SnackbarUtil.SnackbarDuration.SHORT, null);
                item.setChecked(true);
                break;

            case R.id.menu_search_online_recipe_source_food_2_fork:
                mPresenter.setSourceApi(OnlineSourceApi.FOOD2FORK);
                message = String.format(Locale.getDefault(), getString(R.string.activity_search_online_recipe_api_changed), getString(R.string.activity_search_online_recipe_api_name_food_2_fork));
                SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.SUCCESS, message, SnackbarUtil.SnackbarDuration.SHORT, null);
                item.setChecked(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleSearch() {
        if(!mSwipeRefreshLayout.isRefreshing()) {
            if (mQuery.getText().toString().trim().isEmpty()) {
                SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.NOTICE, R.string.activity_search_online_recipe_invalid_query, SnackbarUtil.SnackbarDuration.SHORT, null);
                return;
            }
            mPresenter.searchRecipes(mQuery.getText().toString());

            //Hide keyboard
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mQuery.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }




    /* SearchOnlineRecipeView interface implementation */

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void clearRecipes() {
        mSearchOnlineRecipeListAdapter.getItems().clear();
        mSearchOnlineRecipeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mSearchOnlineRecipeListAdapter.getItems().clear();
        mSearchOnlineRecipeListAdapter.getItems().addAll(recipes);
        mSearchOnlineRecipeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRecipeDetail(int position, Recipe recipe) {
        //Update recipe data (with ingredients if it is Food2Fork)!
        mSearchOnlineRecipeListAdapter.getItems().set(position, recipe);
        mSearchOnlineRecipeListAdapter.notifyItemChanged(position);

        Intent viewOnlineRecipeDetailIntent = new Intent(this, RecipeDetailActivity.class);
        viewOnlineRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE, recipe);
        viewOnlineRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE, RecipeSource.ONLINE);
        startActivity(viewOnlineRecipeDetailIntent);
    }

    @Override
    public void showMessage(Message message, SnackbarUtil.SnackbarType snackbarType) {
        SnackbarUtil.showSnackbar(mContainer, snackbarType, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }
}
