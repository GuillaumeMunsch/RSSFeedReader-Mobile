package com.example.guillaumemunsch.rssfeedreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewsDetailActivity extends AppCompatActivity {
    private int newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        if (getIntent().hasExtra("id"))
            newsId = getIntent().getIntExtra("id", 0);

    }
}
