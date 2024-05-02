package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalizedlearningexperienceapp.R;

import java.util.List;

import Class.*;
import ManagerDB.ManagerDB;

public class SignUpActivity extends AppCompatActivity {
    EditText usernameET, emailET, confirmEmailET, passwordET, confirmPasswordET, phoneNumberET;
    Button createBTN;
    ManagerDB managerDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        managerDB = new ManagerDB(this);

        initializeViews();
        setupButton();
    }

    private void initializeViews() {
        usernameET = findViewById(R.id.UsernameText);
        emailET = findViewById(R.id.EmailText);
        confirmEmailET = findViewById(R.id.ConfirmEmailText);
        passwordET = findViewById(R.id.PasswordText);
        confirmPasswordET = findViewById(R.id.ConfirmPasswordText);
        phoneNumberET = findViewById(R.id.PhoneNumberText);
        createBTN = findViewById(R.id.createButton);
    }

    private void setupButton() {
        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = usernameET.getText().toString().trim();
                String Email = emailET.getText().toString().trim();
                String ConfirmEmail = confirmEmailET.getText().toString().trim();
                String Password = passwordET.getText().toString().trim();
                String ConfirmPassword = confirmPasswordET.getText().toString().trim();
                String PhoneNumber = phoneNumberET.getText().toString().trim();
                if (Username.isEmpty() || Email.isEmpty() || ConfirmEmail.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty() || PhoneNumber.isEmpty()) {
                    Toast.makeText(SignUpActivity.this,
                            "Please fill in all the information",
                            Toast.LENGTH_SHORT).show();
                } else if (!Email.equals(ConfirmEmail)) {
                    Toast.makeText(SignUpActivity.this,
                            "Email address does not match",
                            Toast.LENGTH_SHORT).show();
                } else if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(SignUpActivity.this,
                            "Password does not match",
                            Toast.LENGTH_SHORT).show();
                } else {
                    User user = managerDB.addUser(new User(Username, Email, Password, PhoneNumber, null, null));
                    Toast.makeText(SignUpActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SelectInterestActivity.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                    clearAllET();
                }
            }
        });
    }

    private void clearAllET() {
        usernameET.getText().clear();
        emailET.getText().clear();
        confirmEmailET.getText().clear();
        passwordET.getText().clear();
        confirmPasswordET.getText().clear();
        phoneNumberET.getText().clear();
    }
}