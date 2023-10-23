package com.example.imanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Notif extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 1;

    private TextView tvPermissionStatus, tvNotificationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        tvPermissionStatus = findViewById(R.id.tvPermissionStatus);
        tvNotificationInfo = findViewById(R.id.tvNotificationInfo);
        Button btnRequestPermission = findViewById(R.id.btnRequestPermission);

        btnRequestPermission.setOnClickListener(v -> requestSmsPermission());

        // Check for SMS permission
        if (hasSmsPermission()) {
            tvPermissionStatus.setText("Permission Status: Granted");
        } else {
            tvPermissionStatus.setText("Permission Status: Not Granted");
        }
    }

    private boolean hasSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        if (!hasSmsPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvPermissionStatus.setText("Permission Status: Granted");
            } else {
                tvPermissionStatus.setText("Permission Status: Denied");
            }
        }
    }
}
