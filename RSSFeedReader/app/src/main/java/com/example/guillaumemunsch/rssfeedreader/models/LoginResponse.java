package com.example.guillaumemunsch.rssfeedreader.models;

import java.io.Serializable;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

public class LoginResponse implements Serializable {
    private int id;
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResponse(int id, String token) {
        this.id = id;
        this.token = token;
    }
}
