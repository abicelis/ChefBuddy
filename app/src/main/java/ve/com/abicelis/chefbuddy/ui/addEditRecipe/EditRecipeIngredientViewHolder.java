package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        mIconImage.setImageResource(mCurrent.getMeasurement().getIconRes());

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
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_edit_recipe_ingredient_delete_container:
                AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setTitle(mActivity.getString(R.string.dialog_add_edit_recipe_delete_ingredient_title))
                        .setMessage(mActivity.getString(R.string.dialog_add_edit_recipe_delete_ingredient_message))
                        .setPositiveButton(mActivity.getString(R.string.dialog_delete),  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.removeItem(mPosition);
                            }
                        })
                        .setNegativeButton(mActivity.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
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
