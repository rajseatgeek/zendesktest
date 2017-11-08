package com.test.zendesk.controllers;

import android.support.annotation.Nullable;

import com.test.zendesk.utils.AccountKey;
import com.test.zendesk.utils.ChatUtils;
import com.zopim.android.sdk.api.ZopimChatApi;
import com.zopim.android.sdk.data.observers.AccountObserver;
import com.zopim.android.sdk.model.Account;

import static com.zendesk.logger.Logger.setLoggable;

public class LiveChatController {

    private static LiveChatController instance;

    public static LiveChatController getInstance() {
        setLoggable(true);
        if (instance == null) {
            synchronized (LiveChatController.class) {
                if (instance == null) {
                    instance = new LiveChatController();
                }
            }
        }

        return instance;
    }

    private LiveChatController() {
        // NOP
    }

    @Nullable
    private ChatAvailabilityCallback chatAvailabilityCallback;

    public void initialize() {
        ZopimChatApi.init(AccountKey.ACCOUNT_KEY);

        ZopimChatApi.setVisitorInfo(ChatUtils.getTestVisitorInfo());

        ZopimChatApi.getDataSource().addAccountObserver(new AccountObserver() {
            @Override
            public void update(Account account) {
                if (chatAvailabilityCallback != null && account.getStatus() != null) {
                    switch (account.getStatus()) {
                        case ONLINE:
                            chatAvailabilityCallback.onChatAvailable();
                            break;
                        default:
                            chatAvailabilityCallback.onChatUnavailable();
                            break;
                    }
                }
            }
        });
    }

    public void setChatAvailabilityCallback(ChatAvailabilityCallback chatAvailabilityCallback) {
        this.chatAvailabilityCallback = chatAvailabilityCallback;
    }

    public interface ChatAvailabilityCallback {
        void onChatAvailable();

        void onChatUnavailable();
    }
}
