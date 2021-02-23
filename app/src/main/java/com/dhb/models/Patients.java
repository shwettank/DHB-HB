package com.dhb.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhb.request_model.GeneralUserInfoModel;
import com.google.gson.annotations.SerializedName;

public class Patients implements Parcelable {

    // Model info

    private String id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("middle_name")
    private String middleName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("date_of_birth")
    private long dob;
    @SerializedName("healthcare_firm_id")
    private String healthCareFirmId;

    @SerializedName("dhb_id")
    private String dhbId;
    @SerializedName("family_id")
    private String familyId;
    private String password;
    @SerializedName("mobile1")
    private String mobile1;
    @SerializedName("mobile2")
    private String mobile2;
    @SerializedName("mobile3")
    private String mobile3;
    private GeneralUserInfoModel info;
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
    @SerializedName("registration_status")
    private String registrationStatus;
    @SerializedName("is_mr")
    private String isMr;
    @SerializedName("sync_status")
    private String syncStatus;
    @SerializedName("sync_action")
    private String syncAction;
    @SerializedName("registration_plan")
    private String registrationPlan;

    /////////////// only used while sync apis are called
    @SerializedName("mobile_patient_id")
    private String mobilePatientId;

    public Patients() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public String getHealthCareFirmId() {
        return healthCareFirmId;
    }

    public void setHealthCareFirmId(String healthCareFirmId) {
        this.healthCareFirmId = healthCareFirmId;
    }

    public String getDhbId() {
        return dhbId;
    }

    public void setDhbId(String dhbId) {
        this.dhbId = dhbId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMobile3() {
        return mobile3;
    }

    public void setMobile3(String mobile3) {
        this.mobile3 = mobile3;
    }

    public GeneralUserInfoModel getInfo() {
        return info;
    }

    public void setInfo(GeneralUserInfoModel info) {
        this.info = info;
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

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getIsMr() {
        return isMr;
    }

    public void setIsMr(String isMr) {
        this.isMr = isMr;
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

    public String getRegistrationPlan() {
        return registrationPlan;
    }

    public void setRegistrationPlan(String registrationPlan) {
        this.registrationPlan = registrationPlan;
    }

    /////////////// only used while sync apis are called

    public String getMobilePatientId() {
        return mobilePatientId;
    }

    public void setMobilePatientId(String mobilePatientId) {
        this.mobilePatientId = mobilePatientId;
    }

    ////////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeLong(dob);
        dest.writeString(healthCareFirmId);
        dest.writeString(dhbId);
        dest.writeString(familyId);
        dest.writeString(password);
        dest.writeString(mobile1);
        dest.writeString(mobile2);
        dest.writeString(mobile3);
        dest.writeLong(createdAt);
        dest.writeString(createdBy);
        dest.writeLong(updatedAt);
        dest.writeString(updatedBy);
        dest.writeString(recordStatus);
        dest.writeString(registrationStatus);
        dest.writeString(isMr);
        dest.writeString(syncStatus);
        dest.writeString(syncAction);
        dest.writeParcelable(info, flags);
        dest.writeString(mobilePatientId);
        dest.writeString(registrationPlan);
    }

    protected Patients(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        dob = in.readLong();
        healthCareFirmId = in.readString();
        dhbId = in.readString();
        familyId = in.readString();
        password = in.readString();
        mobile1 = in.readString();
        mobile2 = in.readString();
        mobile3 = in.readString();
        createdAt = in.readLong();
        createdBy = in.readString();
        updatedAt = in.readLong();
        updatedBy = in.readString();
        recordStatus = in.readString();
        registrationStatus = in.readString();
        isMr = in.readString();
        syncStatus = in.readString();
        syncAction = in.readString();
        info = in.readParcelable(GeneralUserInfoModel.class.getClassLoader());
        mobilePatientId = in.readString();
        registrationPlan = in.readString();
    }

    public static final Parcelable.Creator<Patients> CREATOR = new Creator<Patients>() {
        @Override
        public Patients createFromParcel(Parcel in) {
            return new Patients(in);
        }

        @Override
        public Patients[] newArray(int size) {
            return new Patients[size];
        }

    };

}
