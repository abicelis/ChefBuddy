package ve.com.abicelis.chefbuddy.ui.main;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.database.ChefBuddyDAO;
import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Recipe;

public class MainActivity extends AppCompatActivity {

    //DATA
    @Inject
    ChefBuddyDAO mDao;
    private static final String TAG = MainActivity.class.getSimpleName();

    //UI
    @BindView(R.id.label)
    TextView label;

    @BindView(R.id.label2)
    TextView label2;

    @BindView(R.id.wheelview)
    WheelView wheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ChefBuddyApplication)getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);

        try {
            for (Recipe recipe : mDao.getRecipes()) {
                Log.d(TAG, "Found recipe in DB:\n\r" + recipe.toString() + "\n\r\n\r");
            }
        } catch (CouldNotGetDataException e) {
            Log.d(TAG, "Error getting recipes from DB");
        }


        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                return ContextCompat.getDrawable(MainActivity.this, android.R.drawable.alert_dark_frame);
            }

            @Override
            public int getCount() {
                //return the count
                return 10;
            }
        });

        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent,  Drawable itemDrawable, int position) {
                label.setText("Selected: " + position);
                //the adapter position that is closest to the selection angle and it's drawable
            }
        });

        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                Toast.makeText(MainActivity.this, "Click! Pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        wheelView.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
            @Override
            public void onWheelAngleChange(float angle) {
                label2.setText("Angle: " + angle);
            }
        });

    }
}
