package com.test.zendesk.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.test.zendesk.R;
import com.test.zendesk.utils.ChatUtils;
import com.zopim.android.sdk.api.Chat;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.chatlog.ZopimChatLogFragment;
import com.zopim.android.sdk.prechat.ChatListener;
import com.zopim.android.sdk.prechat.ZopimChatFragment;

public class ChatHostActivity extends AppCompatActivity implements ChatListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_host);

        final Chat chat = ZopimChat.resume(this);
        if (!chat.hasEnded()) {
            resumeChat();
        } else {
            startNewChat();
        }
    }

    private void resumeChat() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(ZopimChatLogFragment.class.getName()) == null) {
            final ZopimChatLogFragment chatLogFragment = new ZopimChatLogFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(com.zopim.android.sdk.R.id.chat_fragment_container, chatLogFragment, ZopimChatLogFragment.class.getName());
            transaction.commit();
        }
    }

    private void startNewChat() {
        ZopimChatFragment fragment = ZopimChatFragment.newInstance(ChatUtils.getSessionConfig());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_container, fragment, ZopimChatFragment.class.getName());
        transaction.commit();
    }

    @Override
    public void onChatLoaded(Chat chat) {
        // TODO
    }

    @Override
    public void onChatInitialized() {
        // TODO
    }

    @Override
    public void onChatEnded() {
        finish();
    }
}
