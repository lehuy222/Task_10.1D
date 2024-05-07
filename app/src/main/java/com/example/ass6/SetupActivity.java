package com.example.ass6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class SetupActivity extends AppCompatActivity {

    EditText usernameEt, passwordEt, confirmPasswordEt, emailEt, confirmEmailEt, phoneEt;

    Button createAccountButton;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setup);

        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        emailEt = findViewById(R.id.emailEt);
        confirmEmailEt = findViewById(R.id.confirmEmailEt);
        phoneEt = findViewById(R.id.phoneEt);
        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        // Get the values from the EditText fields
        String email = emailEt.getText().toString().trim();
        String confirmEmail = confirmEmailEt.getText().toString().trim();
        String username = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();

        // Validate the inputs
        if (validateInputs(username, password, confirmPassword, email, confirmEmail, phone)) {
            db.addUser(new User(username, password, email, phone, Arrays.asList(), "Default"));
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SetupActivity.this, InterestSelectionActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        }
    }

    private boolean validateInputs(String username, String password, String confirmPassword, String email, String confirmEmail, String phone) {
        if (email.isEmpty()) {
            showToast("Full name cannot be empty");
            return false;
        }
        if (username.isEmpty()) {
            showToast("Username cannot be empty");
            return false;
        }
        if (password.isEmpty()) {
            showToast("Password cannot be empty");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }
        if (!email.equals(confirmEmail)) {
            showToast("Passwords do not match");
            return false;
        }
//        if (password.length() < 8) {
//            showToast("Password must be at least 8 characters long");
//            return false;
//        }
        User check_available = db.getUserByUsername(username);
        if(check_available != null){
            showToast("Username already exits. Please choose another name!");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}