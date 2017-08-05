package ve.com.abicelis.chefbuddy.util;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.model.BackupConnectionType;
import ve.com.abicelis.chefbuddy.model.BackupFrequencyType;

/**
 * Created by abice on 1/4/2017.
 */

public class SharedPreferenceUtil {


    /* BACKUP FREQUENCY TYPE */
    public static BackupFrequencyType getBackupFrequencyType() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext());
        String value = preferences.getString(Constants.SHARED_PREFERENCE_BACKUP_FREQUENCY_TYPE, null);
        BackupFrequencyType pref;
        try {
            pref = BackupFrequencyType.valueOf(value);
        } catch (Exception e) {
            pref = null;
        }

        if(pref == null) {
            Log.d("SharedPreferenceUtil", "getBackupFrequencyType() found null, setting WEEKLY");
            pref = BackupFrequencyType.WEEKLY;
            setBackupFrequencyType(pref);
        }

        return pref;
    }
    public static void setBackupFrequencyType(BackupFrequencyType value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext()).edit();
        editor.putString(Constants.SHARED_PREFERENCE_BACKUP_FREQUENCY_TYPE, value.name());
        editor.apply();
    }




    /* BACKUP CONNECTION TYPE */
    public static BackupConnectionType getBackupConnectionType() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext());
        String value = preferences.getString(Constants.SHARED_PREFERENCE_BACKUP_CONNECTION_TYPE, null);
        BackupConnectionType pref;
        try {
            pref = BackupConnectionType.valueOf(value);
        } catch (Exception e) {
            pref = null;
        }

        if(pref == null) {
            Log.d("SharedPreferenceUtil", "getBackupConnectionType() found null, setting WIFI");
            pref = BackupConnectionType.WIFI;
            setBackupConnectionType(pref);
        }

        return pref;
    }
    public static void setBackupConnectionType(BackupConnectionType value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext()).edit();
        editor.putString(Constants.SHARED_PREFERENCE_BACKUP_CONNECTION_TYPE, value.name());
        editor.apply();
    }





    /* GOOGLE DRIVE BACKUP ENABLED */
    public static boolean getGoogleDriveBackupEnabled() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext());
        return preferences.getBoolean(Constants.SHARED_PREFERENCE_GOOGLE_DRIVE_BACKUP_ENABLED, false);
    }
    public static void setGoogleDriveBackupEnabled(boolean enabled) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext()).edit();
        editor.putBoolean(Constants.SHARED_PREFERENCE_GOOGLE_DRIVE_BACKUP_ENABLED, enabled);
        editor.apply();
    }



    /* FIRST TIME LAUNCHING APP */
    public static boolean isFirstTimeLaunchingApp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext());
        boolean firstTimeLaunchingApp = preferences.getBoolean(Constants.SHARED_PREFERENCE_FIRST_TIME_LAUNCHING_APP, true);

        if(firstTimeLaunchingApp) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.SHARED_PREFERENCE_FIRST_TIME_LAUNCHING_APP, false);
            editor.apply();
        }

        return firstTimeLaunchingApp;
    }



    /* BACKUP ALARM TIME */
    public static long getNextBackupAlarmTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext());
        return preferences.getLong(Constants.SHARED_PREFERENCE_BACKUP_ALARM_TIME, -1);
    }
    public static void setNextBackupAlarmTime(long alarmTime) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChefBuddyApplication.getContext()).edit();
        editor.putLong(Constants.SHARED_PREFERENCE_BACKUP_ALARM_TIME, alarmTime);
        editor.apply();
    }


}
