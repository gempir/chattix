package com.gempir.chattix;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gempir.chattix.api.TwitchAPI;
import com.gempir.chattix.persistence.AppDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Chattix chattix;

    private AppDatabase database;
    private TwitchAPI api;

    /**
     * Regex used for obtaining OAuth Token from URL.
     */
    private Pattern pattern = Pattern.compile("#access_token=(.*)&");

    private interface IOAuthHandler {
        void onOAuthReceived(String oauth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initDatabase();

        if (isLoggedIn()) {
            openChat();
        } else {
            prepareLogin(oauth -> {
                api = new TwitchAPI(oauth);
                initChattix();
                initUserData();
            });
        }
    }

    public void initChattix() {
        Chattix.createInstance(api, database);
    }

    public void initDatabase() {
        // TODO: If performance becomes a problem, its cause may be here. (allowMainThreadQueries)
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "chattixDB").allowMainThreadQueries().build();
    }

    /**
     * Opens chat on successful user retrieval.
     */
    public void initUserData() {
        api.retrieveUser(data -> openChat());
    }

    /**
     * Opens Twitch Login page in WebView for obtaining OAuth Token.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void prepareLogin(final IOAuthHandler handler) {
        final WebView webTwitchLogin = findViewById(R.id.twitchLogin);
        webTwitchLogin.getSettings().setJavaScriptEnabled(true);

        webTwitchLogin.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Matcher matcher = pattern.matcher(url);

                if (matcher.find()) {
                    handler.onOAuthReceived(matcher.group(1));
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webTwitchLogin.loadUrl(TwitchAPI.OAUTH_URL);
    }

    /**
     * @return Returns true if there exists an user already logged in.
     */
    private boolean isLoggedIn() {
        return database.userDao().getUser() != null;
    }

    private void openChat() {
        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
        startActivity(intent);
    }
}
