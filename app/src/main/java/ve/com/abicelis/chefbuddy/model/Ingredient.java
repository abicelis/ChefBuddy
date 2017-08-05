package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.app.Constants;

/**
 * Created by abicelis on 10/7/2017.
 */

public class Ingredient implements Serializable {

    private long id;
    private String name;

    /*
     * When creating a new ingredient, no ID
     */
    public Ingredient(@NonNull String name) {
        this.id = -1;
        setName(name);
    }

    /*
     * When fetching ingredient from DB
     */
    public Ingredient(long id, @NonNull String name) {
        this.id = id;
        setName(name);
    }



    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        if(name.length() > Constants.MAX_LENGTH_INGREDIENT_NAME)
            this.name = name.substring(0, Constants.MAX_LENGTH_INGREDIENT_NAME);
        else
            this.name = name;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s (ID=%2$d)", name, id);
    }
}
