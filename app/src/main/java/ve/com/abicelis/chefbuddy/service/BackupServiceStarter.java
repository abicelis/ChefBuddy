package ve.com.abicelis.chefbuddy.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.util.SharedPreferenceUtil;

/**
 * Created by abicelis on 4/8/2017.
 */

public class BackupServiceStarter extends BroadcastReceiver {
    public static final int PENDING_INTENT_REQUEST_CODE = 29;

    @Override
    public void onReceive(Context context, Intent intent) {

        //If boot completed, set alarm
        if ( intent.getAction().equals(Constants.BOOT_COMPLETED_ACTION)) {
            setAlarmForBackup(context);
        }

        //If starting app for the first time (such as when recently installed), set alarm
        if (intent.getAction().equals(Constants.APP_STARTED_ACTION) && SharedPreferenceUtil.isFirstTimeLaunchingApp()) {
            setAlarmForBackup(context);
        }

        //If backup frequency changed, set alarm
        if (intent.getAction().equals(Constants.BACKUP_FREQUENCY_CHANGED_ACTION)) {
            setAlarmForBackup(context);
        }
    }


    private void setAlarmForBackup(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Set alarm to occur at nextAlarmTime, and repeat every intervalMillis
        long intervalMillis = SharedPreferenceUtil.getBackupFrequencyType().getMillis();
        long nextAlarmTime = SystemClock.elapsedRealtime() + intervalMillis;

        //Create intent and pendingIntent
        Intent triggerBackupService = new Intent(context, BackupService.class);
        PendingIntent triggerBackupServicePendingIntent =
                PendingIntent.getService(context, PENDING_INTENT_REQUEST_CODE, triggerBackupService, PendingIntent.FLAG_UPDATE_CURRENT);

        //Set alarm
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, nextAlarmTime, intervalMillis, triggerBackupServicePendingIntent);
    }
}
