package InitialActivity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.quizsmart.R;

import HttpModel.*;
import API.*;
import Class.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText EmailET, PasswordET;
    Button LoginBTN, GmailBTN;
    TextView SignUpBTN;
    ToggleButton VisibleBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();
        setupTextWatchers();
        setupButton();
    }

    private void initializeView() {
        EmailET = findViewById(R.id.EmailEditText);
        PasswordET = findViewById(R.id.PasswordEditText);
        LoginBTN = findViewById(R.id.LoginButton);
        GmailBTN = findViewById(R.id.GmailButton);
        SignUpBTN = findViewById(R.id.SignUpButton);
        VisibleBTN = findViewById(R.id.VisibleButton);

        LoginBTN.setEnabled(false); // Initially disable the login button
    }

    private void setupButton() {
        // Login button
        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email, Password;
                Email = EmailET.getText().toString().trim();
                Password = PasswordET.getText().toString().trim();
                if (!Email.isEmpty() && !Password.isEmpty()) {
                    LoginRequest loginRequest = new LoginRequest(Email, Password);
                    ApiService apiService = ApiClient.getApiService("DB");
                    Call<LoginResponse> call = apiService.verifyLogin(loginRequest);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                LoginResponse loginResponse = response.body();
                                if ("Employer".equals(loginResponse.getUserType())) {
                                    Employer employer = (Employer) loginResponse.getUser();
                                    Intent intent = new Intent(LoginActivity.this, EmployerActivity.SummaryActivity.class);
                                    intent.putExtra("employer", employer);
                                    startActivity(intent);
                                } else if ("Employee".equals(loginResponse.getUserType())) {
                                    Employee employee = (Employee) loginResponse.getUser();
                                    Intent intent = new Intent(LoginActivity.this, EmployeeActivity.SummaryActivity.class);
                                    intent.putExtra("employee", employee);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid Information", Toast.LENGTH_LONG).show();
                                Log.e("DB_CALL", "Login failed: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DB_CALL", "Request failed: " + t.getMessage());
                        }
                    });
                }
            }
        });

        // Gmail button
        GmailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Sign up button
        SignUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RolesSelectionActivity.class);
                startActivity(intent);
            }
        });

        // Visible button
        VisibleBTN.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                // Show password
                PasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password
                PasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            // Move the cursor to the end of the text
            PasswordET.setSelection(PasswordET.getText().length());
        });
    }

    private void setupTextWatchers() {
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used, but must be implemented
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used, but must be implemented
            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = EmailET.getText().toString().trim();
                String password = PasswordET.getText().toString().trim();

                // Enable the login button only if both fields are not empty
                LoginBTN.setEnabled(!email.isEmpty() && !password.isEmpty());
            }
        };

        // Apply the text watcher to both text fields
        EmailET.addTextChangedListener(loginTextWatcher);
        PasswordET.addTextChangedListener(loginTextWatcher);
    }
}