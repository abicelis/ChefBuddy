package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.itemTouchHelper.ItemTouchHelperAdapter;

/**
 * Created by abicelis on 15/7/2017.
 */

public class EditImageAdapter extends RecyclerView.Adapter<EditImageViewHolder> implements ItemTouchHelperAdapter {

    //DATA
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<String> mImageFilenames;
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
        holder.setData(this, mActivity, mImageFilenames.get(position), position);
        holder.setListeners(mDragStartListener);
    }

    @Override
    public int getItemCount() {
        return mImageFilenames.size();
    }



    public void setItems(List<String> imageFilenames) {
        mImageFilenames = imageFilenames;
        notifyDataSetChanged();
    }

    public void addItem(String imageFilename) {
        mImageFilenames.add(imageFilename);
        notifyItemInserted(getItemCount());
    }

    public void removeItem(int position) {
        mImageFilenames.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }



    /* ItemTouchHelperAdapter interface implementation */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String prev = mImageFilenames.remove(fromPosition);
        mImageFilenames.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
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


