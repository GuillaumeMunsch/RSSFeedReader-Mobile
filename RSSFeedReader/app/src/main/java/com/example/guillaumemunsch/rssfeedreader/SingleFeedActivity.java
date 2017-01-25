package com.example.guillaumemunsch.rssfeedreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guillaumemunsch.rssfeedreader.http.RestAPI;
import com.example.guillaumemunsch.rssfeedreader.models.Feed;
import com.example.guillaumemunsch.rssfeedreader.models.News;
import com.example.guillaumemunsch.rssfeedreader.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SingleFeedActivity extends AppCompatActivity {
    int feedId;
    Context context;
    ListView newsListView;
    List<News> newsList;
    TextView emptyText;
    IconTextView loading;

    public void loadContent(){
        loading.setVisibility(View.GONE);
        if (newsList.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            newsListView.setVisibility(View.INVISIBLE);
        } else {
            newsListView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Utils.transform(newsList, "title"));
        newsListView.setAdapter(adapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SingleFeedActivity.class);
                intent.putExtra("id", newsList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void fetchFeed() {
        RestAPI.get("/feed/" + feedId, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    newsList = new Gson().fromJson(response.toString(), new TypeToken<List<Feed>>() {}.getType());
                    loadContent();
                } catch (Throwable ex) {
                    Log.d("Feed", "Unable to parse feeds.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Api error", errorResponse.toString());
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("id"))
            feedId = getIntent().getIntExtra("id", 0);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_single_feed);
        context = this;
        newsListView = (ListView) findViewById(R.id.feedList);
        newsListView.setVisibility(View.GONE);
        emptyText = (TextView)findViewById(R.id.empty_view);
        emptyText.setVisibility(View.GONE);
        loading = (IconTextView)findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
    }
}
