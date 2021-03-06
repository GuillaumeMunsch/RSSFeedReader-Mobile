package com.example.guillaumemunsch.rssfeedreader;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.guillaumemunsch.rssfeedreader.http.RestAPI;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AddFeedActivity extends AppCompatActivity {
    Context context;
    EditText label, url;
    Button button;

    private void tryAddFeed() {
        JSONObject params = new JSONObject();
        StringEntity stringEntity = null;
        try {
            params.put("name", label.getText().toString());
            params.put("url", url.getText().toString());
            stringEntity = new StringEntity(params.toString());
        } catch (Throwable ex) {
            Log.d("Add Feed", ex.getMessage());
        }
        RestAPI.put(context, "me/feeds", stringEntity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Toast.makeText(context, "Feed added", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Throwable ex) {
                    Log.d("Fetching response", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "Unable to add feed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);
        context = this;
        label = (EditText)findViewById(R.id.label);
        url = (EditText)findViewById(R.id.url);
        button = (Button)findViewById(R.id.addFeedButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAddFeed();
            }
        });
    }
}
