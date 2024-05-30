package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.quizsmart.R;

import HttpModel.*;
import API.*;
import Class.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    TextView CompanyNameTV, PublishedSessionsTV, TotalCompletedTV;
    Button StartBTN;
    RecyclerView RecentRV;
    Employer Employer;
    List<Session> sessions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_summary);
        Employer = (Employer) getIntent().getSerializableExtra("employer");

        initializeView();
        setupRetrofit();
        setupTextView();
        setupButton();
    }

    private void initializeView() {
        CompanyNameTV = findViewById(R.id.CompanyName);
        PublishedSessionsTV = findViewById(R.id.PublishedSessionsTextView);
        TotalCompletedTV = findViewById(R.id.TotalCompletedTextView);
        StartBTN = findViewById(R.id.StartButton);
        RecentRV = findViewById(R.id.RecentRecyclerView);
    }

    private void setupTextView() {
        CompanyNameTV.setText(Employer.getCompanyName());
        PublishedSessionsTV.setText(String.valueOf(sessions.size()));
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
    }

    private void setupRetrofit() {
        ApiService apiService = ApiClient.getApiService("DB");
        Call<List<Session>> call = apiService.getEmployerSessions(Employer.getId());
        call.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                if (response.isSuccessful()) {
                    sessions = response.body();
                    Log.d("DB_CALL", "Successful " + sessions);
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
}