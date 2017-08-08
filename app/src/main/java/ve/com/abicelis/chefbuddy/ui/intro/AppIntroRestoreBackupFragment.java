package ve.com.abicelis.chefbuddy.ui.intro;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntroBaseFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.ui.intro.presenter.AppIntroRestoreBackupPresenter;
import ve.com.abicelis.chefbuddy.ui.intro.view.AppIntroRestoreBackupView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by abicelis on 7/8/2017.
 */

public class AppIntroRestoreBackupFragment extends AppIntroBaseFragment implements AppIntroRestoreBackupView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;

    @Inject
    AppIntroRestoreBackupPresenter mPresenter;

    @BindView(R.id.fragment_intro_restore_backup_google_drive)
    Button mGoogleDrive;
    @BindView(R.id.fragment_intro_restore_backup_local)
    Button mLocal;
    // TODO: 7/8/2017 missing a button here, RESTORE button.


    public static AppIntroRestoreBackupFragment newInstance(SliderPage sliderPage) {
        AppIntroRestoreBackupFragment slide = new AppIntroRestoreBackupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, sliderPage.getTitleString());
        args.putString(ARG_TITLE_TYPEFACE, sliderPage.getTitleTypeface());
        args.putString(ARG_DESC, sliderPage.getDescriptionString());
        args.putString(ARG_DESC_TYPEFACE, sliderPage.getDescTypeface());
        args.putInt(ARG_DRAWABLE, sliderPage.getImageDrawable());
        args.putInt(ARG_BG_COLOR, sliderPage.getBgColor());
        args.putInt(ARG_TITLE_COLOR, sliderPage.getTitleColor());
        args.putInt(ARG_DESC_COLOR, sliderPage.getDescColor());
        slide.setArguments(args);

        return slide;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

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
        // TODO: 7/8/2017 missing a button here, RESTORE button.

        return v;
    }


    public void fragmentVisibilityChanged(boolean visible){
        if(visible)
            mPresenter.fragmentIsVisible(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        else
            mPresenter.fragmentLostVisibility();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_intro_restore_backup;
    }


    @Override
    public void changeStatus(RestoreBackupState restoreBackupState) {
        switch (restoreBackupState) {
            case NO_PERMISSIONS:
                break;
            case CHOOSING_LOCAL_AND_OR_GOOGLE:
                break;
            case NO_BACKUPS_FOUND:
                break;
            case CHOOSING_BACKUP_TO_RESTORE:
                break;
        }
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
    public void showErrorMessage(Message message) {
        Toast.makeText(getActivity(), message.getFriendlyNameRes(), Toast.LENGTH_SHORT).show();
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


}

