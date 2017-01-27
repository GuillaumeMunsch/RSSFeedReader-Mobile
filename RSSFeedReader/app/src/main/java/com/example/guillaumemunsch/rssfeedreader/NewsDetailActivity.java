package com.example.guillaumemunsch.rssfeedreader;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Button;

public class NewsDetailActivity extends AppCompatActivity {
    private String title = "No title", description = "No description", url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView titleView, descriptionView;
        Button urlButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        if (getIntent().hasExtra("url")) {
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            url = getIntent().getStringExtra("url");
        }
        titleView = (TextView)findViewById(R.id.title);
        titleView.setText(title);
        descriptionView = (TextView)findViewById(R.id.description);
        descriptionView.setText(description);
        urlButton = (Button)findViewById(R.id.url);
        urlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });
    }
}
