package ve.com.abicelis.chefbuddy.views;

import android.content.Context;
import android.content.res.TypedArray;
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
        init(context, null);
    }
    public FancyTitleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public FancyTitleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        //Get/apply custom xml configs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.fancy_title_text_view);
        boolean useDefaultPadding = a.getBoolean(R.styleable.fancy_title_text_view_default_padding, true);
        a.recycle();


        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        this.setTextColor(ContextCompat.getColor(context, R.color.primary));
        if(useDefaultPadding)
            this.setPadding((int)DimensionUtil.convertDpToPixel(24), (int)DimensionUtil.convertDpToPixel(24), 0, 0);

    }


}
