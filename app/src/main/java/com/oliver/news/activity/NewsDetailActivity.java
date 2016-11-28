package com.oliver.news.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.oliver.news.R;

/**
 * @desc 新闻显示界面
 * @auther Oliver
 * @time 2016/11/28 13:00
 */
public class NewsDetailActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_news_detail);
    }
}
