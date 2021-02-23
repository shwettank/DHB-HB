package com.dhb.models;

import com.google.gson.annotations.SerializedName;

public class PatientQueueCountModel {

    private String id;
    @SerializedName("healthcare_firm_id")
    private String healthcareFirmId;
    @SerializedName("patient_count_in_queue")
    private String patientCountQueue;
    @SerializedName("total_patient")
    private String totalPatient;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHealthcareFirmId() {
        return healthcareFirmId;
    }

    public void setHealthcareFirmId(String healthcareFirmId) {
        this.healthcareFirmId = healthcareFirmId;
    }

    public String getPatientCountQueue() {
        return patientCountQueue;
    }

    public void setPatientCountQueue(String patientCountQueue) {
        this.patientCountQueue = patientCountQueue;
    }

    public String getTotalPatient() {
        return totalPatient;
    }

    public void setTotalPatient(String totalPatient) {
        this.totalPatient = totalPatient;
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

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getSyncAction() {
        return syncAction;
    }

    public void setSyncAction(String syncAction) {
        this.syncAction = syncAction;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
