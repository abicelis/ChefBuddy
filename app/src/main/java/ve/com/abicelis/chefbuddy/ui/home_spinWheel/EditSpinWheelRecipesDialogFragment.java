package ve.com.abicelis.chefbuddy.ui.home_spinWheel;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.CheckedRecipe;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter.EditSpinWheelRecipesPresenter;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.view.EditSpinWheelRecipesView;

/**
 * Created by abice on 16/3/2017.
 */

public class EditSpinWheelRecipesDialogFragment extends DialogFragment implements View.OnClickListener, EditSpinWheelRecipesView {

    @Inject
    EditSpinWheelRecipesPresenter mPresenter;

    //UI
    private WheelRecipesEditedListener mListener;

    @BindView(R.id.dialog_edit_wheel_recipes_subtitle)
    TextView mSubtitle;

    @BindView(R.id.dialog_edit_wheel_recipes_recycler)
    RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private EditSpinWheelRecipeListAdapter mAdapter;

    @BindView(R.id.dialog_edit_wheel_recipes_cancel)
    Button mCancel;

    @BindView(R.id.dialog_edit_wheel_recipes_set)
    Button mSet;



    public EditSpinWheelRecipesDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditSpinWheelRecipesDialogFragment newInstance() {
        EditSpinWheelRecipesDialogFragment frag = new EditSpinWheelRecipesDialogFragment();
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

        View dialogView =  inflater.inflate(R.layout.dialog_edit_spin_wheel_recipes, container);
        ButterKnife.bind(this, dialogView);


        //Set subtitle
        mSubtitle.setText(
                String.format(Locale.getDefault(),
                        getResources().getString(R.string.dialog_edit_spin_wheel_recipes_subtitle),
                        Constants.MIN_SPIN_WHEEL_RECIPE_AMOUNT,
                        Constants.MAX_SPIN_WHEEL_RECIPE_AMOUNT ));

        //Set listeners for buttons
        mSet.setOnClickListener(this);
        mCancel.setOnClickListener(this);


        //Init recycler
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new EditSpinWheelRecipeListAdapter(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
        //mRecycler.setNestedScrollingEnabled(false);

        mPresenter.getRecipes();
        return dialogView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.dialog_edit_wheel_recipes_cancel:
                dismiss();
                break;

            case R.id.dialog_edit_wheel_recipes_set:
                mPresenter.setWheelRecipes(mAdapter.getItems());
                break;
        }
    }


    /* EditSpinWheelRecipesView interface implementation */
    @Override
    public void showRecipes(List<CheckedRecipe> checkedRecipes) {
        mAdapter.getItems().clear();
        mAdapter.getItems().addAll(checkedRecipes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void wheelRecipesSetSoDismiss() {
        mListener.onWheelRecipesEdited();
        dismiss();
    }

    @Override
    public void showErrorMessage(Message message) {
        Toast.makeText(getActivity(), message.getFriendlyName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

    }


    /* Listener to relay data to calling class */
    public void setListener(WheelRecipesEditedListener listener) {
        mListener = listener;
    }

    public interface WheelRecipesEditedListener {
        void onWheelRecipesEdited();
    }
}
