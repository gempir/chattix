package com.gempir.chattix;

public class JoinChannelEvent {

    private String channel;

    public JoinChannelEvent(String channel)
    {
        this.channel = channel;
    }

    public String getChannel()
    {
        return channel;
    }
}
