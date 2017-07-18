package ve.com.abicelis.chefbuddy.ui.recipeDetail;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.model.Image;

/**
 * Created by abicelis on 15/7/2017.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private ImageAdapter mAdapter;
    private Activity mActivity;
    private Image mCurrent;
    private int mPosition;

    //UI
    //@BindView(R.id.list_item_image_image)
    ImageView mImage;


    public ImageViewHolder(View itemView) {
        super(itemView);
        //ButterKnife.bind(this, mActivity);

        mImage = (ImageView) itemView;
    }

    public void setData(ImageAdapter adapter, Activity activity, Image current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        mImage.setImageBitmap(mCurrent.getImage());

    }

    public void setListeners() {
        mImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_image_image:
                Toast.makeText(v.getContext(), "Image clicked pos=" + mPosition, Toast.LENGTH_SHORT).show();

                //Intent viewImageDetail = new Intent(mActivity, Blah.class);
                //viewImageDetail.putExtra(Constants.EXTRA, mCurrent);
                //mActivity.startActivity(viewImageDetail);
                break;
        }
    }
}
