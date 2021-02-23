package com.dhb.request_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GeneralUserInfoModel implements Parcelable {

    private String gender;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("profile_image_thumb")
    private String profileImageThumbnail;

    private String state;

    private String taluka;

    private String district;

    private String village;

    private String area;

    private String pincode;

    @SerializedName("address1")
    private String addressLine1;

    @SerializedName("address2")
    private String addressLine2;

    private String mobile1;

    private String mobile2;

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

    @SerializedName("address3")
    private String addressLine3;

    @SerializedName("address4")
    private String addressLine4;

    @SerializedName("emergency_contact_name")
    private String emergencyContactName;

    @SerializedName("emergency_contact_number")
    private String emergencyContactNumber;

    @SerializedName("emergency_contact_landline_number")
    private String emergencyContactLandLineNumber;

    @SerializedName("blood_group")
    private String bloodGroup;

    @SerializedName("id_proof_type")
    private String idProofType;

    @SerializedName("id_proof_number")
    private String idProofNo;

    @SerializedName("email_id")
    private String emailId;

    @SerializedName("qualification")
    private String qualification;

    @SerializedName("higher_qualification")
    private ArrayList<String> higherQualification;

    @SerializedName("title")
    private String title;

    @SerializedName("email")
    private String userEmail;

    @SerializedName("birth_weight")
    private String birthWeight;

    public GeneralUserInfoModel() {
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressLine4() {
        return addressLine4;
    }

    public void setAddressLine4(String addressLine4) {
        this.addressLine4 = addressLine4;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public String getEmergencyContactLandLineNumber() {
        return emergencyContactLandLineNumber;
    }

    public void setEmergencyContactLandLineNumber(String emergencyContactLandLineNumber) {
        this.emergencyContactLandLineNumber = emergencyContactLandLineNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdProofNo() {
        return idProofNo;
    }

    public void setIdProofNo(String idProofNo) {
        this.idProofNo = idProofNo;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageThumbnail() {
        return profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public ArrayList<String> getHigherQualification() {
        return higherQualification;
    }

    public void setHigherQualification(ArrayList<String> higherQualification) {
        this.higherQualification = higherQualification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(String birthWeight) {
        this.birthWeight = birthWeight;
    }

    protected GeneralUserInfoModel(Parcel in) {
        gender = in.readString();
        profileImage = in.readString();
        profileImageThumbnail = in.readString();
        state = in.readString();
        taluka = in.readString();
        district = in.readString();
        village = in.readString();
        area = in.readString();
        pincode = in.readString();
        mobile1 = in.readString();
        mobile2 = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        addressLine3 = in.readString();
        addressLine4 = in.readString();
        emergencyContactName = in.readString();
        emergencyContactNumber = in.readString();
        emergencyContactLandLineNumber = in.readString();
        bloodGroup = in.readString();
        idProofType = in.readString();
        idProofNo = in.readString();
        emailId = in.readString();
        qualification = in.readString();
        birthWeight = in.readString();
//        higherQualification = (ArrayList<String>) in.readSerializable();
        higherQualification = in.readArrayList(String.class.getClassLoader());
        title = in.readString();
        userEmail = in.readString();
    }

    public static final Creator<GeneralUserInfoModel> CREATOR = new Creator<GeneralUserInfoModel>() {
        @Override
        public GeneralUserInfoModel createFromParcel(Parcel in) {
            return new GeneralUserInfoModel(in);
        }

        @Override
        public GeneralUserInfoModel[] newArray(int size) {
            return new GeneralUserInfoModel[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gender);
        parcel.writeString(profileImage);
        parcel.writeString(profileImageThumbnail);
        parcel.writeString(state);
        parcel.writeString(taluka);
        parcel.writeString(district);
        parcel.writeString(village);
        parcel.writeString(area);
        parcel.writeString(pincode);
        parcel.writeString(mobile1);
        parcel.writeString(mobile2);
        parcel.writeString(addressLine1);
        parcel.writeString(addressLine2);
        parcel.writeString(addressLine3);
        parcel.writeString(addressLine4);
        parcel.writeString(emergencyContactName);
        parcel.writeString(emergencyContactNumber);
        parcel.writeString(emergencyContactLandLineNumber);
        parcel.writeString(bloodGroup);
        parcel.writeString(idProofType);
        parcel.writeString(idProofNo);
        parcel.writeString(emailId);
        parcel.writeString(qualification);
        parcel.writeStringList(higherQualification);
        parcel.writeString(title);
        parcel.writeString(userEmail);
        parcel.writeString(birthWeight);
    }

}
