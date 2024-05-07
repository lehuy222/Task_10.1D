package com.example.ass6;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        Button shareButton = findViewById(R.id.btnShare);
        TextView usernameTv = findViewById(R.id.usernameTv);

        String username = getIntent().getStringExtra("USERNAME");
        usernameTv.setText(username);

        Button backToMenuButton = findViewById(R.id.backToMenuButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap screenshot = takeScreenshot();
                Uri imageUri = saveBitmap(screenshot);

                if (imageUri != null) {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    sendIntent.setType("image/png");

                    Intent chooser = Intent.createChooser(sendIntent, "Share Screenshot");

                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                    startActivity(chooser);

                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to capture screenshot", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(ProfileActivity.this, UserMainActivity.class);
                new_intent.putExtra("USERNAME", username);
                startActivity(new_intent);
            }
        });
    }

    private Bitmap takeScreenshot() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private Uri saveBitmap(Bitmap bitmap) {
        File imagePath = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagePath.mkdirs();
            File imageFile = new File(imagePath, "screenshot.png");

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            uri = FileProvider.getUriForFile(this, "com.example.ass6.fileprovider", imageFile);
        } catch (Exception e) {
            Log.e("ShareError", "Error while saving bitmap", e);
        }
        return uri;
    }
}