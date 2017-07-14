package ve.com.abicelis.chefbuddy.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.ui.home.fragment.history.HistoryFragment;
import ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList.RecipeListFragment;
import ve.com.abicelis.chefbuddy.ui.home.fragment.spinWheel.SpinWheelFragment;
import ve.com.abicelis.chefbuddy.ui.home.presenter.HomePresenter;
import ve.com.abicelis.chefbuddy.ui.home.view.HomeView;

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
    @BindView(R.id.activity_home_viewpager)
    ViewPager mViewpager;

    @BindView(R.id.activity_home_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.activity_home_search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.activity_home_appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.activity_home_toolbar)
    Toolbar mToolbar;

    private HomeViewPagerAdapter mHomeViewPagerAdapter;
    private RecipeListFragment mRecipeListFragment;
    private SpinWheelFragment mSpinWheelFragment;
    private HistoryFragment mHistoryFragment;

    //DATA
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        presenter.attachView(this);

        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setLogo(R.drawable.ic_toolbar_home);

        setupViewPagerAndTabLayout();
        setupSearchView();
    }

    private void setupViewPagerAndTabLayout() {

        //Clear lists
        titleList.clear();
        fragmentList.clear();

        //Populate title list
        titleList.add(getResources().getString(R.string.activity_home_tab_recipe_title));
        titleList.add(getResources().getString(R.string.activity_home_tab_spin_wheel_title));
        titleList.add(getResources().getString(R.string.activity_home_tab_history_title));

        //Populate fragment list
        mRecipeListFragment = new RecipeListFragment();
        fragmentList.add(mRecipeListFragment);
        mSpinWheelFragment = new SpinWheelFragment();
        fragmentList.add(mSpinWheelFragment);
        mHistoryFragment = new HistoryFragment();
        fragmentList.add(mHistoryFragment);


        //Setup adapter, viewpager and tabLayout
        mHomeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        mViewpager.setAdapter(mHomeViewPagerAdapter);
        mViewpager.setCurrentItem(0);     //Start at page 1
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //If scrolling out of page one, close search
                if(position == 0 && mSearchView.isSearchOpen()) {
                    mSearchView.closeSearch();
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }

    private void setupSearchView() {
        mSearchView.setVoiceSearch(true);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit " + query);
                if(mHomeViewPagerAdapter.getRegisteredFragment(0) != null) {
                    if (query.isEmpty())
                        ((RecipeListFragment) mHomeViewPagerAdapter.getRegisteredFragment(0)).cancelFilterRecipes();
                    else
                        ((RecipeListFragment) mHomeViewPagerAdapter.getRegisteredFragment(0)).filterRecipes(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mHomeViewPagerAdapter.getRegisteredFragment(0) != null) {
                    if (newText.isEmpty())
                        ((RecipeListFragment) mHomeViewPagerAdapter.getRegisteredFragment(0)).cancelFilterRecipes();
                    else
                        ((RecipeListFragment) mHomeViewPagerAdapter.getRegisteredFragment(0)).filterRecipes(newText);
                }
                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //mAppBarLayout.setExpanded(false, true);
                mTabLayout.setVisibility(View.GONE);
                Log.d(TAG, "onSearchViewShown");
            }

            @Override
            public void onSearchViewClosed() {
                //mAppBarLayout.setExpanded(true, true);
                mTabLayout.setVisibility(View.VISIBLE);
                ((RecipeListFragment) mHomeViewPagerAdapter.getRegisteredFragment(0)).cancelFilterRecipes();
                Log.d(TAG, "onSearchViewClosed");
            }
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
//            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            if (matches != null && matches.size() > 0) {
//                String searchWrd = matches.get(0);
//                if (!TextUtils.isEmpty(searchWrd)) {
//                    mSearchView.setQuery(searchWrd, false);
//                }
//            }
//
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem item = menu.findItem(R.id.menu_home_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_home_about:
                Toast.makeText(this, "About screen under construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_home_settings:
                Toast.makeText(this, "Settings screen under construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_home_search:
                Toast.makeText(this, "Search under construction", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
