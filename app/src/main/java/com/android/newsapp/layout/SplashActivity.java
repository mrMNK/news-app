package com.android.newsapp.layout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.android.newsapp.database.NewsDBItem;
import com.android.newsapp.database.NewsDatabase;
import com.android.newsapp.utils.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private NewsDatabase newsDatabase;
    private ArrayList<JSONObject> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsDatabase = Room.databaseBuilder(getApplicationContext(),
                NewsDatabase.class, "NewsDB").build();

        fillDB(Util.JSON_URL);
    }

    // Заполняет БД
    private void fillDB(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject((response));
                            JSONArray jsonArray = object.getJSONArray(Util.JSON_ARRAY_NAME);
                            newsList = getArrayListFromJSONArray(jsonArray);
                            new AddNewsAsyncTask().execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(stringRequest);
    }

    //Возвращает динамический массив объектов JSON из JSONArray
    private ArrayList<JSONObject> getArrayListFromJSONArray (JSONArray jsonArray){
        ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
        try {
            if(jsonArray!=null){
                for(int i = 0; i<jsonArray.length(); i++){
                    arrayList.add(jsonArray.getJSONObject(i));
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return arrayList;
    }

    //Добавляем записи в БД в новом потоке
    private class AddNewsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            newsDatabase.clearAllTables();

            for(JSONObject news : newsList) {
                try {
                    JSONObject updatedNews = checkNullString(news);

                    NewsDBItem item = new NewsDBItem(
                            updatedNews.getString(Util.JSON_OBJ_TITLE),
                            updatedNews.getString(Util.JSON_OBJ_DESCRIPTION),
                            updatedNews.getString(Util.JSON_OBJ_URL_IMAGE),
                            updatedNews.getString(Util.JSON_OBJ_CONTENT),
                            updatedNews.getString(Util.JSON_OBJ_PUBLISHED_AT));
                    newsDatabase.getNewsDAO().addNews(item);
                    saveImageFromURLToDiskCache(news.getString(Util.JSON_OBJ_URL_IMAGE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Сохраняет изображение в кэш диска
        private void saveImageFromURLToDiskCache(String url){

            Glide.with(getApplicationContext())
                    .downloadOnly()
                    .load(url)
                    .submit(500, 500);
        }

        // Проверка на сожержание строки "null" в полях JSONObject
        private JSONObject checkNullString(JSONObject jObject) throws JSONException {
            if (jObject.getString(Util.JSON_OBJ_TITLE).equals("null")){
                jObject.getJSONObject(Util.JSON_ARRAY_NAME).put(Util.JSON_OBJ_TITLE, "");
            }
            if (jObject.getString(Util.JSON_OBJ_DESCRIPTION).equals("null")){
                jObject.getJSONObject(Util.JSON_ARRAY_NAME).put(Util.JSON_OBJ_DESCRIPTION, "");
            }
            if (jObject.getString(Util.JSON_OBJ_CONTENT).equals("null")){
                jObject.getJSONObject(Util.JSON_ARRAY_NAME).put(Util.JSON_OBJ_CONTENT, "");
            }
            if (jObject.getString(Util.JSON_OBJ_PUBLISHED_AT).equals("null")){
                jObject.getJSONObject(Util.JSON_ARRAY_NAME).put(Util.JSON_OBJ_PUBLISHED_AT, "");
            }
            return jObject;
        }
    }

}
