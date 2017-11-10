package com.gempir.chattix;

import com.squareup.otto.Subscribe;

class IrcBot {


    public IrcBot()
    {
        Factory.getBus().register(this);
    }

    @Subscribe
    public void onJoinChannelEvent(JoinChannelEvent joinChannelEvent)
    {
        System.out.println("JOINING " + joinChannelEvent.getChannel());
    }
}
