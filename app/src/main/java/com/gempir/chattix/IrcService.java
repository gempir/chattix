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

    private final IBinder mBinder = new LocalBinder();

    private User user;

    public class LocalBinder extends Binder {
        public IrcService getService() {
            return IrcService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user = Factory.getAppDatabase(IrcService.this).userDao().getUser();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void joinChannel(final String channel) {

        final Thread t = new Thread() {
            @Override
            public void run() {
                final Twirk twirk = new TwirkBuilder(channel, user.name, "oauth" + user.accessToken).build();

                twirk.addIrcListener( new TwirkListenerBaseImpl() {
                    public void onPrivMsg(TwitchUser sender, TwitchMessage message) {
                        twirk.channelMessage("pong " + sender.getDisplayName() );
                    }
                } );

                try {
                    // MAIN THREAD
                    twirk.connect();
                } catch (Exception e) {
                    Log.e("IrcService", "e: " + e.toString());
                }
            }
        };
        t.start();
    }
}
