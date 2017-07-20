package ve.com.abicelis.chefbuddy.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;

/**
 * Created by abicelis on 3/7/2017.
 */

public class FileUtil {


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

    public static File getImageFilesDir() {
        return new File(ChefBuddyApplication.getContext().getExternalFilesDir(null), Constants.IMAGE_FILES_DIR);
    }

    public static File getExternalStorageDir() {
        return new File(Environment.getExternalStorageDirectory().getPath());
    }

    public static void createDirIfNotExists(File directory) throws IOException, SecurityException  {
        if (directory.mkdirs()){
            File nomedia = new File(directory, ".nomedia");
            nomedia.createNewFile();
        }
    }


    /**
     * Creates an empty file at the specified directory, with the given name if it doesn't already exist
     *
     */
    public static File createNewFileIfNotExistsInDir(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);
        file.createNewFile();
        return file;
    }



    public static void deleteImageAttachment(Activity activity, String filename) {
        if(filename != null && !filename.isEmpty()) { //Delete file
            File file = new File(FileUtil.getImageFilesDir(), filename);
            file.delete();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static File savePdfDocumentToSD(PdfDocument pdfDocument, String filename) throws IOException {
        File filePath = new File(getExternalStorageDir(), filename.replaceAll("[^A-Za-z0-9\\s]", "") + Constants.CHEFF_BUDDY + Constants.PDF_FILE_EXTENSION);
        pdfDocument.writeTo(new FileOutputStream(filePath));
        return filePath;
    }


//    public static File createTempImageFileInDir(File directory, String fileExtension) throws IOException, SecurityException {
//        if(fileExtension.toCharArray()[0] != '.')
//            fileExtension = "." + fileExtension;
//
//        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String fileName = "TEMP_" + UUID.randomUUID().toString() + "_";
//        File file = File.createTempFile(fileName, fileExtension, directory);
//        return file;
//    }

}