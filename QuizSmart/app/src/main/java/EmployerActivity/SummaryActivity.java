package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.quizsmart.R;

import Adapter.VerticalAdapter;
import HttpModel.*;
import API.*;
import Class.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    TextView CompanyNameTV, PublishedSessionsTV, TotalCompletedTV;
    LinearLayout PublishedBTN;
    Button StartBTN;
    RecyclerView RecentRV;
    Employer Employer;
    List<Session> sessions = new ArrayList<>();
    int Count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_summary);
        Employer = (Employer) getIntent().getSerializableExtra("employer");

        initializeView();
        setupSessionList();
        setupTextView();
        setupButton();
    }

    private void initializeView() {
        CompanyNameTV = findViewById(R.id.CompanyName);
        PublishedSessionsTV = findViewById(R.id.PublishedSessionsTextView);
        TotalCompletedTV = findViewById(R.id.TotalCompletedTextView);
        StartBTN = findViewById(R.id.StartButton);
        PublishedBTN = findViewById(R.id.PublishedButton);
        RecentRV = findViewById(R.id.RecentRecyclerView);
    }

    private void setupTextView() {
        CompanyNameTV.setText(Employer.getCompanyName());
        PublishedSessionsTV.setText(String.valueOf(sessions.size()));
        TotalCompletedTV.setText(String.valueOf(Count));
    }

    private void setupButton() {
        StartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, EmployerActivity.InformationActivity.class);
                intent.putExtra("employer", Employer);
                startActivity(intent);
            }
        });
        PublishedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, EmployerActivity.HistoryActivity.class);
                intent.putExtra("employer", Employer);
                intent.putExtra("sessions", (Serializable) sessions);
                startActivity(intent);
            }
        });
    }

    private void setupSessionList() {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<List<Session>> call = apiService.getEmployerSessions(Employer.getId());
        call.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                if (response.isSuccessful()) {
                    sessions = response.body();
                    Log.d("DB_CALL", "Successful Get Sessions: " + sessions);
                    setupTextView();
                    countCompleted();
                    setupRecyclerView();
                } else {
                    Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                Log.e("DB_CALL", "Failure: " + t.getMessage());
            }
        });
    }

    private void countCompleted() {
        ApiService apiService = ApiClient.getApiService("DB");
        for (Session Session : sessions) {
            Call<List<EmployeeSession>> call = apiService.getEmployeeSessionBySessionId(Session.getId());
            call.enqueue(new Callback<List<EmployeeSession>>() {
                @Override
                public void onResponse(Call<List<EmployeeSession>> call, Response<List<EmployeeSession>> response) {
                    if (response.isSuccessful()) {
                        Log.d("DB_CALL", "Successful Count: " + response.body().size() + " employee sessions got");
                        Count += response.body().size();
                        setupTextView();
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

    private void setupRecyclerView() {
        RecentRV.setLayoutManager(new LinearLayoutManager(this));
        VerticalAdapter<Session> recentAdapter = new VerticalAdapter<>(sessions, new VerticalAdapter.onItemClickListener<Session>() {
            @Override
            public void itemClick2(Session item) {

            }

            @Override
            public void itemClick1(Session item) {
                Intent intent = new Intent(SummaryActivity.this, EmployerActivity.SessionInformationActivity.class);
                intent.putExtra("session", item);
                intent.putExtra("employer", Employer);
                startActivity(intent);
            }

        });
        RecentRV.setAdapter(recentAdapter);
    }
}