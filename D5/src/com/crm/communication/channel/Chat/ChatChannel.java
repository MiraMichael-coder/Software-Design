package com.crm.communication.channel.Chat;

import com.crm.communication.channel.CommunicationChannel;

public class ChatChannel implements CommunicationChannel {
    @Override
    public void sendMessage(String to, String message) {
        // Skeleton: no real chat integration
        System.out.println("Chat message to " + to + ": " + message);
    }
}
