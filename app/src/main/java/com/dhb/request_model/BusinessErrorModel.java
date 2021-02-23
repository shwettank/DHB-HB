package com.dhb.request_model;

import com.google.gson.annotations.SerializedName;

public class BusinessErrorModel {
    private String type;

    @SerializedName("status")
    private int statusCode;

    @SerializedName("messages")
    private BusinessErrorType messages;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BusinessErrorType getMessages() {
        return messages;
    }

    public void setMessages(BusinessErrorType messages) {
        this.messages = messages;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
