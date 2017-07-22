package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;

/**
 * Created by abicelis on 28/6/2017.
 */

public enum Servings {
    SERVINGS_1(1),
    SERVINGS_2(2),
    SERVINGS_3(3),
    SERVINGS_4(4),
    SERVINGS_5(5),
    SERVINGS_6(6),
    SERVINGS_7(7),
    SERVINGS_8(8),
    SERVINGS_9(9),
    SERVINGS_10(10),
    SERVINGS_11(11),
    SERVINGS_12(12),
    SERVINGS_13(13),
    SERVINGS_14(14),
    SERVINGS_15(15),
    SERVINGS_16(16),
    SERVINGS_17(17),
    SERVINGS_18(18),
    SERVINGS_19(19),
    SERVINGS_20(20),
    SERVINGS_30(30),
    SERVINGS_40(40),
    SERVINGS_50(50);

    private static final @StringRes int servingsSingular = R.string.servings_singular;
    private static final @StringRes int servingsPlural = R.string.servings_plural;

    private int value;

    Servings(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public String getFriendlyName() {
        return value + " " + ChefBuddyApplication.getContext().getString((value == 1 ? servingsSingular : servingsPlural));
    }


    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (Servings x : values()) {
            friendlyValues.add(x.value + " " + ChefBuddyApplication.getContext().getString((x.value == 1 ? servingsSingular : servingsPlural)));
        }
        return friendlyValues;
    }

    public static Servings getServingsForInt(int servingsInt) {
        if(servingsInt <= 1)
            return values()[0];
        if (servingsInt >= SERVINGS_50.getValue())
            return values()[values().length-1];         //last enum value

        for(int i = 1; i < values().length; i++) {
            Servings current = values()[i];
            Servings previous = values()[i-1];

            if(servingsInt >= previous.getValue() && servingsInt < current.getValue())
                return previous;
        }
        return values()[values().length-1];             //last enum value
    }
}
