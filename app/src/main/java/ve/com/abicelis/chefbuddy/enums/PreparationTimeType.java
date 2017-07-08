package ve.com.abicelis.chefbuddy.enums;

import android.support.annotation.StringRes;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

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


    public String getFriendlyName(String time) {
        int timeInt;
        try {
            timeInt = Integer.parseInt(time);
            if(timeInt == 1)
                return ChefBuddyApplication.getContext().getString(friendlyNameSingular);
            else
                return ChefBuddyApplication.getContext().getString(friendlyNamePlural);
        } catch (NumberFormatException e) {
            return ChefBuddyApplication.getContext().getString(friendlyNameSingular);
        }
    }

}