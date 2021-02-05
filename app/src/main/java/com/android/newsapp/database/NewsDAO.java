package com.android.newsapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDAO {

    @Insert
    public long addNews(NewsDBItem news);

    @Update
    public void updateNews(NewsDBItem news);

    @Delete
    public void deleteNews(NewsDBItem news);

    @Query("select * from NewsDB")
    public List<NewsDBItem> getAllNews();

    @Query("select * from NewsDb where news_id ==:newsId")
    public NewsDBItem getNews(long newsId);
}
