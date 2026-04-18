package com.crm.customer.model;

import com.crm.common.enums.CommunicationChannel;
import com.crm.common.enums.MessageDirection;
import java.time.LocalDateTime;

<<<<<<< Updated upstream
public class Message {
    private String messageId;
    private CommunicationChannel channel;
    private MessageDirection direction;
    private String content;
=======
public abstract class Message {
    private String messageId;
    private CommunicationChannel channel;
    private MessageDirection direction;
    protected String content;
>>>>>>> Stashed changes
    private LocalDateTime timestamp;
    private boolean read;

    public Message(String messageId, CommunicationChannel channel, MessageDirection direction, String content) {
        this.messageId = messageId;
        this.channel = channel;
        this.direction = direction;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

<<<<<<< Updated upstream
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public CommunicationChannel getChannel() { return channel; }
    public void setChannel(CommunicationChannel channel) { this.channel = channel; }

    public MessageDirection getDirection() { return direction; }
    public void setDirection(MessageDirection direction) { this.direction = direction; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public boolean isRead() { return read; }
=======
    public abstract void prepare();

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public CommunicationChannel getChannel() {
        return channel;
    }

    public void setChannel(CommunicationChannel channel) {
        this.channel = channel;
    }

    public MessageDirection getDirection() {
        return direction;
    }

    public void setDirection(MessageDirection direction) {
        this.direction = direction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }
>>>>>>> Stashed changes

    public void markAsRead() {
        this.read = true;
    }
}
