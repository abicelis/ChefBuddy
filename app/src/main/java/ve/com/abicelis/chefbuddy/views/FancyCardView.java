package ve.com.abicelis.chefbuddy.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.util.DimensionUtil;

/**
 * Created by abicelis on 14/7/2017.
 */

public class FancyCardView extends CardView {

    public FancyCardView(Context context) {
        super(context);
        init(context);
    }
    public FancyCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public FancyCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setRadius(DimensionUtil.convertDpToPixel(6));
        setCardElevation(0);
        int contentPadding = (int)DimensionUtil.convertDpToPixel(12);
        setContentPadding(contentPadding, contentPadding, contentPadding, contentPadding);
    }


}
