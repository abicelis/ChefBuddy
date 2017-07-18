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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 14/7/2017.
 */

public class FancyAutoCompleteTextView extends LinearLayout {

    @BindView(R.id.fancy_auto_complete_text_view_icon)
    ImageView mIcon;

    @BindView(R.id.fancy_auto_complete_text_view_text)
    AutoCompleteTextView mAutoCompleteTextView;

    public FancyAutoCompleteTextView(Context context) {
        super(context);
        init(context, null);
    }
    public FancyAutoCompleteTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public FancyAutoCompleteTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fancy_auto_complete_text_view, this);

        ButterKnife.bind(this);

        //Get/apply custom xml configs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.fancy_auto_complete_text_view);
        int iconId = a.getResourceId(R.styleable.fancy_auto_complete_text_view_icon, -1);
        int textId = a.getResourceId(R.styleable.fancy_auto_complete_text_view_text, -1);
        int hintId = a.getResourceId(R.styleable.fancy_auto_complete_text_view_hint, -1);

        setIcon(iconId);
        setText(textId);
        setHint(hintId);
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
        mAutoCompleteTextView.setHint(hint);
    }
    public void setHint(@StringRes int hintId) {
        if (hintId != -1)
            mAutoCompleteTextView.setHint(hintId);
    }


    public void setText(String text) {
        mAutoCompleteTextView.setText(text);
    }
    public void setText(@StringRes int textId) {
        if (textId != -1)
            mAutoCompleteTextView.setText(textId);
    }
    public String getText() {
        return mAutoCompleteTextView.getText().toString();
    }

    public void setMaxLenght(int lenght) {
        mAutoCompleteTextView.setFilters( new InputFilter[] { new InputFilter.LengthFilter(lenght) } );
    }

    /* Bypass functions for internal autoCompleteTextView */
    public void setItems(List<String> items) {
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.fancy_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.fancy_spinner_dropdown_item);
        mAutoCompleteTextView.setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        mAutoCompleteTextView.setOnItemClickListener(listener);
    }

}
