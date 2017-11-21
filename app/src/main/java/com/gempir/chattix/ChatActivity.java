package com.gempir.chattix;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.gempir.chattix.api.IRCBinder;
import com.gempir.chattix.persistence.Channel;
import com.gikk.twirk.events.TwirkListener;
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

import java.util.Collection;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ServiceConnection {
    private ConstraintLayout chatLayout;
    private IRCBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Bind this object to IRC Service.
        bindService(new Intent(ChatActivity.this, IRCService.class), this, Context.BIND_AUTO_CREATE);

        chatLayout = findViewById(R.id.chatLayout);
        Button addChannel = findViewById(R.id.addChannel);

        loadChannels();

        Chattix.instance().getDatabase().channelsDao().getAllChannelsSync().observe(this, new Observer<List<Channel>>() {
            @Override
            public void onChanged(@Nullable List<Channel> channels) {
                loadChannels();
            }
        });

        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Join Channel");

                final EditText input = new EditText(ChatActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Join", (dialog, which) -> joinChannel(input.getText().toString()));
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            }
        });
    }

    private void loadChannels() {
        /*List<Channel> channels = Chattix.instance().getDatabase().channelsDao().getAllChannels();

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.removeAllTabs();

        for (Channel channel : channels) {

            tabLayout.addTab(tabLayout.newTab().setText(channel.name));

            final ScrollView channelScrollView = new ScrollView(this);

            chatLayout.addView(channelScrollView);

            channelScrollView.setVisibility(View.VISIBLE);
        }*/
    }

    private void joinChannel(String channel) {
        Channel channelObj = new Channel();
        channelObj.name = channel;

        binder.joinChannel(channel, new TwirkListener() {
            @Override
            public void onAnything(String unformatedMessage) {

            }

            @Override
            public void onPrivMsg(TwitchUser sender, TwitchMessage message) {

            }

            @Override
            public void onWhisper(TwitchUser sender, TwitchMessage message) {

            }

            @Override
            public void onJoin(String joinedNick) {

            }

            @Override
            public void onPart(String partedNick) {

            }

            @Override
            public void onConnect() {

            }

            @Override
            public void onDisconnect() {

            }

            @Override
            public void onNotice(Notice notice) {

            }

            @Override
            public void onHost(HostTarget hostNotice) {

            }

            @Override
            public void onSubscriberEvent(SubscriberEvent subscriberEvent) {

            }

            @Override
            public void onMode(Mode mode) {

            }

            @Override
            public void onUserstate(Userstate userstate) {

            }

            @Override
            public void onRoomstate(Roomstate roomstate) {

            }

            @Override
            public void onClearChat(ClearChat clearChat) {

            }

            @Override
            public void onNamesList(Collection<String> namesList) {

            }

            @Override
            public void onUsernotice(Usernotice usernotice) {

            }

            @Override
            public void onUnknown(String unformatedMessage) {

            }
        });
        //Chattix.instance().getDatabase().channelsDao().insertChannel(channelObj);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.i("IRC Service", "IRC Service bound");
        binder = (IRCBinder) iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.w("IRC Service", "IRC Service disconnected!");
        binder = null;
    }
}
