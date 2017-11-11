package com.gempir.chattix;

import android.net.Uri;

public class TwitchApi {
    public static Uri BASE_URI = Uri.parse("https://api.twitch.tv/kraken/");
    public static Uri USER_URI = Uri.parse("https://api.twitch.tv/kraken/user");
    public static Uri BASE_CHAT_URI = Uri.parse("https://api.twitch.tv/kraken/ChatActivity");
    public static String EMOTICON_URI = "http://static-cdn.jtvnw.net/emoticons/v1/";
    public static String EMOTE_SMALL = "1.0";
    public static String EMOTE_MEDIUM = "2.0";
    public static String EMOTE_LARGE = "3.0";
    public static String CLIENT_ID = "t9pxj5cckmhwalp7j1mwf47vxzwfnt";
    public static String REDIRECT_URL = "http://localhost";

    public static String OAUTH_URL = Uri.parse("https://api.twitch.tv/kraken/oauth2/authorize")
            .buildUpon()
            .appendQueryParameter("response_type", "token")
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URL)
            .appendQueryParameter("scope", "user_read chat_login").build().toString();

    public static String buildUsernameUrlString(String accessToken)
    {
        return USER_URI
                .buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("oauth_token", accessToken)
                .build()
                .toString();
    }
}