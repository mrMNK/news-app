package com.android.newsapp.layout;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.newsapp.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (!(fragment instanceof NewsItemFragment) && !(fragment instanceof NewsListFragment)){
            fragment = new NewsListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /*Скрывает клавиатуру, если пользователь во время ввода текста в EditText нажал
    на любой элемент активити*/
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
        return super.dispatchTouchEvent(event);
    }

    /*Переопределяет метод, обрабатывающий нажатие кнопки "назад":
       - если кнопка нажата, когда на экране отображена новость (детальный экран новости),
       тогда пользователь возвращается на экран со списком новостей
       - если кнопка нажата на экране со списком новостей, то пользователь выходит из приложения*/
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment instanceof NewsListFragment){
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
