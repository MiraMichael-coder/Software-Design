package com.crm.customer.controller;

<<<<<<< Updated upstream
=======
import com.crm.communication.Notification;
import com.crm.communication.channel.CommunicationChannel;
import com.crm.communication.factory.CommunicationFactory;
>>>>>>> Stashed changes
import com.crm.customer.model.Message;
import com.crm.customer.repository.MessageRepository;

public class MessageController {
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

<<<<<<< Updated upstream
    public void sendMessage(Message message) {
        messageRepository.save(message);
=======
    /**
     * Demonstrates using the Abstract Factory to manage a family of related
     * products.
     */
    public void sendCommunication(CommunicationFactory factory, String content, String title, String recipient) {
        // 1. Create a related family of objects via the Abstract Factory
        Message message = factory.createMessage(content);
        Notification notification = factory.createNotification(title);
        CommunicationChannel channel = factory.createChannel();

        // 2. Perform the communication workflow
        message.prepare();
        channel.sendMessage(recipient, content);
        notification.send();

        // 3. Persist via repository
        messageRepository.save(message.getMessageId(), message);
>>>>>>> Stashed changes
    }

    public Message getMessage(String messageId) {
        return messageRepository.findById(messageId);
    }

    public void updateMessage(Message message) {
<<<<<<< Updated upstream
        messageRepository.update(message);
=======
        messageRepository.update(message.getMessageId(), message);
>>>>>>> Stashed changes
    }

    public void deleteMessage(String messageId) {
        messageRepository.delete(messageId);
    }
}
