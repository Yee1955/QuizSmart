package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView topStoriesRV;
    RecyclerView newsRV;
    List<News> _stories;
    List<News> _news;
    HorizontalAdapter hAdapter;
    VerticalAdapter vAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topStoriesRV = findViewById(R.id.topStoriesRecyclerView);
        newsRV = findViewById(R.id.newsRecyclerView);

        _stories = createSampleStories();
        _news = createSampleNews();
        createTopStoriesList();
        createNewsList();
    }

    private void createTopStoriesList() {
        hAdapter = new HorizontalAdapter(_stories, new onItemClickListener() {
            @Override
            public void itemClick(News news) {
                // No Action Needed for Top Stories
            }
        });

        topStoriesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topStoriesRV.setAdapter(hAdapter);
    }

    private void createNewsList() {
        vAdapter = new VerticalAdapter(_news, new onItemClickListener() {
            @Override
            public void itemClick(News news) {
                Intent intent = new Intent(MainActivity.this, NewsDetailsActivity.class);
                intent.putExtra("News", news);
                intent.putExtra("NewsList", new ArrayList<Serializable>(_news));
                startActivity(intent);
            }
        });

        newsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        newsRV.setAdapter(vAdapter);
    }

    private List<News> createSampleStories() {
        List<News> _news = new ArrayList<>();
        _news.add(new News("Breaking News: New Tech Breakthrough", "Researchers have announced a major breakthrough in quantum computing.", "https://source.unsplash.com/featured/?quantum,computing"));
        _news.add(new News("Global Markets Rally", "Stock markets around the world rallied today on news of a potential trade agreement.", "https://source.unsplash.com/featured/?stockmarket,global"));
        _news.add(new News("Local Hero Saves Family", "A local resident becomes a hero after saving a family from a burning building last night.", "https://source.unsplash.com/featured/?hero,fire"));
        _news.add(new News("Health Update: New Diet Trend", "Nutritionists are buzzing about this new diet that promises results without sacrificing taste.", "https://source.unsplash.com/featured/?diet,nutrition"));
        _news.add(new News("Technology: The Future of Smartphones", "Next-gen smartphones could include AI capabilities that predict user needs.", "https://source.unsplash.com/featured/?smartphone,nextgen"));
        _news.add(new News("Sports Roundup: Championship Highlights", "Highlights from last night's championship game, which ended in a thrilling overtime victory.", "https://source.unsplash.com/featured/?sports,championship"));
        _news.add(new News("Art and Culture: Museum Exhibit Opening", "A new exhibit opens this week at the City Museum, featuring works from renowned modern artists.", "https://source.unsplash.com/featured/?museum,art"));
        _news.add(new News("Travel: Top 10 Destinations for 2024", "Experts reveal the top travel destinations for the upcoming year, including several surprising picks.", "https://source.unsplash.com/featured/?travel,destinations"));
        _news.add(new News("Science: Climate Change Report", "A new report details the accelerating impacts of climate change and the urgent need for action.", "https://source.unsplash.com/featured/?climatechange"));
        _news.add(new News("Economy: Housing Market Forecast", "Economists predict the housing market will experience significant growth in several regions next year.", "https://source.unsplash.com/featured/?housing,market"));
        return  _news;
    }

    private List<News> createSampleNews() {
        List<News> _news = new ArrayList<>();
        _news.add(new News("Education Reform: New Policies Introduced", "Governments worldwide introduce new education reforms aimed at increasing digital literacy.", "https://source.unsplash.com/featured/?education,reform"));
        _news.add(new News("Automotive Innovation: Electric Vehicle Milestones", "A major car manufacturer sets a record for the longest distance traveled by an electric vehicle on a single charge.", "https://source.unsplash.com/featured/?electricvehicle"));
        _news.add(new News("Entertainment: Blockbuster Movie Release", "This year's most anticipated movie shatters box office records during its opening weekend.", "https://source.unsplash.com/featured/?blockbustermovie"));
        _news.add(new News("Environmental Initiative: Reforestation Efforts", "A global initiative successfully plants millions of trees worldwide as part of a reforestation effort.", "https://source.unsplash.com/featured/?reforestation"));
        _news.add(new News("Healthcare Advancements: New Vaccine Developed", "Scientists announce the development of a vaccine that targets multiple strains of a virus.", "https://source.unsplash.com/featured/?vaccine"));
        _news.add(new News("Space Exploration: Mars Rover Discovery", "The latest Mars rover sends back exciting data revealing possible signs of past life.", "https://source.unsplash.com/featured/?marsrover"));
        _news.add(new News("Fashion Trends: Sustainable Clothing", "The fashion industry takes a major step towards sustainability with new eco-friendly materials.", "https://source.unsplash.com/featured/?sustainablefashion"));
        _news.add(new News("Culinary Delights: New Cuisine Fusion", "A new trend in fusion cuisine merges flavors from the East and West, captivating food enthusiasts.", "https://source.unsplash.com/featured/?fusioncuisine"));
        _news.add(new News("Legal Developments: Landmark Court Decision", "A high-profile court case concludes with a landmark decision that may change legal precedents.", "https://source.unsplash.com/featured/?courthouse"));
        return _news;
    }

}