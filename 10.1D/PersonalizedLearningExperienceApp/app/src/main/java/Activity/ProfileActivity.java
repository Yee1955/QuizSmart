package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.personalizedlearningexperienceapp.R;
import Class.*;

public class ProfileActivity extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra("User");


    }
}