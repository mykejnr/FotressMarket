package com.mikeltek.fotressmarket.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.mikeltek.fotressmarket.R;
import com.mikeltek.fotressmarket.forms.FormSet;
import com.mikeltek.fotressmarket.forms.components.AppTextBox;
import com.mikeltek.fotressmarket.models.User;
import com.mikeltek.fotressmarket.services.AuthService;

import java.util.Optional;

import io.reactivex.rxjava3.core.Single;

public class Register extends AppCompatActivity {
    FormSet formSet;
    AuthService authService;

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

        authService = AuthService.getInstance(getApplicationContext());
        initFormSet();

        findViewById(R.id.btnRegister).setOnClickListener(v -> register() );
        
        findViewById(R.id.register_buttonLogin)
            .setOnClickListener(v -> goToLogin(false) );
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

        var user = new User();
        user.surname = values.get(R.id.appTextSurname);
        user.otherNames = values.get(R.id.appTextOtherNames);
        user.email = values.get(R.id.appTextEmail);

        var d = authService.createUserAsync(user, values.get(R.id.appTextPassword))
                .onErrorResumeNext(err -> {
                    showRegisterError(err.getMessage());
                    return Single.just(false);
                })
                .subscribe(created -> {
                    if (created) goToLogin(true);
                });

    }

    private void showRegisterError(String errMessage) {
        var toast = Toast.makeText(this, errMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    private void goToLogin(boolean showRegisterToast) {
        if (showRegisterToast) {
            var msg = "You have registered successfully. You can now login";
            var toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
        }
        startActivity( new Intent(Register.this, Login.class) );
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