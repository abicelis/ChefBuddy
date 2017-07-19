package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.itemTouchHelper.ItemTouchHelperAdapter;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeIngredientViewHolder;

/**
 * Created by abicelis on 15/7/2017.
 */

public class EditRecipeIngredientAdapter extends RecyclerView.Adapter<EditRecipeIngredientViewHolder> implements ItemTouchHelperAdapter {

    //DATA
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<RecipeIngredient> mRecipeIngredients;
    private OnDragStartListener mDragStartListener;


    public EditRecipeIngredientAdapter(Activity activity, OnDragStartListener dragStartListener) {
        mActivity = activity;
        mDragStartListener = dragStartListener;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public EditRecipeIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditRecipeIngredientViewHolder(mInflater.inflate(R.layout.list_item_edit_recipe_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(EditRecipeIngredientViewHolder holder, int position) {
        holder.setData(this, mActivity, mRecipeIngredients.get(position), position);
        holder.setListeners(mDragStartListener);
    }

    @Override
    public int getItemCount() {
        return mRecipeIngredients.size();
    }



    public void setItems(List<RecipeIngredient> recipeIngredients) {
        mRecipeIngredients = recipeIngredients;
        notifyDataSetChanged();

    }

    public void addItem(RecipeIngredient recipeIngredient) {
        mRecipeIngredients.add(recipeIngredient);
        notifyItemInserted(getItemCount());
    }

    public void removeItem(int position) {
        mRecipeIngredients.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }



    /* ItemTouchHelperAdapter interface implementation */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        RecipeIngredient prev = mRecipeIngredients.remove(fromPosition);
        mRecipeIngredients.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //Unused
    }



    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }
}


