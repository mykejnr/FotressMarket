package com.mikeltek.fotressmarket.views;

import android.annotation.SuppressLint;
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

import com.mikeltek.fotressmarket.MainActivity;
import com.mikeltek.fotressmarket.R;
import com.mikeltek.fotressmarket.forms.FormSet;
import com.mikeltek.fotressmarket.services.AuthService;

public class Login extends AppCompatActivity {

    FormSet formSet;
    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_MainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authService = new AuthService(getApplicationContext());
        initFormSet();

        findViewById(R.id.login_buttonLogin)
            .setOnClickListener(v -> login() );
        findViewById(R.id.login_buttonRegister)
           .setOnClickListener(v -> gotoRegister());
    }

    private void initFormSet() {
        var container = (ConstraintLayout)findViewById(R.id.login_MainLayout);
        formSet = new FormSet(container);
        registerValidators();
    }

    private void registerValidators() {
        formSet.register(R.id.login_appTextEmail, FormSet.Required);
        formSet.register(R.id.login_appTextPassword, FormSet.Required);
    }

    private void login() {
        if (!formSet.isValid()) return;

        var values = formSet.getValues();
        var email = values.get(R.id.login_appTextEmail);
        var password = values.get(R.id.login_appTextPassword);

        authService.loginAsync(email, password)
                .onErrorComplete( err -> {
                    showLoginError(err.getMessage());
                    return true;
                })
//            .doOnError(t -> showLoginError(t.getMessage()))
            .subscribe(u -> gotoDashboard());
    }

    private void showLoginError(String errMessage) {
        Log.d("Login Failed", errMessage);
        Log.d("Login Failed", "Thread: " + Thread.currentThread());
        var toast = Toast.makeText(this, errMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    private void gotoRegister() {
        startActivity(new Intent(Login.this, Register.class));
    }

    private void gotoDashboard() {
        startActivity(new Intent(Login.this, MainActivity.class));
    }
}