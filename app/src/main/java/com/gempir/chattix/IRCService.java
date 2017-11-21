package com.gempir.chattix;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gempir.chattix.api.IRCBinder;
import com.gempir.chattix.persistence.User;
import com.gikk.twirk.Twirk;
import com.gikk.twirk.TwirkBuilder;
import com.gikk.twirk.events.TwirkListener;
import com.gikk.twirk.events.TwirkListenerBaseImpl;
import com.gikk.twirk.types.clearChat.ClearChat;
import com.gikk.twirk.types.hostTarget.HostTarget;
import com.gikk.twirk.types.mode.Mode;
import com.gikk.twirk.types.notice.Notice;
import com.gikk.twirk.types.roomstate.Roomstate;
import com.gikk.twirk.types.subscriberEvent.SubscriberEvent;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.usernotice.Usernotice;
import com.gikk.twirk.types.users.TwitchUser;
import com.gikk.twirk.types.users.Userstate;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class IRCService extends Service {

    private User user;
    private IRCBinder binder;

    /**
     * It seems that Twirk instances are 'bound' to channels.
     */
    private HashMap<String, Twirk> channels;

    @Override
    public void onCreate() {
        super.onCreate();

        channels = new HashMap<>();
        binder = new IRCBinder(this);
        user = Chattix.instance().getDatabase().userDao().getUser();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Twirk getChannelInstance(String channelName) {
        return channels.get(channelName);
    }

    public void joinChannel(String channelName) {
        Twirk twirk = new TwirkBuilder(channelName, user.name, user.accessToken).build();
        try {
            twirk.connect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channels.put(channelName, twirk);
        }
    }
}
