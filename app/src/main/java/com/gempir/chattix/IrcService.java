package com.gempir.chattix;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gempir.chattix.persistence.User;
import com.gikk.twirk.Twirk;
import com.gikk.twirk.TwirkBuilder;
import com.gikk.twirk.events.TwirkListenerBaseImpl;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;

public class IrcService extends Service {

    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        user = Chattix.instance().getDatabase().userDao().getUser();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void joinChannel(final String channel) {
        final Twirk twirk = new TwirkBuilder(channel, user.name, "oauth" + user.accessToken).build();

        twirk.addIrcListener( new TwirkListenerBaseImpl() {
            public void onPrivMsg(TwitchUser sender, TwitchMessage message) {
                twirk.channelMessage("pong " + sender.getDisplayName() );
            }
        } );

        try {
            twirk.connect();
        } catch (Exception e) {
            Log.e("IrcService", "e: " + e.toString());
        }
    }
}
