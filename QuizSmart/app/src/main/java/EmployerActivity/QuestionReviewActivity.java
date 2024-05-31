package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.quizsmart.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import Adapter.VerticalAdapter;
import Enumerable.SessionStatus;
import HttpModel.*;
import Class.*;
import API.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionReviewActivity extends AppCompatActivity {
    ImageButton BackBTN;
    RecyclerView QuestionReviewRV;
    LinearLayout RegenerateBTN;
    ImageView RegenerateIcon;
    Button PublishBTN;
    Session Session;
    Employer Employer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_question_review);
        Session = (Session) getIntent().getSerializableExtra("session");
        Employer = (Employer) getIntent().getSerializableExtra("employer");

        initializeView();
        setupAdapter();
        setupButton();
    }

    private void initializeView() {
        BackBTN = findViewById(R.id.BackButton);
        QuestionReviewRV = findViewById(R.id.QuestionReviewRecyclerView);
        RegenerateBTN = findViewById(R.id.RegenerateButton);
        PublishBTN = findViewById(R.id.PublishButton);
        RegenerateIcon = findViewById(R.id.RegenerateIcon);
    }

    private void setupButton() {
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionReviewActivity.this, EmployerActivity.InformationActivity.class);
                intent.putExtra("session", Session);
                intent.putExtra("employer", Employer);
                startActivity(intent);
            }
        });
        RegenerateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupQuiz(Session.getJobPosition(), Session.getJobRequirement(), Session.getJobResponsibilities(), Session.getCompanyCulture());
            }
        });
        PublishBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = ExtensionMethod.showCustomDialog(QuestionReviewActivity.this, "Publishing", R.layout.loading_custom, R.id.LoadingIcon, R.id.ProgressTextView);
                ApiService apiService = ApiClient.getApiService("DB");
                Session.setStatus(SessionStatus.Started);
                Call<Session> call = apiService.addSession(Session);
                call.enqueue(new Callback<Session>() {
                    @Override
                    public void onResponse(Call<Session> call, Response<Session> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Session = response.body();
                            Log.d("DB_CALL", "Successful: Session Code is " + Session.getSessionCode() + " questions generated");
                            Intent intent = new Intent(QuestionReviewActivity.this, EmployerActivity.SessionInformationActivity.class);
                            intent.putExtra("session", Session);
                            intent.putExtra("employer", Employer);
                            startActivity(intent);
                        } else {
                            Log.e("DB_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<Session> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("DB_CALL", "Failure: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void setupAdapter() {
        QuestionReviewRV.setLayoutManager(new LinearLayoutManager(this));

        // Parse string to QuizResponse object
        List<QuizResponse> QuizList = new ArrayList<>();
        String[] questions = Session.getQuestionString().split("---");
        for (String question : questions) {
            String questionTitle = question.split(":::")[0];
            List<String> options = new ArrayList<>();
            options.add(question.split(":::")[1]);
            options.add(question.split(":::")[2]);
            options.add(question.split(":::")[3]);
            QuizList.add(new QuizResponse(questionTitle, options));
        }

        VerticalAdapter<QuizResponse> ReviewAdapter = new VerticalAdapter<>(QuizList, null);
        QuestionReviewRV.setAdapter(ReviewAdapter);
    }

    private void setupQuiz(String title, String requirements, String responsibilities, String culture) {
        ApiService apiService = ApiClient.getApiService("LLM");
        Call<List<QuizResponse>> call = apiService.getQuestions(title, requirements, responsibilities, culture);
        RegenerateIcon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_360));    // Start rotate icon
        call.enqueue(new Callback<List<QuizResponse>>() {
            @Override
            public void onResponse(Call<List<QuizResponse>> call, Response<List<QuizResponse>> response) {
                RegenerateIcon.clearAnimation();    // Stop rotation
                if (response.isSuccessful()) {
                    Log.d("LLM_CALL", "Successful: " + response.body().size() + " questions generated");
                    if (response.body().size() == 10) {
                        // Convert quiz list to string
                        List<QuizResponse> QuizList = response.body();
                        StringJoiner joiner = new StringJoiner("---");
                        for (QuizResponse quiz : QuizList) {
                            joiner.add(quiz.toString());
                        }
                        String questionString = joiner.toString();
                        Session.setQuestionString(questionString);
                        setupAdapter();
                    } else {
                        ExtensionMethod.showCustomToast("Something went wrong, please try again", QuestionReviewActivity.this);
                    }
                } else {
                    Log.e("LLM_CALL", "Error with status code: " + response.code() + " and body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<QuizResponse>> call, Throwable t) {
                RegenerateIcon.clearAnimation();    // Stop rotation
                Log.e("LLM_CALL", "Failure: " + t.getMessage());
            }
        });
    }
}