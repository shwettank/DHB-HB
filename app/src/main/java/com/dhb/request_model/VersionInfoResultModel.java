package com.dhb.request_model;

import com.dhb.models.VersionInfo;
import com.google.gson.annotations.SerializedName;

public class VersionInfoResultModel {
    private String message;
    private String type;
    @SerializedName("status")
    private int statusCode;
    @SerializedName("result")
    private VersionInfo versionInfo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
