package com.example.guillaumemunsch.rssfeedreader.models;

import java.io.Serializable;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

public class Feed implements Serializable {
    private int id;
    private String name;
    private String url;
    private int owner_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public Feed(int id, String name, String url, int owner_id) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.owner_id = owner_id;
    }
}
