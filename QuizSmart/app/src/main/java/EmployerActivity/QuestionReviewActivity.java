package EmployerActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.quizsmart.R;

import java.util.List;

import HttpModel.*;
import Class.*;
import API.*;

public class QuestionReviewActivity extends AppCompatActivity {
    Employer Employer;
    List<QuizResponse> QuizList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_question_review);
        Employer = (Employer) getIntent().getSerializableExtra("employer");
        QuizList = (List<QuizResponse>) getIntent().getSerializableExtra("quizList");
    }
}