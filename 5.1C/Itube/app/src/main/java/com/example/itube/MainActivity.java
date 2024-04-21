package com.example.itube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<User> AccountList;
    EditText username, password;
    Button loginBtn, signUpBtn;
    UserManagerDB userManagerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userManagerDB = new UserManagerDB(MainActivity.this);

        username = findViewById(R.id.UsernameEditText);
        password = findViewById(R.id.PasswordEditText);
        loginBtn = findViewById(R.id.LoginButton);
        signUpBtn = findViewById(R.id.SigUpButton);

        setupButton();
    }

    private void setupButton() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                if(Username.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Please fill in all the information",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String userId = userManagerDB.verifyUserCredentials(Username, Password);
                    if(userId != null) {
                        User user = userManagerDB.getUser(userId);
                        Intent intent = new Intent(MainActivity.this, URLActivity.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Invalid Information",
                                Toast.LENGTH_SHORT).show();
                    };
                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}