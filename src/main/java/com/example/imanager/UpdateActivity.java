package com.example.imanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText itemNameInput2, itemQuantityInput2;
    Button saveButton2, deleteButton;
    String id, itemName, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        itemNameInput2 = findViewById(R.id.itemNameInput2);
        itemQuantityInput2 = findViewById(R.id.itemQuantityInput2);
        saveButton2 = findViewById(R.id.saveButton2);
        deleteButton = findViewById(R.id.deleteButton);
        getAndSetIntentData();

        saveButton2.setOnClickListener(view -> {
            // initialize db helper
            DBHandler dbHandler = new DBHandler(UpdateActivity.this);

            // Update table row with new information
            dbHandler.updateData(id, itemNameInput2.getText().toString(),
                    Integer.parseInt(itemQuantityInput2.getText().toString()));

            // Return and reload main activity
            finish();
        });

        deleteButton.setOnClickListener(view -> {
            // initialize db helper
            DBHandler dbHandler = new DBHandler(UpdateActivity.this);

            // Delete the row
            dbHandler.deleteItem(id);

            // Return and reload main activity
            finish();
        });
    }

    void getAndSetIntentData(){
        if (getIntent().hasExtra("itemName") && getIntent().hasExtra("quantity")){
            // Get data from intent
            id = getIntent().getStringExtra("id");
            itemName = getIntent().getStringExtra("itemName");
            quantity = getIntent().getStringExtra("quantity");
            // Set Intent data
            itemNameInput2.setText(itemName);
            itemQuantityInput2.setText(quantity);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }
}