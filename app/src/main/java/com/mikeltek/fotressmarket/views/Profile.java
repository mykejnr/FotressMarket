package com.mikeltek.fotressmarket.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikeltek.fotressmarket.MainActivity;
import com.mikeltek.fotressmarket.R;
import com.mikeltek.fotressmarket.services.AuthService;

public class Profile extends AppCompatActivity {

    private TextView txtName;
    private TextView txtEmail;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authService = AuthService.getInstance(getApplicationContext());
        init();
    }

    private void init() {
        txtName = findViewById(R.id.profile_TextName);
        txtEmail = findViewById(R.id.profile_TextEmail);
        subscribeToAuthUser();
        configureHomeButton();
    }

    private void configureHomeButton() {
        var btn = (ImageButton)findViewById(R.id.appBar_BtnUser);
        btn.setBackgroundResource(R.drawable.baseline_home_24);
        btn.setOnClickListener(v ->
            startActivity(new Intent(Profile.this, MainActivity.class))
        );
    }

    private void subscribeToAuthUser() {
        var d = authService.authUser.subscribe(u -> {
            if (u.isEmpty()) return;
            var user = u.get();
            assert user != null;
            txtName.setText(String.format("%s %s", user.otherNames, user.surname));
            txtEmail.setText(user.email);
        });
    }
}