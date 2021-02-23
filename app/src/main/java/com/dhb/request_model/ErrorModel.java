package com.dhb.request_model;

public class ErrorModel {

    // @SerializedName("TYPE")
    private String type;

    // @SerializedName("MESSAGE")
    private String message;

    // @SerializedName("STATUSCODE")
    private int status;

    private long id;

    private long errorId;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int statusCode) {
        this.status = statusCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getErrorId() {
        return errorId;
    }

    public void setErrorId(long errorId) {
        this.errorId = errorId;
    }
}
