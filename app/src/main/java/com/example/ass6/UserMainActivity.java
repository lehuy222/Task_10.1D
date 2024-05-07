package com.example.ass6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserMainActivity extends AppCompatActivity {

    TextView tv_greeting;
    Button btn_task_action, btnViewProfile, btnViewHistory, btnUpgradeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_main);

        tv_greeting = findViewById(R.id.tv_greeting);
        btn_task_action = findViewById(R.id.btn_task_action);
        btnViewProfile = findViewById(R.id.btnViewProfile);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnUpgradeAccount = findViewById(R.id.btnUpgradeAccount);

        String username = getIntent().getStringExtra("USERNAME");
        tv_greeting.setText("Hello "+ username);

        btn_task_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(UserMainActivity.this, QuizActivity.class);
                new_intent.putExtra("USERNAME", username);
                startActivity(new_intent);
            }
        });

        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(UserMainActivity.this, ProfileActivity.class);
                new_intent.putExtra("USERNAME", username);
                startActivity(new_intent);
            }
        });

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(UserMainActivity.this, HistoryActivity.class);
                new_intent.putExtra("USERNAME", username);
                startActivity(new_intent);
            }
        });

        btnUpgradeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(UserMainActivity.this, UpgradeActivity.class);
                new_intent.putExtra("USERNAME", username);
                startActivity(new_intent);
            }
        });

    }
}