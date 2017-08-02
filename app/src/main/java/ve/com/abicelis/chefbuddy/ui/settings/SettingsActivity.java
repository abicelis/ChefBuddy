package ve.com.abicelis.chefbuddy.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 2/8/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    //UI
    @BindView(R.id.activity_settings_toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setUpToolbar();

        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_settings_fragment, fragment);
            ft.commit();
        }
    }

    private void setUpToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.activity_settings_toolbar);
        mToolbar.setTitle(getResources().getString(R.string.activity_settings_toolbar_title));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));

        //Set toolbar as actionbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

}
