package com.test.zendesk.application;

import android.app.Application;

import com.test.zendesk.controllers.LiveChatController;


public class ZendeskTestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LiveChatController.getInstance().initialize();
    }
}
