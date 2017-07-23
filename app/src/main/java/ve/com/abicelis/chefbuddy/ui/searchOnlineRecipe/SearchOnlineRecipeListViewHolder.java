package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeDetailActivity;

/**
 * Created by abicelis on 9/7/2017.
 */

public class SearchOnlineRecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private SearchOnlineRecipeListAdapter mAdapter;
    private Activity mActivity;
    private Recipe mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_recipe_container)
    LinearLayout mContainer;
    @BindView(R.id.list_item_recipe_image)
    ImageView mImage;
    @BindView(R.id.list_item_recipe_name)
    TextView mName;
    @BindView(R.id.list_item_recipe_ingredients)
    TextView mIngredients;


    public SearchOnlineRecipeListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(SearchOnlineRecipeListAdapter adapter, Activity activity, Recipe current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        Picasso.with(activity)
                .load(mCurrent.getFeaturedImage())
                .error(R.drawable.default_recipe_image)
                .into(mImage);

        mName.setText(mCurrent.getName());
        mIngredients.setText(mCurrent.getSimpleIngredientsString());
    }

    public void setListeners() {
        mContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_recipe_container:
                mAdapter.forwardRecyclerViewClick();    //Forward click to adapter
                Intent viewOnlineRecipeDetailIntent = new Intent(mActivity, RecipeDetailActivity.class);
                viewOnlineRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE, mCurrent);
                viewOnlineRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE, RecipeSource.ONLINE);
                mActivity.startActivity(viewOnlineRecipeDetailIntent);
                break;
        }
    }
}
