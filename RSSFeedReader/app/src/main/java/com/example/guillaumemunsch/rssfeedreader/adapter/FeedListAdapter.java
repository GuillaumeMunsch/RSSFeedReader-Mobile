package com.example.guillaumemunsch.rssfeedreader.adapter;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guillaumemunsch.rssfeedreader.R;
import com.example.guillaumemunsch.rssfeedreader.models.Feed;

import java.util.List;

/**
 * Created by guillaumemunsch on 11/10/2016.
 */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.MyViewHolder> {

private List<Feed> feedList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView name;

    public MyViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.feedTitle);
    }
}

    public FeedListAdapter(List<Feed> feedList) {
        this.feedList = feedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item_feed, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Feed feed = feedList.get(position);
        holder.name.setText(feed.getName());
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}