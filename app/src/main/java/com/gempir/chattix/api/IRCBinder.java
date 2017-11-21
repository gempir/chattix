package com.gempir.chattix.api;

import android.os.Binder;

import com.gempir.chattix.IRCService;
import com.gikk.twirk.events.TwirkListener;

public class IRCBinder extends Binder {
    private IRCService service;

    public IRCBinder(IRCService service) {
        this.service = service;
    }

    public void joinChannel(String channelName, TwirkListener listener) {
        service.joinChannel(channelName);
        service.getChannelInstance(channelName).addIrcListener(listener);
    }
}
