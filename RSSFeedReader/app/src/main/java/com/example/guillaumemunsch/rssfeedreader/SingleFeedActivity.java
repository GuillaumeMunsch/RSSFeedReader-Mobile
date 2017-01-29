package com.example.guillaumemunsch.rssfeedreader;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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
    News news;
    TextView emptyText;
    IconTextView loading;
    SwipeRefreshLayout swipeContainer;

    public void loadContent(){
        loading.setVisibility(View.GONE);
        if (news.getItems().size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
        } else {
            swipeContainer.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Utils.transform(news.getItems(), "title"));
        newsListView.setAdapter(adapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", news.getItems().get(position).getTitle());
            intent.putExtra("description", news.getItems().get(position).getDescription());
            intent.putExtra("url", news.getItems().get(position).getLink());
            startActivity(intent);
            }
        });
    }

    private void fetchNews() {
        RestAPI.get("me/feeds/" + feedId, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    news = new Gson().fromJson(response.toString(), new TypeToken<News>() {}.getType());
                    swipeContainer.setRefreshing(false);
                    loadContent();
                } catch (Throwable ex) {
                    Log.d("Feed", "Unable to parse news.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable throwable) {
                Log.d("Api error", responseBody);
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
        newsListView = (ListView) findViewById(R.id.newsList);
        emptyText = (TextView)findViewById(R.id.empty_view);
        emptyText.setVisibility(View.GONE);
        loading = (IconTextView)findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setVisibility(View.GONE);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {fetchNews();}
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        fetchNews();
    }
}
