package com.crm.communication.channel.Email;

import com.crm.communication.Notification;

public class EmailNotification extends Notification {
    public EmailNotification(String title) {
        super(title);
    }

    @Override
    public void send() {
        System.out.println("Sending Email notification with title: " + title);
    }
}
