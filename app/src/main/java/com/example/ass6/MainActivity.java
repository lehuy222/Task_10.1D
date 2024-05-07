package com.example.ass6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    EditText usernameEt, passwordEt;
    Button loginButton, registerButton;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("????", usernameEt.getText().toString());
                login(usernameEt.getText().toString());

            }
        });
    }

    public void login(String username){
        Log.d("main", "1");
        User check_available = db.getUserByUsername(username);
        if(Objects.equals(check_available.getUsername(), usernameEt.getText().toString()) && Objects.equals(check_available.getPassword(), passwordEt.getText().toString())){
            Intent intent = new Intent(MainActivity.this, UserMainActivity.class);
            intent.putExtra("USERNAME", usernameEt.getText().toString());
            startActivity(intent);
        }
    }
}