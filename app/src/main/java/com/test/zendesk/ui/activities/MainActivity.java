package com.test.zendesk.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.zendesk.R;
import com.test.zendesk.controllers.LiveChatController;
import com.zopim.android.sdk.api.ChatApi;
import com.zopim.android.sdk.api.ChatServiceBinder;
import com.zopim.android.sdk.api.ZopimChatApi;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private TextView accountAvailability;

    private Button startChat;

    LiveChatController.ChatAvailabilityCallback chatAvailabilityCallback = new LiveChatController.ChatAvailabilityCallback() {
        @Override
        public void onChatAvailable() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (accountAvailability != null) {
                        accountAvailability.setText(R.string.chat_available);
                    }

                    if (startChat != null) {
                        startChat.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @Override
        public void onChatUnavailable() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (accountAvailability != null) {
                        accountAvailability.setText(R.string.chat_unvailable);
                    }

                    if (startChat != null) {
                        startChat.setVisibility(View.GONE);
                    }
                }
            });
        }
    };

    private ChatApi chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accountAvailability = findViewById(R.id.account_availability);
        startChat = findViewById(R.id.start_chat);
        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatHostActivity.class));
            }
        });

        LiveChatController.getInstance().setChatAvailabilityCallback(chatAvailabilityCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        chat = ZopimChatApi.resume(this);

        if (chat != null) {
            if (chat.hasEnded()) {
                chat = ZopimChatApi.start(this);
                Log.d(TAG, "chat has ended: " + chat.hasEnded());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (chat != null && !chat.hasEnded()) {
            chat.endChat();
            Fragment chatServiceFragment = getSupportFragmentManager().findFragmentByTag(ChatServiceBinder.class.getName());
            if (chatServiceFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(chatServiceFragment)
                        .commitNow();
            }
        }
    }
}
