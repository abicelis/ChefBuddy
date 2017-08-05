package ve.com.abicelis.chefbuddy.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.ui.about.AboutActivity;
import ve.com.abicelis.chefbuddy.ui.backup.BackupActivity;

/**
 * Created by abicelis on 2/8/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {


    //UI
    private Preference mBackup;
    private Preference mAbout;
    private Preference mRate;
    private Preference mContact;


    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.chef_buddy_settings);

        mBackup = findPreference(getString(R.string.settings_backup_key));
        mBackup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent goToBackupActivity = new Intent(getActivity(), BackupActivity.class);
                startActivity(goToBackupActivity);
                return true;
            }
        });

        mAbout = findPreference(getResources().getString(R.string.settings_about_key));
        mAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent goToAboutActivity = new Intent(getActivity(), AboutActivity.class);
                startActivity(goToAboutActivity);
                return true;
            }
        });


        mRate = findPreference(getResources().getString(R.string.settings_rate_key));
        mRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
                playStoreIntent.setData(Uri.parse(getResources().getString(R.string.url_market)));
                startActivity(playStoreIntent);
                return true;
            }
        });


        mContact = findPreference(getResources().getString(R.string.settings_contact_key));
        mContact.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",getResources().getString(R.string.address_email), null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                return true;
            }
        });
    }
}
