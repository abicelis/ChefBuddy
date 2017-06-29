package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;

import ve.com.abicelis.chefbuddy.enums.Measurement;

/**
 * Created by abicelis on 28/6/2017.
 */

public class Ingredient {
    private String mAmount;
    private Measurement mMeasurement;
    private String mName;

//    public Ingredient(@NonNull String name) {
//        mName = name;
//    }

    public Ingredient(@NonNull String amount, @NonNull Measurement measurement, @NonNull String name) {
        mAmount = amount;
        mMeasurement = measurement;
        mName = name;
    }


//    public void setAmount(@NonNull String amount, @NonNull Measurement measurement) {
//        mAmount = amount;
//        mMeasurement = measurement;
//    }


    public String getAmount() {
        return mAmount;
    }
    public Measurement getMeasurement() {
        return mMeasurement;
    }
    public String getName() {
        return mName;
    }



    public String getAmountString() {
        if (mMeasurement == null || mAmount == null)
            return "";
        else
            return mAmount + " " + mMeasurement.getAbbreviation();

    }

}
