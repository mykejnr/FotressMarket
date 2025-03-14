package com.mikeltek.fotressmarket.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikeltek.fotressmarket.MainActivity;
import com.mikeltek.fotressmarket.R;

public class Profile extends AppCompatActivity {

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

        configureHomeButton();
    }

    private void configureHomeButton() {
        var btn = (ImageButton)findViewById(R.id.appBar_BtnUser);
        btn.setBackgroundResource(R.drawable.baseline_home_24);
        btn.setOnClickListener(v ->
            startActivity(new Intent(Profile.this, MainActivity.class))
        );
    }
}