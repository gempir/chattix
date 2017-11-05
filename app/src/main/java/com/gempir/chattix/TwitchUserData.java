package com.gempir.chattix;

class TwitchUserData {
    private static String accessToken;
    private static String username;

    public static void setAccessToken(String accessToken) {
        TwitchUserData.accessToken = accessToken;
    }

    public static void setUsername(String username) {
        TwitchUserData.username = username;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getUsername() {
        return username;
    }
}
