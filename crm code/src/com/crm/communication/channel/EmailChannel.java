package com.crm.communication.channel;

public class EmailChannel implements CommunicationChannel {
    @Override
    public void sendMessage(String to, String message) {
        // Skeleton: no real email integration
        System.out.println("Email to " + to + ": " + message);
    }
}
