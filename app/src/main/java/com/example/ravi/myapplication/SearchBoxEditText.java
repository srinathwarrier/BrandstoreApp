package com.example.ravi.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;


public class SearchBoxEditText extends EditText {
    private Drawable searchButton = getResources().getDrawable(R.drawable.ic_action_search);

    public SearchBoxEditText(Context context) {
        super(context);
        init();
    }

    public SearchBoxEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchBoxEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SearchBoxEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        searchButton.setBounds(0, 0, searchButton.getIntrinsicWidth(), searchButton.getIntrinsicHeight());
        this.setCompoundDrawables(null, null, searchButton, null);


    }


}
