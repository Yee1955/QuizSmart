package com.example.itube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    EditText fullName, userName, password, confirmPassword;
    UserManagerDB userManagerDB;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userManagerDB = new UserManagerDB(SignUpActivity.this);

        fullName = findViewById(R.id.FullnameEditText);
        userName = findViewById(R.id.UsernameEditText);
        password = findViewById(R.id.PasswordEditText);
        confirmPassword = findViewById(R.id.ConfirnEditText);
        createBtn = findViewById(R.id.CreateButton);

        setupButton();
    }

    private void setupButton() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FullName = fullName.getText().toString();
                String UserName = userName.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString();

                if (FullName.isEmpty() || UserName.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this,
                            "Please fill in all the information",
                            Toast.LENGTH_SHORT).show();
                } else if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(SignUpActivity.this,
                            "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(userManagerDB.generateUniqueId(userManagerDB),FullName, UserName, Password);
                    boolean isAdded = userManagerDB.addUser(user);
                    if (isAdded) {
                        Toast.makeText(SignUpActivity.this,
                                "Account Successfully Created",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                "Failed to create account, please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}