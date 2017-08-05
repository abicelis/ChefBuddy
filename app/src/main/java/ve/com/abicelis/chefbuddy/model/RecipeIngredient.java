package ve.com.abicelis.chefbuddy.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.app.Constants;

/**
 * Created by abicelis on 28/6/2017.
 */

public class RecipeIngredient implements Serializable {

    private long id;
    private String amount;
    private Measurement measurement;
    private Ingredient ingredient;

    /*
     * When creating a new ingredient, no ID
     */
    public RecipeIngredient(@NonNull String amount, @NonNull Measurement measurement, @NonNull Ingredient ingredient) {
        this.id = -1;
        setAmount(amount);
        this.measurement = measurement;
        this.ingredient = ingredient;
    }

    /*
     * When fetching ingredient from DB
     */
    public RecipeIngredient(long id, @NonNull String amount, @NonNull Measurement measurement, @NonNull Ingredient ingredient) {
        this.id = id;
        setAmount(amount);
        this.measurement = measurement;
        this.ingredient = ingredient;
    }


    public long getId() {
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
        if(amount.isEmpty() && measurement != Measurement.NONE)
            return "";
        return amount + " " + (measurement != Measurement.NONE ? measurement.getAbbreviation() : "");
    }

    public void setAmount(String amount) {
        if(amount.length() > Constants.MAX_LENGTH_RECIPE_INGREDIENT_AMOUNT)
            this.amount = amount.substring(0, Constants.MAX_LENGTH_RECIPE_INGREDIENT_AMOUNT);
        else
            this.amount = amount;
    }
    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s%2$s %3$s (ID=%4$d)", amount, measurement.getAbbreviation(), ingredient.getName(), id);
    }
}
