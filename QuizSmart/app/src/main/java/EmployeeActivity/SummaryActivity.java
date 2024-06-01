package EmployeeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quizsmart.R;

import java.util.ArrayList;
import java.util.List;

import API.ApiClient;
import API.ApiService;
import Adapter.VerticalAdapter;
import Class.*;
import EmployerActivity.InformationActivity;
import Enumerable.SessionStatus;
import HttpModel.QuizResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {
    TextView YourNameTV, ScoreTV, TotalSessionsTV;
    EditText SessionCodeET;
    LinearLayout AverageScoreBTN, TotalSessionsBTN;
    RecyclerView RecentRV;
    Employee Employee;
    private int pendingOperations = 0;
    List<EmployeeHistoryDTO> EmployeeHistories = new ArrayList<>(); // Async purpose
    List<EmployeeSession> EmployeeSessions = new ArrayList<>(); // Async purpose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_summary);
        Employee = (Employee) getIntent().getSerializableExtra("employee");

        initializeView();
        setupEditText();
        setupTextView();
        setupButton();
    }

    private void initializeView() {
        YourNameTV = findViewById(R.id.YourNameTextView);
        ScoreTV = findViewById(R.id.ScoreTextView);
        TotalSessionsTV = findViewById(R.id.TotalSessionsTextView);
        SessionCodeET = findViewById(R.id.SessionCodeEditText);
        AverageScoreBTN = findViewById(R.id.AverageScoreButton);
        TotalSessionsBTN = findViewById(R.id.TotalSessionsButton);
        RecentRV = findViewById(R.id.RecentRecyclerView);
    }

    private void setupEditText() {
        SessionCodeET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER) {
                    String SessionCode = SessionCodeET.getText().toString().trim();
                    ApiService apiService = ApiClient.getApiService("DB");
                    Call<Session> call = apiService.getSessionByCode(SessionCode);
                    Dialog dialog = ExtensionMethod.showCustomDialog(SummaryActivity.this, "Finding", R.layout.loading_custom, R.id.LoadingIcon, R.id.ProgressTextView);
                    call.enqueue(new Callback<Session>() {
                        @Override
                        public void onResponse(Call<Session> call, Response<Session> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()) {
                                Log.d("DB_CALL", "Successful: session id is " + response.body().getId());
                                Intent intent = new Intent(SummaryActivity.this, EmployeeActivity.StartQuizActivity.class);
                                intent.putExtra("employee", Employee);
                                intent.putExtra("session", response.body());
                                startActivity(intent);
                            } else {
                                Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                                ExtensionMethod.showCustomToast("Session not found", SummaryActivity.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<Session> call, Throwable t) {
                            dialog.dismiss();
                            Log.e("DB_CALL", "Failure: " + t.getMessage());
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    private void setupTextView() {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<List<EmployeeSession>> call = apiService.getEmployeeSessionByEmployeeId(Employee.getId());
        call.enqueue(new Callback<List<EmployeeSession>>() {
            @Override
            public void onResponse(Call<List<EmployeeSession>> call, Response<List<EmployeeSession>> response) {
                if (response.isSuccessful()) {
                    List<EmployeeSession> employeeSessionList = response.body();
                    Log.d("DB_CALL", "Successful: " + response.body().size() + " employee sessions got by employee id");
                    TotalSessionsTV.setText(String.valueOf(employeeSessionList.size()));

                    // Count average score
                    float averageScore = 0F;
                    if (employeeSessionList.size() != 0) {
                        for (EmployeeSession employeeSession : employeeSessionList) {
                            if (employeeSession.getStatus().equals(SessionStatus.Completed)) {
                                averageScore += employeeSession.getAverageScore();
                            }
                        }
                        averageScore /= employeeSessionList.size();
                        ScoreTV.setText(String.format("%.2f", averageScore));
                    } else {
                        ScoreTV.setText(String.format("%.2f", averageScore));
                    }
                } else {
                    Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                    TotalSessionsTV.setText("0");
                    ScoreTV.setText(String.format("%.2f", 0.00));
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeSession>> call, Throwable t) {
                Log.e("DB_CALL", "Failure: " + t.getMessage());
            }
        });
    }

    private void setupButton() {
        TotalSessionsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, EmployeeActivity.HistoryActivity.class);
                intent.putExtra("employee", Employee);
                startActivity(intent);
            }
        });
        AverageScoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, EmployeeActivity.ScoreActivity.class);
                intent.putExtra("employee", Employee);
                startActivity(intent);
            }
        });
    }

}