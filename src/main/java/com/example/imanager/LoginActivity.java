package com.example.imanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, createAccountButton;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the database handler
        dbHandler = new DBHandler(this);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        createAccountButton = findViewById(R.id.createAccount);

        // Set listeners
        loginButton.setOnClickListener(v -> loginUser());
        createAccountButton.setOnClickListener(v -> register());
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (dbHandler.userCheck(username, password)) {
            // User exists, proceed to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // finish current login activity so that user can't come back by pressing the back button
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        } else {
            // Invalid login attempt
            Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHandler.addUser(username, password);
        Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();

        usernameEditText.setText("");
        passwordEditText.setText("");
    }

    private void userExistsCheck() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!dbHandler.userCheck(username, password)) {
            Toast.makeText(this, "User with this combination does not exist!", Toast.LENGTH_SHORT).show();
        }
    }

}
