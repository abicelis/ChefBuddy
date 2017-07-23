package ve.com.abicelis.chefbuddy.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.RecipeSource;


/**
 * Created by abice on 16/3/2017.
 */

public class SelectRecipeSourceDialogFragment extends DialogFragment implements View.OnClickListener {

    //DATA
    private RecipeSourceSelectedListener mListener;

    //UI
    @BindView(R.id.dialog_select_recipe_source_manual)
    FloatingActionButton mManual;

    @BindView(R.id.dialog_select_recipe_source_online)
    FloatingActionButton mOnline;

    @BindView(R.id.dialog_select_recipe_source_cancel)
    Button mCancel;


    public SelectRecipeSourceDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SelectRecipeSourceDialogFragment newInstance() {
        SelectRecipeSourceDialogFragment frag = new SelectRecipeSourceDialogFragment();
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
        View dialogView =  inflater.inflate(R.layout.dialog_select_recipe_source, container);
        ButterKnife.bind(this, dialogView);


        mManual.setOnClickListener(this);
        mOnline.setOnClickListener(this);
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
            case R.id.dialog_select_recipe_source_manual:
                mListener.onSourceSelected(RecipeSource.DATABASE);
                break;

            case R.id.dialog_select_recipe_source_online:
                mListener.onSourceSelected(RecipeSource.ONLINE);
                break;

            case R.id.dialog_select_recipe_source_cancel:
                break;
        }
        dismiss();
    }


    public void setListener(RecipeSourceSelectedListener listener) {
        mListener = listener;
    }


    public interface RecipeSourceSelectedListener {
        void onSourceSelected(RecipeSource recipeSource);
    }
}
