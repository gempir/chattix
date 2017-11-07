package com.gempir.chattix;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class Factory {

    private static TwitchUserData userData;

    private static Bus bus;

    public static TwitchUserData getTwitchUserData()
    {
        if (userData == null) {
            userData = new TwitchUserData();

        }
        return userData;
    }

    public static Bus getBus()
    {
        if (bus == null) {
            bus = new Bus(ThreadEnforcer.ANY);
        }
        return bus;
    }
}
