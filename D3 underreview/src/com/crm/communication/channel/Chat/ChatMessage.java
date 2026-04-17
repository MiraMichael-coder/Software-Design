package com.crm.communication.channel.Chat;

import com.crm.customer.model.Message;
import com.crm.common.enums.CommunicationChannel;
import com.crm.common.enums.MessageDirection;
import java.util.UUID;

public class ChatMessage extends Message {
    public ChatMessage(String content) {
        super(UUID.randomUUID().toString(), CommunicationChannel.Chat, MessageDirection.OUT, content);
    }

    @Override
    public void prepare() {
        System.out.println("Preparing Chat message: " + content);
    }
}
