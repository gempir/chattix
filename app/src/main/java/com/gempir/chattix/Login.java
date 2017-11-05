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


class Login extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private Pattern pattern = Pattern.compile("#access_token=(.*)&");
    private Login currentActivity = this;

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
                    TwitchUserData.setAccessToken(matcher.group(1));
                    System.out.println(TwitchUserData.getAccessToken());
                    setUsername(TwitchApi.buildUsernameUrlString(TwitchUserData.getAccessToken()));

                    Intent myIntent = new Intent(currentActivity, Chat.class);
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
                    TwitchUserData.setUsername(doc.getJSONObject("token").getString("user_name"));
                } catch (JSONException e) {
                    Log.e("Login", e.getMessage());
                }
            }
        });

    }
}
