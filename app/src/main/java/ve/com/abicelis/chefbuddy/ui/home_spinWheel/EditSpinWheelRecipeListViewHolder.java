package ve.com.abicelis.chefbuddy.ui.home_spinWheel;

import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.CheckedRecipe;
import ve.com.abicelis.chefbuddy.util.FileUtil;

/**
 * Created by abicelis on 15/7/2017.
 */

public class EditSpinWheelRecipeListViewHolder extends RecyclerView.ViewHolder {

    //DATA
    private EditSpinWheelRecipeListAdapter mAdapter;
    private Activity mActivity;
    private CheckedRecipe mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_spin_wheel_recipe_image)
    CircleImageView mImage;

    @BindView(R.id.list_item_spin_wheel_recipe_checkbox)
    AppCompatCheckBox mCheckbox;

    @BindView(R.id.list_item_spin_wheel_recipe_name)
    TextView mName;


    public EditSpinWheelRecipeListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(EditSpinWheelRecipeListAdapter adapter, Activity activity, CheckedRecipe current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        if(current.getRecipe().getImages().size() > 0) {
            Picasso.with(mActivity)
                    .load(new File(FileUtil.getImageFilesDir(), current.getRecipe().getImages().get(0)))
                    .fit().centerCrop()
                    .error(R.drawable.default_recipe_image)
                    .into(mImage);
        } else {
            Picasso.with(mActivity)
                    .load(R.drawable.default_recipe_image)
                    .fit().centerCrop()
                    .into(mImage);
        }

        mName.setText(mCurrent.getRecipe().getName());
        mCheckbox.setChecked(mCurrent.getChecked());
    }

    public void setListeners() {
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCurrent.setChecked(isChecked);
            }
        });
    }

}
