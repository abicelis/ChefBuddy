package ve.com.abicelis.chefbuddy.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;

import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.util.FileUtil;

/**
 * Created by abicelis on 2/8/2017.
 */

public class BackupService extends IntentService {

    //CONSTS
    private static final String TAG = BackupService.class.getSimpleName();
    private static final int BUFFER_SIZE = 8192;


    @Inject
    ChefBuddyDAO mDao;

    public BackupService(){
        super("BackupService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);

        //Create backup dir if not exists
        File backupDir = new File(Constants.BACKUP_SERVICE_BACKUP_DIR);
        if(!backupDir.exists()) {
            backupDir.mkdir();

            //If dir could not be made, publish error
            if(!backupDir.exists()) {
                publishResults(false, Message.ERROR_CREATING_BACKUP_DIRECTORY);
                return;
            }
        }

        //Get the amount of recipes stored in DB
        int recipeCount;
        try {
            recipeCount = mDao.getRecipeCount();
        }catch (CouldNotGetDataException e) {
            publishResults(false, Message.ERROR_CREATING_BACKUP_DIRECTORY);
            return;
        }

        //Get and count the list of images in app
        File recipeImages = new File(FileUtil.getImageFilesDir().getPath());
        int imageCount = recipeImages.listFiles().length;



        //Get paths
        String zipFilePath = String.format(Locale.getDefault(),
                "%1$s/%2$d_%3$d_%4$d.zip",
                Constants.BACKUP_SERVICE_BACKUP_DIR,
                Calendar.getInstance().getTimeInMillis(),
                recipeCount,
                imageCount);
        String databasePath = this.getDatabasePath(Constants.DATABASE_NAME).getPath();
        String zipFolderForImages = Constants.BACKUP_SERVICE_ZIP_IMAGES_DIR;

        try {
            createZipBackup(zipFilePath, databasePath, recipeImages.listFiles(), zipFolderForImages);
        } catch (IOException e) {
            publishResults(false, null);
        }

        publishResults(true, null);
    }



    private void publishResults(boolean success, @Nullable Message message) {
        Intent intent = new Intent(Constants.BACKUP_SERVICE_BROADCAST_BACKUP_DONE);
        intent.putExtra(Constants.BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_RESULT, success);
        intent.putExtra(Constants.BACKUP_SERVICE_BROADCAST_INTENT_EXTRA_MESSAGE, message);


        //Using LocalBroadcastManager instead of sendBroadcast() because...
        //LocalBroadcastManager is a helper class to register for and send broadcasts of Intents to local objects
        // within your process. This approach improves security as the broadcast events are only
        // visible within your process and is faster than using standard events.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        //sendBroadcast(intent);
    }









    /* Zip related methods */

    /**
     * Creates a new Chef Buddy zip backup file containing the SQL database and the recipe images
     * @param zipFilePath The path to the zip file where the backup will be stored
     * @param databasePath The path to the database file used by the app
     * @param imagePaths Array of paths to the images to back up
     */
    private static void createZipBackup(String zipFilePath, String databasePath, File[] imagePaths, String zipFolderForImages) throws IOException {

        //Create zip file
        File zipFile = new File(zipFilePath);
        zipFile.createNewFile();

        //Create a zipOutputStream
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));

        //Add database to zip
        addFileToZip(databasePath, null, out);

        //Add 'images' dir entry to zip
        addFolderToZip(zipFolderForImages, null, out);

        //Add images to zip
        for (File image : imagePaths) {
            addFileToZip(image.getPath(), zipFolderForImages, out);
        }

        out.close();
    }


    /**
     * Add a ZipEntry to a ZipOutputStream. This entry can only be a FILE, it can be inside a folder or in the root of the zip.
     * @param filePath the path to the file to be zipped
     * @param inFolder Specify a folder where to save {@code filePath}. Parameter must be in the form of 'firstFolder/', 'firstFolder/secondFolder/' or NULL if savint to root of zip
     * @param out A ZipOutputStream where the zipEntry will be added to
     * @throws IOException For errors regarding putNextEntry(), read() or write() on {@code out}
     */
    private static void addFileToZip(String filePath, @Nullable String inFolder, ZipOutputStream out) throws IOException {
        BufferedInputStream origin;
        byte data[] = new byte[BUFFER_SIZE];

        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String zipEntryFile = (inFolder != null ? inFolder : "" ) + fileName;


        FileInputStream fi = new FileInputStream(filePath);
        origin = new BufferedInputStream(fi, BUFFER_SIZE);
        try {
            ZipEntry entry = new ZipEntry(zipEntryFile);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            }
        }
        finally {
            fi.close();
            origin.close();
        }
    }


    /**
     * Add a FOLDER ZipEntry to a ZipOutputStream. This entry can be inside another folder or in the root of the zip.
     * @param folderName the name of the folder to be created. E.g.: 'folderName/'
     * @param inFolder Specify a folder where to save {@code folderName}. Parameter must be in the form of 'firstFolder/', 'firstFolder/secondFolder/' or NULL if savint to root of zip
     * @param out A ZipOutputStream where the zipEntry will be added to
     * @throws IOException For errors regarding putNextEntry(), read() or write() on {@code out}
     */
    private static void addFolderToZip(String folderName, @Nullable String inFolder, ZipOutputStream out) throws IOException {
        String zipEntryFile = (inFolder != null ? inFolder : "" ) + folderName;
        ZipEntry entry = new ZipEntry(zipEntryFile);
        out.putNextEntry(entry);
    }

}
