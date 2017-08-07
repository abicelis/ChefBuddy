package ve.com.abicelis.chefbuddy.app;

import android.support.annotation.StringRes;

import java.io.Serializable;

import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 8/7/2017.
 */

public enum Message implements Serializable {
    ERROR_LOADING_RECIPES(R.string.error_loading_recipes, MessageType.ERROR),
    ERROR_LOADING_RECIPE(R.string.error_loading_recipe, MessageType.ERROR),
    ERROR_DOWNLOADING_RECIPE(R.string.error_downloading_recipe, MessageType.ERROR),
    ERROR_DELETING_RECIPE(R.string.error_deleting_recipe, MessageType.ERROR),
    ERROR_SAVING_RECIPE(R.string.error_saving_recipe, MessageType.ERROR),
    ERROR_EXPORTING_RECIPE_TO_PDF(R.string.error_exporting_recipe_to_pdf, MessageType.ERROR),
    ERROR_LOADING_INGREDIENTS(R.string.error_loading_ingredients, MessageType.ERROR),
    ERROR_LOADING_IMAGES(R.string.error_loading_images, MessageType.ERROR),
    ERROR_CREATING_IMAGE(R.string.error_creating_image, MessageType.ERROR),
    ERROR_SAVING_IMAGE(R.string.error_saving_image, MessageType.ERROR),
    ERROR_NO_CAMERA_INSTALLED(R.string.error_no_camera_installed, MessageType.ERROR),
    ERROR_REQUESTING_CAMERA(R.string.error_requesting_camera, MessageType.ERROR),
    ERROR_SAVING_PDF(R.string.error_saving_pdf, MessageType.ERROR),
    ERROR_LOADING_ONLINE_RECIPES(R.string.error_loading_online_recipes, MessageType.ERROR),

    NOTICE_LOADING_RECIPE_DETAILS(R.string.notice_loading_recipes_details, MessageType.NOTICE),

    INVALID_RECIPE_NAME(R.string.invalid_recipe_name, MessageType.ERROR),
    INVALID_RECIPE_SERVINGS(R.string.invalid_recipe_servings, MessageType.ERROR),
    INVALID_RECIPE_PREPARATION_TIME(R.string.invalid_recipe_preparation_time, MessageType.ERROR),
    INVALID_RECIPE_DIRECTIONS(R.string.invalid_recipe_directions, MessageType.ERROR),
    INVALID_RECIPE_INGREDIENT_NAME(R.string.invalid_recipe_ingredient_name, MessageType.ERROR),
    INVALID_WHEEL_RECIPE_AMOUNT_TOO_MANY(R.string.invalid_wheel_recipe_amount_too_many, MessageType.ERROR),
    INVALID_WHEEL_RECIPE_AMOUNT_TOO_FEW(R.string.invalid_wheel_recipe_amount_too_few, MessageType.ERROR),

    PERMISSIONS_WRITE_STORAGE_NOT_GRANTED(R.string.permissions_write_storage_not_granted, MessageType.ERROR),
    PERMISSIONS_NOT_GRANTED(R.string.permissions_not_granted, MessageType.ERROR),


    // Backup Service
    ERROR_CREATING_BACKUP_DIRECTORY(R.string.error_creating_backup_directory, MessageType.ERROR),
    ERROR_CREATING_ZIP_FILE(R.string.error_creating_zip_file, MessageType.ERROR),
    ERROR_UNKNOWN_CREATING_BACKUP(R.string.error_unknown_creating_backup, MessageType.ERROR),
    SUCCESS_CREATING_LOCAL_BACKUP(R.string.success_creating_local_backup, MessageType.SUCCESS),
    ERROR_CONNECTING_GOOGLE_API(R.string.error_connecting_google_api, MessageType.ERROR),
    ERROR_DRIVE_CREATING_FILE_CONTENTS(R.string.error_drive_creating_file_contents, MessageType.ERROR),
    ERROR_DRIVE_WRITING_FILE_CONTENTS(R.string.error_drive_writing_file_contents, MessageType.ERROR),
    ERROR_DRIVE_UPLOADING_BACKUP(R.string.error_drive_uploading_backup, MessageType.ERROR),
    SUCCESS_CREATING_LOCAL_AND_GOOGLE_DRIVE_BACKUP(R.string.success_creating_local_and_google_drive_backup, MessageType.SUCCESS),
    ERROR_DRIVE_LISTING_FILES(R.string.error_drive_listing_files, MessageType.ERROR),
    ERROR_DRIVE_DELETING_OLD_BACKUPS(R.string.error_drive_deleting_old_backups, MessageType.ERROR),


    ;

    @StringRes int friendlyName;
    MessageType messageType;

    Message(@StringRes int friendlyName, MessageType messageType) {
        this.friendlyName = friendlyName;
        this.messageType = messageType;
    }


    public @StringRes int getFriendlyNameRes() {
        return friendlyName;
    }
    public MessageType getMessageType() {
        return messageType;
    }
    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }

    public enum MessageType {SUCCESS, ERROR, NOTICE}
}
