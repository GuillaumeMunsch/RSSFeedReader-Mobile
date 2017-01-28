package com.example.guillaumemunsch.rssfeedreader.http;

/**
 * Created by guillaumemunsch on 25/01/2017.
 */

import android.content.Context;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.StringEntity;

public class RestAPI {
    private static final String BASE_URL = "http://www.socialhive.fr:4242/";

    private static AsyncHttpClient client = SingletonAsyncHttpClient.getInstance();

    public static AsyncHttpClient getClient() {return client; }

    public static void addHeader(String header, String value) {
        client.addHeader(header, value);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.put(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.delete(getAbsoluteUrl(url), params, responseHandler);
    }
    
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}