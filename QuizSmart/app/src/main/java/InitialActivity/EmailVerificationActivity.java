package InitialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quizsmart.R;
import Class.*;

public class EmailVerificationActivity extends AppCompatActivity {
    ImageButton BackBTN;
    TextView EmailTV, ResendCodeBTN;
    EditText CodeET1, CodeET2, CodeET3, CodeET4;
    Button VerifyBTN;
    Employer Employer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        Employer = (Employer) getIntent().getSerializableExtra("employer");

        sendCodeToEmail();
        initializeView();
    }

    private void initializeView() {
        BackBTN = findViewById(R.id.BackButton);
        EmailTV = findViewById(R.id.EmailTextView);
        ResendCodeBTN = findViewById(R.id.ResendCodeButton);
        CodeET1 = findViewById(R.id.CodeEditText1);
        CodeET2 = findViewById(R.id.CodeEditText2);
        CodeET3 = findViewById(R.id.CodeEditText3);
        CodeET4 = findViewById(R.id.CodeEditText4);
        VerifyBTN = findViewById(R.id.VerifyButton);

        VerifyBTN.setEnabled(false);
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
                String Code1 = CodeET1.getText().toString().trim();
                String Code2 = CodeET2.getText().toString().trim();
                String Code3 = CodeET3.getText().toString().trim();
                String Code4 = CodeET4.getText().toString().trim();

                // Enable the login button only if both fields are not empty
                VerifyBTN.setEnabled(!Code1.isEmpty() && !Code2.isEmpty() && !Code3.isEmpty() && !Code4.isEmpty());
            }
        };

        // Apply the text watcher to both text fields
        CodeET1.addTextChangedListener(loginTextWatcher);
        CodeET2.addTextChangedListener(loginTextWatcher);
        CodeET3.addTextChangedListener(loginTextWatcher);
        CodeET4.addTextChangedListener(loginTextWatcher);
    }

    private void sendCodeToEmail() {

    }
}