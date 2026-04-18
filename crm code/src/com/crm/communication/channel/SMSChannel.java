package com.crm.communication.channel;

public class SMSChannel implements CommunicationChannel {
    @Override
    public void sendMessage(String to, String message) {
        // Skeleton: no real sms integration
        System.out.println("SMS to " + to + ": " + message);
    }
}
