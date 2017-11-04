package com.gempir.chattix

import android.net.Uri

object TwitchApi {
    val BASE_URI = Uri.parse("https://api.twitch.tv/kraken/")
    val BASE_CHAT_URI = Uri.parse("https://api.twitch.tv/kraken/chat")
    val EMOTICON_URI = "http://static-cdn.jtvnw.net/emoticons/v1/"
    val EMOTE_SMALL = "1.0"
    val EMOTE_MEDIUM = "2.0"
    val EMOTE_LARGE = "3.0"
    val CLIENT_ID = "t9pxj5cckmhwalp7j1mwf47vxzwfnt"
    val REDIRECT_URL = "http://localhost"

    val OAUTH_URL = Uri.parse("https://api.twitch.tv/kraken/oauth2/authorize")
            .buildUpon()
            .appendQueryParameter("response_type", "token")
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URL)
            .appendQueryParameter("scope", "user_read chat_login").build().toString()

    val USERNAME_REQUEST = BASE_URI.buildUpon().appendQueryParameter("client_id", CLIENT_ID)

    fun buildUsernameUrlString(accessToken : String) : String
    {
        return BASE_URI
                .buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("oauth_token", accessToken)
                .build()
                .toString()
    }
}