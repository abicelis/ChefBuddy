package ve.com.abicelis.chefbuddy.ui.home_history;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.ui.home_history.presenter.EditDailyRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.home_history.view.EditDailyRecipeView;
import ve.com.abicelis.chefbuddy.views.FancySpinner;


/**
 * Created by abice on 16/3/2017.
 */

public class EditDailyRecipeDialogFragment extends DialogFragment implements View.OnClickListener, EditDailyRecipeView {

    //DATA
    private DailyRecipeUpdatedListener mListener;
    @Inject
    EditDailyRecipePresenter mPresenter;



    @BindView(R.id.dialog_edit_daily_recipe_recipe_spinner)
    FancySpinner mRecipeSpinner;

    @BindView(R.id.dialog_edit_daily_recipe_set)
    Button mSet;

    @BindView(R.id.dialog_edit_daily_recipe_cancel)
    Button mCancel;


    public EditDailyRecipeDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditDailyRecipeDialogFragment newInstance(@NonNull Calendar date, long recipeId) {
        EditDailyRecipeDialogFragment frag = new EditDailyRecipeDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        args.putLong("recipeId", recipeId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView =  inflater.inflate(R.layout.dialog_edit_daily_recipe, container);
        ButterKnife.bind(this, dialogView);
        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);


        mPresenter.attachView(this);
        mPresenter.setDailyRecipeData((Calendar)getArguments().getSerializable("date"), getArguments().getLong("recipeId"));
        mPresenter.getRecipes();

        mSet.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        return dialogView;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.dialog_edit_daily_recipe_set:
                mPresenter.saveAndDismiss();
                break;

            case R.id.dialog_edit_daily_recipe_cancel:
                dismiss();
                break;
        }
    }


    public void setListener(DailyRecipeUpdatedListener listener) {
        mListener = listener;
    }



    /* EditDailyRecipe implementation */

    @Override
    public void populateRecipeSpinner(List<Recipe> recipes, int selection) {
        List<String> recipesStrList = new ArrayList<>();
        for (Recipe recipe : recipes)
            recipesStrList.add(recipe.getName());

        //Change first item to NONE
        recipesStrList.set(0, getString(R.string.dialog_edit_daily_recipe_none));

        mRecipeSpinner.setItems(recipesStrList);

        mRecipeSpinner.setSelection(selection);

        mRecipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setRecipe(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    @Override
    public void recipeSavedSoDismiss(Calendar date) {
        mListener.onDailyRecipeUpdated(date);
        dismiss();
    }

    @Override
    public void showErrorMessage(Message message) {
        Toast.makeText(getActivity(), message.getFriendlyName(), Toast.LENGTH_SHORT).show();
    }


    public interface DailyRecipeUpdatedListener {
        void onDailyRecipeUpdated(Calendar date);
    }
}
