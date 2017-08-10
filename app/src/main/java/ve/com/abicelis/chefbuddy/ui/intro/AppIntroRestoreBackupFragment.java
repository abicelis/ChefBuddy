package ve.com.abicelis.chefbuddy.ui.intro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.github.paolorotolo.appintro.ISlideSelectionListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.BackupInfo;
import ve.com.abicelis.chefbuddy.ui.intro.presenter.AppIntroRestoreBackupPresenter;
import ve.com.abicelis.chefbuddy.ui.intro.view.AppIntroRestoreBackupView;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by abicelis on 7/8/2017.
 */

public class AppIntroRestoreBackupFragment extends Fragment implements AppIntroRestoreBackupView,
        ISlideSelectionListener,
        ISlideBackgroundColorHolder,
        ISlidePolicy,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //DATA
    private int mBgColor;
    private GoogleApiClient mGoogleApiClient;
    private boolean mCanSlide;

    @Inject
    AppIntroRestoreBackupPresenter mPresenter;


    @BindView(R.id.fragment_intro_restore_backup_container)
    LinearLayout mContainer;

    @BindView(R.id.fragment_intro_restore_backup_no_permissions_container)
    LinearLayout mNoPermissionsContainer;

    @BindView(R.id.fragment_intro_restore_backup_choose_restore_source_container)
    LinearLayout mChooseRestoreSourceContainer;
    @BindView(R.id.fragment_intro_restore_backup_google_drive)
    Button mGoogleDrive;
    @BindView(R.id.fragment_intro_restore_backup_local)
    Button mLocal;

    @BindView(R.id.fragment_intro_restore_choose_backup_container)
    LinearLayout mChooseBackupContainer;
    @BindView(R.id.fragment_intro_restore_choose_backup_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_intro_restore_choose_backup_restore)
    Button mRestore;
    private LinearLayoutManager mLayoutManager;
    private BackupAdapter mBackupAdapter;


    @BindView(R.id.fragment_intro_restore_backup_no_backups_found_container)
    LinearLayout mNoBackupsFoundContainer;

    @BindView(R.id.fragment_intro_restore_backup_restore_done_container)
    LinearLayout mRestoreDoneContainer;

    @BindView(R.id.fragment_intro_restore_backup_restore_canceled_container)
    LinearLayout mRestoreCanceledContainer;

    public static AppIntroRestoreBackupFragment newInstance() {
        AppIntroRestoreBackupFragment slide = new AppIntroRestoreBackupFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_TITLE, sliderPage.getTitleString());
//        args.putString(ARG_TITLE_TYPEFACE, sliderPage.getTitleTypeface());
//        args.putString(ARG_DESC, sliderPage.getDescriptionString());
//        args.putString(ARG_DESC_TYPEFACE, sliderPage.getDescTypeface());
//        args.putInt(ARG_DRAWABLE, sliderPage.getImageDrawable());
//        args.putInt(ARG_BG_COLOR, sliderPage.getBgColor());
//        args.putInt(ARG_TITLE_COLOR, sliderPage.getTitleColor());
//        args.putInt(ARG_DESC_COLOR, sliderPage.getDescColor());
//        slide.setArguments(args);
        return slide;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro_restore_backup, container, false);
        ButterKnife.bind(this, v);
        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

        mBgColor = ContextCompat.getColor(getContext(), R.color.primary);
        setBackgroundColor(mBgColor);


        //Click listeners for buttons
        mGoogleDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.restoreTypeChosen(false);
            }
        });
        mLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.restoreTypeChosen(true);
            }
        });
        mRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.restoreBackup(mBackupAdapter.getSelectedBackup());
            }
        });

        return v;
    }


    @Override
    public void changeStatus(RestoreBackupState restoreBackupState) {
        mChooseBackupContainer.setVisibility(View.GONE);
        mNoPermissionsContainer.setVisibility(View.GONE);
        mRestoreDoneContainer.setVisibility(View.GONE);
        mChooseRestoreSourceContainer.setVisibility(View.GONE);
        mNoBackupsFoundContainer.setVisibility(View.GONE);
        mRestoreCanceledContainer.setVisibility(View.GONE);

        switch (restoreBackupState) {
            case NO_PERMISSIONS:
                mNoPermissionsContainer.setVisibility(View.VISIBLE);
                mCanSlide = true;
                break;

            case CHOOSE_RESTORE_SOURCE:
                mChooseRestoreSourceContainer.setVisibility(View.VISIBLE);
                mCanSlide = true;
                break;

            case NO_BACKUPS_FOUND:
                mNoBackupsFoundContainer.setVisibility(View.VISIBLE);
                mCanSlide = true;
                break;

            case CHOOSE_BACKUP:
                mChooseBackupContainer.setVisibility(View.VISIBLE);
                mCanSlide = false;
                break;

            case RESTORE_DONE:
                mRestoreDoneContainer.setVisibility(View.VISIBLE);
                mCanSlide = true;
                break;

            case RESTORE_CANCELED:
                mRestoreCanceledContainer.setVisibility(View.VISIBLE);
                mCanSlide = true;
                break;

        }
    }

    @Override
    public void setUpBackupsList(List<BackupInfo> backupInfos) {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBackupAdapter = new BackupAdapter(getActivity(), backupInfos);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), mLayoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.item_decoration_complete_line));
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBackupAdapter);
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
    public void downloadGoogleDriveBackupFile(BackupInfo backupInfo) {


        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                //Create local file
                File localFile = new File(FileUtil.getBackupDir(), params[0]);
                if (!localFile.exists()) {
                    try {
                        localFile.createNewFile();
                    } catch (IOException e) {
                        //Handle error
                    }
                }

                //Query the file
                Query query = new Query.Builder()
                        .addFilter(Filters.eq(SearchableField.TITLE, params[0]))
                        .build();

                DriveApi.MetadataBufferResult result = Drive.DriveApi.query(mGoogleApiClient, query).await();

                if (!result.getStatus().isSuccess()) {
                    //Handle error
                }

                if(result.getMetadataBuffer().getCount() > 0) {
                    //File exists
                    DriveFile driveFile = result.getMetadataBuffer().get(0).getDriveId().asDriveFile();
                    DriveApi.DriveContentsResult driveContentsResult = driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).await();

                    if (!result.getStatus().isSuccess()) {
                        //Handle error
                    }

                    //File read successfully
                    int count;
                    byte data[] = new byte[Constants.BUFFER_SIZE];
                    try {
                        BufferedInputStream in = new BufferedInputStream(driveContentsResult.getDriveContents().getInputStream());
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
                        try {
                            while ((count = in.read(data, 0, Constants.BUFFER_SIZE)) != -1)
                                out.write(data, 0, count);
                        }
                        finally {
                            out.close();
                        }
                    } catch (Exception e) {
                        //Handle error
                    }

                }

                return params[0];
            }

            @Override
            protected void onPostExecute(String backupFileName) {
                super.onPostExecute(backupFileName);
                mPresenter.backupReadyToRestore(backupFileName);
            }
        }.execute(backupInfo.getFilename());


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
                connectionResult.startResolutionForResult(getActivity(), Constants.REQUEST_RESOLVE_GOOGLE_API_CONNECTION_INTRO);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), 0).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.REQUEST_RESOLVE_GOOGLE_API_CONNECTION_INTRO:
                if (resultCode == RESULT_OK ) {
                    mGoogleApiClient.connect();
                } else {
                    mPresenter.googleApiClientNotConnected();
                }
                break;
        }
    }


    @Override
    public int getDefaultBackgroundColor() {
        return mBgColor;
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        mContainer.setBackgroundColor(backgroundColor);

    }

    @Override
    public void onSlideSelected() {
        mPresenter.fragmentIsVisible(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onSlideDeselected() {
        mPresenter.fragmentLostVisibility();
    }






    /* ISlidePolicy interface implementation */

    @Override
    public boolean isPolicyRespected() {
        return mCanSlide;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.dialog_skip_restore_title))
                .setMessage(getResources().getString(R.string.dialog_skip_restore_message))
                .setPositiveButton(getResources().getString(R.string.dialog_skip),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeStatus(RestoreBackupState.RESTORE_CANCELED);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}

