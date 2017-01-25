package com.example.guillaumemunsch.rssfeedreader;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guillaumemunsch.rssfeedreader.http.RestAPI;
import com.example.guillaumemunsch.rssfeedreader.models.Feed;
import com.example.guillaumemunsch.rssfeedreader.recycler.DividerItemDecoration;
import com.example.guillaumemunsch.rssfeedreader.recycler.RecyclerItemClickListener;
import com.example.guillaumemunsch.rssfeedreader.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FeedsListActivity extends AppCompatActivity {
    Context context;
    FloatingActionButton addFeedButton;
    ListView feedsListView;
    List<Feed> feedsList;
    TextView emptyText;
    IconTextView loading;
    SwipeRefreshLayout swipeContainer;

    public void loadContent(){
        loading.setVisibility(View.GONE);
        if (feedsList.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            feedsListView.setVisibility(View.INVISIBLE);
        } else {
            feedsListView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Utils.transform(feedsList, "title"));
        feedsListView.setAdapter(adapter);
        feedsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SingleFeedActivity.class);
                intent.putExtra("id", feedsList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void fetchFeed() {
        RestAPI.get("/feed", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    feedsList = new Gson().fromJson(response.toString(), new TypeToken<List<Feed>>() {}.getType());
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
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_feeds_list);
        context = this;
        addFeedButton = (FloatingActionButton)findViewById(R.id.addFeedButton);
        addFeedButton.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_plus).colorRes(R.color.white));
        feedsListView = (ListView) findViewById(R.id.feedList);
        feedsListView.setVisibility(View.GONE);
        emptyText = (TextView)findViewById(R.id.empty_view);
        emptyText.setVisibility(View.GONE);
        loading = (IconTextView)findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        addFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddFeedActivity.class));
            }
        });
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {fetchFeed();}
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        fetchFeed();
    }
}
