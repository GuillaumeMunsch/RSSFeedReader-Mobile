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
import android.widget.Toast;

import com.example.guillaumemunsch.rssfeedreader.adapter.FeedListAdapter;
import com.example.guillaumemunsch.rssfeedreader.http.RestAPI;
import com.example.guillaumemunsch.rssfeedreader.models.Feed;
import com.example.guillaumemunsch.rssfeedreader.recycler.DividerItemDecoration;
import com.example.guillaumemunsch.rssfeedreader.recycler.RecyclerItemClickListener;
import com.example.guillaumemunsch.rssfeedreader.utils.Utils;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
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
    RecyclerView feedsListView;
    List<Feed> feedsList;
    TextView emptyText;
    IconTextView loading;
    SwipeRefreshLayout swipeContainer;
    private FeedListAdapter mAdapter;

    private void removeFeed(int id) {
        RestAPI.delete("me/feeds/" + id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Feed", "Delete failed");
            }
        });
    }

    public void loadContent(){
        loading.setVisibility(View.GONE);
        if (feedsList.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
        } else {
            swipeContainer.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }

        mAdapter = new FeedListAdapter(feedsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        feedsListView.setLayoutManager(mLayoutManager);
        feedsListView.setItemAnimator(new DefaultItemAnimator());
        feedsListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        feedsListView.setAdapter(mAdapter);
        feedsListView.addOnItemTouchListener(
            new RecyclerItemClickListener(context, feedsListView ,new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, SingleFeedActivity.class);
                    intent.putExtra("id", feedsList.get(position).getId());
                    startActivity(intent);
                }

                @Override public void onLongItemClick(View view, int position) {
                    // do whatever
                }
            })
        );
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(feedsListView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    removeFeed(feedsList.get(position).getId());
                                    feedsList.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    removeFeed(feedsList.get(position).getId());
                                    feedsList.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        feedsListView.addOnItemTouchListener(swipeTouchListener);
    }

    private void fetchFeed() {
        RestAPI.get("me/feeds", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    feedsList = new Gson().fromJson(response.toString(), new TypeToken<List<Feed>>() {}.getType());
                    swipeContainer.setRefreshing(false);
                    loadContent();
                } catch (Throwable ex) {
                    Log.d("Feed", "Unable to parse feeds.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                loading.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);
                swipeContainer.setRefreshing(false);
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
        feedsListView = (RecyclerView)findViewById(R.id.feedList);
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
        swipeContainer.setVisibility(View.GONE);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {fetchFeed();}
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_light);
        fetchFeed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchFeed();
    }
}
