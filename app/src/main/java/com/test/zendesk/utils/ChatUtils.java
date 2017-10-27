package com.test.zendesk.utils;

import com.zopim.android.sdk.model.VisitorInfo;

public class ChatUtils {

    private ChatUtils() {

    }

    public static VisitorInfo getTestVisitorInfo() {
        return new VisitorInfo.Builder()
                .email("test@test.com")
                .name("test user")
                .phoneNumber("1-111-111-1111")
                .build();
    }
}
