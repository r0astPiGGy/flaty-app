package com.rodev.flatyapp.beans;

public class FeedbackRequest {

    private String fullName;
    private String phoneNumber;
    private String request;

    public FeedbackRequest() {

    }

    public FeedbackRequest(String fullName, String phoneNumber, String request) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.request = request;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRequest() {
        return request;
    }
}
