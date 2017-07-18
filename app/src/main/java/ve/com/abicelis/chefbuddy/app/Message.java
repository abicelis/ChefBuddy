package ve.com.abicelis.chefbuddy.app;

import android.support.annotation.StringRes;

import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 8/7/2017.
 */

public enum Message {
    HOME_ACTIVITY_ERROR_LOADING_RECIPES(R.string.message_home_activity_error_loading_recipes),
    RECIPE_DETAIL_ACTIVITY_ERROR_LOADING_RECIPE(R.string.message_recipe_detail_activity_error_loading_recipe),
    ADD_RECIPE_INGREDIENT_DIALOG_FRAGMENT_ERROR_LOADING_INGREDIENTS(R.string.message_recipe_detail_activity_error_loading_recipe)
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
