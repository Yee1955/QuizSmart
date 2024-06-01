package InitialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.example.quizsmart.R;

public class RolesSelectionActivity extends AppCompatActivity {
    ToggleButton EmployerBTN, EmployeeBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles_selection);

        initializeView();
        setupButton();
    }

    private void initializeView() {
        EmployerBTN = findViewById(R.id.EmployerButton);
        EmployeeBTN = findViewById(R.id.EmployeeButton);
    }

    private void setupButton() {
        EmployerBTN.setOnClickListener(v -> {
            Intent intent = new Intent(RolesSelectionActivity.this, EmployerActivity.SignUpActivity.class);
            startActivity(intent);
        });

        EmployeeBTN.setOnClickListener(v -> {
            Intent intent = new Intent(RolesSelectionActivity.this, EmployeeActivity.SignUpActivity.class);
            startActivity(intent);
        });
    }
}