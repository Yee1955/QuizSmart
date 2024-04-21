package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {
    private List<News> _news;
    private final onItemClickListener listener;

    public VerticalAdapter (List<News> _news, onItemClickListener listener) {
        this._news = _news;
        this.listener = listener;
    }

    class NewsPairs {
        private News news1;
        private News news2;
        public NewsPairs (News news1, News news2) {
            this.news1 = news1;
            this.news2 = news2;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImg1, newsImg2;
        private TextView titleTV1, titleTV2, descriptionTV1, descriptionTV2;
        LinearLayout FirstItem, SecondItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImg1 = itemView.findViewById(R.id.news_img1);
            newsImg2 = itemView.findViewById(R.id.news_img2);
            titleTV1 = itemView.findViewById(R.id.TitleTextView1);
            titleTV2 = itemView.findViewById(R.id.TitleTextView2);
            descriptionTV1 = itemView.findViewById(R.id.DescriptionTextView1);
            descriptionTV2 = itemView.findViewById(R.id.DescriptionTextView2);
            FirstItem = itemView.findViewById(R.id.FirstItem);
            SecondItem = itemView.findViewById(R.id.SecondItem);
        }

        public void bind(NewsPairs newsPairs) {
            if (newsPairs.news1 != null) {
                Picasso.get()
                        .load(newsPairs.news1.getImageURL())
                        .resize(70, 50)  // Resize the image to match ImageView, values in pixels
                        .centerCrop()      // Apply a centerCrop to match the ImageView's scaleType
                        .into(newsImg1);
                titleTV1.setText(newsPairs.news1.getTitle());
                descriptionTV1.setText(newsPairs.news1.getDescription());
                FirstItem.setOnClickListener(v -> listener.itemClick(newsPairs.news1));
            } else {
                FirstItem.setVisibility(View.INVISIBLE);
            }

            if (newsPairs.news2 != null) {
                Picasso.get()
                        .load(newsPairs.news2.getImageURL())
                        .resize(70, 50)  // Resize the image to match ImageView, values in pixels
                        .centerCrop()      // Apply a centerCrop to match the ImageView's scaleType
                        .into(newsImg2);
                titleTV2.setText(newsPairs.news2.getTitle());
                descriptionTV2.setText(newsPairs.news2.getDescription());
                SecondItem.setOnClickListener(v -> listener.itemClick(newsPairs.news2));
            } else {
                SecondItem.setVisibility(View.INVISIBLE);
            }
        }
    }


    @NonNull
    @Override
    public VerticalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalAdapter.ViewHolder holder, int position) {
        int index1 = position * 2;
        int index2 = index1 + 1;
        News news1 = _news.get(index1);
        News news2 = index2 < _news.size() ? _news.get(index2) : null;
        NewsPairs newsPairs = new NewsPairs(news1, news2);
        holder.bind(newsPairs);
    }

    @Override
    public int getItemCount() {
        return (_news.size() + 1) / 2;
    }
}