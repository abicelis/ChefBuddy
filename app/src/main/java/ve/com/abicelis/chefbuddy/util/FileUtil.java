package ve.com.abicelis.chefbuddy.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;

/**
 * Created by abicelis on 3/7/2017.
 */

public class FileUtil {

    public static File getDatabaseFile() {
        return ChefBuddyApplication.getContext().getDatabasePath(Constants.DATABASE_NAME);
    }

    public static File getImageFilesDir() {
        return new File(ChefBuddyApplication.getContext().getExternalFilesDir(null), Constants.IMAGE_FILES_DIR);
    }

    public static File getExternalStorageDir() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getBackupDir() {
        return new File(FileUtil.getExternalStorageDir().getPath() + Constants.BACKUP_SERVICE_BACKUP_DIR);
    }

    public static File getBackupTempDir() {
        return new File(FileUtil.getExternalStorageDir().getPath() + Constants.BACKUP_SERVICE_BACKUP_TEMP_DIR);
    }

    public static List<File> getLocalBackupList(boolean sortAscending) {
        List<File> localBackups = new ArrayList<>();
        File backupDir = getBackupDir();
        final File[] backupZipFiles = backupDir.listFiles();

        if(backupZipFiles != null) {
            if(sortAscending)
                Arrays.sort(backupZipFiles);
            else
                Arrays.sort(backupZipFiles, Collections.reverseOrder());


            for (File f : backupZipFiles)
                if (BackupUtil.isValidBackupFilename(f.getName()))
                    localBackups.add(f);
        }
        return localBackups;
    }



    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     *
     * @param fromFile
     *            - FileInputStream for the file to copy from.
     * @param toFile
     *            - FileInputStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    public static void createDirIfNotExists(File directory) throws IOException, SecurityException  {
        if (directory.mkdirs()){
            File nomedia = new File(directory, ".nomedia");
            nomedia.createNewFile();
        }
    }

    public static boolean deleteFile(File directory, String filename) {
        File file = new File(directory, filename);
        return file.delete();
    }


    /**
     * Creates an empty file at the specified directory, with the given name if it doesn't already exist
     */
    public static File createNewFileIfNotExistsInDir(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);
        file.createNewFile();
        return file;
    }

    public static Uri getUriForFile(File file) {
        return FileProvider.getUriForFile(ChefBuddyApplication.getContext(), "ve.com.abicelis.chefbuddy.fileprovider", file);
    }



    /**
     * Saves a JPEG at the given quality to disk at the specified path of the given File
     * @param file The file where the JPEG will be saved
     * @param bitmapToSave The Bitmap to save into the file
     * @param quality The percentage of JPEG compression
     */
    public static void saveBitmapAsJpeg(File file, Bitmap bitmapToSave, int quality) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(ImageUtil.toCompressedByteArray(bitmapToSave, quality));
        fos.close();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static File savePdfDocumentToSD(PdfDocument pdfDocument, String filename) throws IOException {
        File filePath = new File(getExternalStorageDir(), filename.replaceAll("[^A-Za-z0-9\\s]", "") + Constants.CHEFF_BUDDY + Constants.PDF_FILE_EXTENSION);
        pdfDocument.writeTo(new FileOutputStream(filePath));
        return filePath;
    }


}