package EmployeeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizsmart.R;

import java.util.StringJoiner;

import API.ApiClient;
import API.ApiService;
import Class.*;
import EmployerActivity.QuestionReviewActivity;
import HttpModel.EvaluationRequest;
import HttpModel.EvaluationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {
    ImageButton BackBTN;
    TextView ScoreTV, SummaryTV, CandidateInformationTV;
    ProgressBar AlignmentPB, ProblemSolvingPB, CommunicationPB, InnovationPB, CulturalPB;
    EmployeeSession EmployeeSession;
    Employee Employee;
    Session Session;    // Async purpose
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_result);
        EmployeeSession = (EmployeeSession) getIntent().getSerializableExtra("employeeSession");
        Employee = (Employee) getIntent().getSerializableExtra("employee");
        if (EmployeeSession == null || Employee == null) {
            Log.e("IntentError", "EmployeeSession or Employee is null before sending.");
        }
        initializeView();
        setupButton();
        setupTextView();
        setupProgressBar();
    }

    private void initializeView() {
        BackBTN = findViewById(R.id.BackButton);
        ScoreTV = findViewById(R.id.ScoreTextView);
        SummaryTV = findViewById(R.id.SummaryTextView);
        CandidateInformationTV = findViewById(R.id.CandidateInformationTextView);
        AlignmentPB = findViewById(R.id.AlignmentProgressBar);
        ProblemSolvingPB = findViewById(R.id.ProblemSolvingProgressBar);
        CommunicationPB = findViewById(R.id.CommunicationProgressBar);
        InnovationPB = findViewById(R.id.InnovationProgressBar);
        CulturalPB = findViewById(R.id.CulturalProgressBar);
    }

    private void setupButton() {
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, EmployeeActivity.SummaryActivity.class);
                intent.putExtra("employee", Employee);
                startActivity(intent);
            }
        });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale" })
    private void setupTextView() {
        if (Employee != null) {
            CandidateInformationTV.setText("Email: " + Employee.getEmail() + "\nFull Name: " + Employee.getFullName());
        } else {
            CandidateInformationTV.setText("Email: Unknown\nFullName: Unknown");
        }
        if (EmployeeSession != null) {
            if (EmployeeSession.getSummary() != null) {
                System.out.println(EmployeeSession.getScoreAlignment());
                ScoreTV.setText(String.format("%.2f", EmployeeSession.getAverageScore()));
                SummaryTV.setText(EmployeeSession.getSummary());
                setupProgressBar();
            } else {
                loadResult();
            }
        } else {
            System.out.println("EmployeeSession object is null.");
        }
    }

    private void setupProgressBar() {
        if (EmployeeSession != null && EmployeeSession.getSummary() != null) {
            AlignmentPB.setProgress((int) (EmployeeSession.getScoreAlignment() * 100), true);
            ProblemSolvingPB.setProgress((int) (EmployeeSession.getScoreProblemSolving() * 100), true);
            CommunicationPB.setProgress((int) (EmployeeSession.getScoreCommunication() * 100), true);
            InnovationPB.setProgress((int) (EmployeeSession.getScoreInnovation() * 100), true);
            CulturalPB.setProgress((int) (EmployeeSession.getScoreTeamFit() * 100), true);
        }
    }

    private void loadResult() {
        // Load question string
        EmployeeSession.getSessionAsync(new EmployeeSession.SessionCallback() {
            @Override
            public void onSessionLoaded(Session session) {
                Session = session;
                StringJoiner questionString = new StringJoiner("---");
                String[] questions = Session.getQuestionString().split("---");
                for (String question : questions) {
                    questionString.add(question.split(":::")[0]);
                }

                // Load job details
                StringJoiner jobDetailsString = new StringJoiner("---");
                jobDetailsString.add(Session.getJobPosition());
                jobDetailsString.add(Session.getJobRequirement());
                jobDetailsString.add(Session.getJobResponsibilities());
                jobDetailsString.add(Session.getCompanyCulture());

                loadScoreAndSummary(questionString.toString(), jobDetailsString.toString());
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void loadScoreAndSummary(String questionString, String jobDetailsString) {
        // Create request body
        EvaluationRequest evaluationRequest = new EvaluationRequest(questionString, EmployeeSession.getAnswerString(), jobDetailsString);

        ApiService apiService = ApiClient.getApiService("LLM");
        Call<EvaluationResponse> call = apiService.evaluateCandidate(evaluationRequest);
        Dialog dialog = ExtensionMethod.showCustomDialog(ResultActivity.this, "Evaluating", R.layout.loading_custom, R.id.LoadingIcon, R.id.ProgressTextView);
        call.enqueue(new Callback<EvaluationResponse>() {
            @Override
            public void onResponse(Call<EvaluationResponse> call, Response<EvaluationResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("LLM_CALL", "Successful: Summary (" + response.body().getSummary() + ")");
                    EmployeeSession.setScoreAlignment(response.body().getScores().get("AL"));
                    EmployeeSession.setScoreCommunication(response.body().getScores().get("CS"));
                    EmployeeSession.setScoreTeamFit(response.body().getScores().get("CTF"));
                    EmployeeSession.setScoreInnovation(response.body().getScores().get("IC"));
                    EmployeeSession.setScoreProblemSolving(response.body().getScores().get("PB"));
                    EmployeeSession.setSummary(response.body().getSummary());
                    updateEmployeeSession();
                    setupTextView();
                    setupProgressBar();
                } else {
                    Log.e("LLM_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<EvaluationResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("LLM_CALL", "Failure: " + t.getMessage());
            }
        });
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