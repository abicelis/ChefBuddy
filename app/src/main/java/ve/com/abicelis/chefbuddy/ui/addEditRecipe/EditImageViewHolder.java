package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.app.Activity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.itemTouchHelper.ItemTouchHelperViewHolder;
import ve.com.abicelis.chefbuddy.util.FileUtil;

/**
 * Created by abicelis on 15/7/2017.
 */

public class EditImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {

    //DATA
    private EditImageAdapter mAdapter;
    private EditImageAdapter.OnDragStartListener mDragStartListener;
    private Activity mActivity;
    private String mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_edit_image_image)
    ImageView mImage;

    @BindView(R.id.list_item_edit_image_delete)
    //FloatingActionButton mDelete;
            RelativeLayout mDelete;

    @BindView(R.id.list_item_edit_image_drag_handle)
    //FloatingActionButton mDragHandle;
            ImageView mDragHandle;


    public EditImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(EditImageAdapter adapter, Activity activity, String current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        Picasso.with(activity)
                .load(new File(FileUtil.getImageFilesDir(), mCurrent))
                .fit().centerCrop()
                .error(R.drawable.default_recipe_image)
                .into(mImage);
    }

    public void setListeners(final EditImageAdapter.OnDragStartListener dragStartListener) {
        mDelete.setOnClickListener(this);
        mDragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragStartListener.onDragStarted(EditImageViewHolder.this);
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
