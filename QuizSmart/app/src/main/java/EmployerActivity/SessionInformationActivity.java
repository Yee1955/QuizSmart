package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quizsmart.R;

import java.util.ArrayList;
import java.util.List;

import API.ApiClient;
import API.ApiService;
import Adapter.VerticalAdapter;
import Class.*;
import Enumerable.SessionStatus;
import HttpModel.QuizResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionInformationActivity extends AppCompatActivity {
    ImageButton BackBTN;
    TextView JobPositionTV, SessionCodeTV;
    RecyclerView CandidatesRV;
    Button EndBTN;
    Session Session;
    Employer Employer;
    List<EmployeeSession> EmployeeSessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_session_information);
        Session = (Session) getIntent().getSerializableExtra("session");
        Employer = (Employer) getIntent().getSerializableExtra("employer");

        initializeView();
        setupTextView();
        setupButton();
        setupRecyclerView();
    }

    private void initializeView() {
        BackBTN = findViewById(R.id.BackButton);
        JobPositionTV = findViewById(R.id.JobPositionTextView);
        SessionCodeTV = findViewById(R.id.SessionCodeTextView);
        CandidatesRV = findViewById(R.id.CandidatesRecyclerView);
        EndBTN = findViewById(R.id.EndButton);

        EndBTN.setEnabled(Session.getStatus().equals(SessionStatus.Started));
    }

    private void setupTextView() {
        JobPositionTV.setText(Session.getJobPosition());
        SessionCodeTV.setText(Session.getSessionCode());
    }

    private void setupButton() {
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionInformationActivity.this, EmployerActivity.SummaryActivity.class);
                intent.putExtra("employer", Employer);
                startActivity(intent);
            }
        });
        EndBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.setStatus(SessionStatus.Ended);
                ApiService apiService = ApiClient.getApiService("DB");
                Call<Session> call = apiService.updateSession(Session.getId());
                call.enqueue(new Callback<Session>() {
                    @Override
                    public void onResponse(Call<Session> call, Response<Session> response) {
                        if (response.isSuccessful()) {
                            if (Session.getId() == response.body().getId()) Session = response.body();
                            Log.d("DB_CALL", "Successful: status is " + Session.getStatus());
                            initializeView();
                        } else {
                            Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<Session> call, Throwable t) {
                        Log.e("DB_CALL", "Failure: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void setupRecyclerView() {
        CandidatesRV.setLayoutManager(new LinearLayoutManager(this));
        VerticalAdapter<EmployeeSession> reviewAdapter = new VerticalAdapter<>(EmployeeSessions, null);
        CandidatesRV.setAdapter(reviewAdapter);

        // Get "EmployeeSession" by session id
        ApiService apiService = ApiClient.getApiService("DB");
        Call<List<EmployeeSession>> call = apiService.getEmployeeSessionBySessionId(Session.getId());
        call.enqueue(new Callback<List<EmployeeSession>>() {
            @Override
            public void onResponse(Call<List<EmployeeSession>> call, Response<List<EmployeeSession>> response) {
                if (response.isSuccessful()) {
                    Log.d("DB_CALL", "Successful: " + response.body().size() + " employee sessions got");
                    EmployeeSessions.clear();
                    EmployeeSessions.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();
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
}