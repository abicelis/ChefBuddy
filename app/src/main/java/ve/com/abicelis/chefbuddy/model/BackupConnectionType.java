package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

/**
 * Created by abicelis on 4/8/2017.
 */

public enum BackupConnectionType {
    WIFI(R.string.backup_connection_wifi),
    ALL(R.string.backup_connection_all),
    ;


    private @StringRes int friendlyName;

    BackupConnectionType(@StringRes int friendlyName){
        this.friendlyName = friendlyName;
    }


    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (BackupConnectionType x : values()) {
            friendlyValues.add(ChefBuddyApplication.getContext().getString(x.friendlyName));
        }
        return friendlyValues;
    }}
