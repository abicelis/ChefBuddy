package ve.com.abicelis.chefbuddy.app;


/**
 * Created by abicelis on 8/7/2017.
 */

public class Constants {
    public static final String CHEFF_BUDDY = " - Chef Buddy";

    public static final String API_KEY = "394TsayTE0ZxEgLLwuCvpfQWyoWTjGHF3UivkfZB";

    public static final String  IMAGE_FILE_EXTENSION = ".jpg";
    public static final String  PDF_FILE_EXTENSION = ".pdf";
    public static final String  IMAGE_FILES_DIR = "recipe/image";
    public static final String  IMAGE_FILENAMES_SEPARATOR = "|";
    public static final int     IMAGE_JPEG_COMPRESSION_PERCENTAGE = 30;


    public static final String RECIPE_INGREDIENT_STRING_FORMAT = "%1$s%2$s %3$s";

    public static final int MAX_LENGHT_RECIPE_INGREDIENT_AMOUNT = 5;
    public static final int MAX_LENGHT_RECIPE_INGREDIENT_INGREDIENT = 15;


    /* INTENT EXTRAS */
    public static final String RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_ID = "RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_ID";
    public static final String ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID = "ADD_EDIT_RECIPE_ACTIVITY_INTENT_EXTRA_RECIPE_ID";
    public static final String IMAGE_GALLERY_ACTIVITY_INTENT_RECIPE_ID = "IMAGE_GALLERY_ACTIVITY_INTENT_RECIPE_ID";
    public static final String IMAGE_GALLERY_ACTIVITY_INTENT_IMAGE_POSITION = "IMAGE_GALLERY_ACTIVITY_INTENT_IMAGE_POSITION";


    /* PERMISSIONS */
    public static final int RECIPE_DETAIL_ACTIVITY_PERMISSIONS = 200;

}
