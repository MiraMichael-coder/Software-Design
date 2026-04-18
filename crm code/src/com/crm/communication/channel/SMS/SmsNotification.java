package com.crm.communication.channel.SMS;

import com.crm.communication.Notification;

public class SmsNotification extends Notification {
    public SmsNotification(String title) {
        super(title);
    }

    @Override
    public void send() {
        System.out.println("Sending SMS notification with title: " + title);
    }
}
