package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
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
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.AddEditRecipeActivity;
import ve.com.abicelis.chefbuddy.ui.imageGallery.ImageGalleryActivity;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.presenter.RecipeDetailPresenter;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.view.RecipeDetailView;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.PdfUtil;
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
    RecipeDetailPresenter mPresenter;


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


    /* Download FAB */
    @BindView(R.id.activity_recipe_detail_fab_download)
    FloatingActionButton mFabDownload;


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

        //Init Presenter
        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

        //Handle incoming Intent
        if(getIntent().hasExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE)
                && getIntent().hasExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE)) {

            mPresenter.setSourceData(
                    (RecipeSource) getIntent().getSerializableExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE),
                    (Recipe) getIntent().getSerializableExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE));
        } else
            showErrorMessage(Message.ERROR_LOADING_RECIPE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.reloadRecipe();
        mPresenter.clearCachedBitmaps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mPresenter.detachView();
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

    private void handleRecipeShare() {
        //Check for storage write permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.RECIPE_DETAIL_ACTIVITY_PERMISSIONS);
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PdfDocument document = PdfUtil.generatePdfFromRecipe(mPresenter.getLoadedRecipe());
            if(document == null) {
                showErrorMessage(Message.ERROR_EXPORTING_RECIPE_TO_PDF);
                return;
            }

            File filePath;
            try {
                // TODO: 20/7/2017 REMOVE THIS NEXT LINE
                FileUtil.savePdfDocumentToSD(document, "recipe");
                filePath = FileUtil.savePdfDocumentToSD(document, mPresenter.getLoadedRecipe().getName());
                document.close();
            } catch (IOException e) {
                showErrorMessage(Message.ERROR_SAVING_PDF);
                return;
            }


            Intent sharePdfIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharePdfIntent.setType("application/pdf");
            sharePdfIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath));
            startActivity(Intent.createChooser(sharePdfIntent, getString(R.string.activity_recipe_detail_share_recipe_title)));



        } else {    //Old phone, sharing as text.
            Intent shareTextIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareTextIntent.setType("text/plain");
            shareTextIntent.putExtra(Intent.EXTRA_TEXT, "some text");
            startActivity(Intent.createChooser(shareTextIntent, getString(R.string.activity_recipe_detail_share_recipe_title)));
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Constants.RECIPE_DETAIL_ACTIVITY_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                handleRecipeShare();
            else
                showErrorMessage(Message.PERMISSIONS_WRITE_STORAGE_NOT_GRANTED);
        }

    }




    /*
     *   RecipeDetailView interface implementation
     */

    @Override
    public void initViews(final RecipeSource recipeSource) {
        //Hide upper toolbar (toolbarContainer)
        startAlphaAnimation(mToolbar, 0, View.INVISIBLE);

        //Hook into appbar movement to hide/show views
        mAppBarLayout.addOnOffsetChangedListener(this);

        //Setup FAB and toolbars
        switch (recipeSource) {
            case DATABASE:
                mFabDownload.setVisibility(View.GONE);
                mFabShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {handleRecipeShare();
                    }
                });

                mToolbar.inflateMenu(R.menu.menu_recipe_detail);
                mToolbar.setOnMenuItemClickListener(new ToolbarMenuItemClickListener());

                mToolbarBottom.inflateMenu(R.menu.menu_recipe_detail);
                mToolbarBottom.getMenu().findItem(R.id.menu_recipe_detail_share).setVisible(false);   //Hide Share option
                mToolbarBottom.setOnMenuItemClickListener(new ToolbarMenuItemClickListener());
                break;

            case ONLINE:
                mFabShare.setVisibility(View.GONE);
                mFabDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.downloadRecipe();
                    }
                });
                mToolbar.inflateMenu(R.menu.menu_recipe_detail_online_recipe);
                mToolbar.setOnMenuItemClickListener(new ToolbarMenuItemClickListener());
        }

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        mToolbar.setNavigationOnClickListener(new NavigationBackListener());
        mToolbarBottom.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        mToolbarBottom.setNavigationOnClickListener(new NavigationBackListener());


        //Ingredients Recyclerview
        mIngredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIngredientsAdapter = new RecipeIngredientAdapter(this);

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);


        //Image Recyclerview
        mImagesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mImageAdapter = new ImageAdapter(this, recipeSource);
        mImageAdapter.setImageClickListener(new ImageAdapter.ImageClickListener() {
            @Override
            public void onImageClicked(int position) {
                Intent openImageGalleryIntent = new Intent(RecipeDetailActivity.this, ImageGalleryActivity.class);
                openImageGalleryIntent.putExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE, recipeSource);
                openImageGalleryIntent.putExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_RECIPE, mPresenter.getLoadedRecipe());
                openImageGalleryIntent.putExtra(Constants.IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_IMAGE_POSITION, position);
                startActivity(openImageGalleryIntent);
            }
        });
        mImageAdapter.setImageDownloadedListener(new ImageAdapter.ImageDownloadListener() {
            @Override
            public void onImageDownloaded(Bitmap bitmap) {
                mPresenter.cacheBitmap(bitmap);
            }
        });

        mImagesRecyclerView.setLayoutManager(mImagesLayoutManager);
        mImagesRecyclerView.setAdapter(mImageAdapter);
        mImagesRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void showRecipe(Recipe recipe, RecipeSource recipeSource) {
        mToolbarTitle.setText(recipe.getName());
        //mToolbarLogo.setImageBitmap(recipe.getFeaturedImage());

        switch (recipeSource) {
            case DATABASE:
                Picasso.with(this)
                        .load(new File(FileUtil.getImageFilesDir(), recipe.getFeaturedImage()))
                        .error(R.drawable.default_recipe_image)
                        .into(mTitleImage);
                Picasso.with(this)
                        .load(new File(FileUtil.getImageFilesDir(), recipe.getFeaturedImage()))
                        .error(R.drawable.default_recipe_image)
                        .into(mImage);
                break;
            case ONLINE:
                Picasso.with(this)
                        .load(recipe.getFeaturedImage())
                        .error(R.drawable.default_recipe_image)
                        .into(mTitleImage);
                Picasso.with(this)
                        .load(recipe.getFeaturedImage())
                        .error(R.drawable.default_recipe_image)
                        .into(mImage);
                break;
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
    public void recipeDeletedSoFinish() {
        BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                onBackPressed();
            }
        };
        SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.SUCCESS, R.string.activity_recipe_detail_success_deleting_recipe, SnackbarUtil.SnackbarDuration.SHORT, callback);
    }

    @Override
    public void recipeDownloadedSoFinish() {
        BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                onBackPressed();
            }
        };
        SnackbarUtil.showSnackbar(mCoordinatorLayout, SnackbarUtil.SnackbarType.SUCCESS, R.string.activity_recipe_detail_success_downloading_recipe, SnackbarUtil.SnackbarDuration.SHORT, callback);
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
                    editRecipeIntent.putExtra(Constants.ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID, mPresenter.getLoadedRecipe().getId());
                    startActivity(editRecipeIntent);
                    break;

                case R.id.menu_recipe_detail_delete:
                    AlertDialog dialog = new AlertDialog.Builder(RecipeDetailActivity.this)
                            .setTitle(getResources().getString(R.string.dialog_recipe_detail_delete_title))
                            .setMessage(String.format(Locale.getDefault(),
                                    getResources().getString(R.string.dialog_recipe_detail_delete_message),
                                    mPresenter.getLoadedRecipe().getName()))
                            .setPositiveButton(getResources().getString(R.string.dialog_delete),  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mPresenter.deleteRecipe();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                    break;

                case R.id.menu_recipe_detail_share:
                    handleRecipeShare();
                    break;

                case R.id.menu_recipe_detail_download:
                    mPresenter.downloadRecipe();
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