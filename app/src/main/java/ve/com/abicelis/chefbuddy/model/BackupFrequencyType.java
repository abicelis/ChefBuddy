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
    MINUTES_5(R.string.backup_frecuency_minutes_5,  300000L),           // 1 * 1 * 5 * 60 * 1000
    DAILY(R.string.backup_frecuency_daily,          86400000L),         // 1 * 24 * 60 * 60 * 1000
    WEEKLY(R.string.backup_frecuency_weekly,        604800000L),        // 7 * 24 * 60 * 60 * 1000
    MONTHLY(R.string.backup_frecuency_monthly,      2592000000L)        // 30 * 24 * 60 * 60 * 1000
    ;


    private @StringRes int friendlyName;
    private long millis;


    BackupFrequencyType(@StringRes int friendlyName, long millis){
        this.friendlyName = friendlyName;
        this.millis = millis;
    }


    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }

    public long getMillis() {
        return millis;
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (BackupFrequencyType x : values()) {
            friendlyValues.add(ChefBuddyApplication.getContext().getString(x.friendlyName));
        }
        return friendlyValues;
    }}
