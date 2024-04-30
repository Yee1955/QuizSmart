package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalizedlearningexperienceapp.R;
import Class.*;

import org.w3c.dom.Text;

import ManagerDB.ManagerDB;

public class MainActivity extends AppCompatActivity {
    EditText usernameET, passwordET;
    TextView signUpTV;
    Button loginBTN;
    ManagerDB managerDB;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        managerDB = new ManagerDB(this);
        usernameET = (EditText) findViewById(R.id.UsernameText);
        passwordET = (EditText) findViewById(R.id.PasswordText);
        loginBTN = (Button) findViewById(R.id.LoginButton);
        signUpTV = (TextView) findViewById(R.id.NeedAnAccountTextView);

        setupButton();
    }

    private void setupButton() {
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = usernameET.getText().toString().trim();
                String Password = passwordET.getText().toString().trim();
                if (Username.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Please fill in all the information",
                            Toast.LENGTH_SHORT).show();
                } else {
                    user = managerDB.verifyCredentials(Username, Password);
                    if (user != null) {
                        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Invalid Information",
                                Toast.LENGTH_SHORT).show();
                    }
                    ;
                }
            }
        });
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}