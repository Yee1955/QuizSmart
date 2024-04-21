package com.example.itube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class PlayListActivity extends AppCompatActivity {
    User user;
    UserManagerDB userManagerDB;
    TextView URLListTV;
    List<PlayList> playLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        userManagerDB = new UserManagerDB(PlayListActivity.this);
        user = (User) getIntent().getSerializableExtra("User");

        URLListTV = findViewById(R.id.URLListTextView);
        playLists = userManagerDB.getPlaylistsForUser(user.getId());

        // Build a single string from the list
        StringBuilder stringBuilder = new StringBuilder();
        for (PlayList playList : playLists) {
            stringBuilder.append(playList.toString()).append("\n"); // Append each PlayList and a newline
        }

        // Set up Text View
        if (playLists.isEmpty()) {
            URLListTV.setText("The Playlists is empty");
        } else {
            URLListTV.setText(stringBuilder.toString());
        }
    }
}