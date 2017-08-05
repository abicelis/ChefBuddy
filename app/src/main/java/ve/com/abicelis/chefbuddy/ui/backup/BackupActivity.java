package ve.com.abicelis.chefbuddy.ui.backup;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupConnectionType;
import ve.com.abicelis.chefbuddy.model.BackupFrequencyType;
import ve.com.abicelis.chefbuddy.model.BackupInfo;
import ve.com.abicelis.chefbuddy.service.BackupService;
import ve.com.abicelis.chefbuddy.ui.backup.presenter.BackupPresenter;
import ve.com.abicelis.chefbuddy.ui.backup.view.BackupView;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 2/8/2017.
 */

public class BackupActivity  extends AppCompatActivity implements BackupView {

    @Inject
    BackupPresenter mPresenter;

    @BindView(R.id.activity_backup_container)
    LinearLayout mContainer;

    @BindView(R.id.activity_backup_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_backup_last_backup)
    LinearLayout mLastBackupContainer;
    @BindView(R.id.activity_backup_last_backup_1)
    TextView mLastBackupL1;
    @BindView(R.id.activity_backup_last_backup_2)
    TextView mLastBackupL2;

    @BindView(R.id.activity_backup_no_backups)
    TextView mNoBackup;

    @BindView(R.id.activity_backup_backing_up_container)
    LinearLayout mBackingUpContainer;
    @BindView(R.id.activity_backup_backing_up)
    TextView mBackingUp;


    @BindView(R.id.activity_backup_backup_frequency_container)
    LinearLayout mBackupFrequencyContainer;
    @BindView(R.id.activity_backup_backup_frequency_value)
    TextView mBackupFrequencyValue;


    @BindView(R.id.activity_backup_google_drive_switch)
    SwitchCompat mGoogleDriveSwitch;


    @BindView(R.id.activity_backup_account_container)
    LinearLayout mAccountContainer;
    @BindView(R.id.activity_backup_account_value)
    TextView mAccountValue;

    @BindView(R.id.activity_backup_backup_connection_type_container)
    LinearLayout mBackupConnectionTypeContainer;
    @BindView(R.id.activity_backup_backup_connection_type_value)
    TextView mBackupConnectionTypeValue;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        ButterKnife.bind(this);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.activity_backup_toolbar_title);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Set listeners
        mBackupFrequencyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BackupActivity.this, "TODO mBackupFrequencyContainer", Toast.LENGTH_SHORT).show();
            }
        });
        mAccountContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BackupActivity.this, "TODO mAccountContainer", Toast.LENGTH_SHORT).show();
            }
        });
        mBackupConnectionTypeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BackupActivity.this, "TODO mBackupConnectionTypeContainer", Toast.LENGTH_SHORT).show();
            }
        });
        mGoogleDriveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(BackupActivity.this, "TODO mGoogleDriveSwitch. Checked:" + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        //Register receiver for service
        BroadcastReceiver mBackupCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean result = intent.getBooleanExtra(Constants.BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_RESULT, false);
                Message message = (Message)intent.getSerializableExtra(Constants.BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_MESSAGE);
                mPresenter.manualBackupComplete(result, message);
            }
        };
        LocalBroadcastManager.getInstance(BackupActivity.this).registerReceiver(mBackupCompleteReceiver, new IntentFilter(Constants.BACKUP_SERVICE_BROADCAST_BACKUP_DONE));


        //Check if WRITE_EXTERNAL_STORAGE permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mPresenter.start();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(BackupActivity.this)
                    .setCancelable(false)
                    .setTitle(getResources().getString(R.string.dialog_grant_storage_permissions_title))
                    .setMessage(getResources().getString(R.string.dialog_grant_storage_permissions_message))
                    .setPositiveButton(getResources().getString(R.string.dialog_agree),  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BackupActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.BACKUP_ACTIVITY_PERMISSIONS);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();
            dialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_backup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_backup_backup_now:
                handleManualBackup();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Constants.BACKUP_ACTIVITY_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mPresenter.start();
            else {
                BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        finish();
                    }
                };
                SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, Message.PERMISSIONS_WRITE_STORAGE_NOT_GRANTED.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, callback);
            }
        }

        if(requestCode == Constants.BACKUP_ACTIVITY_HANDLE_BACKUP_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                runManualBackup();
            else {
                BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        finish();
                    }
                };
                SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, Message.PERMISSIONS_WRITE_STORAGE_NOT_GRANTED.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, callback);
            }
        }
    }
    private void handleManualBackup() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            runManualBackup();
        else
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.BACKUP_ACTIVITY_HANDLE_BACKUP_PERMISSIONS);
    }


    public void runManualBackup() {

        //Update UI
        TransitionManager.beginDelayedTransition(mContainer, new Fade());
        mLastBackupContainer.setVisibility(View.GONE);
        mNoBackup.setVisibility(View.GONE);
        mBackingUpContainer.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Start the service
                Intent startBackupService = new Intent(BackupActivity.this, BackupService.class);
                startService(startBackupService);
            }
        }, 1000);
    }







    /* BackupView interface implementation */

    @Override
    public void updateLastBackupInfo(BackupInfo backupInfo) {
        TransitionManager.beginDelayedTransition(mContainer, new Fade());
        mBackingUpContainer.setVisibility(View.GONE);

        if(backupInfo == null) { //No backups found
            mLastBackupContainer.setVisibility(View.GONE);
            mNoBackup.setVisibility(View.VISIBLE);
        } else {
            mLastBackupL1.setText(backupInfo.getBackupDetailStr());
            mLastBackupL2.setText(backupInfo.getBackupDetailStr2());

            mNoBackup.setVisibility(View.GONE);
            mLastBackupContainer.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void updateBackupFrequencyType(BackupFrequencyType backupFrequencyType) {
        mBackupFrequencyValue.setText(backupFrequencyType.getFriendlyName());
    }

    @Override
    public void updateBackupConnectionType(BackupConnectionType backupConnectionType) {
        mBackupConnectionTypeValue.setText(backupConnectionType.getFriendlyName());
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }
}
