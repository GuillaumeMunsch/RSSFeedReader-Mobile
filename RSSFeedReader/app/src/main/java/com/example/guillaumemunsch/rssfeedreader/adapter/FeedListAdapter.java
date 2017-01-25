package com.example.guillaumemunsch.rssfeedreader.adapter;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RatingBar;
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
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
//            title = (TextView) view.findViewById(R.id.feedTitle);
        }
    }

/*    public FeedListAdapter(List<Feed> moviesList) {
        this.feedList = moviesList;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Feed plugin = feedList.get(position);
/*        holder.icon.setImageIcon(plugin.getIcon());
        holder.name.setText(plugin.getName());
        holder.description.setText(plugin.getRepository());
        if (plugin.getRate() == -1f) {
            holder.rateText.setText("No rate");
            holder.rate.setVisibility(View.INVISIBLE);
        }
        else
            holder.rate.setRating(plugin.getRate());*/
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}