package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsDetailsActivity extends AppCompatActivity {
    News news;
    List<News> _news;
    ImageView newsImg;
    TextView titleTV, descriptionTV;
    RecyclerView relatedStoriesRV;
    VerticalAdapter vAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        news = (News) getIntent().getSerializableExtra("News");
        _news = (List<News>)getIntent().getSerializableExtra("NewsList");

        newsImg = findViewById(R.id.news_img);
        titleTV = findViewById(R.id.TitleTextView);
        descriptionTV = findViewById(R.id.DescriptionTextView);
        relatedStoriesRV = findViewById(R.id.RelatedStoriesRecyclerView);

        createNewsList();
        setUpNewsDetails();
    }

    private void createNewsList() {
        vAdapter = new VerticalAdapter(_news, new onItemClickListener() {
            @Override
            public void itemClick(News selectedNews) {
                Intent intent = new Intent(NewsDetailsActivity.this, NewsDetailsActivity.class);
                intent.putExtra("News", selectedNews);
                intent.putExtra("NewsList", new ArrayList<Serializable>(_news));
                startActivity(intent);
            }
        });

        relatedStoriesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        relatedStoriesRV.setAdapter(vAdapter);
    }

    private void setUpNewsDetails() {
        Picasso.get()
                .load(news.getImageURL())
                .resize(80, 50)  // Resize the image to match ImageView, values in pixels
                .centerCrop()      // Apply a centerCrop to match the ImageView's scaleType
                .into(newsImg);
        titleTV.setText(news.getTitle());
        descriptionTV.setText(news.getDescription());
        relatedStoriesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        relatedStoriesRV.setAdapter(vAdapter);
    }
}