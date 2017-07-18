package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

/**
 * Created by abicelis on 28/6/2017.
 */

public enum PreparationTime {
    MINUTE_1(1, true),
    MINUTE_5(5, true),
    MINUTE_10(10, true),
    MINUTE_20(20, true),
    MINUTE_30(30, true),
    MINUTE_45(45, true),
    HOUR_1(1, false),
    HOUR_2(2, false),
    HOUR_3(3, false),
    HOUR_4(4, false),
    HOUR_5(5, false),
    HOUR_8(8, false),
    HOUR_10(10, false);

    private static final @StringRes int minuteSingular = R.string.preparation_time_type_minute;
    private static final @StringRes int minutePlural = R.string.preparation_time_type_minutes;
    private static final @StringRes int hourSingular = R.string.preparation_time_type_hour;
    private static final @StringRes int hourPlural = R.string.preparation_time_type_hours;

    private int timeValue;
    private boolean minutes;

    PreparationTime(int timeValue, boolean minutes){
        this.timeValue = timeValue;
        this.minutes = minutes;
    }


    public String getFriendlyName() {
        if(timeValue == 1)
            return timeValue + " " + ChefBuddyApplication.getContext().getString((minutes? minuteSingular : hourSingular));
        else
            return timeValue + " " + ChefBuddyApplication.getContext().getString((minutes? minutePlural : hourPlural));
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (PreparationTime x : values()) {
            if(x.timeValue == 1)
                friendlyValues.add( x.timeValue + " " + ChefBuddyApplication.getContext().getString((x.minutes? minuteSingular : hourSingular)));
            else
                friendlyValues.add( x.timeValue + " " + ChefBuddyApplication.getContext().getString((x.minutes? minutePlural : hourPlural)));
        }
        return friendlyValues;
    }
}
