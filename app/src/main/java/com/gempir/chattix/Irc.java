package com.gempir.chattix;


import com.cavariux.twitchirc.Core.TwitchBot;

class Irc extends TwitchBot implements Runnable {
    private TwitchUserData twitchUserData = Factory.getTwitchUserData();

    private String botChannel;

    public Irc(String channel)
    {
        this.setUsername(twitchUserData.getUsername());
        this.setOauth_Key("oauth:" + twitchUserData.getAccessToken());
        this.setClientID(TwitchApi.CLIENT_ID);

        this.botChannel = "#" + channel.toLowerCase();

//        this.run();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        this.connect();
        this.joinChannel("#" + this.botChannel);
        this.start();
    }
}
