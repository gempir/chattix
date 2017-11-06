package com.gempir.chattix;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;

import com.squareup.otto.Bus;


class Login extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private Pattern pattern = Pattern.compile("#access_token=(.*)&");

    public Login()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new IrcBot();
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TabLayout tabs = findViewById(R.id.tabs);
        TabLayout.Tab loginTab = tabs.newTab();
        loginTab.setText("Login");
        tabs.addTab(loginTab);

        WebView twitchLogin = findViewById(R.id.twitchLogin);

        twitchLogin.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Matcher matcher = pattern.matcher(request.getUrl().toString());

                if (matcher.find()) {
                    Factory.getTwitchUserData().setAccessToken(matcher.group(1));
                    setUsername(TwitchApi.buildUsernameUrlString(matcher.group(1)));

                    Intent myIntent = new Intent(Login.this, Chat.class);
                    startActivity(myIntent);
                }

                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        twitchLogin.getSettings().setJavaScriptEnabled(true);
        twitchLogin.loadUrl(TwitchApi.OAUTH_URL);
    }

    private void setUsername(String url) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Login", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    JSONObject doc = new JSONObject(response.body().toString());
                    String username = doc.getJSONObject("token").getString("user_name");
                    Factory.getTwitchUserData().setUsername(username);
                    BusProvider.getInstance().post(new LoginEvent(username, Factory.getTwitchUserData().getAccessToken()));
                } catch (JSONException e) {
                    Log.e("Login", e.getMessage());
                }
            }
        });

    }
}
