package com.dhb.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Queue implements Parcelable {

    private String id;
    @SerializedName("patient_id")
    private String patientId;
    @SerializedName("sender_id")
    private String senderId;
    @SerializedName("receiver_id")
    private String receiverID;
    @SerializedName("status")
    private int status;
    @SerializedName("misscall_time")
    private long missCallTime;
    @SerializedName("appointment_start_time")
    private long appointmentStartTime;
    @SerializedName("appointment_end_time")
    private long appointmentEndTime;
    @SerializedName("parent_queue_id")
    private String parentQueuId;
    @SerializedName("sender_comments")
    private String senderComments;
    @SerializedName("receiver_comments")
    private String receiverComments;
    @SerializedName("healthcare_firm_id")
    private String healthcareFirmId;
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

    private Patients patient;
    @SerializedName("documents")
    private Document documents;

    @SerializedName("patient_healthcare_firm_map")
    private PatientHealthCareFirmMap patientHealthCareFirmMap;



    public PatientHealthCareFirmMap getPatientHealthCareFirmMap() {
        return patientHealthCareFirmMap;
    }

    public void setPatientHealthCareFirmMap(PatientHealthCareFirmMap patientHealthCareFirmMap) {
        this.patientHealthCareFirmMap = patientHealthCareFirmMap;
    }

    public Document getDocuments() {
        return documents;
    }

    public void setDocuments(Document documents) {
        this.documents = documents;
    }

// Constructors

    public Queue() {
    }

    // getters and setters

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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getMissCallTime() {
        return missCallTime;
    }

    public void setMissCallTime(long missCallTime) {
        this.missCallTime = missCallTime;
    }

    public long getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(long appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public long getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(long appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public String getParentQueuId() {
        return parentQueuId;
    }

    public void setParentQueuId(String parentQueuId) {
        this.parentQueuId = parentQueuId;
    }

    public String getSenderComments() {
        return senderComments;
    }

    public void setSenderComments(String senderComments) {
        this.senderComments = senderComments;
    }

    public String getReceiverComments() {
        return receiverComments;
    }

    public void setReceiverComments(String receiverComments) {
        this.receiverComments = receiverComments;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public Patients getPatient() {
        return patient;
    }

    public void setPatient(Patients patient) {
        this.patient = patient;
    }

    public String getHealthcareFirmId() {
        return healthcareFirmId;
    }

    public void setHealthcareFirmId(String healthcareFirmId) {
        this.healthcareFirmId = healthcareFirmId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(patientId);
        dest.writeString(senderId);
        dest.writeString(receiverID);
        dest.writeInt(status);
        dest.writeLong(missCallTime);
        dest.writeLong(appointmentStartTime);
        dest.writeLong(appointmentEndTime);
        dest.writeString(parentQueuId);
        dest.writeString(senderComments);
        dest.writeString(receiverComments);
        dest.writeString(healthcareFirmId);
        dest.writeLong(createdAt);
        dest.writeString(createdBy);
        dest.writeLong(updatedAt);
        dest.writeString(updatedBy);
        dest.writeString(recordStatus);
        dest.writeString(syncStatus);
        dest.writeString(syncAction);
        dest.writeParcelable(patient, flags);
    }

    protected Queue(Parcel in) {
        id = in.readString();
        patientId = in.readString();
        senderId = in.readString();
        receiverID = in.readString();
        status = in.readInt();
        missCallTime = in.readLong();
        appointmentStartTime = in.readLong();
        appointmentEndTime = in.readLong();
        parentQueuId = in.readString();
        senderComments = in.readString();
        receiverComments = in.readString();
        healthcareFirmId = in.readString();
        createdAt = in.readLong();
        createdBy = in.readString();
        updatedAt = in.readLong();
        updatedBy = in.readString();
        recordStatus = in.readString();
        syncStatus = in.readString();
        syncAction = in.readString();
        patient = in.readParcelable(Patients.class.getClassLoader());
    }

    public static final Creator<Queue> CREATOR = new Creator<Queue>() {
        @Override
        public Queue createFromParcel(Parcel in) {
            return new Queue(in);
        }

        @Override
        public Queue[] newArray(int size) {
            return new Queue[size];
        }

    };


}
