package com.example.a31c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity2 extends AppCompatActivity {

    User user;
    TextView currentQuestionIndex;
    TextView questionTitle;
    TextView questionDetails;
    ProgressBar progressBar;
    Button answerButton1, answerButton2, answerButton3, submitButton;
    ArrayList<Question> quizList = new ArrayList<Question>();
    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Retrieve User's data from login page
        user = (User) getIntent().getSerializableExtra("USER_DATA");

        // Set up view and snackbar
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, "Welcome " + user.getName(), Snackbar.LENGTH_SHORT);

        // To show the Snackbar at the top, we need to customize its layout params
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);

        snackbar.show();

        // Find Components by ID
        currentQuestionIndex = findViewById(R.id.textView);
        questionTitle = findViewById(R.id.textView2);
        questionDetails = findViewById(R.id.textView3);
        progressBar = findViewById(R.id.progressBar3);
        answerButton1 = findViewById(R.id.answer1);
        answerButton2 = findViewById(R.id.answer2);
        answerButton3 = findViewById(R.id.answer3);
        submitButton = findViewById(R.id.submitButton);

        // Create Quiz List
        Quiz();
        user.setTotalNumber(quizList.size());

        // Set up components for each question
        setUpSubmitButtonListener();    // Index increase by here
        QuestionSetUpView();
        setUpAnswerButtonListeners();
    }

    // Create Quiz List using Question class
    public void Quiz() {
        quizList.add(new Question("Animal Homes", "Where do birds live?", "Nest", "Burrow", "Cave"));
        quizList.add(new Question("Counting", "How many legs does a spider have?", "8", "6", "4"));
        quizList.add(new Question("Simple Math", "What is 2 + 2?", "4", "3", "5"));
        quizList.add(new Question("Fruit Colors", "What color are bananas?", "Yellow", "Red", "Purple"));
        quizList.add(new Question("Ocean Life", "What is the largest mammal in the ocean?", "Blue Whale", "Shark", "Dolphin"));
    }

    // Set Up ProgressBar
    public void setUpProgressBar(int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMin(0);
        }
        progressBar.setMax(quizList.size());
        progressBar.setProgress(progress, true);
    }

    // Set up layout for each question
    public void QuestionSetUpView() {

        // Get the current question
        Question currentQuestion = quizList.get(currentIndex);

        currentQuestionIndex.setText((currentIndex) + "/" + quizList.size());
        setUpProgressBar(currentIndex);
        questionTitle.setText(currentQuestion.getQuestionTitle());
        questionDetails.setText(currentQuestion.getQuestionDetails());

        // Change "Next" to "Submit" button if it is the last question
        if (currentIndex == quizList.size() - 1) submitButton.setText("Submit");

        // Create a list to hold all the answers
        ArrayList<String> answers = new ArrayList<>();
        answers.add(currentQuestion.getCorrectAnswer());
        answers.add(currentQuestion.getWrongAnswer1());
        answers.add(currentQuestion.getWrongAnswer2());

        // Shuffle the list to randomize the order of the answers
        Collections.shuffle(answers);

        answerButton1.setText(answers.get(0));
        answerButton2.setText(answers.get(1));
        answerButton3.setText(answers.get(2));
    }

    private void setUpAnswerButtonListeners() {
        // Retrieve current question object
        Question currentQuestion = quizList.get(currentIndex);

        // Reset backgrounds for all buttons to default
        resetAnswerButton();
        View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int green = Color.parseColor("#32962f");

                // Reset backgrounds for all buttons to default
                setAllWrongAnswerColor();

                // Check if the answer is correct
                if (currentQuestion.isCorrect(((Button) v).getText().toString())) {
                    v.setBackgroundColor(green);
                    setAnswerButtonDisable();
                    submitButton.setEnabled(true);

                    // Update User's score
                    user.addScoreByOne();
                } else {
                    // Determine the correct answer and changed its button colour
                    if (currentQuestion.isCorrect(answerButton1.getText().toString())) {
                        answerButton1.setBackgroundColor(green);
                    } else if (currentQuestion.isCorrect(answerButton2.getText().toString())) {
                        answerButton2.setBackgroundColor(green);
                    } else if (currentQuestion.isCorrect(answerButton3.getText().toString())) {
                        answerButton3.setBackgroundColor(green);
                    }

                    setAnswerButtonDisable();
                    submitButton.setEnabled(true);
                }
            }
        };

        answerButton1.setOnClickListener(answerButtonClickListener);
        answerButton2.setOnClickListener(answerButtonClickListener);
        answerButton3.setOnClickListener(answerButtonClickListener);
    }

    private void setUpSubmitButtonListener() {
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (currentIndex == (quizList.size() - 1)) {
                    Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                    intent.putExtra("USER_DATA", user);
                    startActivity(intent);

                    finish();
                } else {
                    currentIndex++;

                    // Set up components for each question
                    setUpSubmitButtonListener();
                    QuestionSetUpView();
                    setUpAnswerButtonListeners();
                }
            }
        });
    }

    private void resetAnswerButton() {
        // Reset button colour
        int defaultBackground = Color.parseColor("#495CC1");
        answerButton1.setBackgroundColor(defaultBackground);
        answerButton2.setBackgroundColor(defaultBackground);
        answerButton3.setBackgroundColor(defaultBackground);

        // Enable all buttons
        answerButton1.setEnabled(true);
        answerButton2.setEnabled(true);
        answerButton3.setEnabled(true);
    }

    private void setAllWrongAnswerColor() {
        // Assuming defaultBackground is the default color or drawable of your buttons
        int red = Color.parseColor("#c93c3c");

        answerButton1.setBackgroundColor(red);
        answerButton2.setBackgroundColor(red);
        answerButton3.setBackgroundColor(red);
    }

    private void setAnswerButtonDisable() {
        answerButton1.setEnabled(false);
        answerButton2.setEnabled(false);
        answerButton3.setEnabled(false);
    }

}