package com.example.itube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        urlString = getIntent().getStringExtra("URL");

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = extractVideoId(urlString);
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0);
                } else {
                    Log.e("YouTubePlayer", "Invalid video URL: " + urlString);
                }
            }
        });
    }

    private String extractVideoId(String url) {
        String videoId = null;

        try {
            if (url != null && url.trim().length() > 0) {
                Uri videoUri = Uri.parse(url);
                if (url.contains("youtu.be")) {
                    videoId = videoUri.getLastPathSegment();
                } else if (url.contains("youtube.com")) {
                    videoId = videoUri.getQueryParameter("v");
                }
            }
        } catch (Exception e) {
            Log.e("VideoIdExtraction", "Failed to extract video ID", e);
        }

        return videoId;
    }
}