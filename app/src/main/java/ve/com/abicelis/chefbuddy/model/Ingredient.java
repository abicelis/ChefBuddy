package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;

import ve.com.abicelis.chefbuddy.enums.Measurement;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Ingredient {
    private String amount;
    private Measurement measurement;
    private String name;

//    public Ingredient(@NonNull String name) {
//        name = name;
//    }

    public Ingredient(@NonNull String amount, @NonNull Measurement measurement, @NonNull String name) {
        this.amount = amount;
        this.measurement = measurement;
        this.name = name;
    }


//    public void setAmount(@NonNull String amount, @NonNull Measurement measurement) {
//        amount = amount;
//        measurement = measurement;
//    }


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

}
