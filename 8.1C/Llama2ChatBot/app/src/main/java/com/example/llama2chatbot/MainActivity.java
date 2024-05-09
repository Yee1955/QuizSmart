package com.example.llama2chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDateTime;

import API.ChatActivity;

public class MainActivity extends AppCompatActivity {
    EditText UsernameET;
    Button GoBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup view components
        UsernameET = findViewById(R.id.UsernameEditText);
        GoBTN = findViewById(R.id.GoButton);

        // Setup edittext
        UsernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GoBTN.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        // Setup button
        GoBTN.setEnabled(false);
        GoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = UsernameET.getText().toString().trim();
                if (!Username.isEmpty() && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Message message = new Message("My name is " + Username + ", short greeting to me", Username, LocalDateTime.now());
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("Message", message);
                    startActivity(intent);
                }
            }
        });
    }
}