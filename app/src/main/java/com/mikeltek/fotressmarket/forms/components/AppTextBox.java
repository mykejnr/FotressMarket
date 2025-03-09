package com.mikeltek.fotressmarket.forms.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.mikeltek.fotressmarket.R;

public class AppTextBox extends LinearLayout {
    public AppTextBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.app_text_box, this);
    }
}
