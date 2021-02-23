package com.dhb.models;

import com.google.gson.annotations.SerializedName;

public class PatientHealthCareFirmMap {

    // Model info
    private String id;
    @SerializedName("patient_id")
    private String patientId;
    @SerializedName("healthcare_firm_id")
    private String healthCareFirmId;
    @SerializedName("ref_number")
    private String refNumber;
    @SerializedName("reference_tag")
    private String refTag;
    @SerializedName("old_hin")
    private String oldHin;
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
    private String syncStatus;
    private String syncAction;

    private HealthCareFirm healthCareFirm;

    //getter and setter



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

    public String getHealthCareFirmId() {
        return healthCareFirmId;
    }

    public void setHealthCareFirmId(String healthCareFirmId) {
        this.healthCareFirmId = healthCareFirmId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getRefTag() {
        return refTag;
    }

    public void setRefTag(String refTag) {
        this.refTag = refTag;
    }

    public String getOldHin() {
        return oldHin;
    }

    public void setOldHin(String oldHin) {
        this.oldHin = oldHin;
    }

    public HealthCareFirm getHealthCareFirm() {
        return healthCareFirm;
    }

    public void setHealthCareFirm(HealthCareFirm healthCareFirm) {
        this.healthCareFirm = healthCareFirm;
    }

}
