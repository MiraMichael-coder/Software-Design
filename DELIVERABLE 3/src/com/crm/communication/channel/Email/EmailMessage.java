package com.crm.communication.channel.Email;

import com.crm.customer.model.Message;
import com.crm.common.enums.CommunicationChannel;
import com.crm.common.enums.MessageDirection;
import java.util.UUID;

public class EmailMessage extends Message {
    public EmailMessage(String content) {
        super(UUID.randomUUID().toString(), CommunicationChannel.Email, MessageDirection.OUT, content);
    }

    @Override
    public void prepare() {
        System.out.println("Preparing Email message: " + content);
    }
}
