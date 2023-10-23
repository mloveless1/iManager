package com.example.imanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText itemNameInput;
    EditText itemQuantityInput;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Bring views into our function
        itemNameInput = findViewById(R.id.itemNameInput);
        itemQuantityInput = findViewById(R.id.itemQuantityInput);
        saveButton = findViewById(R.id.saveButton);

        // Listener for the button
        saveButton.setOnClickListener(view -> {
            // Database handler object
            DBHandler dbHandler =  new DBHandler(AddActivity.this);

            // Add item to database
            dbHandler.addItem(itemNameInput.getText().toString().trim(),
                    Integer.parseInt(itemQuantityInput.getText().toString()));

            // Finish the activity and send us to the main activity
            finish();
        });
    }
}