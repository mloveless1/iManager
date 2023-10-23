package com.example.imanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    FloatingActionButton smsButton;

    DBHandler dbHandler;
    ArrayList<String> itemId, itemName, itemQuantity;
    CustomAdapter customAdapter;

    private static final int SMS_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.itemView);
        addButton = findViewById(R.id.addButton);
        smsButton = findViewById(R.id.smsButton);

        // Set listeners for buttons to lead to add and sms permission activities
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        // Set listeners for buttons to lead to add and sms permission activities
        smsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Notif.class);
            startActivity(intent);
        });

        // initialize db and lists to store information
        dbHandler = new DBHandler(MainActivity.this);
        itemId = new ArrayList<>();
        itemName = new ArrayList<>();
        itemQuantity = new ArrayList<>();

        // populate lists with db info
        storeData();

        // bring in our adapter to populate our view with rows
        customAdapter = new CustomAdapter(MainActivity.this, itemId, itemName, itemQuantity);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // updates rows view after db operations
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        Cursor cursor = dbHandler.readAllData();

        ArrayList<String> updatedItemId = new ArrayList<>();
        ArrayList<String> updatedItemName = new ArrayList<>();
        ArrayList<String> updatedItemQuantity = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                updatedItemId.add(cursor.getString(0));
                updatedItemName.add(cursor.getString(1));
                updatedItemQuantity.add(cursor.getString(2));

                // Check if item quantity is 0 and send SMS
                if ("0".equals(cursor.getString(2))) {
                    sendSMS("Item: " + cursor.getString(1) + " is out of stock!");
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        customAdapter.updateData(updatedItemId, updatedItemName, updatedItemQuantity);
    }

    void storeData(){
        Cursor cursor = dbHandler.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data to display", Toast.LENGTH_SHORT).show();
        } else{
            while (cursor.moveToNext()){
                itemId.add(cursor.getString(0));
                itemName.add(cursor.getString(1));
                itemQuantity.add(cursor.getString(2));
            }
        }
    }

    private void sendSMS(String message) {
        // First, check if the app has the SEND_SMS permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // You can specify the phone number to send the SMS here.
            SmsManager.getDefault().sendTextMessage("1234567890", null,
                                                       message, null, null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }
}
