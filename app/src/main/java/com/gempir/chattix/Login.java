package com.gempir.chattix;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;

import okhttp3.*;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;

import com.squareup.otto.Bus;

import javax.inject.Inject;

class Login extends AppCompatActivity {

    @Inject
    public OkHttpClient okHttpClient;

    @Inject
    public Bus bus;

    @Inject
    public AppDatabase appDatabase;

    private Pattern pattern = Pattern.compile("#access_token=(.*)&");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new IrcBot();

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
                    fillUserData(matcher.group(1));
                }

                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        twitchLogin.getSettings().setJavaScriptEnabled(true);
        twitchLogin.loadUrl(TwitchApi.OAUTH_URL);
    }

    private void fillUserData(final String accessToken) {
        Request request = new Request.Builder().url(TwitchApi.buildUsernameUrlString(accessToken)).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Login", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                if (response.isSuccessful()) {
                    try {
                        System.out.println(response.body().string());


                        JSONObject doc = new JSONObject(response.body().string());

                        User user = new User();
                        user.id = doc.getInt("_id");
                        user.name = doc.getString("name");
                        user.displayName = doc.getString("display_name");
                        user.accessToken = accessToken;

                        appDatabase.userDao().setUser(user);

//                        bus.post(new LoginEvent(username, accessToken);

                        System.out.println(appDatabase.userDao().getUser().id);

                        Intent myIntent = new Intent(Login.this, Chat.class);
                        startActivity(myIntent);
                    } catch (Exception e) {
                        Log.e("Login", e.getMessage());
                    }
                } else {
                    return;
                }
            }
        });

    }
}
