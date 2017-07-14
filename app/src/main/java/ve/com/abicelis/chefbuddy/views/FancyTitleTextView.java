package ve.com.abicelis.chefbuddy.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.util.DimensionUtil;

/**
 * Created by abicelis on 14/7/2017.
 */

public class FancyTitleTextView extends AppCompatTextView {

    public FancyTitleTextView(Context context) {
        super(context);
        init(context);
    }
    public FancyTitleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public FancyTitleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        this.setTextColor(ContextCompat.getColor(context, R.color.primary));
        this.setPadding((int)DimensionUtil.convertDpToPixel(24), (int)DimensionUtil.convertDpToPixel(24), 0, 0);

    }


}
