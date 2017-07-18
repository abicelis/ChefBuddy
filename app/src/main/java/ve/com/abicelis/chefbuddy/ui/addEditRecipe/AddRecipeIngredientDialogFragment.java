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

    //DATA
    private List<String> mMeasurementList;
    private Measurement mSelectedMeasurement;
    private List<Ingredient> mIngredients;
    private Ingredient mSelectedIngredient;
    @Inject
    AddRecipeIngredientPresenter presenter;

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
        presenter.attachView(this);

        mMeasurementList = Measurement.getFriendlyNames();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final String passedVal = getArguments().getString("passedVal");


        View dialogView =  inflater.inflate(R.layout.dialog_add_recipe_ingredient, container);
        ButterKnife.bind(this, dialogView);
        presenter.getIngredients();

        mAmount.setMaxLenght(Constants.MAX_LENGHT_RECIPE_INGREDIENT_AMOUNT);

        mMeasurement.setItems(mMeasurementList);
        mMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedMeasurement = Measurement.values()[position];
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
                if(mAmount.getText().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.dialog_add_recipe_ingredient_error_amount, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mIngredient.getText().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.dialog_add_recipe_ingredient_error_ingredient, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mSelectedIngredient == null || !mSelectedIngredient.getName().equals(mIngredient.getText()) ) //Ingredient is new
                    mListener.onRecipeIngredientAdded(new RecipeIngredient(mAmount.getText(), mSelectedMeasurement, new Ingredient(mIngredient.getText())));
                else
                    mListener.onRecipeIngredientAdded(new RecipeIngredient(mAmount.getText(), mSelectedMeasurement, mSelectedIngredient));

                dismiss();
                break;
        }
    }


    /* AddRecipeIngredientView interface implementation */
    @Override
    public void populateIngredientSpinner(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        List<String> ingredientsStrList = new ArrayList<>();
        for (Ingredient i : mIngredients)
            ingredientsStrList.add(i.getName());
        mIngredient.setItems(ingredientsStrList);

        mIngredient.setMaxLenght(Constants.MAX_LENGHT_RECIPE_INGREDIENT_INGREDIENT);

        mIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                mSelectedIngredient = mIngredients.get(pos);
            }
        });

    }

    @Override
    public void showErrorMessage(Message message) {
        //TODO: Error!!
    }



    public void setListener(AddRecipeIngredientListener listener) {
        mListener = listener;
    }


    public interface AddRecipeIngredientListener {
        void onRecipeIngredientAdded(RecipeIngredient recipeIngredient);
    }
}
