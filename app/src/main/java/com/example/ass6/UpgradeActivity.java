package com.example.ass6;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class UpgradeActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String username, subscription;
    private Button btnPurchase1, btnPurchase2, btnPurchase3, backToMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        username = getIntent().getStringExtra("USERNAME");
        db = new DatabaseHelper(this);
        User user = db.getUserByUsername(username);
        subscription = user.getSubscription();

        btnPurchase1 = findViewById(R.id.btnPurchase1);
        btnPurchase2 = findViewById(R.id.btnPurchase2);
        btnPurchase3 = findViewById(R.id.btnPurchase3);
        backToMenuButton = findViewById(R.id.backToMenuButton);

        updateSubscriptionButtons(subscription);

        btnPurchase1.setOnClickListener(v -> showPaymentDialog("Starter"));
        btnPurchase2.setOnClickListener(v -> showPaymentDialog("Intermediate"));
        btnPurchase3.setOnClickListener(v -> showPaymentDialog("Advance"));
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(UpgradeActivity.this, UserMainActivity.class);
                new_intent.putExtra("USERNAME", username);
                startActivity(new_intent);
            }
        });
    }

    private void showPaymentDialog(String sub) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment, null);
        builder.setView(dialogView);

        final RadioGroup radioGroupCardType = dialogView.findViewById(R.id.radioGroupCardType);
        final EditText editTextSerialNumber = dialogView.findViewById(R.id.editTextSerialNumber);
        final EditText editTextExpireDate = dialogView.findViewById(R.id.editTextExpireDate);
        final EditText editTextCSC = dialogView.findViewById(R.id.editTextCSC);
        final VideoView videoView = dialogView.findViewById(R.id.videoViewPaymentSuccess);

        radioGroupCardType.setOnCheckedChangeListener((group, checkedId) -> {
            editTextExpireDate.setVisibility(checkedId == R.id.radioMasterCard ? View.VISIBLE : View.GONE);
            editTextCSC.setVisibility(checkedId == R.id.radioMasterCard ? View.VISIBLE : View.GONE);
        });

        builder.setPositiveButton("Pay", (dialog, which) -> {
            db.changeSubscription(username, sub);
            updateSubscriptionButtons(sub);
            launchVideoFragment();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void launchVideoFragment() {
        String videoUri = "android.resource://" + getPackageName() + "/" + R.raw.success_animation;
        Fragment videoFragment = VideoFragment.newInstance(videoUri);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, videoFragment) // Ensure you have a container in your activity layout
                .addToBackStack(null) // Adds the transaction to the back stack (optional)
                .commit();
    }

    private void updateSubscriptionButtons(String subscription) {
        btnPurchase1.setEnabled(!"Starter".equals(subscription));
        btnPurchase2.setEnabled(!"Intermediate".equals(subscription));
        btnPurchase3.setEnabled(!"Advance".equals(subscription));
    }
}
