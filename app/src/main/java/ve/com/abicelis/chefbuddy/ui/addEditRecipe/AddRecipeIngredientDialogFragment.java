package ve.com.abicelis.chefbuddy.ui.addEditRecipe;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.presenter.AddRecipeIngredientPresenter;
import ve.com.abicelis.chefbuddy.ui.addEditRecipe.view.AddRecipeIngredientView;
import ve.com.abicelis.chefbuddy.views.FancyAutoCompleteTextView;
import ve.com.abicelis.chefbuddy.views.FancyEditText;
import ve.com.abicelis.chefbuddy.views.FancySpinner;

/**
 * Created by abice on 16/3/2017.
 */

public class AddRecipeIngredientDialogFragment extends DialogFragment implements View.OnClickListener, AddRecipeIngredientView {

    @Inject
    AddRecipeIngredientPresenter mPresenter;

    //UI
    private AddRecipeIngredientListener mListener;

    @BindView(R.id.dialog_add_recipe_ingredient_amount)
    FancyEditText mAmount;

    @BindView(R.id.dialog_add_recipe_ingredient_measurement)
    FancySpinner mMeasurement;

    @BindView(R.id.dialog_add_recipe_ingredient_ingredient)
    FancyAutoCompleteTextView mIngredient;

    @BindView(R.id.dialog_add_recipe_ingredient_cancel)
    Button mCancel;

    @BindView(R.id.dialog_add_recipe_ingredient_ok)
    Button mOk;

    public AddRecipeIngredientDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddRecipeIngredientDialogFragment newInstance() {
        AddRecipeIngredientDialogFragment frag = new AddRecipeIngredientDialogFragment();
        //Bundle args = new Bundle();
        //args.putString("example", passedVal);
        //frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final String passedVal = getArguments().getString("passedVal");


        View dialogView =  inflater.inflate(R.layout.dialog_add_recipe_ingredient, container);
        ButterKnife.bind(this, dialogView);
        mPresenter.getIngredients();

        mAmount.setMaxLenght(Constants.MAX_LENGHT_RECIPE_INGREDIENT_AMOUNT);

        mMeasurement.setItems(Measurement.getFriendlyNames());
        mMeasurement.setSelection(1);
        mMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Measurement selectedMeasurement = Measurement.values()[position];
                mPresenter.setSelectedMeasurement(selectedMeasurement);
                mMeasurement.setIcon(selectedMeasurement.getIconRes());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialogView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.dialog_add_recipe_ingredient_cancel:
                dismiss();
                break;

            case R.id.dialog_add_recipe_ingredient_ok:
                mPresenter.checkRecipeIngredientValues(mAmount.getText(), mIngredient.getText());
                break;
        }
    }


    /* AddRecipeIngredientView interface implementation */
    @Override
    public void populateIngredientSpinner(List<Ingredient> ingredients) {
        List<String> ingredientsStrList = new ArrayList<>();
        for (Ingredient i : ingredients)
            ingredientsStrList.add(i.getName());
        mIngredient.setItems(ingredientsStrList);

        mIngredient.setMaxLenght(Constants.MAX_LENGHT_RECIPE_INGREDIENT_INGREDIENT);

        mIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                mPresenter.setSelectedIngredient(pos);
            }
        });

    }

    @Override
    public void showErrorMessage(Message message) {
        Toast.makeText(getActivity(), message.getFriendlyName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recipeIngredientSelectedSoDismiss(RecipeIngredient recipeIngredient) {
        mListener.onRecipeIngredientAdded(recipeIngredient);
        dismiss();
    }




    /* Listener to relay data to calling class */
    public void setListener(AddRecipeIngredientListener listener) {
        mListener = listener;
    }

    public interface AddRecipeIngredientListener {
        void onRecipeIngredientAdded(RecipeIngredient recipeIngredient);
    }
}
