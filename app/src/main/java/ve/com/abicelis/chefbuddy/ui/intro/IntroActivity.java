package ve.com.abicelis.chefbuddy.ui.intro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.ui.home.HomeActivity;

/**
 * Created by abicelis on 7/8/2017.
 */

public class IntroActivity extends AppIntro {

    private static final int RESTORE_BACKUP_SLIDE_INDEX = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //if(SharedPreferenceUtil.isFirstTimeLaunchingApp()) {
        if(true) {
            //Hide StatusBar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            showStatusBar(false);


            SliderPage welcomeSlider = new SliderPage();
            welcomeSlider.setTitle(getString(R.string.activity_intro_slider_welcome_title));
            welcomeSlider.setDescription(getString(R.string.activity_intro_slider_welcome_description));
            welcomeSlider.setImageDrawable(R.drawable.icon_chef_buddy);
            welcomeSlider.setBgColor(ContextCompat.getColor(this, R.color.primary));
            addSlide(AppIntroFragment.newInstance(welcomeSlider));

            SliderPage storagePermissionSlider = new SliderPage();
            storagePermissionSlider.setTitle(getString(R.string.activity_intro_slider_storage_permission_title));
            storagePermissionSlider.setDescription(getString(R.string.activity_intro_slider_storage_permission_description));
            storagePermissionSlider.setImageDrawable(R.drawable.ic_cloud_upload);
            storagePermissionSlider.setBgColor(ContextCompat.getColor(this, R.color.intro_blue));
            addSlide(AppIntroFragment.newInstance(storagePermissionSlider));

            addSlide(AppIntroRestoreBackupFragment.newInstance());

            SliderPage allSetSlider = new SliderPage();
            allSetSlider.setTitle(getString(R.string.activity_intro_slider_all_set_title));
            allSetSlider.setImageDrawable(R.drawable.ic_all_set);
            allSetSlider.setBgColor(ContextCompat.getColor(this, R.color.intro_green));
            addSlide(AppIntroFragment.newInstance(allSetSlider));

            askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);


        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        //Make sure permission is asked again if user denies permission and decides to go back to grant it
        if(newFragment instanceof AppIntroRestoreBackupFragment) {
            if(!(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
                askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Forward result to fragment
        if(requestCode == Constants.REQUEST_RESOLVE_GOOGLE_API_CONNECTION_INTRO) {
            getSlides().get(RESTORE_BACKUP_SLIDE_INDEX).onActivityResult(requestCode, resultCode, data);
        }

    }
}
