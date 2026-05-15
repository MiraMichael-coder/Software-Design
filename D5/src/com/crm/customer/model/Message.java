package com.crm.customer.model;

import com.crm.common.enums.CommunicationChannel;
import com.crm.common.enums.MessageDirection;
import java.time.LocalDateTime;

public abstract class Message {
    private String messageId;
    private CommunicationChannel channel;
    private MessageDirection direction;
    protected String content;
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

    public void markAsRead() {
        this.read = true;
    }
}
