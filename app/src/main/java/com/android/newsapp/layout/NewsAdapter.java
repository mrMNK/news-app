package com.android.newsapp.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.newsapp.R;
import com.android.newsapp.database.NewsDBItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.RecyclerViewViewHolder> {

    private List<NewsDBItem> newsList;
    private Context context;

    public NewsAdapter(List<NewsDBItem> newsList){
        this.newsList = newsList;
    }

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder
                                                implements View.OnClickListener {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView imageView;
        private String imageURL;
        private String contentString;
        private TextView publishedAtTextView;

        private RecyclerViewViewHolder(View item) {
            super(item);
            item.setOnClickListener(this);
            titleTextView = item.findViewById(R.id.cardTitleTextView);
            descriptionTextView = item.findViewById(R.id.cardDescriptionTextView);
            imageView = item.findViewById(R.id.cardImageView);
            publishedAtTextView = item.findViewById(R.id.cardPublishedAt);
        }

        @Override
        public void onClick(View view) {

            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            Fragment fragment = new NewsItemFragment(
                    (String)titleTextView.getText(),
                    contentString,
                    imageURL,
                    (String)publishedAtTextView.getText());

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item,
            viewGroup, false);
        context = view.getContext();
        RecyclerViewViewHolder recyclerViewViewHolder = new RecyclerViewViewHolder(view);
        return recyclerViewViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int i) {
        NewsDBItem newsItem = newsList.get(i);
        holder.titleTextView.setText(newsItem.getTitle());
        holder.descriptionTextView.setText(newsItem.getDescription());
        holder.imageURL = newsItem.getNewsImageURL();
        holder.contentString = newsItem.getContent();
        holder.publishedAtTextView.setText(newsItem.getDate());

        // Загружает изображение из кэша
        Glide.with(context).load(holder.imageURL).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
