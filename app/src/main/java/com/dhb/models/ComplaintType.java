package com.dhb.models;

import com.google.gson.annotations.SerializedName;

public class ComplaintType {
    // Model info

    private String id;
    @SerializedName("chief_complaints_id")
    private String chiefComplaintsId;
    private String name;
    @SerializedName("created_at")
    private long createdAt;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("updated_at")
    private long updatedAt;
    @SerializedName("updated_by")
    private String updatedBy;
    @SerializedName("is_chief")
    private String isChief;
    @SerializedName("record_status")
    private String recordStatus;
    @SerializedName("sync_status")
    private String syncStatus;
    @SerializedName("sync_action")
    private String syncAction;
    @SerializedName("is_master")
    private String isMaster;
    @SerializedName("visit_id")
    private String visitId;
    private boolean isSelected;
    private Complaint complaint;

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    // getters and setters

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsChief() {
        return isChief;
    }

    public void setIsChief(String isChief) {
        this.isChief = isChief;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncAction() {
        return syncAction;
    }

    public void setSyncAction(String syncAction) {
        this.syncAction = syncAction;
    }

    public String getChiefComplaintsId() {
        return chiefComplaintsId;
    }

    public void setChiefComplaintsId(String chiefComplaintsId) {
        this.chiefComplaintsId = chiefComplaintsId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }
}
