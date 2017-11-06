package com.gempir.chattix;


import android.util.Log;

import com.squareup.otto.Subscribe;

class IrcBot {
    private TwitchUserData twitchUserData = Factory.getTwitchUserData();


    public IrcBot()
    {
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent)
    {
        Log.d("IrcBot", "logged in: " + loginEvent.getUsername() + ", " + loginEvent.getAccessToken());
    }

    @Subscribe
    public void onJoinChannelEvent(JoinChannelEvent joinChannelEvent)
    {
        Log.d("IrcBot", "JOINING " + joinChannelEvent.getChannel());
    }
}
