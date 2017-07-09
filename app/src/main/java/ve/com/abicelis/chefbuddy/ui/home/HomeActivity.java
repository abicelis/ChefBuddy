package ve.com.abicelis.chefbuddy.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.ui.home.fragment.HistoryFragment;
import ve.com.abicelis.chefbuddy.ui.home.fragment.RecipeListFragment;
import ve.com.abicelis.chefbuddy.ui.home.fragment.SpinWheelFragment;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_home_toolbar_title);

        setupViewPagerAndTabLayout();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
                // if(mHomeViewPagerAdapter.getRegisteredFragment(position).mActionMode != null)
                //      mHomeViewPagerAdapter.getRegisteredFragment(position).mActionMode.finish(); //If changing tabs, hide actionMode (ContextMenu)
            }

            @Override
            public void onPageSelected(int position) {
                //If changing tabs, hide actionMode (ContextMenu)
//                for(int i = 0; i < mHomeViewPagerAdapter.getRegisteredFragments().size(); i++) {
//                    int key = mHomeViewPagerAdapter.getRegisteredFragments().keyAt(i);
//
//                    if(mHomeViewPagerAdapter.getRegisteredFragments().get(key).mActionMode != null)
//                        mHomeViewPagerAdapter.getRegisteredFragments().get(key).mActionMode.finish();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }



}
