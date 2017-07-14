package ve.com.abicelis.chefbuddy.ui.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 14/7/2017.
 */

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Poop");
        getSupportActionBar().setLogo(R.drawable.hummus);

    }
}
