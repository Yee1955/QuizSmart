package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizsmart.R;

import java.util.List;

import Adapter.VerticalAdapter;
import Class.*;

public class HistoryActivity extends AppCompatActivity {

    ImageButton BackBTN;
    TextView EmptyHistory;
    ImageView EmptyImageView;
    RecyclerView HistoryRV;
    View Line;
    Employer Employer;
    List<Session> Sessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_history);
        Employer = (Employer) getIntent().getSerializableExtra("employer");
        Sessions = (List<Session>) getIntent().getSerializableExtra("sessions");

        initializeView();
        setupRecyclerView();
        setupButton();
    }

    private void initializeView() {
        BackBTN = findViewById(R.id.BackButton);
        EmptyHistory = findViewById(R.id.EmptyHistory);
        EmptyImageView = findViewById(R.id.EmptyImageView);
        HistoryRV = findViewById(R.id.HistoryRecyclerView);
        Line = findViewById(R.id.Line);
    }

    private void setupRecyclerView() {
        if (Sessions == null || Sessions.isEmpty()) {
            EmptyImageView.setVisibility(View.VISIBLE);
            EmptyImageView.setVisibility(View.VISIBLE);
            EmptyHistory.setVisibility(View.VISIBLE);
            HistoryRV.setVisibility(View.GONE);
            Line.setVisibility(View.GONE);
        } else {
            EmptyImageView.setVisibility(View.GONE);
            EmptyHistory.setVisibility(View.GONE);
            HistoryRV.setVisibility(View.VISIBLE);
            Line.setVisibility(View.VISIBLE);
            HistoryRV.setLayoutManager(new LinearLayoutManager(this));
            VerticalAdapter<Session> historyAdapter = new VerticalAdapter<>(Sessions, new VerticalAdapter.onItemClickListener<Session>() {
                @Override
                public void itemClick2(Session item) {

                }

                @Override
                public void itemClick1(Session item) {
                    Intent intent = new Intent(HistoryActivity.this, EmployerActivity.SessionInformationActivity.class);
                    intent.putExtra("session", item);
                    intent.putExtra("employer", Employer);
                    startActivity(intent);
                }
            });
            HistoryRV.setAdapter(historyAdapter);
        }
    }

    private void setupButton() {
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, EmployerActivity.SummaryActivity.class);
                intent.putExtra("employer", Employer);
                startActivity(intent);
            }
        });
    }
}