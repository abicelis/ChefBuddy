package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

/**
 * Created by abicelis on 4/8/2017.
 */

public enum BackupFrequencyType {
    DAILY(R.string.backup_frecuency_daily),
    WEEKLY(R.string.backup_frecuency_weekly),
    MONTHLY(R.string.backup_frecuency_monthly)
    ;


    private @StringRes int friendlyName;


    BackupFrequencyType(@StringRes int friendlyName){
        this.friendlyName = friendlyName;
    }


    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (BackupFrequencyType x : values()) {
            friendlyValues.add(ChefBuddyApplication.getContext().getString(x.friendlyName));
        }
        return friendlyValues;
    }}
