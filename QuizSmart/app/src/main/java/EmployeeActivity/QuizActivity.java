package EmployeeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.quizsmart.R;

import java.util.List;

import API.ApiClient;
import API.ApiService;
import Class.*;
import Enumerable.SessionStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {
    TextView QuestionTV;
    RadioButton OptionRBTN1, OptionRBTN2, OptionRBTN3;
    Button NextBTN;
    EditText OptionRBTN4;
    EmployeeSession EmployeeSession;
    Employee Employee;
    Session Session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_quiz);
        EmployeeSession = (EmployeeSession) getIntent().getSerializableExtra("employeeSession");
        Employee = (Employee) getIntent().getSerializableExtra("employee");
        if (EmployeeSession == null || Employee == null) {
            Log.e("IntentError", "EmployeeSession or Employee is null before sending.");
        }

        initializeView();
        updateProgress(EmployeeSession.getProgress());
        setupRadioButton();
        loadSession();
        setupButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateEmployeeSession();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateEmployeeSession();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateEmployeeSession();
    }

    private void initializeView() {
        QuestionTV = findViewById(R.id.QuestionTextView);
        OptionRBTN1 = findViewById(R.id.OptionRadioButton1);
        OptionRBTN2 = findViewById(R.id.OptionRadioButton2);
        OptionRBTN3 = findViewById(R.id.OptionRadioButton3);
        OptionRBTN4 = findViewById(R.id.OptionRadioButton4);
        NextBTN = findViewById(R.id.NextButton);

        NextBTN.setEnabled(false);
    }
    private void setupButton() {
        NextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeSession.setProgress(EmployeeSession.getProgress() + 1);
                updateProgress(EmployeeSession.getProgress());

                // Adding answer to answerString
                if (EmployeeSession.getAnswerString() == null || EmployeeSession.getAnswerString().isEmpty()) {
                    EmployeeSession.setAnswerString(getCurrentSelection());
                } else {
                    EmployeeSession.setAnswerString(EmployeeSession.getAnswerString() + ":::" + getCurrentSelection());
                }

                if (!getCurrentSelection().isEmpty() && EmployeeSession.getProgress() == 10) {
                    EmployeeSession.setStatus(SessionStatus.Completed);
                    Intent intent = new Intent(QuizActivity.this, EmployeeActivity.ResultActivity.class);
                    intent.putExtra("employeeSession", EmployeeSession);
                    intent.putExtra("employee", Employee);
                    startActivity(intent);
                } else if (!getCurrentSelection().isEmpty()) {
                    initializeView();
                    setupRadioButton();
                    setupTextView();
                }
            }
        });
    }

    private void setupTextView() {
        if (Session == null || EmployeeSession == null) QuestionTV.setText("Something went wrong");
        else {
            String[] questionString = Session.getQuestionString().split("---");
            String[] question = questionString[EmployeeSession.getProgress()].split(":::");
            QuestionTV.setText(question[0]);
            OptionRBTN1.setText(question[1]);
            OptionRBTN2.setText(question[2]);
            OptionRBTN3.setText(question[3]);
            if (EmployeeSession.getProgress() == 9) NextBTN.setText("Submit");
        }
    }

    private void setupRadioButton() {
        RadioButton[] radioButtons = {OptionRBTN1, OptionRBTN2, OptionRBTN3};
        EditText editText = OptionRBTN4;
        for (RadioButton rb : radioButtons) {
            rb.setChecked(false);
            editText.clearFocus();
            editText.getText().clear();
        }

        View.OnClickListener radioButtonListener = v -> {
            NextBTN.setEnabled(!getCurrentSelection().equals(""));
            for (RadioButton rb : radioButtons) {
                rb.setChecked(rb == v);  // Only check the clicked one
            }
            editText.clearFocus();
            NextBTN.setEnabled(!getCurrentSelection().isEmpty());
            hideKeyboard();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        };

        for (RadioButton rb : radioButtons) {
            rb.setOnClickListener(radioButtonListener);
        }

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Ensure text watcher is only added once
                if (editText.getTag() == null) {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            NextBTN.setEnabled(!s.toString().trim().isEmpty());
                        }
                    });
                    editText.setTag("hasTextWatcher");  // Mark the EditText as having a text watcher
                }

                for (RadioButton rb : radioButtons) {
                    rb.setChecked(false);
                }
                editText.setBackground(ContextCompat.getDrawable(this, R.drawable.background_option_checked));
            } else {
                editText.setBackground(ContextCompat.getDrawable(this, R.drawable.background_option_unchecked));
            }
            // Update button state based on focus change irrespective of text change
            NextBTN.setEnabled(!getCurrentSelection().isEmpty());
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
                NextBTN.setEnabled(!getCurrentSelection().isEmpty());
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadSession() {
        EmployeeSession.getSessionAsync(new EmployeeSession.SessionCallback() {
            @Override
            public void onSessionLoaded(Session session) {
                Session = session;
                setupTextView();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private String getCurrentSelection() {
        if (OptionRBTN1.isChecked()) {
            return OptionRBTN1.getText().toString();
        } else if (OptionRBTN2.isChecked()) {
            return OptionRBTN2.getText().toString();
        } else if (OptionRBTN3.isChecked()) {
            return OptionRBTN3.getText().toString();
        } else if (!OptionRBTN4.getText().toString().trim().isEmpty() && OptionRBTN4.isFocused()) {
            return OptionRBTN4.getText().toString().trim();
        }
        return "";  // Return an empty string if no selection is made
    }


    private void updateProgress(int progress) {
        GridLayout gridLayout = findViewById(R.id.progressContainer);
        int childCount = gridLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View box = gridLayout.getChildAt(i);
            if (i < progress) {
                // Set the box to black background
                Drawable blackDrawable = ContextCompat.getDrawable(this, R.drawable.background_black);
                box.setBackground(blackDrawable);
            } else {
                // Set the box to grey background
                Drawable greyDrawable = ContextCompat.getDrawable(this, R.drawable.background_grey);
                box.setBackground(greyDrawable);
            }
        }
    }

    private void updateEmployeeSession() {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<EmployeeSession> call = apiService.updateEmployeeSession(EmployeeSession.getId(), EmployeeSession);
        call.enqueue(new Callback<EmployeeSession>() {
            @Override
            public void onResponse(Call<EmployeeSession> call, Response<EmployeeSession> response) {
                if (response.isSuccessful()) {
                    Log.d("DB_CALL", "Successful: updated session id is " + response.body().getId());
                } else {
                    Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<EmployeeSession> call, Throwable t) {
                Log.e("DB_CALL", "Failure: " + t.getMessage());
            }
        });
    }

}