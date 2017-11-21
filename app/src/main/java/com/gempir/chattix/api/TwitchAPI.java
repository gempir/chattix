package com.gempir.chattix.api;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gempir.chattix.persistence.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 public static String EMOTICON_URI = "http://static-cdn.jtvnw.net/emoticons/v1/";
 public static String EMOTE_SMALL = "1.0";
 public static String EMOTE_MEDIUM = "2.0";
 public static String EMOTE_LARGE = "3.0";
 */

public class TwitchAPI {
    private String oauth;
    private OkHttpClient okHttpClient;
    private static String CLIENT_ID = "t9pxj5cckmhwalp7j1mwf47vxzwfnt";

    private static Uri BASE_URI = Uri.parse("https://api.twitch.tv/kraken/");
    private static Uri USER_URI = Uri.parse("https://api.twitch.tv/kraken/user");
    private static Uri BASE_CHAT_URI = Uri.parse("https://api.twitch.tv/kraken/ChatActivity");

    private static String REDIRECT_URL = "http://localhost";

    public static String OAUTH_URL = Uri.parse("https://api.twitch.tv/kraken/oauth2/authorize")
            .buildUpon()
            .appendQueryParameter("response_type", "token")
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URL)
            .appendQueryParameter("scope", "user_read chat_login").build().toString();

    private static String buildUsernameUrlString(String accessToken) {
        return USER_URI
                .buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("oauth_token", accessToken)
                .build()
                .toString();
    }

    public TwitchAPI(String oauth) {
        this.oauth = oauth;
        okHttpClient = new OkHttpClient();
    }

    public String getOAuth() {
        return oauth;
    }

    /**
     * Obtains User using his OAuth token.
     * Requires injected OAuth.
     */
    public void retrieveUser(final IAPIHandler<User> handler) {
        Request request = new Request.Builder().url(TwitchAPI.buildUsernameUrlString(oauth)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("LoginActivity", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();

                    try {
                        JSONObject userJson = new JSONObject(jsonString);
                        User user = new User();
                        user.id = userJson.getInt("_id");
                        user.name = userJson.getString("name");
                        user.displayName = userJson.getString("display_name");
                        user.accessToken = oauth;

                        handler.onSuccess(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}