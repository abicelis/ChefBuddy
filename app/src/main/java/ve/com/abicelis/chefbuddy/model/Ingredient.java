package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;

import java.util.Locale;

import ve.com.abicelis.chefbuddy.enums.Measurement;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Ingredient {

    private int id;
    private String amount;
    private Measurement measurement;
    private String name;

//    public Ingredient(@NonNull String name) {
//        name = name;
//    }

    public Ingredient(int id, @NonNull String amount, @NonNull Measurement measurement, @NonNull String name) {
        this.id = id;
        this.amount = amount;
        this.measurement = measurement;
        this.name = name;
    }


//    public void setAmount(@NonNull String amount, @NonNull Measurement measurement) {
//        amount = amount;
//        measurement = measurement;
//    }


    public int getId() {
        return id;
    }
    public String getAmount() {
        return amount;
    }
    public Measurement getMeasurement() {
        return measurement;
    }
    public String getName() {
        return name;
    }



    public String getAmountString() {
        if (measurement == null || amount == null)
            return "";
        else
            return amount + " " + measurement.getAbbreviation();

    }


    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s %2$s %3$s (ID=%4$d)", amount, measurement.getAbbreviation(), name, id);
    }
}
