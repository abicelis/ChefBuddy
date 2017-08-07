package ve.com.abicelis.chefbuddy.ui.backup;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;

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
import ve.com.abicelis.chefbuddy.service.BackupServiceV2;
import ve.com.abicelis.chefbuddy.ui.backup.presenter.BackupPresenter;
import ve.com.abicelis.chefbuddy.ui.backup.view.BackupView;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 2/8/2017.
 */

public class BackupActivity  extends AppCompatActivity implements BackupView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;

    @Inject
    BackupPresenter mPresenter;

    //VIEWS
    @BindView(R.id.activity_backup_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_backup_container)
    LinearLayout mContainer;



    // LAST BACKUP
    @BindView(R.id.activity_backup_last_backup_loading)
    ProgressBar mLastBackupLoading;

    @BindView(R.id.activity_backup_last_backup_info_container)
    LinearLayout mLastBackupInfoContainer;
    @BindView(R.id.activity_backup_last_backup_info_1)
    TextView mLastBackupInfoL1;
    @BindView(R.id.activity_backup_last_backup_info_2)
    TextView mLastBackupInfoL2;

    @BindView(R.id.activity_backup_last_backup_no_backups)
    TextView mLastBackupNoBackups;

    @BindView(R.id.activity_backup_last_backup_backing_up_container)
    LinearLayout mLastBackupBackingUpContainer;
    @BindView(R.id.activity_backup_last_backup_backing_up)
    TextView mLastBackupBackingUp;



    // GENERAL
    @BindView(R.id.activity_backup_backup_frequency_container)
    LinearLayout mBackupFrequencyContainer;
    @BindView(R.id.activity_backup_backup_frequency_value)
    TextView mBackupFrequencyValue;



    // GOOGLE DRIVE
    @BindView(R.id.activity_backup_google_drive_switch)
    SwitchCompat mGoogleDriveSwitch;

    @BindView(R.id.activity_backup_google_drive_loading)
    ProgressBar mGDLoading;

    @BindView(R.id.activity_backup_google_drive_disabled)
    TextView mGDDisabled;

    @BindView(R.id.activity_backup_google_drive_enabled_container)
    LinearLayout mGDEnabledContainer;
    @BindView(R.id.activity_backup_google_drive_enabled_account_container)
    LinearLayout mGDEnabledAccountContainer;
    @BindView(R.id.activity_backup_google_drive_enabled_account_value)
    TextView mGDEnabledAccountValue;
    @BindView(R.id.activity_backup_google_drive_enabled_backup_connection_type_container)
    LinearLayout mGDEnabledConnectionTypeContainer;
    @BindView(R.id.activity_backup_google_drive_enabled_backup_connection_type_value)
    TextView mGDEnabledConnectionTypeValue;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        ButterKnife.bind(this);

        initViews();

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);


        //Register receiver for service
        BroadcastReceiver backupServiceProgressReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = (Message)intent.getSerializableExtra(Constants.BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_MESSAGE);
                mPresenter.backupServiceProgressReport(message);
            }
        };
        LocalBroadcastManager.getInstance(BackupActivity.this).registerReceiver(backupServiceProgressReceiver, new IntentFilter(Constants.BACKUP_SERVICE_BROADCAST_BACKUP_PROGRESS));


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
    protected void onStop() {
        super.onStop();
        disconnectGoogleApiClient();
    }

    private void initViews() {

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

        //Set click listeners
        mBackupFrequencyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = BackupFrequencyType.getFriendlyNames().toArray(new CharSequence[BackupFrequencyType.getFriendlyNames().size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
                builder.setTitle(R.string.dialog_set_backup_frequency_title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.backupFrequencyUpdated(BackupFrequencyType.values()[which]);
                    }
                });
                builder.show();
            }
        });
        mGDEnabledAccountContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
            }
        });
        mGDEnabledConnectionTypeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BackupActivity.this, "TODO mGDEnabledConnectionTypeContainer", Toast.LENGTH_SHORT).show();
            }
        });
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
                runManualBackup();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: 6/8/2017 This manual backup must be disabled when loading stuff...
    public void runManualBackup() {

        //Update UI
        TransitionManager.beginDelayedTransition(mContainer, new Fade());
        mLastBackupInfoContainer.setVisibility(View.GONE);
        mLastBackupNoBackups.setVisibility(View.GONE);
        mLastBackupBackingUpContainer.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Start the service
                Intent startBackupService = new Intent(BackupActivity.this, BackupServiceV2.class);
                startService(startBackupService);
            }
        }, 1000);
    }










    /* BackupView interface implementation */

    @Override
    public void startGoogleApiClient() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(ChefBuddyApplication.getContext())
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_APPFOLDER)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        } else {
            mPresenter.googleApiClientConnected();
        }
    }

    @Override
    public void disconnectGoogleApiClient() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public List<String> getGoogleDriveFileList() {
        List<String> files = new ArrayList<>();

        //Order backups by modified date, so oldest first.
        SortOrder sortOrder = new SortOrder.Builder()
                .addSortAscending(SortableField.MODIFIED_DATE).build();

        //Build a query, with no filters and just a sortOrder
        Query query = new Query.Builder().setSortOrder(sortOrder).build();

        DriveApi.MetadataBufferResult result = Drive.DriveApi.getAppFolder(mGoogleApiClient)
        //DriveApi.MetadataBufferResult result = Drive.DriveApi.getRootFolder(mGoogleApiClient)
                .queryChildren(mGoogleApiClient, query).await();

        if(result.getStatus().isSuccess())
            for(Metadata metadata : result.getMetadataBuffer())
                files.add(metadata.getTitle());

        return files;
    }

    @Override
    public void updateBackupFrequencyType(BackupFrequencyType backupFrequencyType) {
        mBackupFrequencyValue.setText(backupFrequencyType.getFriendlyName());
    }

    @Override
    public void updateLastBackupSection(final LastBackupState state, final @Nullable BackupInfo backupInfo) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TransitionManager.beginDelayedTransition(mContainer, new Fade());
                mLastBackupLoading.setVisibility(View.GONE);
                mLastBackupInfoContainer.setVisibility(View.GONE);
                mLastBackupNoBackups.setVisibility(View.GONE);
                mLastBackupBackingUpContainer.setVisibility(View.GONE);

                switch (state) {
                    case LOADING:
                        mLastBackupLoading.setVisibility(View.VISIBLE);
                        break;

                    case SHOW_BACKUP_INFO:
                        mLastBackupInfoL1.setText(backupInfo.getBackupDetailStr());
                        mLastBackupInfoL2.setText(backupInfo.getBackupDetailStr2());
                        mLastBackupInfoContainer.setVisibility(View.VISIBLE);
                        break;

                    case NO_BACKUPS:
                        mLastBackupNoBackups.setVisibility(View.VISIBLE);
                        break;
                    case BACKING_UP:
                        mLastBackupBackingUpContainer.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    @Override
    public void updateGoogleDriveSection(GoogleDriveBackupState state, @Nullable String account, @Nullable BackupConnectionType backupConnectionType) {
        TransitionManager.beginDelayedTransition(mContainer, new Fade());
        mGDDisabled.setVisibility(View.GONE);
        mGDEnabledContainer.setVisibility(View.GONE);
        mGDLoading.setVisibility(View.GONE);


        switch (state) {
            case LOADING:
                mGoogleDriveSwitch.setEnabled(false);
                mGoogleDriveSwitch.setChecked(false);
                mGoogleDriveSwitch.setOnCheckedChangeListener(null);

                mGDLoading.setVisibility(View.VISIBLE);
                break;

            case ENABLED:
                mGoogleDriveSwitch.setEnabled(true);
                mGoogleDriveSwitch.setChecked(true);
                mGoogleDriveSwitch.setOnCheckedChangeListener(switchListener);

                mGDEnabledAccountValue.setText(account);
                mGDEnabledConnectionTypeValue.setText(backupConnectionType.getFriendlyName());
                mGDEnabledContainer.setVisibility(View.VISIBLE);
                break;

            case DISABLED:
                mGoogleDriveSwitch.setEnabled(true);
                mGoogleDriveSwitch.setChecked(false);
                mGoogleDriveSwitch.setOnCheckedChangeListener(switchListener);

                mGDDisabled.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void triggerBackupServiceStarter() {
        Intent triggerBackupServiceStarter = new Intent(Constants.BACKUP_FREQUENCY_CHANGED_ACTION);
        sendBroadcast(triggerBackupServiceStarter);
    }

    @Override
    public void showInfo(String info) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.SUCCESS, info, SnackbarUtil.SnackbarDuration.SHORT, null);
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }






    /* Google API Client Callbacks */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPresenter.googleApiClientConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Called when for instance Google Play services is Force Stopped, in which case reconnection is automatic
        //Or if user uninstalls Google Play Services (WHY U DO DIS?), in which case onConnectionFailed() gets called seconds after
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, Constants.REQUEST_RESOLVE_GOOGLE_API_CONNECTION);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_RESOLVE_GOOGLE_API_CONNECTION:
                if (resultCode == RESULT_OK ) {
                    mGoogleApiClient.connect();
                } else {
                    mPresenter.googleApiClientNotConnected();
                }
                break;
        }
    }








    CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mPresenter.googleDriveSwitchToggled(isChecked);
        }
    };
}
