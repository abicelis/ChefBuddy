package ve.com.abicelis.chefbuddy.enums;

import android.support.annotation.StringRes;

import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 28/6/2017.
 */

public enum PreparationTimeType {
    MINUTE(  R.string.preparation_time_type_minute,  R.string.preparation_time_type_minutes),
    HOUR(    R.string.preparation_time_type_hour,    R.string.preparation_time_type_hours),
    ;

    private @StringRes int friendlyNameSingular;
    private @StringRes int friendlyNamePlural;

    PreparationTimeType(@StringRes int friendlyNameSingular, @StringRes int friendlyNamePlural){
        this.friendlyNameSingular = friendlyNameSingular;
        this.friendlyNamePlural = friendlyNamePlural;
    }


    public String getFriendlyName(int time) {
        if(time == 1)
            return time + " "+ friendlyNameSingular;
        else
            return time + " "+ friendlyNamePlural;
    }
}
