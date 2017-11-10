package com.gempir.chattix;

import com.squareup.otto.Subscribe;

class IrcBot {


    public IrcBot()
    {
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
