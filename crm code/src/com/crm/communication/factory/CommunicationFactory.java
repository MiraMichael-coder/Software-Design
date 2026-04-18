package com.crm.communication.factory;

<<<<<<< Updated upstream
=======
import com.crm.customer.model.Message;
import com.crm.communication.Notification;
>>>>>>> Stashed changes
import com.crm.communication.channel.CommunicationChannel;

public interface CommunicationFactory {
    CommunicationChannel createChannel();
<<<<<<< Updated upstream
=======

    Message createMessage(String content);

    Notification createNotification(String title);
>>>>>>> Stashed changes
}
