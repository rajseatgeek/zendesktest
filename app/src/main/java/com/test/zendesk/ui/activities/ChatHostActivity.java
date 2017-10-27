package com.test.zendesk.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.test.zendesk.R;
import com.zopim.android.sdk.api.ChatApi;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.api.ZopimChatApi;
import com.zopim.android.sdk.prechat.EmailTranscript;
import com.zopim.android.sdk.prechat.PreChatForm;
import com.zopim.android.sdk.prechat.ZopimChatFragment;

public class ChatHostActivity extends AppCompatActivity {

    private ChatApi chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_host);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startNewChat();
    }

    private void startNewChat() {
        chat = ZopimChatApi.resume(this);

        endChat();

        PreChatForm preChatForm = new PreChatForm.Builder()
                .name(PreChatForm.Field.REQUIRED)
                .email(PreChatForm.Field.REQUIRED)
                .phoneNumber(PreChatForm.Field.OPTIONAL_EDITABLE)
                .message(PreChatForm.Field.OPTIONAL_EDITABLE)
                .build();

        ZopimChat.SessionConfig config = new ZopimChat.SessionConfig()
                .fileSending(false)
                .emailTranscript(EmailTranscript.DISABLED)
                .preChatForm(preChatForm);

        ZopimChatFragment fragment = ZopimChatFragment.newInstance(config);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_container, fragment, ZopimChatFragment.class.getName());
        transaction.commit();
    }

    private void endChat() {
        if (chat != null && !chat.hasEnded()) {
            chat.endChat();
        }
    }
}
