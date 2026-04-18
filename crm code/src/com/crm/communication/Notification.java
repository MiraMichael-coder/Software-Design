package com.crm.communication;

public abstract class Notification {
    protected String title;

    public Notification(String title) {
        this.title = title;
    }

    public abstract void send();
}
