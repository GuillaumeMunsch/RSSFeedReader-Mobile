package com.example.guillaumemunsch.rssfeedreader.models;

import java.io.Serializable;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

public class Feed implements Serializable {
    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Feed(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
