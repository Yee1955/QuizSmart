package EmployeeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.quizsmart.R;

import API.ApiClient;
import API.ApiService;
import Class.*;
import Enumerable.SessionStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartQuizActivity extends AppCompatActivity {
    ImageButton BackBTN;
    Button StartBTN;
    Employee Employee;
    Session Session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_quiz_start);
        Employee = (Employee) getIntent().getSerializableExtra("employee");
        Session = (Session) getIntent().getSerializableExtra("session");

        initializeView();
        setupButton();
    }

    private void initializeView() {
        BackBTN = findViewById(R.id.BackButton);
        StartBTN = findViewById(R.id.StartButton);
    }

    private void setupButton() {
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartQuizActivity.this, EmployeeActivity.SummaryActivity.class);
                intent.putExtra("employee", Employee);
                startActivity(intent);
            }
        });
        StartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeSession EmployeeSession = new EmployeeSession(Employee.getId(), Session.getId(), 0, SessionStatus.Started, null,
                        0F, 0F, 0F, 0F, 0F, null);
                ApiService apiService = ApiClient.getApiService("DB");
                Call<EmployeeSession> call = apiService.addEmployeeSession(EmployeeSession);
                call.enqueue(new Callback<EmployeeSession>() {
                    @Override
                    public void onResponse(Call<EmployeeSession> call, Response<EmployeeSession> response) {
                        if (response.isSuccessful()) {
                            Log.d("DB_CALL", "Successful: inserted session id is" + response.body().getId());
                            Intent intent = new Intent(StartQuizActivity.this, EmployeeActivity.QuizActivity.class);
                            intent.putExtra("employeeSession", response.body());
                            intent.putExtra("employee", Employee);
                            startActivity(intent);
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
        });
    }
}