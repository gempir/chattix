package com.gempir.chattix;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;

import com.gempir.chattix.api.IAPIHandler;
import com.gempir.chattix.api.TwitchAPI;
import com.gempir.chattix.persistence.AppDatabase;
import com.gempir.chattix.persistence.User;

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
            prepareLogin(new IOAuthHandler() {
                @Override
                public void onOAuthReceived(String oauth) {
                    api = new TwitchAPI(oauth);
					initChattix();
					
                    initUserData();
                }
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
        api.retrieveUser(new IAPIHandler<User>() {
            @Override
            public void onSuccess(User data) {
                openChat();
            }
        });
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
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Matcher matcher = pattern.matcher(request.getUrl().toString());

                if (matcher.find()) {
                    handler.onOAuthReceived(matcher.group(1));
                    webTwitchLogin.stopLoading();
                    webTwitchLogin.setVisibility(WebView.GONE);
                }

                return super.shouldOverrideUrlLoading(view, request);
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
