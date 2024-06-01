package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizsmart.R;
import Class.*;
import Enumerable.SessionStatus;

public class ResultActivity extends AppCompatActivity {
    ImageButton BackBTN;
    TextView ScoreTV, SummaryTV, CandidateInformationTV;
    ProgressBar AlignmentPB, ProblemSolvingPB, CommunicationPB, InnovationPB, CulturalPB;
    Employer Employer;
    Session Session;
    EmployeeSession EmployeeSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_result);
        Employer = (Employer) getIntent().getSerializableExtra("employer");
        EmployeeSession = (EmployeeSession) getIntent().getSerializableExtra("employeeSession");
        Session = (Session) getIntent().getSerializableExtra("session");

        initializeView();
        setupProgressBar();
        setupTextView();
        setupButton();
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

    private void setupTextView() {
        ScoreTV.setText(String.valueOf(EmployeeSession.getAverageScore()));
        if (EmployeeSession.getSummary() != null) SummaryTV.setText(EmployeeSession.getSummary());
        EmployeeSession.getEmployeeAsync(new EmployeeSession.EmployeeCallback() {
            @Override
            public void onEmployeeLoaded(Employee employee) {
                CandidateInformationTV.setText("Email: " + employee.getEmail() + "\nFull Name: " + employee.getFullName());
            }

            @Override
            public void onError(String error) {
                CandidateInformationTV.setText("Email: Unknown\nFullName: Unknown");
            }
        });
    }

    private void setupButton() {
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, EmployerActivity.SessionInformationActivity.class);
                intent.putExtra("session", Session);
                intent.putExtra("employer", Employer);
                startActivity(intent);
            }
        });
    }

    private void setupProgressBar() {
        AlignmentPB.setProgress((int) (EmployeeSession.getScoreAlignment() * 100), true);
        ProblemSolvingPB.setProgress((int) (EmployeeSession.getScoreProblemSolving() * 100), true);
        CommunicationPB.setProgress((int) (EmployeeSession.getScoreCommunication() * 100), true);
        InnovationPB.setProgress((int) (EmployeeSession.getScoreInnovation() * 100), true);
        CulturalPB.setProgress((int) (EmployeeSession.getScoreTeamFit() * 100), true);
    }
}