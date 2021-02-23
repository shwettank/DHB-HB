package com.dhb.request_model;

import com.google.gson.annotations.SerializedName;

public class UserDocumentModel {
    private String id;
    private String user_id;
    @SerializedName("upload_date")
    private long created_at;
    private String created_by;
    private String record_status;
    private String document_type_id;
    private String type_name;
    @SerializedName("file_name")
    private String doc_url;
    @SerializedName("thumb_path")
    private String doc_thumb_url;
    @SerializedName("title")
    private String doc_title;
    @SerializedName("queue_id")
    private String mobileQueueId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getRecord_status() {
        return record_status;
    }

    public void setRecord_status(String record_status) {
        this.record_status = record_status;
    }

    public String getDocument_type_id() {
        return document_type_id;
    }

    public void setDocument_type_id(String document_type_id) {
        this.document_type_id = document_type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public void setDoc_url(String doc_url) {
        this.doc_url = doc_url;
    }

    public String getDoc_thumb_url() {
        return doc_thumb_url;
    }

    public void setDoc_thumb_url(String doc_thumb_url) {
        this.doc_thumb_url = doc_thumb_url;
    }

    public String getDoc_title() {
        return doc_title;
    }

    public String getMobileQueueId() {
        return mobileQueueId;
    }

    public void setMobileQueueId(String mobileQueueId) {
        this.mobileQueueId = mobileQueueId;
    }

    public void setDoc_title(String doc_title) {
        this.doc_title = doc_title;
    }
}
