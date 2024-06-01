package EmployeeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quizsmart.R;
import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import API.ApiClient;
import API.ApiService;
import Class.*;
import EmployerActivity.QuestionReviewActivity;
import Enumerable.SessionStatus;
import HttpModel.ChatRequest;
import HttpModel.ChatResponse;
import HttpModel.QuizResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreActivity extends AppCompatActivity {
    Employee Employee;
    RadarView mRadarView;
    TextView DescriptionTV;
    ImageButton BackButton;
    List<EmployeeSession> EmployeeSessions; // Async purpose
    StringJoiner SummaryList = new StringJoiner("---");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_score);
        Employee = (Employee) getIntent().getSerializableExtra("employee");

        initializeView();
        setupButton();
        loadEmployeeSession();
    }

    private void initializeView() {
        mRadarView = findViewById(R.id.radar);
        DescriptionTV = findViewById(R.id.DescriptionTextView);
        BackButton = findViewById(R.id.BackButton);
    }

    private void setupRadarChart(List<RadarHolder> RadarDataList) {
        mRadarView.setData(RadarDataList);
        mRadarView.setInteractive(true);
    }

    private void setupButton() {
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, EmployeeActivity.SummaryActivity.class);
                intent.putExtra("employee", Employee);
                startActivity(intent);
            }
        });
    }

    private void loadEmployeeSession() {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<List<EmployeeSession>> call = apiService.getEmployeeSessionByEmployeeId(Employee.getId());
        call.enqueue(new Callback<List<EmployeeSession>>() {
            @Override
            public void onResponse(Call<List<EmployeeSession>> call, Response<List<EmployeeSession>> response) {
                if (response.isSuccessful()) {
                    Log.d("DB_CALL", "Successful: " + response.body().size() + " employee sessions got by employee id");
                    EmployeeSessions = response.body();

                    // Count average and concatenate summary
                    int Count = 0;
                    float Alignment = 0f, Communication  = 0f, Cultural  = 0f, Innovation  = 0f, ProblemSolving  = 0f;
                    for (int i = 0; i < EmployeeSessions.size(); i++) {
                        if (EmployeeSessions.get(i).getSummary() != null && i != 11) {
                            SummaryList.add(EmployeeSessions.get(i).getSummary());
                        } else if (EmployeeSessions.get(i).getSummary() != null) {
                            Count++;
                            Alignment += EmployeeSessions.get(i).getScoreAlignment();
                            Communication += EmployeeSessions.get(i).getScoreCommunication();
                            Cultural += EmployeeSessions.get(i).getScoreTeamFit();
                            Innovation += EmployeeSessions.get(i).getScoreInnovation();
                            ProblemSolving += EmployeeSessions.get(i).getScoreProblemSolving();

                        }
                    }

                    // Setup radar chart
                    List<RadarHolder> RadarDataList = new ArrayList<>();
                    RadarDataList.add(new RadarHolder("AL", (int) (Alignment / Count)));
                    RadarDataList.add(new RadarHolder("CS", (int) (Communication / Count)));
                    RadarDataList.add(new RadarHolder("CTF", (int) (Cultural / Count)));
                    RadarDataList.add(new RadarHolder("IC", (int) (Innovation / Count)));
                    RadarDataList.add(new RadarHolder("PB", (int) (ProblemSolving / Count)));
                    setupRadarChart(RadarDataList);

                    // Summarise
                    String query = "Generate a direct feedback summary based on the summary list provided. " +
                            "The feedback should reflect the overall performance and highlight key areas of strength or areas for improvement, " +
                            "focusing directly on the candidate's qualities without an introductory sentence. Keep the summary brief, not exceeding 50 words.\n\n" +
                            "Summary List: " + SummaryList + "(split by \"---\")";
                    loadSummary(query);

                } else {
                    Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());

                }
            }

            @Override
            public void onFailure(Call<List<EmployeeSession>> call, Throwable t) {
                Log.e("DB_CALL", "Failure: " + t.getMessage());
            }
        });
    }

    private void loadSummary(String inputMessage) {
        ApiService apiService = ApiClient.getApiService("LLM");
        Call<ChatResponse> call = apiService.normalChat(new ChatRequest(inputMessage));
        Dialog dialog = ExtensionMethod.showCustomDialog(ScoreActivity.this, "Summarizing", R.layout.loading_custom, R.id.LoadingIcon, R.id.ProgressTextView);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("LLM_CALL", "Successful: summary generated");
                    DescriptionTV.setText(response.body().getResponse());
                } else {
                    Log.e("LLM_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("LLM_CALL", "Failure: " + t.getMessage());
            }
        });
    }
}