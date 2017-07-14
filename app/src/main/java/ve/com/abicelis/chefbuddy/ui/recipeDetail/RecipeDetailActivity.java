package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 13/7/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity

//    @BindView(R.id.activity_recipe_detail_check)
//    FloatingActionButton mFabCheck;
//
//    @BindView(R.id.activity_recipe_detail_share)
//    FloatingActionButton mFabShare;
//
//    @BindView(R.id.activity_recipe_detail_header_text)
//    TextView mHeaderText;
//
//    @BindView(R.id.activity_recipe_detail_appbar)
//    AppBarLayout mAppBarLayout;
//
//    @BindView(R.id.activity_recipe_detail_toolbar)
//    Toolbar mToolbar;

        // @Override
        //  protected void onCreate(@Nullable Bundle savedInstanceState) {
        //      super.onCreate(savedInstanceState);
        //      setContentView(R.layout.activity_recipe_detail_2);
        //      ButterKnife.bind(this);
//        setSupportActionBar(mToolbar);
//
//
//        // TODO: 13/7/2017 Actually, send the extra to the presenter and let it tell view to display recipe or show error...
//        if(getIntent().hasExtra(Constants.RECIPE_LIST_INTENT_EXTRA_RECIPE_ID)) {
//            long recipeId = getIntent().getLongExtra(Constants.RECIPE_LIST_INTENT_EXTRA_RECIPE_ID, -1);
//            mHeaderText.setText("ID of recipe=" + String.valueOf(recipeId));
//
//        } else {
//            //TODO: Error
//            mHeaderText.setText("Error");
//
//        }
//
//        mAppBarLayout.addOnOffsetChangedListener(this);



        //}

        //private void setupToolbar() {

        //Setup toolbar
//        getSupportActionBar().setTitle("Recipe name");
//        mToolbar.inflateMenu(R.menu.menu_recipe_detail);
//
//        getSupportActionBar().setLogo(R.drawable.ic_toolbar_home);






        implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.1f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;



    @BindView(R.id.activity_recipe_detail_appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.activity_recipe_detail_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    /* Big round CircleImageView */
    @BindView(R.id.activity_recipe_detail_circle_image)
    CircleImageView mImage;



    @BindView(R.id.activity_recipe_detail_header_recipe_linearlayout)
    LinearLayout mTitleContainer;

    @BindView(R.id.activity_recipe_detail_header_recipe_title)
    TextView mTitleTitle;

    @BindView(R.id.activity_recipe_detail_header_recipe_detail)
    TextView mTitleDetail;



    @BindView(R.id.activity_recipe_detail_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_recipe_detail_toolbar_container)
    LinearLayout mToolbarContainer;

    @BindView(R.id.activity_recipe_detail_toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.activity_recipe_detail_toolbar_logo)
    CircleImageView mToolbarLogo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_2);
        ButterKnife.bind(this);

        //Hook into appbar movement to hide/show views
        mAppBarLayout.addOnOffsetChangedListener(this);

        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        //Hide toolbar logo and title (toolbarContainer)
        startAlphaAnimation(mToolbarContainer, 0, View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_recipe_detail_edit:
                SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.NOTICE, R.string.activity_recipe_detail_edit, SnackbarUtil.SnackbarDuration.SHORT, null);
                break;
            case R.id.menu_recipe_detail_delete:
                SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.ERROR, R.string.activity_recipe_detail_delete, SnackbarUtil.SnackbarDuration.SHORT, null);
                break;
        }
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitleAndCircularImage(percentage);
        handleToolbarContainerVisibility(percentage);
    }

    private void handleToolbarContainerVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mToolbarContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mToolbarContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitleAndCircularImage(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}