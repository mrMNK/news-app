package com.android.newsapp.layout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.newsapp.R;
import com.android.newsapp.database.NewsDBItem;
import com.android.newsapp.database.NewsDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewsListFragment extends Fragment {

    private List<NewsDBItem> newsList;
    private View view;
    private EditText searchEditText;
    private Boolean isRecyclerViewShowFilteredNews;

    public NewsListFragment(){
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        searchEditText = view.findViewById(R.id.searchTextView);
        setOnEnterKeyListener(searchEditText);
        new GetAllNewsAsyncTask().execute();

        return view;
    }

    // Устанавливает обработчик нажатия на клавишу "Enter" при вводе текста в EditText
    private void setOnEnterKeyListener (final EditText editText) {
        editText.setOnKeyListener(new OnKeyListener()
            {
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if(event.getAction() == KeyEvent.ACTION_DOWN &&
                            (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (editText.getText().toString().equals("")) {
                            if (isRecyclerViewShowFilteredNews){
                                fillRecyclerView(newsList);
                                isRecyclerViewShowFilteredNews = false;
                            }
                            hideKeyboard(v);
                            return true;
                        }
                        String searchPhrase = editText.getText().toString();
                        hideKeyboard(v);
                        List<NewsDBItem> newNewsList = searchPhraseInNewsList(searchPhrase, newsList);
                        fillRecyclerView(newNewsList);
                        return true;
                    }
                    return false;
                }
            }
        );
    }

    /*Осуществляет фильтр новостей по вхождению (регистр букв искомой фразы игнорируется).
    Возвращает отфильтрованный список новостей*/
    private List<NewsDBItem> searchPhraseInNewsList(String sPhrase, List<NewsDBItem> nList){
        List<NewsDBItem> updatedList;
        ArrayList<NewsDBItem> tempArr = new ArrayList<>();
        for(NewsDBItem item : nList){
            if (item.getTitle().toLowerCase().contains(sPhrase.toLowerCase())
                    || item.getDescription().toLowerCase().contains(sPhrase.toLowerCase())
                    || item.getContent().toLowerCase().contains(sPhrase.toLowerCase()))
            {
                tempArr.add(item);
            }
        }
        updatedList = tempArr;
        isRecyclerViewShowFilteredNews = true;
        return updatedList;
    }

    // Скравает клавиатуру
    private void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    // Заполняет RecyclerView контентом
    private void fillRecyclerView (List<NewsDBItem> list){
        RecyclerView recyclerView = view.findViewById(R.id.newsRecyclerView);
        RecyclerView.Adapter newsAdapter = new NewsAdapter(list);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Сортирует новости в списке по дате создания (от самой свежей до наиболее поздней)
    private List<NewsDBItem> sortNewsByDate(List<NewsDBItem> originalList){
        Collections.sort(originalList, new Comparator<NewsDBItem>() {
            @Override
            public int compare(NewsDBItem o1, NewsDBItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return originalList;
    }

    // Получает данные из БД в фоновом потоке, затем заполняет RecyclerView
    private class GetAllNewsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            NewsDatabase newsDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                    NewsDatabase.class, "NewsDB").build();
            newsList = sortNewsByDate(newsDatabase.getNewsDAO().getAllNews());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (!searchEditText.getText().toString().equals("")) {
                List<NewsDBItem> newNewsList = searchPhraseInNewsList(
                        searchEditText.getText().toString(), newsList);
                fillRecyclerView(newNewsList);
                isRecyclerViewShowFilteredNews = true;
            } else {
                fillRecyclerView(newsList);
                isRecyclerViewShowFilteredNews = false;
            }
        }
    }
}
