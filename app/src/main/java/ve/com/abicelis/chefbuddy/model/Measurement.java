package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.StringRes;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

/**
 * Created by abicelis on 28/6/2017.
 */

public enum Measurement {
    NONE(               R.string.measurement_none,                  R.string.measurement_none),
    INCH(               R.string.measurement_inch,              R.string.measurement_inch_abbr),
    TABLESPOON(         R.string.measurement_tablespoon,        R.string.measurement_tablespoon_abbr),
    TEASPOON(           R.string.measurement_teaspoon,          R.string.measurement_teaspoon_abbr),
    CUP(                R.string.measurement_cup,               R.string.measurement_cup_abbr),
    PINT(               R.string.measurement_pint,              R.string.measurement_pint_abbr),
    QUART(              R.string.measurement_quart,             R.string.measurement_quart_abbr),
    GALLON(             R.string.measurement_gallon,            R.string.measurement_gallon_abbr),
    OUNCE(              R.string.measurement_ounce,             R.string.measurement_ounce_abbr),
    FLUID_OUNCE(        R.string.measurement_fluid_ounce,       R.string.measurement_fluid_ounce_abbr),

    CENTIMETER(         R.string.measurement_centimeter,        R.string.measurement_centimeter_abbr),
    METER(              R.string.measurement_meter,             R.string.measurement_meter_abbr),

    CUBIC_CENTIMITER(   R.string.measurement_cubic_centimeter,  R.string.measurement_cubic_centimeter_abbr),
    MILILITER(          R.string.measurement_mililiter,         R.string.measurement_mililiter_abbr),
    LITER(              R.string.measurement_liter,             R.string.measurement_liter_abbr),

    GRAM(               R.string.measurement_gram,              R.string.measurement_gram_abbr),
    KILOGRAM(           R.string.measurement_kilogram,          R.string.measurement_kilogram_abbr),
    ;

    private @StringRes int friendlyName;
    private @StringRes int abbreviation;

    Measurement(@StringRes int friendlyName,  @StringRes int abbreviation){
        this.friendlyName = friendlyName;
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return ChefBuddyApplication.getContext().getString(abbreviation);
    }

    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }
}
