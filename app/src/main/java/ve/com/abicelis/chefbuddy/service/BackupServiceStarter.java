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
    public static final int PENDING_INTENT_REQUEST_CODE = 300;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ( intent.getAction().equals(Constants.BOOT_COMPLETED_ACTION) || intent.getAction().equals(Constants.APP_STARTED_ACTION) ) {
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            // TODO: 5/8/2017 check a sharedpreferences value like.. nextAlarm = 23989238
            //imcrement value by SharedPreferenceUtil.getBackupFrequencyType().getMillis(), check if > currentMillis,
            //when it is > current, save to sharedPref, set alarm etc.

            Intent triggerBackupService = new Intent(context, BackupService.class);
            PendingIntent triggerBackupServicePendingIntent = PendingIntent.getService(context, PENDING_INTENT_REQUEST_CODE, triggerBackupService, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + SharedPreferenceUtil.getBackupFrequencyType().getMillis(),
                    SharedPreferenceUtil.getBackupFrequencyType().getMillis(),
                    triggerBackupServicePendingIntent);

        }
    }
}
