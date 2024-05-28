package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.personalizedlearningexperienceapp.R;

import java.util.List;

import Adapter.VerticalAdapter;
import Class.*;

public class ResultActivity extends AppCompatActivity {
    private List<Result> resultList;
    RecyclerView ResultRV;
    VerticalAdapter ResultAdapter;
    Button ContinueBTN;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        user = (User) getIntent().getSerializableExtra("User");

        // Initialize view components
        ResultRV = (RecyclerView) findViewById(R.id.ResultRecyclerView);
        ContinueBTN = (Button) findViewById(R.id.ContinueButton);

        // Get data from previous activity
        resultList = (List<Result>) getIntent().getSerializableExtra("ResultList");

        // Add question counting to user object
        for (Result result : resultList) {
            user.setTotalQuestions(user.getTotalQuestions() + 1); // Counting total answered
            if (result.getCorrect()) user.setCorrectlyAnswered(user.getCorrectlyAnswered() + 1);
            else user.setIncorrectAnswers(user.getIncorrectAnswers() + 1);
        }

        // Setup recyclerview
        ResultRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ResultAdapter = new VerticalAdapter(resultList, null);
        ResultRV.setAdapter(ResultAdapter);

        // Setup button
        ContinueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, TaskActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}