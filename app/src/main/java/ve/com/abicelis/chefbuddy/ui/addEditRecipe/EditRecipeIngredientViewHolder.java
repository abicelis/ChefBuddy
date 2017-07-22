package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.app.Activity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.itemTouchHelper.ItemTouchHelperViewHolder;

/**
 * Created by abicelis on 15/7/2017.
 */

public class EditRecipeIngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {

    //DATA
    private EditRecipeIngredientAdapter mAdapter;
    private EditRecipeIngredientAdapter.OnDragStartListener mDragStartListener;
    private Activity mActivity;
    private RecipeIngredient mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_edit_recipe_ingredient_icon_image)
    ImageView mIconImage;

    @BindView(R.id.list_item_edit_recipe_ingredient_amount_measurement)
    TextView mAmountMeasurement;

    @BindView(R.id.list_item_edit_recipe_ingredient_name)
    TextView mName;

    @BindView(R.id.list_item_edit_recipe_ingredient_delete_container)
    RelativeLayout mDelete;

    @BindView(R.id.list_item_edit_recipe_ingredient_drag_handle)
    ImageView mDragHandle;


    public EditRecipeIngredientViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(EditRecipeIngredientAdapter adapter, Activity activity, RecipeIngredient current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        mIconImage.setImageDrawable(mCurrent.getMeasurement().getIcon());

        mAmountMeasurement.setVisibility(View.VISIBLE);
        if (Measurement.NONE.equals(mCurrent.getMeasurement())) {
            if (mCurrent.getAmount().isEmpty())
                mAmountMeasurement.setVisibility(View.GONE);
            else
                mAmountMeasurement.setText(mCurrent.getAmount());
        } else {
            mAmountMeasurement.setText(mCurrent.getAmount() + " " + mCurrent.getMeasurement().getAbbreviation());
        }

        mName.setText(mCurrent.getIngredient().getName());

    }

    public void setListeners(final EditRecipeIngredientAdapter.OnDragStartListener dragStartListener) {
        mDelete.setOnClickListener(this);
        mDragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragStartListener.onDragStarted(EditRecipeIngredientViewHolder.this);
                }
                return false;            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_edit_recipe_ingredient_delete_container:

                Toast.makeText(mActivity, "Are you sure...?" + mPosition, Toast.LENGTH_SHORT).show();
                mAdapter.removeItem(mPosition);
                break;
        }
    }

    /* ItemTouchHelperViewHolder interface implementation */
    @Override
    public void onItemSelected() {
        //Unused
    }

    @Override
    public void onItemClear() {
        //Unused
    }

    @Override
    public void onItemMoved(int newPosition) {
        mPosition = newPosition;
    }
}
