package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.itemTouchHelper.SimpleItemTouchHelperCallback;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddEditRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddEditRecipeView;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 16/7/2017.
 */

public class AddEditRecipeActivity extends AppCompatActivity implements AddEditRecipeView, EditRecipeIngredientAdapter.OnDragStartListener {


    @Inject
    AddEditRecipePresenter presenter;

    @BindView(R.id.activity_add_edit_recipe_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_add_edit_recipe_container)
    LinearLayout mContainer;

    @BindView(R.id.activity_add_edit_recipe_ingredients_add)
    Button mAddIngredient;

    /* Ingredients recycler */
    @BindView(R.id.activity_add_edit_recipe_ingredients_recycler)
    RecyclerView mIngredientsRecyclerView;
    private LinearLayoutManager mIngredientsLayoutManager;
    private EditRecipeIngredientAdapter mEditIngredientsAdapter;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);
        ButterKnife.bind(this);

        //TODO true?
        initViews(true);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        presenter.attachView(this);
        presenter.getRecipe(8);    //TODO check if editing or not


    }

    private void initViews(boolean addingRecipe) {

        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle((addingRecipe ? R.string.activity_add_edit_recipe_title_add : R.string.activity_add_edit_recipe_title_edit));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.icon_back_material));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEditRecipeActivity.this, "Back pressed, must check here if user wants to discard changes", Toast.LENGTH_SHORT).show();
                //onBackPressed();
            }
        });

        //Ingredients RecyclerView
        mIngredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mEditIngredientsAdapter = new EditRecipeIngredientAdapter(this, this);

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mIngredientsRecyclerView.setAdapter(mEditIngredientsAdapter);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mEditIngredientsAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mIngredientsRecyclerView);

        mAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditIngredientsAdapter.getItems().add(new RecipeIngredient("1/2", Measurement.CUP, new Ingredient("Poop")));
                mEditIngredientsAdapter.notifyItemInserted(mEditIngredientsAdapter.getItemCount());
            }
        });

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
                Toast.makeText(this, "Save todo", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void showRecipe(Recipe recipe) {
        //Ingredients recyclerView
        mEditIngredientsAdapter.getItems().addAll(recipe.getRecipeIngredients());
        mEditIngredientsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);

    }


    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
