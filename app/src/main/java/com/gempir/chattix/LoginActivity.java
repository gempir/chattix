package com.gempir.chattix;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.gempir.chattix.persistence.AppDatabase;
import com.gempir.chattix.persistence.User;

public class LoginActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private AppDatabase appDatabase = Factory.getAppDatabase(LoginActivity.this);

    private Pattern pattern = Pattern.compile("#access_token=(.*)&");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TabLayout tabs = findViewById(R.id.tabs);
        TabLayout.Tab loginTab = tabs.newTab();
        loginTab.setText("Login");
        tabs.addTab(loginTab);

        if (isLoggedIn()) {
            openChat();
        } else {
            prepareLogin();
        }
    }

    /**
     * Opens Twitch Login page in WebView for obtaining OAuth Token.
     */
    private void prepareLogin() {
        WebView twitchLogin = findViewById(R.id.twitchLogin);
        twitchLogin.getSettings().setJavaScriptEnabled(true);

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

        twitchLogin.loadUrl(TwitchApi.OAUTH_URL);
    }

    /**
     * @return Returns true if there exists an user already logged in.
     */
    private boolean isLoggedIn() {
        return appDatabase.userDao().getUser() != null;
    }

    /**
     * Opens a request to Twitch API to get user information.
     * @param accessToken OAuth token required for communication with Twitch API
     */
    private void fillUserData(String accessToken) {
        Request request = new Request.Builder().url(TwitchApi.buildUsernameUrlString(accessToken)).build();
        okHttpClient.newCall(request).enqueue(new UserLoginFeedback(accessToken));
    }

    /**
     *
     */
    private class UserLoginFeedback implements Callback {

        private String accessToken;

        public UserLoginFeedback(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e("LoginActivity", e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            if (response.isSuccessful()) {
                try {

                    JSONObject doc = new JSONObject(response.body().string());

                    User user = new User();
                    user.id = doc.getInt("_id");
                    user.name = doc.getString("name");
                    user.displayName = doc.getString("display_name");
                    user.accessToken = accessToken;

                    appDatabase.userDao().setUser(user);

                    openChat();
                } catch (Exception e) {
                    Log.e("LoginMeme", e.getMessage());
                }
            }
        }
    }

    private void openChat() {
        Intent myIntent = new Intent(LoginActivity.this, ChatActivity.class);
        startActivity(myIntent);
    }
}
