package ve.com.abicelis.chefbuddy.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 14/7/2017.
 */

public class FancyEditText extends LinearLayout {

    @BindView(R.id.fancy_edit_text_view_icon)
    ImageView mIcon;

    @BindView(R.id.fancy_edit_text_view_edit_text)
    EditText mEditText;

    public FancyEditText(Context context) {
        super(context);
        init(context, null);
    }
    public FancyEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public FancyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fancy_edit_text, this);

        ButterKnife.bind(this);

        //Get/apply custom xml configs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.fancy_edit_text);
        int iconId = a.getResourceId(R.styleable.fancy_edit_text_icon, -1);
        int textId = a.getResourceId(R.styleable.fancy_edit_text_text, -1);
        int hintId = a.getResourceId(R.styleable.fancy_edit_text_hint, -1);
        int maxLines = a.getInt(R.styleable.fancy_edit_text_maxLines, -1);


        setIcon(iconId);
        setText(textId);
        setHint(hintId);
        setMaxLines(maxLines);
        a.recycle();


        this.setBackgroundResource(R.drawable.white_round_edges_background);
        this.setGravity(Gravity.CENTER_VERTICAL);
    }


    public void setIcon(@DrawableRes int iconRes) {
        if (iconRes != -1) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageResource(iconRes);
        } else {
            mIcon.setVisibility(View.GONE);
        }
    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }
    public void setHint(@StringRes int hintId) {
        if (hintId != -1)
            mEditText.setHint(hintId);
    }

    public void setMaxLenght(int lenght) {
        mEditText.setFilters( new InputFilter[] { new InputFilter.LengthFilter(lenght) } );
    }

    public void setMaxLines(int maxLines) {
        if(maxLines != -1) {
            mEditText.setMaxLines(maxLines);
            if(maxLines == 1)
                mEditText.setSingleLine();
        }
    }

    public void setText(String text) {
        mEditText.setText(text);
    }
    public void setText(@StringRes int textId) {
        if (textId != -1)
            mEditText.setText(textId);
    }
    public String getText() {
        return mEditText.getText().toString();
    }


}
