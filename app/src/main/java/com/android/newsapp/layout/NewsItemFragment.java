package com.android.newsapp.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.newsapp.R;
import com.bumptech.glide.Glide;


public class NewsItemFragment extends Fragment {

    private String title;
    private String content;
    private String imageURL;
    private String date;

    public NewsItemFragment(String title, String content, String imageURL, String date){
        this.imageURL = imageURL;
        this.title = title;
        this.content = content;
        this.date = date;
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_item, container, false);
        TextView titleTextView = view.findViewById(R.id.newsItemTitle);
        TextView contentTextView = view.findViewById(R.id.newsItemContent);
        ImageView imageView = view.findViewById(R.id.newsItemImageView);
        TextView dateTextView = view.findViewById(R.id.newsItemPublishedAt);

        titleTextView.setText(title);
        contentTextView.setText(content);
        dateTextView.setText(date);

        // Загружает изображение из кэша
        Glide.with(getContext()).load(imageURL).into(imageView);

        return view;
    }
}
