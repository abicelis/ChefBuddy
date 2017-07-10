package ve.com.abicelis.chefbuddy.ui.home.fragment.recipeList;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 9/7/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListViewHolder> {

    //DATA
    private List<Recipe> mRecipes = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;


    public RecipeListAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeListViewHolder(mInflater.inflate(R.layout.list_item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        holder.setData(this, mActivity, mRecipes.get(position), position);
        holder.setListeners();
    }

    public List<Recipe> getItems() {
        return mRecipes;
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}
