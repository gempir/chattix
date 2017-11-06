package com.gempir.chattix;

class LoginEvent
{
    private String username;
    private String accessToken;

    public LoginEvent(String username, String accessToken)
    {
        this.username = username;
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
