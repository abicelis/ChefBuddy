package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.Image;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 15/7/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    //DATA
    private Activity mActivity;
    private LayoutInflater mInflater;
    private ImageClickListener mListener;
    private List<Image> mImages = new ArrayList<>();

    public ImageAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mInflater.inflate(R.layout.list_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setData(this, mActivity, mImages.get(position), position);
        holder.setListeners();
    }

    public List<Image> getItems() {
        return mImages;
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }



    /* Interface for notifying activity when user clicked on an image */
    public void notifyImageClick(int position) {
        mListener.onImageClicked(position);
    }

    public void setImageClickListener(ImageClickListener listener) {
        mListener = listener;
    }

    public interface ImageClickListener {
        void onImageClicked(int position);
    }
}
