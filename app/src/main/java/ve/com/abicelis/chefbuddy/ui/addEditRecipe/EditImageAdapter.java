package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.Image;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.itemTouchHelper.ItemTouchHelperAdapter;

/**
 * Created by abicelis on 15/7/2017.
 */

public class EditImageAdapter extends RecyclerView.Adapter<EditImageViewHolder> implements ItemTouchHelperAdapter {

    //DATA
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<Image> mImages = new ArrayList<>();
    private OnDragStartListener mDragStartListener;


    public EditImageAdapter(Activity activity, OnDragStartListener dragStartListener) {
        mActivity = activity;
        mDragStartListener = dragStartListener;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public EditImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditImageViewHolder(mInflater.inflate(R.layout.list_item_edit_image, parent, false));
    }

    @Override
    public void onBindViewHolder(EditImageViewHolder holder, int position) {
        holder.setData(this, mActivity, mImages.get(position), position);
        holder.setListeners(mDragStartListener);
    }

    public List<Image> getItems() {
        return mImages;
    }

    public void removeItem(int position) {
        mImages.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }



    /* ItemTouchHelperAdapter interface implementation */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Image prev = mImages.remove(fromPosition);
        mImages.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
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


