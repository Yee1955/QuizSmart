package com.example.itube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class URLActivity extends AppCompatActivity {
    User user;
    UserManagerDB userManagerDB;
    Button playBtn, addBtn, playListBtn;
    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlactivity);
        userManagerDB = new UserManagerDB(URLActivity.this);
        user = (User) getIntent().getSerializableExtra("User");

        playBtn = findViewById(R.id.PlayButton);
        addBtn = findViewById(R.id.AddButton);
        playListBtn = findViewById(R.id.PlaylistButton);
        url = findViewById(R.id.URLEditText);

        setupButton();
    }

    private void setupButton() {

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = url.getText().toString();
                if (urlString.isEmpty()) {
                    Toast.makeText(URLActivity.this,
                            "URL is blank",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(URLActivity.this, VideoActivity.class);
                    intent.putExtra("URL", urlString);
                    startActivity(intent);
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = url.getText().toString();
                if (urlString.isEmpty()) {
                    Toast.makeText(URLActivity.this,
                            "URL is blank",
                            Toast.LENGTH_SHORT).show();
                } else {
                    userManagerDB.addPlaylist(user.getId(), urlString);
                    Toast.makeText(URLActivity.this,
                            "Successfully Added",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        playListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(URLActivity.this, PlayListActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}