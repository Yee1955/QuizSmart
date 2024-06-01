package EmployeeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizsmart.R;

import java.util.ArrayList;
import java.util.List;

import API.ApiClient;
import API.ApiService;
import Adapter.VerticalAdapter;
import Class.*;
import EmployerActivity.QuestionReviewActivity;
import Enumerable.SessionStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    ImageView ImageView;
    TextView EmptyHistory;
    RecyclerView HistoryRV;
    View Line;
    Employee Employee;
    private int pendingOperations = 0;
    List<EmployeeHistoryDTO> EmployeeHistories = new ArrayList<>();
    List<EmployeeSession> EmployeeSessions = new ArrayList<>(); // Async purpose
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_history);
        Employee = (Employee) getIntent().getSerializableExtra("employee");

        initializeView();
        Dialog dialog = ExtensionMethod.showCustomDialog(HistoryActivity.this, "Loading", R.layout.loading_custom, R.id.LoadingIcon, R.id.ProgressTextView);
        loadEmployeeSessions();
        dialog.dismiss();
    }

    private void initializeView() {
        ImageView = findViewById(R.id.ImageView);
        EmptyHistory = findViewById(R.id.EmptyHistory);
        HistoryRV = findViewById(R.id.HistoryRecyclerView);
        Line = findViewById(R.id.Line);
    }

    private void setupRecyclerView() {
        if (EmployeeSessions.size() == 0) {
            ImageView.setVisibility(View.VISIBLE);
            EmptyHistory.setVisibility(View.VISIBLE);
            HistoryRV.setVisibility(View.GONE);
            Line.setVisibility(View.GONE);
        } else {
            ImageView.setVisibility(View.GONE);
            EmptyHistory.setVisibility(View.GONE);
            HistoryRV.setVisibility(View.VISIBLE);
            Line.setVisibility(View.VISIBLE);
            HistoryRV.setLayoutManager(new LinearLayoutManager(this));
            VerticalAdapter<EmployeeHistoryDTO> historyAdapter = new VerticalAdapter<>(EmployeeHistories, new VerticalAdapter.onItemClickListener<EmployeeHistoryDTO>() {
                @Override
                public void itemClick1(EmployeeHistoryDTO item) {
                    ApiService apiService = ApiClient.getApiService("DB");
                    Call<EmployeeSession> call = apiService.getEmployeeSession(item.getEmployeeSessionId());
                    call.enqueue(new Callback<EmployeeSession>() {
                        @Override
                        public void onResponse(Call<EmployeeSession> call, Response<EmployeeSession> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(HistoryActivity.this, EmployeeActivity.QuizActivity.class);
                                intent.putExtra("employee", Employee);
                                intent.putExtra("employeeSession", response.body());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<EmployeeSession> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void itemClick2(EmployeeHistoryDTO item) {
                    ApiService apiService = ApiClient.getApiService("DB");
                    Call<EmployeeSession> call = apiService.getEmployeeSession(item.getEmployeeSessionId());
                    call.enqueue(new Callback<EmployeeSession>() {
                        @Override
                        public void onResponse(Call<EmployeeSession> call, Response<EmployeeSession> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(HistoryActivity.this, EmployeeActivity.ResultActivity.class);
                                intent.putExtra("employee", Employee);
                                intent.putExtra("employeeSession", response.body());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<EmployeeSession> call, Throwable t) {

                        }
                    });
                }
            });
            HistoryRV.setAdapter(historyAdapter);
        }
    }

    private void loadEmployeeSessions() {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<List<EmployeeSession>> call = apiService.getEmployeeSessionByEmployeeId(Employee.getId());
        pendingOperations++;  // Increment because an asynchronous call is being made
        call.enqueue(new Callback<List<EmployeeSession>>() {
            @Override
            public void onResponse(Call<List<EmployeeSession>> call, Response<List<EmployeeSession>> response) {
                if (response.isSuccessful()) {
                    EmployeeSessions = response.body();
                    loadEmployeeHistory();  // This will manage its own pending operations count
                } else {
                    Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
                pendingOperations--;  // Decrement because this operation is complete
                attemptSetupRecyclerView();  // Attempt to setup RecyclerView if all data is loaded
            }

            @Override
            public void onFailure(Call<List<EmployeeSession>> call, Throwable t) {
                Log.e("DB_CALL", "Failure: " + t.getMessage());
                pendingOperations--;  // Decrement because this operation failed but is complete
                attemptSetupRecyclerView();
            }
        });
    }

    private void loadEmployeeHistory() {
        for (EmployeeSession employeeSession : EmployeeSessions) {
            pendingOperations++;  // Increment for each asynchronous operation
            employeeSession.getSessionAsync(new EmployeeSession.SessionCallback() {
                @Override
                public void onSessionLoaded(Session session) {
                    session.getEmployerAsync(new Session.EmployerCallback() {
                        @Override
                        public void onEmployerLoaded(Employer employer) {
                            EmployeeHistoryDTO history = new EmployeeHistoryDTO(employeeSession.getId(), employer.getCompanyName(), session.getJobPosition(), session.getDate(), employeeSession.getStatus());
                            EmployeeHistories.add(history);
                            pendingOperations--;  // Decrement upon successful load
                            attemptSetupRecyclerView();
                        }

                        @Override
                        public void onError(String error) {
                            pendingOperations--;  // Decrement even on error to avoid deadlock
                            attemptSetupRecyclerView();
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    pendingOperations--;
                    attemptSetupRecyclerView();
                }
            });
        }
    }

    private void attemptSetupRecyclerView() {
        if (pendingOperations == 0) {  // Check if all operations are completed
            runOnUiThread(this::setupRecyclerView);  // Ensure setupRecyclerView runs on the UI thread
        }
    }
}