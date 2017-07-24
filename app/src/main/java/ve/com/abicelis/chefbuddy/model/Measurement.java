package ve.com.abicelis.chefbuddy.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

/**
 * Created by abicelis on 28/6/2017.
 */

public enum Measurement {
    NONE(               R.string.measurement_none,              R.string.measurement_none,                      R.drawable.ic_fab_check),
    INCH(               R.string.measurement_inch,              R.string.measurement_inch_abbr,                 R.drawable.ic_measurement_inch),
    TABLESPOON(         R.string.measurement_tablespoon,        R.string.measurement_tablespoon_abbr,           R.drawable.ic_measurement_tablespoon),
    TEASPOON(           R.string.measurement_teaspoon,          R.string.measurement_teaspoon_abbr,             R.drawable.ic_measurement_teaspoon),
    CUP(                R.string.measurement_cup,               R.string.measurement_cup_abbr,                  R.drawable.ic_measurement_cup),
    PINT(               R.string.measurement_pint,              R.string.measurement_pint_abbr,                 R.drawable.ic_measurement_pint),
    QUART(              R.string.measurement_quart,             R.string.measurement_quart_abbr,                R.drawable.ic_measurement_quart),
    GALLON(             R.string.measurement_gallon,            R.string.measurement_gallon_abbr,               R.drawable.ic_measurement_gallon),
    POUND(              R.string.measurement_pound,             R.string.measurement_pound_abbr,                R.drawable.ic_measurement_pound),
    OUNCE(              R.string.measurement_ounce,             R.string.measurement_ounce_abbr,                R.drawable.ic_measurement_ounce),
    FLUID_OUNCE(        R.string.measurement_fluid_ounce,       R.string.measurement_fluid_ounce_abbr,          R.drawable.ic_measurement_fluid_ounce),

    CENTIMETER(         R.string.measurement_centimeter,        R.string.measurement_centimeter_abbr,           R.drawable.ic_measurement_centimeter),
    METER(              R.string.measurement_meter,             R.string.measurement_meter_abbr,                R.drawable.ic_measurement_meter),

    CUBIC_CENTIMITER(   R.string.measurement_cubic_centimeter,  R.string.measurement_cubic_centimeter_abbr,     R.drawable.ic_measurement_cubic_centimeter),
    MILILITER(          R.string.measurement_mililiter,         R.string.measurement_mililiter_abbr,            R.drawable.ic_measurement_mililiter),
    LITER(              R.string.measurement_liter,             R.string.measurement_liter_abbr,                R.drawable.ic_measurement_liter),

    GRAM(               R.string.measurement_gram,              R.string.measurement_gram_abbr,                 R.drawable.ic_measurement_gram),
    KILOGRAM(           R.string.measurement_kilogram,          R.string.measurement_kilogram_abbr,             R.drawable.ic_measurement_kg),

    CAN(                R.string.measurement_can,               R.string.measurement_can_abbr,                  R.drawable.ic_measurement_can),
    NUT(                R.string.measurement_nut,               R.string.measurement_nut_abbr,                  R.drawable.ic_measurement_nut),
    CLOVE(              R.string.measurement_clove,             R.string.measurement_clove_abbr,                R.drawable.ic_measurement_clove),
    JAR(                R.string.measurement_jar,               R.string.measurement_jar_abbr,                  R.drawable.ic_measurement_jar),
    PACKAGE(            R.string.measurement_package,           R.string.measurement_package_abbr,              R.drawable.ic_measurement_box),
    BOX(                R.string.measurement_box,               R.string.measurement_box_abbr,                  R.drawable.ic_measurement_box),
    SHEET(              R.string.measurement_sheet,             R.string.measurement_sheet_abbr,                R.drawable.ic_measurement_sheet),
    ;

    private @StringRes int friendlyName;
    private @StringRes int abbreviation;
    private @DrawableRes int icon;

    Measurement(@StringRes int friendlyName,  @StringRes int abbreviation, @DrawableRes int icon){
        this.friendlyName = friendlyName;
        this.abbreviation = abbreviation;
        this.icon = icon;
    }

    public String getAbbreviation() {
        return ChefBuddyApplication.getContext().getString(abbreviation);
    }

    public String getFriendlyName() {
        return ChefBuddyApplication.getContext().getString(friendlyName);
    }

    public @Nullable Drawable getIcon() {
        return ContextCompat.getDrawable(ChefBuddyApplication.getContext(), icon);
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (Measurement x : values()) {
            friendlyValues.add(ChefBuddyApplication.getContext().getString(x.friendlyName));
        }
        return friendlyValues;
    }
}
