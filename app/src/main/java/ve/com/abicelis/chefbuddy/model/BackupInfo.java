package ve.com.abicelis.chefbuddy.model;

import android.text.format.DateUtils;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.util.BackupUtil;
import ve.com.abicelis.chefbuddy.util.CalendarUtil;

/**
 * Created by abicelis on 4/8/2017.
 */

public class BackupInfo {

    private String filename;
    private Calendar date = CalendarUtil.getNewInstanceZeroedCalendar();
    private int recipeCount;
    private int imageCount;
    private BackupType backupType;
    private boolean selected;

    public BackupInfo(String filename, BackupType backupType) {
        this.filename = filename;
        this.backupType = backupType;

        if(!BackupUtil.isValidBackupFilename(filename))
            throw new InvalidParameterException("Filename is malformed. " + this.filename);

        //file structure is DateInMillis_RecipeCount_ImageCount
        //Remove .zip
        String aux = filename.substring(0, filename.indexOf(".zip"));
        String[] parts = aux.split("_");

        if(parts.length != 3)
            throw new InvalidParameterException("Filename is malformed. " + this.filename);

        this.date.setTimeInMillis(Long.parseLong(parts[0]));
        this.recipeCount = Integer.parseInt(parts[1]);
        this.imageCount = Integer.parseInt(parts[2]);
    }

    public String getFilename() {
        return filename;
    }

    public String getReadableDate() {
        return DateUtils.getRelativeTimeSpanString(date.getTimeInMillis()).toString();
    }

    /**
     * @return string of type 'Local backup'
     */
    public String getBackupDetailStr(){
        return String.format(Locale.getDefault(), ChefBuddyApplication.getContext().getString(R.string.backup_info_detail_string), backupType.getFriendlyName());
    }

    /**
     * @return string of type 'Done 30 minutes ago'
     */
    public String getBackupDetailStr2(){
        return String.format(Locale.getDefault(), ChefBuddyApplication.getContext().getString(R.string.backup_info_detail_string_2), getReadableDate());
    }

    /**
     * @return string of type 'Backed up 10 recipes, 30 images'
     */
    public String getBackupDetailStr3(){
        return String.format(Locale.getDefault(), ChefBuddyApplication.getContext().getString(R.string.backup_info_detail_string_3), recipeCount, imageCount);
    }



    public Calendar getDate() {
        return date;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    public BackupType getBackupType() {
        return backupType;
    }

    public boolean isSelected() { return selected; }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
