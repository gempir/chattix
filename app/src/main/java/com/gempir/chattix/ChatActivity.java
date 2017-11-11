package com.gempir.chattix;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.gempir.chattix.persistence.Channel;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ConstraintLayout chatLayout;

    public ChatActivity()
    {
        new IrcBot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatLayout = findViewById(R.id.chatLayout);
        Button addChannel = findViewById(R.id.addChannel);

        loadChannels();

        Factory.getAppDatabase(ChatActivity.this).channelsDao().getAllChannelsSync().observe(this, new Observer<List<Channel>>() {
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

                builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joinChannel(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void loadChannels() {

        List<Channel> channels = Factory.getAppDatabase(ChatActivity.this).channelsDao().getAllChannels();

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.removeAllTabs();

        for (Channel channel : channels) {

            tabLayout.addTab(tabLayout.newTab().setText(channel.name));

            final ScrollView channelScrollView = new ScrollView(this);
            channelScrollView.setPadding(dpToPx(6), dpToPx(48), dpToPx(6), 0);

            chatLayout.addView(channelScrollView);

            channelScrollView.setVisibility(View.VISIBLE);
        }
    }

    private void joinChannel(String channel) {
        Channel channelObj = new Channel();
        channelObj.name = channel;

        Factory.getAppDatabase(ChatActivity.this).channelsDao().insertChannel(channelObj);
    }

    private int dpToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}