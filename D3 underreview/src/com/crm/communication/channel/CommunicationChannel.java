package com.crm.communication.channel;

// Bridge Pattern — Implementor interface for communication channels.
public interface CommunicationChannel {
    void sendMessage(String to, String message);
}
