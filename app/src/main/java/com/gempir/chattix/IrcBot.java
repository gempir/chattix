package com.gempir.chattix;


import android.util.Log;

import com.squareup.otto.Subscribe;

class IrcBot {
    private TwitchUserData twitchUserData = Factory.getTwitchUserData();


    public IrcBot()
    {
        System.out.println("IrcBot instance");
        Factory.getBus().register(this);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent)
    {
        System.out.println("logged in: " + loginEvent.getUsername() + ", " + loginEvent.getAccessToken());
    }

    @Subscribe
    public void onJoinChannelEvent(JoinChannelEvent joinChannelEvent)
    {
        System.out.println("JOINING " + joinChannelEvent.getChannel());
    }
}
