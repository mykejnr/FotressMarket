package com.mikeltek.fotressmarket.views;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.mikeltek.fotressmarket.R;
import com.mikeltek.fotressmarket.forms.FormSet;
import com.mikeltek.fotressmarket.forms.components.AppTextBox;
import java.util.Optional;

public class Register extends AppCompatActivity {
    FormSet formSet;

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

        initFormSet();

        var btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> { register(); });
    }

    private void initFormSet() {
        var container = (ConstraintLayout)findViewById(R.id.registerContent);
        formSet = new FormSet(container);
        registerValidators();
    }

    private void registerValidators() {
        formSet.register(R.id.appTextSurname, FormSet.Required);
        formSet.register(R.id.appTextOtherNames, FormSet.Required);
        formSet.register(R.id.appTextEmail, FormSet.Required);
        formSet.register(R.id.appTextPassword, this::validatePassword);
        formSet.register(R.id.appTextConfirmPassword, this::confirmPassword);
    }

    private void register() {
        if (!formSet.isValid()) return;
        var values = formSet.getValues();
    }

    private Optional<String> validatePassword(String password) {
        var errors = FormSet.Required.validate(password);
        if (errors.isPresent()) return errors;
        if ( !testPassword(password) )
            errors = Optional.of("Password must contain uppercase, lowercase, special character");
        return errors;
    }
    private Optional<String> confirmPassword(String cPassword) {
        var view = (AppTextBox)findViewById(R.id.appTextPassword);
        if ( cPassword.equals(view.getValue()) )
            return Optional.empty();
        return Optional.of("Passwords do not match");
    }

    private boolean testPassword(String password) {
        // Test password strength
        return true;
    }
}