package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.personalizedlearningexperienceapp.R;
import Class.*;
import ManagerDB.ManagerDB;

public class ProfileActivity extends AppCompatActivity {

    User user;
    TextView EmailTV, UsernameTV, QuestionNumberTV, CorrectNumberTV, IncorrectNumberTV, SummaryTV;
    LinearLayout ShareBTN, UpgradeBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra("User");

        initializeView();

        // Setup TextView
        UsernameTV.setText(user.getUsername());
        EmailTV.setText(user.getEmail());
        QuestionNumberTV.setText(String.valueOf(user.getTotalQuestions()));
        CorrectNumberTV.setText(String.valueOf(user.getCorrectlyAnswered()));
        IncorrectNumberTV.setText(String.valueOf(user.getIncorrectAnswers()));

        // Calculate the percentage of correct answers
        int totalQuestions = user.getTotalQuestions();
        int correctAnswers = user.getCorrectlyAnswered();
        double percentage = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;

        // Set formatted summary text with percentage
        SummaryTV.setText(String.format("You've scored %.2f%%! Achieved a moderate performance.", percentage));

        // Setup button
        ShareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String Body = "Sharing Profile";
                String Sub = generateProfileSummary();
                intent.putExtra(Intent.EXTRA_TEXT, Body);
                intent.putExtra(Intent.EXTRA_TEXT, Sub);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });
        UpgradeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpgradeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeView() {
        EmailTV = findViewById(R.id.EmailTextView);
        UsernameTV = findViewById(R.id.UsernameTextView);
        QuestionNumberTV = findViewById(R.id.QuestionNumberTextView);
        CorrectNumberTV = findViewById(R.id.CorrectNumberTextView);
        IncorrectNumberTV = findViewById(R.id.IncorrectNumberTextView);
        SummaryTV = findViewById(R.id.SummaryTextView);
        ShareBTN = findViewById(R.id.ShareButton);
        UpgradeBTN = findViewById(R.id.UpgradeButton);
    }

    // Method to generate the profile summary
    public String generateProfileSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("User Profile Summary:\n");
        summary.append("Username: ").append(user.getUsername()).append("\n");
        summary.append("Email: ").append(user.getEmail()).append("\n");
        summary.append("Phone Number: ").append(user.getPhoneNumber()).append("\n\n");

        summary.append("Interests:\n");
        for (Interest interest : user.getInterests()) {
            summary.append("- ").append(interest.getTopic()).append("\n");
        }

        summary.append("\nThis summary provides an overview of my participation and progress in activities I am involved in. Let's connect and discuss more!\n\n");
        summary.append("Best regards,\n");
        summary.append(user.getUsername()).append("\n");

        return summary.toString();
    }
}