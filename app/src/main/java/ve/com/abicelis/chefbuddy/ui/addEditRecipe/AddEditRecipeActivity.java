package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.PreparationTime;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.model.Servings;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.itemTouchHelper.SimpleItemTouchHelperCallback;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddEditRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;
import ve.com.abicelis.chefbuddy.views.FancyEditText;
import ve.com.abicelis.chefbuddy.views.FancySpinner;

/**
 * Created by abicelis on 16/7/2017.
 */

public class AddEditRecipeActivity extends AppCompatActivity implements AddEditRecipeView, EditRecipeIngredientAdapter.OnDragStartListener, EditImageAdapter.OnDragStartListener {

    //DATA
    private List<String> mServingsList = new ArrayList<>();
    private List<String> mPreparationTimesList = new ArrayList<>();


    @Inject
    AddEditRecipePresenter mPresenter;

    @BindView(R.id.activity_add_edit_recipe_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_add_edit_recipe_container)
    LinearLayout mContainer;



    /* BASIC */
    @BindView(R.id.activity_add_edit_recipe_name)
    FancyEditText mName;

    @BindView(R.id.activity_add_edit_recipe_servings)
    FancySpinner mServings;

    @BindView(R.id.activity_add_edit_recipe_preparation_time)
    FancySpinner mPreparationTime;



    /* INGREDIENTS */
    @BindView(R.id.activity_add_edit_recipe_ingredients_add)
    Button mAddIngredient;

    /* Ingredients recycler */
    @BindView(R.id.activity_add_edit_recipe_ingredients_recycler)
    RecyclerView mIngredientsRecyclerView;
    private LinearLayoutManager mIngredientsLayoutManager;
    private EditRecipeIngredientAdapter mEditIngredientsAdapter;
    private ItemTouchHelper mIngredientItemTouchHelper;



    /* PREPARATION */
    @BindView(R.id.activity_add_edit_recipe_preparation)
    FancyEditText mPreparation;


    /* IMAGES */
    @BindView(R.id.activity_add_edit_recipe_images_add)
    Button mAddImage;

    /* Ingredients recycler */
    @BindView(R.id.activity_add_edit_recipe_images_recycler)
    RecyclerView mImagesRecyclerView;
    private LinearLayoutManager mImagesLayoutManager;
    private EditImageAdapter mEditImageAdapter;
    private ItemTouchHelper mImageItemTouchHelper;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);
        ButterKnife.bind(this);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);

        initViews();
        mPresenter.attachViews(this, mEditIngredientsAdapter, mEditImageAdapter);


        //Handle incoming Intent if editing an existing recipe
        if(getIntent().hasExtra(Constants.ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID)) {
            long recipeId = getIntent().getLongExtra(Constants.ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID, -1);
            mPresenter.setExistingRecipe(recipeId);
        } else {
            mPresenter.creatingNewRecipe();
        }

        //If isEditingExistingRecipe, change title to editing
        if(mPresenter.isEditingExistingRecipe())
            getSupportActionBar().setTitle(R.string.activity_add_edit_recipe_title_edit);
    }

    private void initViews() {

        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.activity_add_edit_recipe_title_add);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.icon_back_material));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEditRecipeActivity.this, "Back pressed, must check here if user wants to discard changes", Toast.LENGTH_SHORT).show();
                //onBackPressed();
            }
        });


        //Basic: FancySpinners
        mServingsList.addAll(Servings.getFriendlyNames());
        mServings.setItems(mServingsList);
        mServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setServingsSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        mPreparationTimesList.addAll(PreparationTime.getFriendlyNames());
        mPreparationTime.setItems(mPreparationTimesList);
        mPreparationTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setPreparationTimeSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        //Ingredients RecyclerView
        mIngredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mEditIngredientsAdapter = new EditRecipeIngredientAdapter(this, this);

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mIngredientsRecyclerView.setAdapter(mEditIngredientsAdapter);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mEditIngredientsAdapter);
        mIngredientItemTouchHelper = new ItemTouchHelper(callback);
        mIngredientItemTouchHelper.attachToRecyclerView(mIngredientsRecyclerView);

        mAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = AddEditRecipeActivity.this.getSupportFragmentManager();

                AddRecipeIngredientDialogFragment dialog = AddRecipeIngredientDialogFragment.newInstance();
                dialog.setListener(new AddRecipeIngredientDialogFragment.AddRecipeIngredientListener() {
                    @Override
                    public void onRecipeIngredientAdded(RecipeIngredient recipeIngredient) {
                        mPresenter.addRecipeIngredient(recipeIngredient);
                    }
                });
                dialog.show(fm, "EditLinkAttachmentDialogFragment");
            }
        });


        //Images RecyclerView
        mImagesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mEditImageAdapter = new EditImageAdapter(this, this);

        mImagesRecyclerView.setLayoutManager(mImagesLayoutManager);
        mImagesRecyclerView.setAdapter(mEditImageAdapter);
        mImagesRecyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper.Callback callback2 = new SimpleItemTouchHelperCallback(mEditImageAdapter);
        mImageItemTouchHelper = new ItemTouchHelper(callback2);
        mImageItemTouchHelper.attachToRecyclerView(mImagesRecyclerView);

        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEditRecipeActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                // TODO: 18/7/2017
                //mPresenter.addImage();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_add_edit_recipe_save:
                mPresenter.saveRecipe(mName.getText(), mPreparation.getText());
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    /* AddEditRecipeView interface implementation */
    @Override
    public void showRecipe(Recipe recipe) {
        mName.setText(recipe.getName());
        mServings.setSelection(mPresenter.getServingsSelection());
        mPreparationTime.setSelection(mPresenter.getPreparationTimeSelection());
        mPreparation.setText(recipe.getDirections());
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }

    @Override
    public void recipeSavedSoFinish() {
        BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                finish();
            }
        };
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.SUCCESS, R.string.activity_add_edit_recipe_success_saving_recipe, SnackbarUtil.SnackbarDuration.SHORT, callback);

    }


    /* OnDragStartListener interface implementation */
    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof EditRecipeIngredientViewHolder)
            mIngredientItemTouchHelper.startDrag(viewHolder);

        if(viewHolder instanceof EditImageViewHolder)
            mImageItemTouchHelper.startDrag(viewHolder);
    }
}
