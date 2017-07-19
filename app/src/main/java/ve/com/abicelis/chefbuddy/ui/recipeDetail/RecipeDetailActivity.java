package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.AddEditRecipeActivity;
import ve.com.abicelis.chefbuddy.ui.imageGallery.ImageGalleryActivity;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenter;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.view.RecipeDetailView;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 13/7/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, RecipeDetailView {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @Inject
    RecipeDetailPresenter recipeDetailPresenter;


    @BindView(R.id.activity_recipe_detail_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.activity_recipe_detail_appbar)
    AppBarLayout mAppBarLayout;


    /* Appbar FrameLayout header */
    @BindView(R.id.activity_recipe_detail_header_image)
    ImageView mTitleImage;

    @BindView(R.id.activity_recipe_detail_header_recipe_linearlayout)
    LinearLayout mTitleContainer;

    @BindView(R.id.activity_recipe_detail_header_recipe_title)
    TextView mTitleTitle;

    @BindView(R.id.activity_recipe_detail_header_recipe_detail)
    TextView mTitleDetail;


    /* Top Toolbar */
    @BindView(R.id.activity_recipe_detail_toolbar_top)
    Toolbar mToolbar;

    @BindView(R.id.activity_recipe_detail_toolbar_container)
    LinearLayout mToolbarContainer;

    @BindView(R.id.activity_recipe_detail_toolbar_title)
    TextView mToolbarTitle;

//    @BindView(R.id.activity_recipe_detail_toolbar_logo)
//    CircleImageView mToolbarLogo;


    /* Floating "bottom" Toolbar */
    @BindView(R.id.activity_recipe_detail_toolbar_bottom)
    Toolbar mToolbarBottom;


    /* Big round CircleImageView */
    @BindView(R.id.activity_recipe_detail_circle_image)
    CircleImageView mImage;


    /* Share FAB */
    @BindView(R.id.activity_recipe_detail_fab_share)
    FloatingActionButton mFabShare;


    /* NestedScrollView bottom content */
    @BindView(R.id.activity_recipe_detail_preparation)
    TextView mPreparation;


    /* Ingredients recycler */
    @BindView(R.id.activity_recipe_detail_ingredients_recycler)
    RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.activity_recipe_detail_ingredients_no_items)
    TextView mNoIngredients;
    private LinearLayoutManager mIngredientsLayoutManager;
    private RecipeIngredientAdapter mIngredientsAdapter;


    /* Images recycler */
    @BindView(R.id.activity_recipe_detail_images_recycler)
    RecyclerView mImagesRecyclerView;
    @BindView(R.id.activity_recipe_detail_images_no_items)
    TextView mNoImages;
    private LinearLayoutManager mImagesLayoutManager;
    private ImageAdapter mImageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        initViews();

        //Init Presenter
        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        recipeDetailPresenter.attachView(this);

        //Handle incoming Intent
        if(getIntent().hasExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_ID)) {
            long recipeId = getIntent().getLongExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_ID, -1);
            recipeDetailPresenter.setRecipeId(recipeId);
        } else
            showErrorMessage(Message.ERROR_LOADING_RECIPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recipeDetailPresenter.reloadRecipe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recipeDetailPresenter.detachView();

    }

    private void initViews() {

        //Hide upper toolbar (toolbarContainer)
        startAlphaAnimation(mToolbar, 0, View.INVISIBLE);

        //Hook into appbar movement to hide/show views
        mAppBarLayout.addOnOffsetChangedListener(this);

        //Setup FAB onClick()
        mFabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.SUCCESS, R.string.activity_recipe_detail_share, SnackbarUtil.SnackbarDuration.SHORT, null);
            }
        });

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.icon_back_material));
        mToolbar.inflateMenu(R.menu.menu_recipe_detail);
        mToolbar.setOnMenuItemClickListener(new ToolbarMenuItemClickListener());
        mToolbar.setNavigationOnClickListener(new NavigationBackListener());

        mToolbarBottom.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.icon_back_material));
        mToolbarBottom.inflateMenu(R.menu.menu_recipe_detail);
        mToolbarBottom.getMenu().findItem(R.id.menu_recipe_detail_share).setVisible(false);   //Hide Share option
        mToolbarBottom.setOnMenuItemClickListener(new ToolbarMenuItemClickListener());
        mToolbarBottom.setNavigationOnClickListener(new NavigationBackListener());


        //Ingredients Recyclerview
        mIngredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIngredientsAdapter = new RecipeIngredientAdapter(this);

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);


        //Image Recyclerview
        mImagesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mImageAdapter = new ImageAdapter(this);
        mImageAdapter.setImageClickListener(new ImageAdapter.ImageClickListener() {
            @Override
            public void onImageClicked(int position) {
                Intent openImageGalleryIntent = new Intent(RecipeDetailActivity.this, ImageGalleryActivity.class);
                openImageGalleryIntent.putExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_RECIPE_ID, recipeDetailPresenter.getLoadedRecipe().getId());
                openImageGalleryIntent.putExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_IMAGE_POSITION, position);
                startActivity(openImageGalleryIntent);
            }
        });

        //DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), mLayoutManager.getOrientation());
        //itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.item_decoration_complete_line));
        //mImagesRecyclerView.addItemDecoration(itemDecoration);

        mImagesRecyclerView.setLayoutManager(mImagesLayoutManager);
        mImagesRecyclerView.setAdapter(mImageAdapter);
        mImagesRecyclerView.setNestedScrollingEnabled(false);
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

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mToolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mToolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitleAndCircularImage(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mToolbarBottom, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mToolbarBottom, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }



    /*
    *   RecipeDetailView interface implementation
    */
    @Override
    public void showRecipe(Recipe recipe) {
        mToolbarTitle.setText(recipe.getName());
        //mToolbarLogo.setImageBitmap(recipe.getFeaturedImage());

        if(recipe.getFeaturedImage() != null) {
            mTitleImage.setImageBitmap(recipe.getFeaturedImage());
            mImage.setImageBitmap(recipe.getFeaturedImage());
        } else {
            mImage.setVisibility(View.GONE);
            mTitleImage.setImageResource(R.drawable.default_recipe_image);
        }

        mTitleTitle.setText(recipe.getName());
        mTitleDetail.setText(String.format(Locale.getDefault(),
                getResources().getString(R.string.activity_recipe_detail_title_detail_format),
                recipe.getServings().getFriendlyName(),
                recipe.getPreparationTime().getFriendlyName()));

        mPreparation.setText(recipe.getDirections());


        //Ingredients recyclerView
        mIngredientsAdapter.getItems().clear();
        mIngredientsAdapter.getItems().addAll(recipe.getRecipeIngredients());
        mIngredientsAdapter.notifyDataSetChanged();

        if(mIngredientsAdapter.getItems().size() == 0) {
            mNoIngredients.setVisibility(View.VISIBLE);
            mIngredientsRecyclerView.setVisibility(View.GONE);
        } else {
            mNoIngredients.setVisibility(View.GONE);
            mIngredientsRecyclerView.setVisibility(View.VISIBLE);
        }


        //Images recyclerView
        mImageAdapter.getItems().clear();
        mImageAdapter.getItems().addAll(recipe.getImages());
        mImageAdapter.notifyDataSetChanged();

        if(mImageAdapter.getItems().size() == 0) {
            mNoImages.setVisibility(View.VISIBLE);
            mImagesRecyclerView.setVisibility(View.GONE);
        } else {
            mNoImages.setVisibility(View.GONE);
            mImagesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }


    private class ToolbarMenuItemClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_recipe_detail_edit:
                    Intent editRecipeIntent = new Intent(RecipeDetailActivity.this, AddEditRecipeActivity.class);
                    editRecipeIntent.putExtra(Constants.ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID, recipeDetailPresenter.getLoadedRecipe().getId());
                    startActivity(editRecipeIntent);
                    break;

                case R.id.menu_recipe_detail_delete:
                    SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.ERROR, R.string.activity_recipe_detail_delete, SnackbarUtil.SnackbarDuration.SHORT, null);
                    break;
                case R.id.menu_recipe_detail_share:
                    SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.SUCCESS, R.string.activity_recipe_detail_share, SnackbarUtil.SnackbarDuration.SHORT, null);
                    break;

            }
            return true;
        }
    }
    private class NavigationBackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }
}