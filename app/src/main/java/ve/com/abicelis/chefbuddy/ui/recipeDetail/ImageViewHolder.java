package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.util.FileUtil;

/**
 * Created by abicelis on 15/7/2017.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private ImageAdapter mAdapter;
    private Activity mActivity;
    private String mCurrent;
    private int mPosition;

    //UI
    //@BindView(R.id.list_item_image_image)
    ImageView mImage;


    public ImageViewHolder(View itemView) {
        super(itemView);
        //ButterKnife.bind(this, mActivity);

        mImage = (ImageView) itemView;
    }

    public void setData(ImageAdapter adapter, Activity activity, String current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        Picasso.with(activity)
                .load(new File(FileUtil.getImageFilesDir(), mCurrent))
                .error(R.drawable.default_recipe_image)
                .into(mImage);
    }

    public void setListeners() {
        mImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_image_image:
                mAdapter.notifyImageClick(mPosition);
                break;
        }
    }
}
