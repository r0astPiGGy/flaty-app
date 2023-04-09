package com.rodev.flatyapp.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Notify implements Serializable {
    private String id;
    private String title;
    private String content;
    private String userId;
    private Date dateMake;

    public Notify() {}

    public Notify(String title, String content, String userId) {
        this(title, content, new Date(), userId);
    }

    public Notify(String title, String content, Date dateMake, String userId) {
        this(UUID.randomUUID().toString(), title, content, userId, dateMake);
    }

    public Notify(String id, String title, String content, String userId, Date dateMake) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.dateMake = dateMake;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateMake() {
        return dateMake;
    }
}
