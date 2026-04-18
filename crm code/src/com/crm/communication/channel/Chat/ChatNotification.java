package com.crm.communication.channel.Chat;

import com.crm.communication.Notification;

public class ChatNotification extends Notification {
    public ChatNotification(String title) {
        super(title);
    }

    @Override
    public void send() {
        System.out.println("Sending Chat notification with title: " + title);
    }
}
