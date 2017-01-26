package com.example.guillaumemunsch.rssfeedreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.guillaumemunsch.rssfeedreader.http.RestAPI;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CreateAccount extends AppCompatActivity {
    Context context;
    EditText email, password, confirmPassword;
    Button button;

    private void tryCreateAccount() {
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject params = new JSONObject();
        StringEntity stringEntity = null;
        try {
            params.put("email", email.getText().toString());
            params.put("password", password.getText().toString());
            params.put("first_name", "Marcel");
            params.put("last_name", "Pattulacci");
            params.put("username", "pattulaccidu93");
            stringEntity = new StringEntity(params.toString());
        } catch (Throwable ex) {
            Log.d("Create Account", ex.getMessage());
        }
        RestAPI.post(context, "auth/signup", stringEntity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Throwable ex) {
                    Log.d("Fetching response", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "Unable to create account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        context = this;
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        button = (Button)findViewById(R.id.create_account);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryCreateAccount();
            }
        });
    }
}
