package com.crm.communication.channel.SMS;

import com.crm.communication.channel.CommunicationChannel;

// Bridge Pattern — Concrete Implementor for SMS communication.
public class SMSChannel implements CommunicationChannel {
    @Override
    public void sendMessage(String to, String message) {
        // Skeleton: no real sms integration
        System.out.println("SMS to " + to + ": " + message);
    }
}
