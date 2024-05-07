package com.example.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {
    RadioGroup TypeRG;
    EditText NameET, PhoneET, DescriptionET, DateET, LocationET;
    Button SaveBTN;
    ManagerDB managerDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        managerDB = new ManagerDB(this);

        InitializeView();
        SetupButton();
    }

    private void InitializeView() {
        TypeRG = findViewById(R.id.TypeRadioGroup);
        NameET = findViewById(R.id.NameEditText);
        PhoneET = findViewById(R.id.PhoneEditText);
        DescriptionET = findViewById(R.id.DescriptionEditText);
        DateET = findViewById(R.id.DateEditText);
        LocationET = findViewById(R.id.LocationEditText);
        SaveBTN = findViewById(R.id.SaveButton);
    }

    private void SetupButton() {
        SaveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = NameET.getText().toString().trim();
                String Phone = PhoneET.getText().toString().trim();
                String Description = DescriptionET.getText().toString().trim();
                String Date = DateET.getText().toString().trim();
                String Location = LocationET.getText().toString().trim();

                if (Name.isEmpty() || Phone.isEmpty() || Description.isEmpty() ||
                        Date.isEmpty() || Location.isEmpty() || TypeRG.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CreateActivity.this, "Please fill in all the information", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton radioButton = TypeRG.findViewById(TypeRG.getCheckedRadioButtonId());
                    Post post = new Post(0, Name, Phone, Description, Date, Location, radioButton.getText().toString());
                    if (managerDB.createPost(post).getId() != 0) {
                        Toast.makeText(CreateActivity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                        ClearAllET();
                    } else {
                        Toast.makeText(CreateActivity.this, "Fail to Save", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void ClearAllET() {
        NameET.getText().clear();
        PhoneET.getText().clear();
        DescriptionET.getText().clear();
        DateET.getText().clear();
        LocationET.getText().clear();
    }
}