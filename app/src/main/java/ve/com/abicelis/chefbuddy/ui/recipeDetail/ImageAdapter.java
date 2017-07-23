package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.RecipeSource;

/**
 * Created by abicelis on 15/7/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    //DATA
    private Activity mActivity;
    private RecipeSource mRecipeSource;
    private LayoutInflater mInflater;
    private ImageClickListener mListener;
    private ImageDownloadListener mDownloadListener;
    private List<String> mImages = new ArrayList<>();

    public ImageAdapter(Activity activity, @NonNull RecipeSource recipeSource) {
        mActivity = activity;
        mRecipeSource = recipeSource;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mInflater.inflate(R.layout.list_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setData(this, mActivity, mRecipeSource, mImages.get(position), position);
        holder.setListeners();
    }

    public List<String> getItems() {
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


    /* Interface for notifying activity Picasso has downloaded a bitmap */
    public void notifyImageDownloaded(Bitmap bitmap) {
        mDownloadListener.onImageDownloaded(bitmap);
    }

    public void setImageDownloadedListener(ImageDownloadListener listener) {
        mDownloadListener = listener;
    }

    public interface ImageDownloadListener {
        void onImageDownloaded(Bitmap bitmap);
    }
}
