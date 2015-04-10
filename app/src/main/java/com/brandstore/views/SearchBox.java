package com.brandstore.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.brandstore.R;


public class SearchBox extends EditText {
    private Drawable searchButton = getResources().getDrawable(R.drawable.ic_action_search);

    public SearchBox(Context context) {
        super(context);
        init();
    }

    public SearchBox(Context context, AttributeSet attrs) {
       super(context,attrs);
        init();
    }

    public SearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SearchBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs,defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        searchButton.setBounds(0, 0, searchButton.getIntrinsicWidth(), searchButton.getIntrinsicHeight());
        this.setCompoundDrawables(searchButton, null, null, null);
        this.setHintTextColor(getResources().getColor(R.color.grey));
        this.setCursorVisible(true);
this.setTextColor(getResources().getColor(R.color.black));

    }


}
