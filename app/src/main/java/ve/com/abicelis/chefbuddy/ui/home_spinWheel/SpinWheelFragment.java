package ve.com.abicelis.chefbuddy.ui.home_spinWheel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.presenter.SpinWheelPresenter;
import ve.com.abicelis.chefbuddy.ui.home_spinWheel.view.SpinWheelView;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeDetailActivity;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.ImageUtil;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;
import ve.com.abicelis.chefbuddy.views.FancyCardView;
import ve.com.abicelis.prizewheellib.PrizeWheelView;
import ve.com.abicelis.prizewheellib.WheelEventsListener;
import ve.com.abicelis.prizewheellib.model.WheelBitmapSection;
import ve.com.abicelis.prizewheellib.model.WheelDrawableSection;
import ve.com.abicelis.prizewheellib.model.WheelSection;

/**
 * Created by abicelis on 9/7/2017.
 */

public class SpinWheelFragment extends Fragment implements SpinWheelView {


    @Inject
    SpinWheelPresenter mPresenter;

    @BindView(R.id.fragment_spinwheel_container)
    RelativeLayout mContainer;

    @BindView(R.id.fragment_spinwheel_wheel)
    PrizeWheelView mWheelView;

    @BindView(R.id.fragment_spinwheel_edit)
    ImageView mEditWheel;

    @BindView(R.id.fragment_spinwheel_tutorial_container)
    FancyCardView mTutorialContainer;

    @BindView(R.id.recipe_card_container)
    FancyCardView mRecipeContainer;

    @BindView(R.id.recipe_card_image)
    ImageView mRecipeImage;

    @BindView(R.id.recipe_card_name)
    TextView mRecipeName;

    @BindView(R.id.recipe_card_ingredients)
    TextView mRecipeIngredients;

    @BindView(R.id.fragment_spinwheel_no_items_container)
    RelativeLayout mNoItemsContainer;

    @BindView(R.id.fragment_spinwheel_no_items_text)
    TextView mNoItemsText;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spinwheel, container, false);
        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);
        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);

        //Set edit button listener
        mEditWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                EditSpinWheelRecipesDialogFragment dialog = EditSpinWheelRecipesDialogFragment.newInstance();
                dialog.setListener(new EditSpinWheelRecipesDialogFragment.WheelRecipesEditedListener() {
                    @Override
                    public void onWheelRecipesEdited() {
                        refreshWheel();
                    }
                });
                dialog.show(fm, "EditSpinWheelRecipesDialogFragment");
            }
        });


        mPresenter.attachView(this);
        refreshWheel();

        return view;
    }

    public void refreshWheel() {
        mPresenter.getWheelRecipes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Hide search menu item
        menu.findItem(R.id.menu_home_search).setVisible(false);
    }

    @Override
    public void refreshView(final List<Recipe> recipes) {
        mNoItemsContainer.setVisibility(View.INVISIBLE);
        mEditWheel.setVisibility(View.VISIBLE);
        mWheelView.setVisibility(View.INVISIBLE);

        //Reset fancyCards
        TransitionManager.beginDelayedTransition(mContainer, new Slide(Gravity.BOTTOM));
        mRecipeContainer.setVisibility(View.INVISIBLE);
        mTutorialContainer.setVisibility(View.VISIBLE);


        //Handle the loading of the wheel in an asyncTask since this is processor-intensive
        new WheelLoaderTask().execute(recipes);
    }

    @Override
    public void notEnoughRecipesAvailable() {
        //Hide everything
        mWheelView.setVisibility(View.INVISIBLE);
        mEditWheel.setVisibility(View.INVISIBLE);
        mRecipeContainer.setVisibility(View.INVISIBLE);
        mTutorialContainer.setVisibility(View.INVISIBLE);

        //Show noItemsContainer
        mNoItemsText.setText(String.format(Locale.getDefault(), getString(R.string.fragment_spinwheel_no_items), Constants.MIN_SPIN_WHEEL_RECIPE_AMOUNT));
        mNoItemsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateBottomRecipe(final Recipe recipe) {
        TransitionManager.beginDelayedTransition(mContainer, new Slide(Gravity.BOTTOM));

        Picasso.with(getActivity())
                .load(new File(FileUtil.getImageFilesDir(), recipe.getFeaturedImage()))
                .error(R.drawable.default_recipe_image)
                .fit().centerCrop()
                .into(mRecipeImage);

        mRecipeName.setText(recipe.getName());
        mRecipeIngredients.setText(recipe.getSimpleIngredientsString());
        mRecipeContainer.setVisibility(View.VISIBLE);

        mRecipeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewRecipeDetailIntent = new Intent(getActivity(), RecipeDetailActivity.class);
                viewRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE, recipe);
                viewRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE, RecipeSource.DATABASE);
                getActivity().startActivity(viewRecipeDetailIntent);
            }
        });

    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }





    private class WheelLoaderTask extends AsyncTask<List<Recipe>, String, List<WheelSection>> {

        @Override
        protected List<WheelSection> doInBackground(List<Recipe>... params) {

            //Init WheelSection list
            final List<WheelSection> wheelSections = new ArrayList<>();

            for (Recipe r : params[0]) {

                if(r.getImages().size() > 0) {
                    Bitmap image = ImageUtil.getBitmap(FileUtil.getImageFilesDir(), r.getImages().get(0));
                    wheelSections.add(new WheelBitmapSection(image));
                }
                else
                    wheelSections.add(new WheelDrawableSection(R.drawable.default_recipe_image));
            }
            return wheelSections;
        }

        @Override
        protected void onPostExecute(List<WheelSection> wheelSections) {
            super.onPostExecute(wheelSections);


            //Init mWheelView and set parameters
            mWheelView.setWheelSections(wheelSections);

            mWheelView.setWheelBorderLineColor(R.color.icons);
            mWheelView.setWheelBorderLineThickness(2);

            mWheelView.setWheelSeparatorLineColor(R.color.icons);
            mWheelView.setWheelSeparatorLineThickness(2);

            //Set onSettled listener
            mWheelView.setWheelEventsListener(new WheelEventsListener() {
                @Override
                public void onWheelStopped() {}

                @Override
                public void onWheelFlung() {
                    //Hide fancyCards
                    TransitionManager.beginDelayedTransition(mContainer, new Slide(Gravity.BOTTOM));
                    mRecipeContainer.setVisibility(View.INVISIBLE);
                    mTutorialContainer.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onWheelSettled(int sectionIndex, double angle) {
                    mPresenter.wheelSettledAtPosition(sectionIndex);
                }
            });

            //Finally, generate wheel background
            mWheelView.generateWheel();

            TransitionManager.beginDelayedTransition(mContainer, new Fade());
            mWheelView.setVisibility(View.VISIBLE);

        }


    }


}
