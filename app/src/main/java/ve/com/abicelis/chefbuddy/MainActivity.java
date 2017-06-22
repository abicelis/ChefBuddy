package ve.com.abicelis.chefbuddy;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

public class MainActivity extends AppCompatActivity {


    TextView label;
    TextView label2;
    WheelView wheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wheelView = (WheelView) findViewById(R.id.wheelview);
        label = (TextView) findViewById(R.id.label);
        label2 = (TextView) findViewById(R.id.label2);

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
