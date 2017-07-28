package ve.com.abicelis.chefbuddy.ui.home_spinWheel;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.CheckedRecipe;

/**
 * Created by abicelis on 15/7/2017.
 */

public class EditSpinWheelRecipeListAdapter extends RecyclerView.Adapter<EditSpinWheelRecipeListViewHolder> {

    //DATA
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<CheckedRecipe> mCheckedRecipes = new ArrayList<>();


    public EditSpinWheelRecipeListAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public EditSpinWheelRecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditSpinWheelRecipeListViewHolder(mInflater.inflate(R.layout.list_item_spin_wheel_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(EditSpinWheelRecipeListViewHolder holder, int position) {
        holder.setData(this, mActivity, mCheckedRecipes.get(position), position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return mCheckedRecipes.size();
    }



    public List<CheckedRecipe> getItems() {
        return mCheckedRecipes;
    }


}


