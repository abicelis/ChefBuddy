package ve.com.abicelis.chefbuddy.ui.home_history;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Constants;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.DailyRecipe;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeSource;
import ve.com.abicelis.chefbuddy.ui.home_history.presenter.HistoryPresenter;
import ve.com.abicelis.chefbuddy.ui.home_history.view.HistoryView;
import ve.com.abicelis.chefbuddy.ui.recipeDetail.RecipeDetailActivity;
import ve.com.abicelis.chefbuddy.util.FileUtil;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;
import ve.com.abicelis.chefbuddy.views.FancyCardView;

/**
 * Created by abicelis on 9/7/2017.
 */

public class HistoryFragment extends Fragment implements HistoryView {


    //CONST
    private static final int CARD_ANIMATION_DELAY = 200;

    //UI
    private RecipeDecorator mDecorator = new RecipeDecorator();

    @Inject
    HistoryPresenter mPresenter;


    @BindView(R.id.fragment_history_calendar)
    MaterialCalendarView mCalendar;


    @BindView(R.id.fragment_history_container)
    FrameLayout mContainer;

    //Recipe card
    @BindView(R.id.card_recipe_container)
    FancyCardView mRecipeContainer;

    @BindView(R.id.card_recipe_image)
    ImageView mRecipeImage;

    @BindView(R.id.card_recipe_name)
    TextView mRecipeName;

    @BindView(R.id.card_recipe_ingredients)
    TextView mRecipeIngredients;

    @BindView(R.id.card_recipe_edit)
    ImageView mRecipeEdit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ((ChefBuddyApplication)getActivity().getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this, view);

        //Edit button always visible
        mRecipeEdit.setVisibility(View.VISIBLE);

        //Init calendar
        mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mPresenter.daySelected(date);
            }
        });
        mCalendar.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        mCalendar.addDecorator(mDecorator);

        mPresenter.attachView(this);
        mPresenter.getDailyRecipes();

        return view;
    }

    public void refresh() {
        mCalendar.setSelectedDate(CalendarDay.today());
        mPresenter.daySelected(CalendarDay.today());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Hide search menu item
        menu.findItem(R.id.menu_home_search).setVisible(false);
    }



    /* HistoryView interface implementation */
    @Override
    public void setCalendarDecorations(List<DailyRecipe> dailyRecipes) {
        mDecorator.setDailyRecipes(dailyRecipes);
        mCalendar.invalidateDecorators();
    }

    @Override
    public void displayDailyRecipeDetails(final DailyRecipe dailyRecipe) {
        TransitionManager.beginDelayedTransition(mContainer, new Slide(Gravity.BOTTOM));
        mRecipeContainer.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(mContainer, new Slide(Gravity.BOTTOM));

                Picasso.with(getActivity())
                        .load(new File(FileUtil.getImageFilesDir(), dailyRecipe.getRecipe().getFeaturedImage()))
                        .error(R.drawable.default_recipe_image)
                        .fit().centerCrop()
                        .into(mRecipeImage);

                mRecipeName.setText(dailyRecipe.getRecipe().getName());
                mRecipeIngredients.setText(dailyRecipe.getRecipe().getSimpleIngredientsString());
                mRecipeContainer.setVisibility(View.VISIBLE);

                mRecipeContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent viewRecipeDetailIntent = new Intent(getActivity(), RecipeDetailActivity.class);
                        viewRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE, dailyRecipe.getRecipe());
                        viewRecipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_ACTIVITY_INTENT_EXTRA_RECIPE_SOURCE, RecipeSource.DATABASE);
                        getActivity().startActivity(viewRecipeDetailIntent);
                    }
                });
                mRecipeEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        EditDailyRecipeDialogFragment dialog = EditDailyRecipeDialogFragment.newInstance(dailyRecipe.getDate(), dailyRecipe.getRecipe().getId());
                        dialog.setListener(new EditDailyRecipeDialogFragment.DailyRecipeUpdatedListener() {
                            @Override
                            public void onDailyRecipeUpdated(Calendar date) {
                                mPresenter.dailyRecipeUpdated(date);
                            }
                        });
                        dialog.show(fm, "editDailyRecipeDialog");
                    }
                });
            }
        }, CARD_ANIMATION_DELAY);
    }

    @Override
    public void displayNoDailyRecipe(final Calendar date) {

        TransitionManager.beginDelayedTransition(mContainer, new Slide(Gravity.BOTTOM));
        mRecipeContainer.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(mContainer, new Slide(Gravity.BOTTOM));

                mRecipeImage.setImageResource(R.drawable.default_recipe_image);
                mRecipeName.setText(getString(R.string.fragment_history_no_daily_recipe_title));
                mRecipeIngredients.setText(getString(R.string.fragment_history_no_daily_recipe_subtitle));
                mRecipeContainer.setVisibility(View.VISIBLE);

                mRecipeContainer.setOnClickListener(null);

                mRecipeEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        EditDailyRecipeDialogFragment dialog = EditDailyRecipeDialogFragment.newInstance(date, -1);
                        dialog.setListener(new EditDailyRecipeDialogFragment.DailyRecipeUpdatedListener() {
                            @Override
                            public void onDailyRecipeUpdated(Calendar date) {
                                mPresenter.dailyRecipeUpdated(date);
                            }
                        });
                        dialog.show(fm, "editDailyRecipeDialog");
                    }
                });
            }
        }, CARD_ANIMATION_DELAY);
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }
}
