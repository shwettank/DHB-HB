package com.dhb.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Visit implements Parcelable {


    // Model info

    private String id;
    @SerializedName("patient_id")
    private String patientId;
    @SerializedName("doctor_id")
    private String doctorId;
    @SerializedName("healthcare_firm_id")
    private String healthcareFirmId;
    @SerializedName("title")
    private String title;
    @SerializedName("visit_date")
    private long visitDate;
    @SerializedName("inspection_type")
    private int inspectionType;
    @SerializedName("parent_id")
    private String parentId;
    @SerializedName("queue_id")
    private String queueId;
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

    @SerializedName("is_hold")
    private int isHold;

    @SerializedName("patient_cheif_complaints")
    private ArrayList<PatientChiefComplaints> patientChiefComplaint;

    @SerializedName("chief_complaints")
    ArrayList<Complaint> chiefComplaints;

    @SerializedName("complaint_type")
    ArrayList<ComplaintType> complaintTypes;

    public Visit() {

    }

    // getters and setters

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getHealthcareFirmId() {
        return healthcareFirmId;
    }

    public void setHealthcareFirmId(String healthcareFirmId) {
        this.healthcareFirmId = healthcareFirmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(long visitDate) {
        this.visitDate = visitDate;
    }

    public int getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(int inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public ArrayList<PatientChiefComplaints> getPatientChiefComplaint() {
        return patientChiefComplaint;
    }

    public void setPatientChiefComplaint(ArrayList<PatientChiefComplaints> patientChiefComplaint) {
        this.patientChiefComplaint = patientChiefComplaint;
    }

    public ArrayList<Complaint> getChiefComplaints() {
        return chiefComplaints;
    }

    public void setChiefComplaints(ArrayList<Complaint> chiefComplaints) {
        this.chiefComplaints = chiefComplaints;
    }

    public ArrayList<ComplaintType> getComplaintTypes() {
        return complaintTypes;
    }

    public void setComplaintTypes(ArrayList<ComplaintType> complaintTypes) {
        this.complaintTypes = complaintTypes;
    }

    public int getIsHold() {
        return isHold;
    }

    public void setIsHold(int isHold) {
        this.isHold = isHold;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(patientId);
        dest.writeString(doctorId);
        dest.writeString(healthcareFirmId);
        dest.writeString(title);
        dest.writeLong(visitDate);
        dest.writeInt(inspectionType);
        dest.writeInt(isHold);
        dest.writeString(parentId);
        dest.writeLong(createdAt);
        dest.writeString(createdBy);
        dest.writeLong(updatedAt);
        dest.writeString(updatedBy);
        dest.writeString(recordStatus);
        dest.writeString(syncStatus);
        dest.writeString(syncAction);
    }

    protected Visit(Parcel in) {
        id = in.readString();
        patientId = in.readString();
        doctorId = in.readString();
        healthcareFirmId = in.readString();
        title = in.readString();
        visitDate = in.readLong();
        inspectionType = in.readInt();
        isHold = in.readInt();
        parentId = in.readString();
        createdAt = in.readLong();
        createdBy = in.readString();
        updatedAt = in.readLong();
        updatedBy = in.readString();
        recordStatus = in.readString();
        syncStatus = in.readString();
        syncAction = in.readString();
    }

    public static final Creator<Visit> CREATOR = new Creator<Visit>() {
        @Override
        public Visit createFromParcel(Parcel in) {
            return new Visit(in);
        }

        @Override
        public Visit[] newArray(int size) {
            return new Visit[size];
        }

    };

}
