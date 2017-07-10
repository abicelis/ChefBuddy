package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by abicelis on 28/6/2017.
 */

public class RecipeIngredient {

    private int id;
    private String amount;
    private Measurement measurement;
    private Ingredient ingredient;

    /*
     * When creating a new ingredient, no ID
     */
    public RecipeIngredient(@NonNull String amount, @NonNull Measurement measurement, @NonNull Ingredient ingredient) {
        this.id = -1;
        this.amount = amount;
        this.measurement = measurement;
        this.ingredient = ingredient;
    }

    /*
     * When fetching ingredient from DB
     */
    public RecipeIngredient(int id, @NonNull String amount, @NonNull Measurement measurement, @NonNull Ingredient ingredient) {
        this.id = id;
        this.amount = amount;
        this.measurement = measurement;
        this.ingredient = ingredient;
    }


    public int getId() {
        return id;
    }
    public String getAmount() {
        return amount;
    }
    public Measurement getMeasurement() {
        return measurement;
    }
    public Ingredient getIngredient() {
        return ingredient;
    }

    public String getAmountString() {
        if (measurement == null || amount == null)
            return "";
        else
            return amount + " " + measurement.getAbbreviation();

    }


    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s%2$s %3$s (ID=%4$d)", amount, measurement.getAbbreviation(), ingredient.getName(), id);
    }
}
