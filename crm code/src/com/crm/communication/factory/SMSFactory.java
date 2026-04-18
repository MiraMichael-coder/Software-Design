package com.crm.communication.factory;

<<<<<<< Updated upstream
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.SMSChannel;
=======
import com.crm.customer.model.Message;
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.channel.SMS.SMSChannel;
import com.crm.communication.channel.SMS.SmsMessage;
import com.crm.communication.channel.SMS.SmsNotification;
>>>>>>> Stashed changes

public class SMSFactory implements CommunicationFactory {
    @Override
    public CommunicationChannel createChannel() {
        return new SMSChannel();
    }
<<<<<<< Updated upstream
=======

    @Override
    public Message createMessage(String content) {
        return new SmsMessage(content);
    }

    @Override
    public Notification createNotification(String title) {
        return new SmsNotification(title);
    }
>>>>>>> Stashed changes
}
