package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalizedlearningexperienceapp.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import API.*;
import Class.*;
import Adapter.*;
import ManagerDB.ManagerDB;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskDetailsActivity extends AppCompatActivity {
    RecyclerView QuizRV;
    String topic;
    String description;
    String[] quizSelection;
    User user;
    TextView TitleTV, DescriptionTV;
    Button SubmitBTN;
    ManagerDB managerDB;

    private VerticalAdapter<Quiz> quizAdapter;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        managerDB = new ManagerDB(this);

        // Get data from previous activity
        topic = (String) getIntent().getExtras().get("Topic");
        description = (String) getIntent().getExtras().get("Description");
        user = (User) getIntent().getSerializableExtra("User");

        // Initialize view components
        QuizRV = (RecyclerView) findViewById(R.id.QuizRecyclerView);
        TitleTV = (TextView) findViewById(R.id.GeneratedTask);
        DescriptionTV = (TextView) findViewById(R.id.SmallDescriptionTextView);
        SubmitBTN = (Button) findViewById(R.id.submitButton);

        SubmitBTN.setEnabled(false);
        QuizRV.setLayoutManager(new LinearLayoutManager(this));
        TitleTV.setText(topic);
        DescriptionTV.setText(description);
        quizSelection = new String[3];

        // Setup progress bar
        ProgressDialog pd = new ProgressDialog(TaskDetailsActivity.this);
        pd.setMessage("loading");
        pd.show();

        // Generate quiz
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // this will set the read timeout for 10mins (IMPORTANT: If not your request will exceed the default read timeout)
                .build();

        QuizApiService apiService = retrofit.create(QuizApiService.class);
        Call<TaskResponse> call = apiService.getQuiz(topic);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("TaskDetails", "Raw JSON response: " + new Gson().toJson(response.body()));
                    TaskResponse TaskResponse = response.body();
                    quizList = TaskResponse.getQuizList();
                    initQuizAdapter(TaskResponse.getQuizList());
                    SubmitBTN.setEnabled(true);
                } else {
                    Log.e("TaskDetails", "Response not successful: " + response.code());
                    Toast.makeText(TaskDetailsActivity.this, "Response not successful: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e("TaskDetails", "Failed to fetch data", t);
                pd.dismiss();
            }
        });

        // Setup Button
        SubmitBTN.setOnClickListener(v -> {
            for (String selection : quizSelection) {
                if (selection == null || selection.isEmpty()) {
                    Toast.makeText(TaskDetailsActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            List<Result> resultList = new ArrayList<>();

            // Create resultList
            int i = 0;
            for (Quiz quiz : quizList) {
                resultList.add(new Result(quiz, quizSelection[i]));
                i++;
            }

            // Delete user interest after submitted
            managerDB.deleteUserInterest(user.getId(), topic);

            // Pass all the value and start for next activity
            Intent intent = new Intent(TaskDetailsActivity.this, ResultActivity.class);
            intent.putExtra("ResultList", (Serializable) resultList);
            intent.putExtra("User", user);
            startActivity(intent);
        });
    }

    private void initQuizAdapter(List<Quiz> quizzes) {
        quizAdapter = new VerticalAdapter<>(quizzes, (item, selection) -> {
            int position = quizzes.indexOf(item); // Get position as ID substitute
            Log.d("QuizSelection", "Quiz Position: " + position + " Selection: " + selection);
            quizSelection[position] = selection; // Update selection based on position
        });
        QuizRV.setAdapter(quizAdapter);
    }
}