package com.crm.communication.factory;

<<<<<<< Updated upstream
import com.crm.communication.channel.ChatChannel;
import com.crm.communication.channel.CommunicationChannel;
=======
import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.Chat.ChatChannel;
import com.crm.communication.channel.Chat.ChatMessage;
import com.crm.communication.channel.Chat.ChatNotification;
>>>>>>> Stashed changes

public class ChatFactory implements CommunicationFactory {
    @Override
    public CommunicationChannel createChannel() {
        return new ChatChannel();
    }
<<<<<<< Updated upstream
=======

    @Override
    public Message createMessage(String content) {
        return new ChatMessage(content);
    }

    @Override
    public Notification createNotification(String title) {
        return new ChatNotification(title);
    }
>>>>>>> Stashed changes
}
