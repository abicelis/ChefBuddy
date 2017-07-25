package ve.com.abicelis.chefbuddy.app;

import android.support.annotation.StringRes;

import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 8/7/2017.
 */

public enum Message {
    ERROR_LOADING_RECIPES(R.string.error_loading_recipes),
    ERROR_LOADING_RECIPE(R.string.error_loading_recipe),
    ERROR_DOWNLOADING_RECIPE(R.string.error_downloading_recipe),
    ERROR_DELETING_RECIPE(R.string.error_deleting_recipe),
    ERROR_SAVING_RECIPE(R.string.error_saving_recipe),
    ERROR_EXPORTING_RECIPE_TO_PDF(R.string.error_exporting_recipe_to_pdf),
    ERROR_LOADING_INGREDIENTS(R.string.error_loading_ingredients),
    ERROR_LOADING_IMAGES(R.string.error_loading_images),
    ERROR_CREATING_IMAGE(R.string.error_creating_image),
    ERROR_SAVING_IMAGE(R.string.error_saving_image),
    ERROR_NO_CAMERA_INSTALLED(R.string.error_no_camera_installed),
    ERROR_REQUESTING_CAMERA(R.string.error_requesting_camera),
    ERROR_SAVING_PDF(R.string.error_saving_pdf),
    ERROR_LOADING_ONLINE_RECIPES(R.string.error_loading_online_recipes),

    NOTICE_LOADING_RECIPE_DETAILS(R.string.notice_loading_recipes_details),

    INVALID_RECIPE_NAME(R.string.invalid_recipe_name),
    INVALID_RECIPE_SERVINGS(R.string.invalid_recipe_servings),
    INVALID_RECIPE_PREPARATION_TIME(R.string.invalid_recipe_preparation_time),
    INVALID_RECIPE_DIRECTIONS(R.string.invalid_recipe_directions),
    INVALID_RECIPE_INGREDIENT_NAME(R.string.invalid_recipe_ingredient_name),

    PERMISSIONS_WRITE_STORAGE_NOT_GRANTED(R.string.permissions_write_storage_not_granted),
    PERMISSIONS_NOT_GRANTED(R.string.permissions_not_granted)
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
