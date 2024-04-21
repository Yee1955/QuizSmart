package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {
    private List<News> _news;
    private final onItemClickListener listener;
    public HorizontalAdapter(List<News> newsList, onItemClickListener listener) {
        this._news = newsList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storyImg;

        public ViewHolder(View itemView) {
            super(itemView);
            storyImg = itemView.findViewById(R.id.story_img);
        }

        public void bind(News news) {
            Picasso.get()
                    .load(news.getImageURL())
                    .resize(50, 50)  // Resize the image to match ImageView, values in pixels
                    .centerCrop()      // Apply a centerCrop to match the ImageView's scaleType
                    .into(storyImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClick(news);
                }
            });
        }
    }

    @NonNull
    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalAdapter.ViewHolder holder, int position) {
        News news = _news.get(position);
        holder.bind(news);
    }

    @Override
    public int getItemCount() {
        return (_news == null) ? 0 : _news.size();
    }
}