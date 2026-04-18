package com.crm.communication.factory;

<<<<<<< Updated upstream
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.EmailChannel;
=======
import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.Email.EmailChannel;
import com.crm.communication.channel.Email.EmailMessage;
import com.crm.communication.channel.Email.EmailNotification;
>>>>>>> Stashed changes

public class EmailFactory implements CommunicationFactory {
    @Override
    public CommunicationChannel createChannel() {
        return new EmailChannel();
    }
<<<<<<< Updated upstream
=======

    @Override
    public Message createMessage(String content) {
        return new EmailMessage(content);
    }

    @Override
    public Notification createNotification(String title) {
        return new EmailNotification(title);
    }
>>>>>>> Stashed changes
}
