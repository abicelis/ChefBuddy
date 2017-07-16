package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.Image;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;

/**
 * Created by abicelis on 15/7/2017.
 */

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientViewHolder> {

    //DATA
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<RecipeIngredient> mRecipeIngredients = new ArrayList<>();

    public RecipeIngredientAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeIngredientViewHolder(mInflater.inflate(R.layout.list_item_recipe_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeIngredientViewHolder holder, int position) {
        holder.setData(this, mActivity, mRecipeIngredients.get(position), position);
        //holder.setListeners();
    }

    public List<RecipeIngredient> getItems() {
        return mRecipeIngredients;
    }

    @Override
    public int getItemCount() {
        return mRecipeIngredients.size();
    }
}
