package com.example.guillaumemunsch.rssfeedreader.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

public class News implements Serializable {
    private String title;
    private List<NewsItem> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NewsItem> getItems() {
        return items;
    }

    public void setItems(List<NewsItem> items) {
        this.items = items;
    }

    public News(String title, List<NewsItem> items) {
        this.title = title;
        this.items = items;
    }
}
