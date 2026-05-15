package com.crm.communication.channel.SMS;

import com.crm.customer.model.Message;
import com.crm.common.enums.CommunicationChannel;
import com.crm.common.enums.MessageDirection;
import java.util.UUID;

public class SmsMessage extends Message {
    public SmsMessage(String content) {
        super(UUID.randomUUID().toString(), CommunicationChannel.SMS, MessageDirection.OUT, content);
    }

    @Override
    public void prepare() {
        System.out.println("Preparing SMS message: " + content);
    }
}
