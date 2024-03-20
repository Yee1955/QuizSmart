package com.example.a31c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {

    User user;
    TextView titleName;
    TextView score;
    Button takeNewQuiz;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        titleName = findViewById(R.id.textView4);
        score = findViewById(R.id.textView6);
        takeNewQuiz = findViewById(R.id.button2);
        finish = findViewById(R.id.button4);

        // Retrieve User's data from quiz pages
        user = (User) getIntent().getSerializableExtra("USER_DATA");

        // Compute percentage
        float percentage = (float)(user.getScore() / (float)user.getTotalNumber());

        // Update relevant information on components
        if (percentage > 0.5) titleName.setText("Congratulations " + user.getName());
        else titleName.setText("Try it again, " + user.getName());
        score.setText(user.getScore() + "/" + user.getTotalNumber());

        takeNewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset User score
                user.setScore(0);

                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                intent.putExtra("USER_DATA", user);
                startActivity(intent);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

}