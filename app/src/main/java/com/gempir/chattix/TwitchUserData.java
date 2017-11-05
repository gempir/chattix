package com.gempir.chattix;

public class TwitchUserData {
    private String accessToken;
    private String username;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getUsername() {
        return this.username;
    }
}
