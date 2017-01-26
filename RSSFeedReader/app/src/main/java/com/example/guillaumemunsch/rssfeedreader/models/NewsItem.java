package com.example.guillaumemunsch.rssfeedreader.models;

import java.io.Serializable;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

public class NewsItem implements Serializable {
    private String link;
    private String title;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public NewsItem(String link, String title) {
        this.link = link;
        this.title = title;
    }
}
