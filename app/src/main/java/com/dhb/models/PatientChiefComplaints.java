package com.dhb.models;

import com.google.gson.annotations.SerializedName;

public class PatientChiefComplaints {

    // Model info

    private String id;
    @SerializedName("patient_id")
    private String patientId;
    @SerializedName("complaint_type_id")
    private String complaintTypeId;
    @SerializedName("visit_id")
    private String visitId;
    @SerializedName("created_at")
    private long createdAt;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("updated_at")
    private long updatedAt;
    @SerializedName("updated_by")
    private String updatedBy;
    @SerializedName("record_status")
    private String recordStatus;
    @SerializedName("sync_status")
    private String syncStatus;
    @SerializedName("sync_action")
    private String syncAction;
    private boolean isSelected;
    private ComplaintType complaintType;

    public ComplaintType getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(ComplaintType complaintType) {
        this.complaintType = complaintType;
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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getComplaintTypeId() {
        return complaintTypeId;
    }

    public void setComplaintTypeId(String complaintTypeId) {
        this.complaintTypeId = complaintTypeId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
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

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
