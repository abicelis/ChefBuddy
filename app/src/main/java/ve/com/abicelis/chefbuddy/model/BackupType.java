package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

/**
 * Created by abicelis on 4/8/2017.
 */

public enum BackupType {
    LOCAL(R.string.backup_type_local),
    GOOGLE_DRIVE(R.string.backup_type_google_drive),
    BOTH(R.string.backup_type_both),
    ;


    private @StringRes int friendlyName;

    BackupType(@StringRes int friendlyName){
        this.friendlyName = friendlyName;
    }


    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (BackupType x : values()) {
            friendlyValues.add(ChefBuddyApplication.getContext().getString(x.friendlyName));
        }
        return friendlyValues;
    }}
