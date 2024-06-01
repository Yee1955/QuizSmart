package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.quizsmart.R;

import API.ApiClient;
import API.ApiService;
import Class.*;
import EmployeeActivity.SummaryActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    ImageButton BackBTN;
    TextView SignInBTN;
    EditText EmailET, CompanyNameET, PasswordET, ConfirmPasswordET;
    ToggleButton VisibleBTN1, VisibleBTN2;
    Button SignUpBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_sign_up);

        initializeView();
        setupButton();
        setupTextWatchers();
    }

    private void initializeView() {
        BackBTN = findViewById(R.id.BackButton);
        SignInBTN = findViewById(R.id.SignInButton);
        EmailET = findViewById(R.id.EmailEditText);
        CompanyNameET = findViewById(R.id.CompanyNameEditText);
        PasswordET = findViewById(R.id.PasswordEditText);
        ConfirmPasswordET = findViewById(R.id.ConfirmPasswordEditText);
        VisibleBTN1 = findViewById(R.id.VisibleButton1);
        VisibleBTN2 = findViewById(R.id.VisibleButton2);
        SignUpBTN = findViewById(R.id.SignUpButton);

        SignUpBTN.setEnabled(false);
    }

    private void setupButton() {
        // Next button
        SignUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailET.getText().toString().trim();
                String companyName = CompanyNameET.getText().toString().trim();
                String password = PasswordET.getText().toString().trim();
                String confirmPassword = ConfirmPasswordET.getText().toString().trim();
                if (password.equals(confirmPassword)) {
                    Employer Employer = new Employer(0, email, companyName, password);
                    addEmployer(Employer);
                } else {
                    ExtensionMethod.showCustomToast("Passwords DO not match", SignUpActivity.this);
                }
            }
        });

        // Back button
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, InitialActivity.RolesSelectionActivity.class);
                startActivity(intent);
            }
        });

        // Sign in button
        SignInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, InitialActivity.LoginActivity.class);
                startActivity(intent);
            }
        });

        // Visible button 1
        VisibleBTN1.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

        // Visible button 2
        VisibleBTN2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                // Show password
                ConfirmPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password
                ConfirmPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            // Move the cursor to the end of the text
            ConfirmPasswordET.setSelection(ConfirmPasswordET.getText().length());
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
                String companyName = CompanyNameET.getText().toString().trim();
                String password = PasswordET.getText().toString().trim();
                String confirmPassword = ConfirmPasswordET.getText().toString().trim();

                // Enable the login button only if both fields are not empty
                SignUpBTN.setEnabled(!email.isEmpty() && !companyName.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty());
            }
        };

        // Apply the text watcher to both text fields
        EmailET.addTextChangedListener(loginTextWatcher);
        CompanyNameET.addTextChangedListener(loginTextWatcher);
        PasswordET.addTextChangedListener(loginTextWatcher);
        ConfirmPasswordET.addTextChangedListener(loginTextWatcher);
    }

    private void addEmployer(Employer employer) {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<Employer> call = apiService.addEmployer(employer);
        Dialog dialog = ExtensionMethod.showCustomDialog(SignUpActivity.this, "Creating", R.layout.loading_custom, R.id.LoadingIcon, R.id.ProgressTextView);
        call.enqueue(new Callback<Employer>() {
            @Override
            public void onResponse(Call<Employer> call, Response<Employer> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("DB_CALL", "Successful: employer id is " + response.body().getId());
                    ExtensionMethod.showCustomToast("Account successfully created", SignUpActivity.this);
                    Intent intent = new Intent(SignUpActivity.this, InitialActivity.LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Employer> call, Throwable t) {
                dialog.dismiss();
                Log.e("DB_CALL", "Failure: " + t.getMessage());
            }
        });
    }
}