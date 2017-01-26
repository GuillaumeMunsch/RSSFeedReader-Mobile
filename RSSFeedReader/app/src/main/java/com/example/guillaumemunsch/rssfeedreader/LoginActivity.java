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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    Context context;
    Button connectButton = null;
    Button createAccount;
    EditText emailInput, passwordInput;
    String token;

    private void tryConnection() {
        RequestParams params = new RequestParams();
        try {
            params.add("email", emailInput.getText().toString());
            params.add("password", passwordInput.getText().toString());
        }
        catch (Throwable ex)
        {
            Log.d("Connection", ex.getMessage());
        }
        RestAPI.post("auth/login", params, new JsonHttpResponseHandler() {
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
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                startActivity(new Intent(context, FeedsListActivity.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable throwable) {
                Log.d("Not connected", responseBody.toString());
                Log.d("Not connected---2", throwable.toString());
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
/*        List<Cookie> cookies = myCookieStore.getCookies();
        String session = "";

        if (cookies.isEmpty()) {
            Log.i("TAG", "None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                session += cookies.get(i).getName()+"="+cookies.get(i).getValue()+"; ";

            }
            Log.i("session", session);
        }
        RestAPI.getClient().addHeader("Cookie", session.substring(0, session.length() - 1));*/


        emailInput = (EditText)findViewById(R.id.user);
        passwordInput = (EditText)findViewById(R.id.password);
        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    tryConnection();
                }
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
