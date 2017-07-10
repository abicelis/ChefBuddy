package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by abicelis on 10/7/2017.
 */

public class Ingredient {

    private int id;
    private String name;

    /*
     * When creating a new ingredient, no ID
     */
    public Ingredient(@NonNull String name) {
        this.id = -1;
        this.name = name;
    }

    /*
     * When fetching ingredient from DB
     */
    public Ingredient(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }



    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s (ID=%2$d)", name, id);
    }
}
