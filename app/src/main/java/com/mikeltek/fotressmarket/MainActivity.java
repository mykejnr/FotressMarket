package com.mikeltek.fotressmarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikeltek.fotressmarket.models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextUsername, editTextPassword;
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        var btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(v -> {
            var username = editTextPassword.getText().toString();
            var password = editTextUsername.getText().toString();
            User mike = new User();

//            if ( !mike.confirmCredentials(username, password) ) {
//                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG)
//                        .show();
//                return;
//            }

//            Intent myIntent = new Intent(MainActivity.this, HomePage.class);
//            startActivity(myIntent);
        });
    }
}