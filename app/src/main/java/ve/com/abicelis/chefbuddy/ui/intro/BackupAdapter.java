package ve.com.abicelis.chefbuddy.ui.intro;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.BackupInfo;

/**
 * Created by abicelis on 9/7/2017.
 */

public class BackupAdapter extends RecyclerView.Adapter<BackupViewHolder> {

    //DATA
    private List<BackupInfo> mBackupInfos = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;
    private int selectedBackup = -1;


    public BackupAdapter(Activity activity, List<BackupInfo> backupInfos) {
        mBackupInfos = backupInfos;
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public BackupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BackupViewHolder(mInflater.inflate(R.layout.list_item_backup, parent, false));
    }

    @Override
    public void onBindViewHolder(BackupViewHolder holder, int position) {
        holder.setData(this, mActivity, mBackupInfos.get(position), position);
        holder.setListeners();
    }


    public List<BackupInfo> getItems() {
        return mBackupInfos;
    }

    @Override
    public int getItemCount() {
        return mBackupInfos.size();
    }

    public void backupSelected(int position) {
        selectedBackup = position;

        for (BackupInfo b : mBackupInfos)
            b.setSelected(false);
        mBackupInfos.get(selectedBackup).setSelected(true);

        notifyDataSetChanged();
    }

    public @Nullable BackupInfo getSelectedBackup() {
        if(selectedBackup != -1)
            return mBackupInfos.get(selectedBackup);
        else return null;
    }


}
