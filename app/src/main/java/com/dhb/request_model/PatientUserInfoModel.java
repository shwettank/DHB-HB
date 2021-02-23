package com.dhb.request_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PatientUserInfoModel implements Parcelable {


    private long rowId;

    @SerializedName("user_id")
    private String patientId;

//	@SerializedName("user_id")
//	private String userId;

    @SerializedName("sender_id")
    private String senderId;

    @SerializedName("receiver_id")
    private String receiverId;

    @SerializedName("queue_id")
    private String mobileQueueId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("middle_name")
    private String middleName;
    @SerializedName("last_name")
    private String lastName;
    //	private String mobile;
    @SerializedName("mobile")
    private String mobile1;
    @SerializedName("mobile1")
    private String mobile2;
    @SerializedName("mobile2")
    private String mobile3;
    private String email;
    @SerializedName("date_of_birth")
    private long dateOfBirth;
    @SerializedName("subscription_type")
    private String subscriptionType;
    @SerializedName("appointment_start_time")
    private long appointmentStartTime;
    @SerializedName("appointment_end_time")
    private long appointmentEndTime;
    @SerializedName("registration_status")
    private int registrationStatus;
    @SerializedName("misscall_time")
    private long missedCallTime;
    @SerializedName("created_at")
    private long createdAt;
    @SerializedName("updated_at")
    private long updatedAt;
    @SerializedName("sync_status")
    private boolean syncStatus;
    private int status;
    private ArrayList<UserDocumentModel> documents;
    @SerializedName("record_status")
    private String recordStatus;

    @SerializedName("dhb_id")
    private String displayId;

    @SerializedName("is_mr")
    private String mr;

    @SerializedName("family_id")
    private String familyId;

    @SerializedName("family_count")
    private int familyCount;

    @SerializedName("is_register")
    private String isRegister;

    @SerializedName("info")
    private GeneralUserInfoModel generalInfo;

    @SerializedName("parent_queue_id")
    private String parentQueueId;

    @SerializedName("sender_comments")
    private String senderComments;

    @SerializedName("receiver_comments")
    private String receiverComments;

    @SerializedName("hin")
    private String hin;

    @SerializedName("memberFirstName")
    private String familyMembersNames;

    @SerializedName("visit")
    private ArrayList<VisitModel> visits;

    public PatientUserInfoModel() {
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getMobileQueueId() {
        return mobileQueueId;
    }

    public void setMobileQueueId(String mobileQueueId) {
        this.mobileQueueId = mobileQueueId;
    }

    public boolean isSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(boolean syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(int registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public long getMissedCallTime() {
        return missedCallTime;
    }

    public void setMissedCallTime(long missedCallTime) {
        this.missedCallTime = missedCallTime;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile) {
        this.mobile1 = mobile;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile) {
        this.mobile2 = mobile;
    }

    public String getMobile3() {
        return mobile3;
    }

    public void setMobile3(String mobile) {
        this.mobile3 = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<UserDocumentModel> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<UserDocumentModel> documents) {
        this.documents = documents;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public GeneralUserInfoModel getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(GeneralUserInfoModel generalInfo) {
        this.generalInfo = generalInfo;
    }

    public int getFamilyCount() {
        return familyCount;
    }

    public void setFamilyCount(int familyCount) {
        this.familyCount = familyCount;
    }

    public String getHin() {
        return hin;
    }

    public void setHin(String hin) {
        this.hin = hin;
    }

    public String getFamilyMembersNames() {
        return familyMembersNames;
    }

    public void setFamilyMembersNames(String familyMembersNames) {
        this.familyMembersNames = familyMembersNames;
    }

    public ArrayList<VisitModel> getVisits() {
        return visits;
    }

    public void setVisits(ArrayList<VisitModel> visits) {
        this.visits = visits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(rowId);
        dest.writeString(patientId);
        dest.writeString(mobileQueueId);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
//		dest.writeString(mobile);
        dest.writeString(mobile1);
        dest.writeString(mobile2);
        dest.writeString(mobile3);
        dest.writeString(email);
        dest.writeLong(dateOfBirth);
        dest.writeString(subscriptionType);
        dest.writeLong(appointmentStartTime);
        dest.writeLong(appointmentEndTime);
        dest.writeInt(registrationStatus);
        dest.writeLong(missedCallTime);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
        dest.writeInt(status);
        dest.writeString(recordStatus);
        dest.writeString(displayId);
        dest.writeString(mr);
        dest.writeString(familyId);
        dest.writeString(isRegister);
        dest.writeString(hin);
        dest.writeString(familyMembersNames);
    }

    protected PatientUserInfoModel(Parcel in) {
        rowId = in.readLong();
        patientId = in.readString();
        mobileQueueId = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
//		mobile = in.readString();
        mobile1 = in.readString();
        mobile2 = in.readString();
        mobile3 = in.readString();
        email = in.readString();
        dateOfBirth = in.readLong();
        subscriptionType = in.readString();
        appointmentStartTime = in.readLong();
        appointmentEndTime = in.readLong();
        registrationStatus = in.readInt();
        missedCallTime = in.readLong();
        createdAt = in.readLong();
        updatedAt = in.readLong();
        status = in.readInt();
        recordStatus = in.readString();
        displayId = in.readString();
        mr = in.readString();
        familyId = in.readString();
        isRegister = in.readString();
        hin = in.readString();
        familyMembersNames = in.readString();
    }

    public static final Creator<PatientUserInfoModel> CREATOR = new Creator<PatientUserInfoModel>() {
        @Override
        public PatientUserInfoModel createFromParcel(Parcel in) {
            return new PatientUserInfoModel(in);
        }

        @Override
        public PatientUserInfoModel[] newArray(int size) {
            return new PatientUserInfoModel[size];
        }

    };
}
