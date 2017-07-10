package ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 9/7/2017.
 */

public class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private RecipeListAdapter mAdapter;
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


    public RecipeListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setData(RecipeListAdapter adapter, Activity activity, Recipe current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        //mImage.setImageBitmap(mCurrent.getFeaturedImage());
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
                Toast.makeText(v.getContext(), "Container clicked pos=" + mPosition, Toast.LENGTH_SHORT).show();
        }
    }
}
