package com.example.guillaumemunsch.rssfeedreader.http;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

import com.loopj.android.http.AsyncHttpClient;

public class SingletonAsyncHttpClient extends AsyncHttpClient {
    private static SingletonAsyncHttpClient singleton = new SingletonAsyncHttpClient( );

    private SingletonAsyncHttpClient() {}

    public static SingletonAsyncHttpClient getInstance( ) {
        return singleton;
    }
}