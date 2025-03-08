package com.mikeltek.fotressmarket.views;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikeltek.fotressmarket.R;
import com.mikeltek.fotressmarket.forms.AppFormValidator;
import com.mikeltek.fotressmarket.forms.FormSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Register extends AppCompatActivity {
    FormSet formSet = new FormSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerValidators();
        var btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> { register(); });
    }

    private void register() {
        if (!formSet.isValid()) return;
    }

    private void registerValidators() {
        formSet.register( findViewById( R.id.editTextFirstname ) );
        formSet.register( findViewById( R.id.editTextOtherNames ) );
        formSet.register( findViewById( R.id.editTextEmail ) );
        formSet.register( findViewById( R.id.editTextPassword), List.of(
            FormSet.Required,
            this::validatePassword
        ));
        formSet.register( findViewById( R.id.editTextConfirmPassword ), List.of(
                FormSet.Required,
                this::confirmPassword
        ));
    }

    private void updateViewErrors() {
        ConstraintLayout layout = findViewById(R.id.content);
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            var c = layout.getChildAt(i);
            if (c instanceof TextView) {
                ((TextView) c).setText("");
            }
        }
    }

    private Optional<String> validatePassword(String password) {
        return Optional.empty();
    }

    private Optional<String> confirmPassword(String cPassword) {
        return Optional.empty();
    }
}