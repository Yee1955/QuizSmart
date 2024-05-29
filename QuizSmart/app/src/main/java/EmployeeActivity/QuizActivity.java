package EmployeeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.example.quizsmart.R;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_quiz);
    }

    public void updateProgress(int progress) {
        GridLayout gridLayout = findViewById(R.id.progressContainer);
        int childCount = gridLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View box = gridLayout.getChildAt(i);
            if (i < progress) {
                // Set the box to black background
                Drawable blackDrawable = ContextCompat.getDrawable(this, R.drawable.background_black);
                box.setBackground(blackDrawable);
            } else {
                // Set the box to grey background
                Drawable greyDrawable = ContextCompat.getDrawable(this, R.drawable.background_grey);
                box.setBackground(greyDrawable);
            }
        }
    }

}