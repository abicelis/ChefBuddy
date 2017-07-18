package ve.com.abicelis.chefbuddy.app;

import android.support.annotation.StringRes;

import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 8/7/2017.
 */

public enum Message {
    ERROR_LOADING_RECIPES(R.string.error_loading_recipes),
    ERROR_LOADING_RECIPE(R.string.error_loading_recipe),
    ERROR_LOADING_INGREDIENTS(R.string.error_loading_ingredients)
    ;

    @StringRes int friendlyName;

    Message(@StringRes int friendlyName) {
        this.friendlyName = friendlyName;
    }


    public @StringRes int getFriendlyNameRes() {
        return friendlyName;
    }
    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }
}
