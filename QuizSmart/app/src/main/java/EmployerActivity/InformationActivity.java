package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.quizsmart.R;

import java.io.Serializable;
import java.security.cert.Extension;
import java.util.List;
import java.util.StringJoiner;

import HttpModel.*;
import API.*;
import Class.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationActivity extends AppCompatActivity {
    EditText JobTitleET, JobRequirementET, JobResponsibilitiesET, CompanyCultureET;
    ImageButton BackBTN;
    Button GenerateBTN;
    Session Session;
    Employer Employer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_information);
        Session = (Session) getIntent().getSerializableExtra("session");
        Employer = (Employer) getIntent().getSerializableExtra("employer");

        initializeView();
        setupTextWatchers();
        setupEditText();
        setupButton();
    }

    private void initializeView() {
        JobTitleET = findViewById(R.id.JobTitleEditText);
        JobRequirementET = findViewById(R.id.JobRequirementEditText);
        JobResponsibilitiesET = findViewById(R.id.JobResponsibilitiesEditText);
        CompanyCultureET = findViewById(R.id.CompanyCultureEditText);
        BackBTN = findViewById(R.id.BackButton);
        GenerateBTN = findViewById(R.id.GenerateButton);

        GenerateBTN.setEnabled(false);
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
                String JobTitle = JobTitleET.getText().toString().trim();
                String JobRequirement = JobRequirementET.getText().toString().trim();
                String JobResponsibilities = JobResponsibilitiesET.getText().toString().trim();
                String CompanyCulture = CompanyCultureET.getText().toString().trim();

                // Enable the login button only if both fields are not empty
                GenerateBTN.setEnabled(
                        !JobTitle.isEmpty() &&
                        !JobRequirement.isEmpty() &&
                        !JobResponsibilities.isEmpty() &&
                        !CompanyCulture.isEmpty()
                );
            }
        };

        // Apply the text watcher to both text fields
        JobTitleET.addTextChangedListener(loginTextWatcher);
        JobRequirementET.addTextChangedListener(loginTextWatcher);
        JobResponsibilitiesET.addTextChangedListener(loginTextWatcher);
        CompanyCultureET.addTextChangedListener(loginTextWatcher);
    }

    private void setupButton() {
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationActivity.this, EmployerActivity.SummaryActivity.class);
                intent.putExtra("employer", Employer);
                startActivity(intent);
            }
        });
        GenerateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String JobTitle = JobTitleET.getText().toString().trim();
                String JobRequirement = JobRequirementET.getText().toString().trim();
                String JobResponsibilities = JobResponsibilitiesET.getText().toString().trim();
                String CompanyCulture = CompanyCultureET.getText().toString().trim();
                Session = new Session(Employer.getId(), JobTitle, JobRequirement, JobResponsibilities, CompanyCulture, null);
                setupQuiz(JobTitle, JobRequirement, JobResponsibilities, CompanyCulture);
            }
        });
    }

    private void setupQuiz(String title, String requirements, String responsibilities, String culture) {
        ApiService apiService = ApiClient.getApiService("LLM");
        Call<List<QuizResponse>> call = apiService.getQuestions(title, requirements, responsibilities, culture);
        Dialog dialog = ExtensionMethod.showCustomDialog(this, "Generating", R.layout.loading_custom, R.id.LoadingIcon, R.id.ProgressTextView);
        call.enqueue(new Callback<List<QuizResponse>>() {
            @Override
            public void onResponse(Call<List<QuizResponse>> call, Response<List<QuizResponse>> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    List<QuizResponse> QuizList = response.body();
                    Log.d("LLM_CALL", "Successful: " + QuizList.size() + " questions generated");

                    if (QuizList.size() == 10) {
                        // Convert quiz list to string
                        StringJoiner joiner = new StringJoiner("---");
                        for (QuizResponse quiz : QuizList) {
                            joiner.add(quiz.toString());
                        }
                        String questionString = joiner.toString();
                        Session.setQuestionString(questionString);

                        // Pass object and start the next activity
                        Intent intent = new Intent(InformationActivity.this, EmployerActivity.QuestionReviewActivity.class);
                        intent.putExtra("session",  Session);
                        intent.putExtra("employer", Employer);
                        startActivity(intent);
                    } else {
                        ExtensionMethod.showCustomToast(QuizList.get(0).getQuestion(), InformationActivity.this);
                    }
                } else {
                    Log.e("LLM_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<QuizResponse>> call, Throwable t) {
                dialog.dismiss();
                Log.e("LLM_CALL", "Failure: " + t.getMessage());
            }
        });
    }

    private void setupEditText() {
        if (Session != null) {
            JobTitleET.setText(Session.getJobPosition());
            JobRequirementET.setText(Session.getJobRequirement());
            JobResponsibilitiesET.setText(Session.getJobResponsibilities());
            CompanyCultureET.setText(Session.getCompanyCulture());
        }
    }
}