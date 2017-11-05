package com.gempir.chattix;

public class Factory {

    private static TwitchUserData userData;

    private static Irc irc;

    public static TwitchUserData getTwitchUserData()
    {
        if (userData == null) {
            userData = new TwitchUserData();

        }
        return userData;
    }
}
