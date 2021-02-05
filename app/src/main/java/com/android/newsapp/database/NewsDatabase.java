package com.android.newsapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NewsDBItem.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsDAO getNewsDAO();

}
