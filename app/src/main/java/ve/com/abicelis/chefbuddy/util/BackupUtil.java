package ve.com.abicelis.chefbuddy.util;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import ve.com.abicelis.chefbuddy.app.Constants;

/**
 * Created by abicelis on 9/8/2017.
 */

public class BackupUtil {
    private static final String TAG = BackupUtil.class.getSimpleName();

    /* LOCAL BACKUPS */

    /**
     * Returns the backup dir stored at {@code Constants.BACKUP_SERVICE_BACKUP_DIR}, creates one if none exists
     */
    public static File getBackupDirCreateIfNotExists() {
        File backupDir = FileUtil.getBackupDir();
        if(!backupDir.exists()) {
            backupDir.mkdir();

            if(!backupDir.exists())
                return null;
        }
        return backupDir;
    }

    /**
     * Returns the backup temp dir stored at {@code Constants.BACKUP_SERVICE_BACKUP_TEMP_DIR}, creates one if none exists
     */
    public static File getBackupTempDirCreateIfNotExists() {
        File backupTempDir = FileUtil.getBackupTempDir();
        if(!backupTempDir.exists()) {
            backupTempDir.mkdir();

            if(!backupTempDir.exists())
                return null;
        }
        return backupTempDir;
    }

    /**
     * Deletes oldest local backups if more than {@code Constants.BACKUP_SERVICE_MAXIMUM_BACKUPS}
     */
    public static void deleteOldLocalBackups() {
        List<File> backupZipFiles = FileUtil.getLocalBackupList(true);

        int aux = backupZipFiles.size() - Constants.BACKUP_SERVICE_MAXIMUM_BACKUPS;
        for(int i = 0 ; i < aux ; i++ ) {
            backupZipFiles.get(i).delete();
        }
    }

    /**
     * Creates a new Chef Buddy zip backup file containing the SQL database and the recipe images
     * @param zipFilePath The path to the zip file where the backup will be stored
     */
    public static void createZipBackup(String zipFilePath) throws IOException {
        String zipFolderForImages = Constants.BACKUP_SERVICE_ZIP_IMAGES_DIR;
        String databasePath = FileUtil.getDatabaseFile().getPath();
        File[] imagePaths = FileUtil.getImageFilesDir().listFiles();

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
        byte data[] = new byte[Constants.BUFFER_SIZE];

        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String zipEntryFile = (inFolder != null ? inFolder : "" ) + fileName;


        FileInputStream fi = new FileInputStream(filePath);
        origin = new BufferedInputStream(fi, Constants.BUFFER_SIZE);
        try {
            ZipEntry entry = new ZipEntry(zipEntryFile);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, Constants.BUFFER_SIZE)) != -1) {
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






    /**
     * Restores anew Chef Buddy zip backup file containing the SQL database and the recipe images
     * @param zipFilePath The path to the zip file with the backup
     */
    public static void restoreZipBackup(String zipFilePath) throws IOException {
        //String zipFolderForImages = Constants.BACKUP_SERVICE_ZIP_IMAGES_DIR;
        File imageDir = FileUtil.getImageFilesDir();
        File databaseFile = FileUtil.getDatabaseFile();

//        //Create a temp backup with current data, just in case
//        File tempDir = getBackupTempDirCreateIfNotExists();
//        if(tempDir == null)
//            throw new IOException("Could not create temp folder to store temp zip backup");
        //String tempBackupFilePath = generateTempBackupFilePath();
        //createZipBackup(tempBackupFilePath);


        //Delete old images if any
        File [] files = imageDir.listFiles();
        if(files != null)
            for(File f : files)
                f.delete();


        //Load zip file
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(zipFilePath);
        }catch (Exception e) {
            throw new FileNotFoundException("Zip file does not exist");
        }
        int count;

        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        byte data[] = new byte[Constants.BUFFER_SIZE];
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();

                if(entry.isDirectory())
                    continue;

                File newFile;
                if(entry.getName().equals(Constants.DATABASE_NAME))
                    newFile = databaseFile;
                 else {
                    String imageName = entry.getName();
                    imageName = imageName.substring(Constants.BACKUP_SERVICE_ZIP_IMAGES_DIR.length(), imageName.length());
                    newFile = new File(imageDir, imageName);
                }
                if(!newFile.exists())
                    newFile.createNewFile();

                in = new BufferedInputStream(zipFile.getInputStream(entry));
                out = new BufferedOutputStream(new FileOutputStream(newFile));
                try {
                    while ((count = in.read(data, 0, Constants.BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    out.close();
                }

            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if(in != null)
                in.close();
        }
    }

    public static String generateBackupFilePath(int recipeCount, int imageCount) {
        return String.format(Locale.getDefault(),
                Constants.BACKUP_SERVICE_BACKUP_FILE_FORMAT, FileUtil.getBackupDir().getPath(),
                Calendar.getInstance().getTimeInMillis(), recipeCount, imageCount);
    }

    public static String generateTempBackupFilePath() {
        return String.format(Locale.getDefault(),
                Constants.BACKUP_SERVICE_BACKUP_TEMP_FILE_FORMAT, FileUtil.getBackupTempDir().getPath(),
                Calendar.getInstance().getTimeInMillis());
    }

    public static boolean isValidBackupFilename(String backupFilename) {
        Pattern pattern = Pattern.compile("^\\d+_\\d+_\\d+\\.zip$"); //Matches a valid backup format 237898340_93_28.zip
        return pattern.matcher(backupFilename).matches();
    }
}
