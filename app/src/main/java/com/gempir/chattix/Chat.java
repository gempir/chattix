package com.gempir.chattix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

public class Chat extends AppCompatActivity {

    private ConstraintLayout chatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatLayout = findViewById(R.id.chatLayout);

        Button addChannel = findViewById(R.id.addChannel);

        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
                builder.setTitle("Join Channel");

                final EditText input = new EditText(Chat.this);
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

    private void joinChannel(String channel) {
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(channel));

        final ScrollView channelScrollView = new ScrollView(this);
        channelScrollView.setPadding(dpToPx(6), dpToPx(48), dpToPx(6), 0);

        chatLayout.addView(channelScrollView);

        Irc irc = new Irc(channel) {
//            @Override
//            final public void onMessage(User user, Channel channel, String message)
//            {
//                System.out.println(message);
//                TextView textView = new TextView(Chat.this);
//                textView.setText(String.format("%s: %s", user.toString(), message));
//                channelScrollView.addView(textView);
//            }
        };



        channelScrollView.setVisibility(View.VISIBLE);
    }

    private int dpToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
