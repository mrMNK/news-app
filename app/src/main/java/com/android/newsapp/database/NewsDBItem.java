package com.android.newsapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "NewsDB")
public class NewsDBItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "news_id")
    private long id;
    @ColumnInfo(name = "news_title")
    private String title;
    @ColumnInfo(name = "news_description")
    private String description;
    @ColumnInfo(name = "news_image_url")
    private String newsImageURL;
    @ColumnInfo(name = "news_content")
    private String content;
    @ColumnInfo(name = "news_published_at")
    private String date;

    public NewsDBItem(long id, String title, String description,
                      String newsImageURL, String content, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.newsImageURL = newsImageURL;
        this.content = content;
        this.date = date;
    }

    @Ignore
    public NewsDBItem(String title, String description,
                      String newsImageURL, String content, String date) {
        this.title = title;
        this.description = description;
        this.newsImageURL = newsImageURL;
        this.content = content;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNewsImageURL() {
        return newsImageURL;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
