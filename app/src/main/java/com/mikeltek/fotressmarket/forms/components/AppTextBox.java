package com.mikeltek.fotressmarket.forms.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mikeltek.fotressmarket.R;
import com.mikeltek.fotressmarket.forms.AppFormField;
import com.mikeltek.fotressmarket.forms.AppFormFieldOnChange;

import java.util.List;

public class AppTextBox extends LinearLayout implements AppFormField {
    private String label = "";
    private int textType = 0;

    private EditText textBox;
    private TextView errorLabel;


    public AppTextBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.app_text_box, this);
        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppTextBox)) {
            label = a.getString(R.styleable.AppTextBox_label);
            textType = a.getInt(R.styleable.AppTextBox_android_inputType, InputType.TYPE_CLASS_TEXT);
        }
        initComponent();
    }

    private void initComponent() {
        textBox = findViewById(R.id.appTextBoxEditText);
        TextView textLabel = findViewById(R.id.appTextBoxLabelText);
        errorLabel = findViewById(R.id.appTextBoxErrorText);

        textLabel.setText(label);
        textBox.setInputType(textType);
    }

    @Override
    public void updateViewErrors(List<String> errors) {
        errorLabel.setText(errors.get(0));
        errorLabel.setVisibility(VISIBLE);
    }

    @Override
    public String getValue() {
        return textBox.getText().toString();
    }

    @Override
    public void clearViewErrors() {
        errorLabel.setVisibility(GONE);
    }

    @Override
    public void registerOnChange(AppFormFieldOnChange callBack)
    {
        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                callBack.onChange(s.toString());
            }
        });
    }
}
