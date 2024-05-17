package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.personalizedlearningexperienceapp.R;

import java.util.List;

import Adapter.*;
import Class.*;
import ManagerDB.*;

public class SelectInterestActivity extends AppCompatActivity {
    RecyclerView InterestRV;
    Button NextBTN;
    List<Interest> interestData;
    List<Interest> userInterests;
    ManagerDB managerDB;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interest);

        user = (User) getIntent().getSerializableExtra("User");
        InterestRV = (RecyclerView) findViewById(R.id.InterestRecyclerView);
        NextBTN = (Button) findViewById(R.id.NextButton);
        managerDB = new ManagerDB(this);

        // Setup recyclerview adapter
        interestData = managerDB.getAllInterests();
        InterestVerticalAdapter interestAdapter = new InterestVerticalAdapter(interestData, null);
        InterestRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        InterestRV.setAdapter(interestAdapter);

        // Setup Button
        NextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInterests = ((InterestVerticalAdapter) InterestRV.getAdapter()).getReturnData();
                for (Interest interest : userInterests) {
                    managerDB.addUserInterest(user.getId(), interest.getId());
                }
                Toast.makeText(SelectInterestActivity.this, "Interests updated.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectInterestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}