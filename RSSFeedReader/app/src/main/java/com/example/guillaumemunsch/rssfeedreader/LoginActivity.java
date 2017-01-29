package com.example.guillaumemunsch.rssfeedreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guillaumemunsch.rssfeedreader.http.RestAPI;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    Context context;
    Button connectButton = null;
    Button createAccount;
    EditText emailInput, passwordInput;
    ImageView image;
    String token;

    private void tryConnection() {
        JSONObject params = new JSONObject();
        StringEntity stringEntity = null;
        try {
            params.put("email", emailInput.getText().toString());
            params.put("password", passwordInput.getText().toString());
            stringEntity = new StringEntity(params.toString());
        } catch (Throwable ex) {
            Log.d("Login", ex.getMessage());
        }
        RestAPI.post(context, "auth/login", stringEntity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    token = response.getString("token");
                    startActivity(new Intent(context, FeedsListActivity.class));
                } catch (Throwable ex) {
                    Log.d("Fetching response", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(context, "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkConnected() {
        RestAPI.get("auth/check", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                startActivity(new Intent(context, FeedsListActivity.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable throwable) {
                Log.d("Not connected", responseBody.toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        RestAPI.getClient().setCookieStore(myCookieStore);

        image = (ImageView)findViewById(R.id.image);
        image.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        emailInput = (EditText)findViewById(R.id.user);
        passwordInput = (EditText)findViewById(R.id.password);
        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                tryConnection();
            return false;
            }
        });
        connectButton = (Button)findViewById(R.id.connection_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryConnection();
            }
        });
        createAccount = (Button)findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, CreateAccount.class));
            }
        });
        checkConnected();
    }
}
