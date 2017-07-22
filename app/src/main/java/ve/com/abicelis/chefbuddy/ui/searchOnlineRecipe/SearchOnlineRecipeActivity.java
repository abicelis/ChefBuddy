package ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.app.Message;
import ve.com.abicelis.chefbuddy.model.edamam.EdamamResponse;
import ve.com.abicelis.chefbuddy.network.EdamamApi;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.presenter.SearchOnlineRecipePresenter;
import ve.com.abicelis.chefbuddy.ui.searchOnlineRecipe.view.SearchOnlineRecipeView;
import ve.com.abicelis.chefbuddy.util.SnackbarUtil;

/**
 * Created by abicelis on 21/7/2017.
 */

public class SearchOnlineRecipeActivity extends AppCompatActivity implements SearchOnlineRecipeView {

    @Inject
    SearchOnlineRecipePresenter mPresenter;


    @BindView(R.id.activity_search_online_recipe_container)
    LinearLayout mContainer;

    @BindView(R.id.activity_search_online_recipe_text)
    TextView mText;

    @BindView(R.id.activity_search_online_recipe_loading)
    TextView mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_online_recipe);
        ButterKnife.bind(this);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        mPresenter.attachView(this);

        mPresenter.getQueryRawResults("chicken");
    }

    @Override
    public void showLoading() {
        mLoading.setText("Loading");
    }

    @Override
    public void hideLoading() {
        mLoading.setText("Done Loading");

    }

    @Override
    public void showQueryRawResults(Response<EdamamResponse> response) {
        mText.setText("Results");
    }

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }
}
