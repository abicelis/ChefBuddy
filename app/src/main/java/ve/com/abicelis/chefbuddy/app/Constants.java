package ve.com.abicelis.chefbuddy.app;


import android.os.Environment;

/**
 * Created by abicelis on 8/7/2017.
 */

public class Constants {
    public static final String  CHEFF_BUDDY = " - Chef Buddy";
    public static final String DATABASE_NAME = "ChefBuddy.db";

    public static final String  IMAGE_FILE_EXTENSION = ".jpg";
    public static final String  PDF_FILE_EXTENSION = ".pdf";
    public static final String  IMAGE_FILES_DIR = "recipe/image";
    public static final String  IMAGE_FILENAMES_SEPARATOR = "|";
    public static final int     IMAGE_JPEG_COMPRESSION_PERCENTAGE = 50;

    public static final String  RECIPE_INGREDIENT_STRING_FORMAT = "%1$s%2$s %3$s";


    /* INTENT ACTIONS */
    public static final String  BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
    public static final String  APP_STARTED_ACTION = "ve.com.abicelis.chefbuddy.APP_STARTED";
    public static final String  BACKUP_FREQUENCY_CHANGED_ACTION = "ve.com.abicelis.chefbuddy.BACKUP_FREQUENCY_CHANGED";


    /* SPIN WHEEL CONSTANTS */
    public static final int     DEFAULT_SPIN_WHEEL_RECIPE_AMOUNT = 8;
    public static final int     MIN_SPIN_WHEEL_RECIPE_AMOUNT = 2;
    public static final int     MAX_SPIN_WHEEL_RECIPE_AMOUNT = 10;


    /* MAX LENGTH FIELDS */
    public static final int     MAX_LENGTH_RECIPE_NAME = 30;
    public static final int     MAX_LENGTH_RECIPE_DIRECTIONS = 10000;
    public static final int     MAX_LENGTH_RECIPE_INGREDIENT_AMOUNT = 5;
    public static final int     MAX_LENGTH_INGREDIENT_NAME = 30;


    /* INTENT EXTRAS */
    public static final String  RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE = "RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE";
    public static final String  RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE = "RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE";

    public static final String  ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID = "ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID";

    public static final String  IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE = "IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE";
    public static final String  IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_RECIPE = "IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_RECIPE";
    public static final String  IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_IMAGE_POSITION = "IMAGE_GALLERY_ACTIVITY_INTENT_EXTRA_IMAGE_POSITION";

    public static final String  EDIT_IMAGE_ACTIVITY_INTENT_IMAGE_FILENAME = "EDIT_IMAGE_ACTIVITY_INTENT_IMAGE_FILENAME";


    /* PERMISSIONS */
    public static final int     RECIPE_DETAIL_ACTIVITY_PERMISSIONS = 200;
    public static final int     EDIT_IMAGE_ACTIVITY_PERMISSIONS = 201;
    public static final int     BACKUP_ACTIVITY_PERMISSIONS = 202;
    public static final int     BACKUP_ACTIVITY_HANDLE_BACKUP_PERMISSIONS = 203;


    /* START ACTIVITY REQUEST CODES */
    public static final int     REQUEST_ADD_RECIPE_IMAGE = 122;
    public static final int     REQUEST_IMAGE_CAPTURE = 123;
    public static final int     REQUEST_PICK_IMAGE_GALLERY = 124;
    public static final int     REQUEST_RESOLVE_GOOGLE_API_CONNECTION = 125;



    /* WEB APIS */
    public static final String  EDAMAM_BASE_URL = "https://edamam-recipe-search-and-diet-v1.p.mashape.com/";
    private static final String EDAMAM_API_KEY_TESTING = "FgW2dpYzYzmshHbrNbwec3dlmvy6p1DHtBLjsn0iVf8VxE8zWS";
    public static final String  EDAMAM_API_KEY = EDAMAM_API_KEY_TESTING;

    public static final String  FOOD2FORK_BASE_URL = "http://food2fork.com/api/";
    public static final String  FOOD2FORK_API_KEY = "544dce2bc365fff9a14505aa58c3731b";


    /* SHARED PREFERENCES */
    public static final String  SHARED_PREFERENCE_BACKUP_FREQUENCY_TYPE = "SHARED_PREFERENCE_BACKUP_FREQUENCY_TYPE";
    public static final String  SHARED_PREFERENCE_BACKUP_CONNECTION_TYPE = "SHARED_PREFERENCE_BACKUP_CONNECTION_TYPE";
    public static final String  SHARED_PREFERENCE_GOOGLE_DRIVE_BACKUP_ENABLED = "SHARED_PREFERENCE_GOOGLE_DRIVE_BACKUP_ENABLED";
    public static final String  SHARED_PREFERENCE_GOOGLE_DRIVE_BACKUP_ACCOUNT = "SHARED_PREFERENCE_GOOGLE_DRIVE_BACKUP_ACCOUNT";
    public static final String  SHARED_PREFERENCE_BACKUP_ALARM_TIME = "SHARED_PREFERENCE_BACKUP_ALARM_TIME";
    public static final String  SHARED_PREFERENCE_FIRST_TIME_LAUNCHING_APP = "SHARED_PREFERENCE_FIRST_TIME_LAUNCHING_APP";


    /* BACKUP SERVICE */
    public static final String BACKUP_SERVICE_BROADCAST_BACKUP_PROGRESS = "BACKUP_SERVICE_BROADCAST_BACKUP_PROGRESS";
    public static final String  BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_MESSAGE = "BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_MESSAGE";

    public static final String  BACKUP_SERVICE_BACKUP_DIR = Environment.getExternalStorageDirectory().getPath() + "/Chef Buddy";
    public static final String  BACKUP_SERVICE_BACKUP_FILE_FORMAT = "%1$s/%2$d_%3$d_%4$d.zip";
    public static final String  BACKUP_SERVICE_ZIP_IMAGES_DIR = "images/";
    public static final int     BACKUP_SERVICE_MAXIMUM_BACKUPS = 5;



}
