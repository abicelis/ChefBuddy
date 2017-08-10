package ve.com.abicelis.chefbuddy.ui.intro;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.BackupInfo;

/**
 * Created by abicelis on 9/7/2017.
 */

public class BackupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private BackupAdapter mAdapter;
    private Activity mActivity;
    private BackupInfo mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_backup_container)
    RelativeLayout mContainer;
    @BindView(R.id.list_item_backup_radio)
    RadioButton mRadio;
    @BindView(R.id.list_item_backup_1)
    TextView m1;
    @BindView(R.id.list_item_backup_2)
    TextView m2;
    @BindView(R.id.list_item_backup_3)
    TextView m3;


    public BackupViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(BackupAdapter adapter, Activity activity, BackupInfo current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        m1.setText(mCurrent.getBackupDetailStr());
        m2.setText(mCurrent.getBackupDetailStr2());
        m3.setText(mCurrent.getBackupDetailStr3());
        //mRadio.setChecked(mCurrent.isSelected());
//        mRadio.post(new Runnable(){
//            @Override
//            public void run(){
//                mRadio.setChecked(mCurrent.isSelected());
//            }
//        });

        //Fix for animating checkbox checked or unchecked
        Handler mainHandler = new Handler(activity.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mRadio.setChecked(mCurrent.isSelected());
            }
        });
    }

    public void setListeners() {
        mContainer.setOnClickListener(this);
        mRadio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!mCurrent.isSelected()) {
            mAdapter.backupSelected(mPosition);
        }
    }
}
