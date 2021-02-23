package com.dhb.utils;

import android.app.Activity;
import android.content.Context;

import com.dhb.models.VersionInfo;

public class AppPreferenceManager {
    private AppPreference appPreference;

    private String AppVersion = "app_version";
    private String APISessionKey = "ApiSessionKey";
    private String UserEmail = "UserEmail";
    private String firstName = "firstName";
    private String lastName = "lastName";
    private String middleName = "middleName";
    private String phone = "phone";
    private String dateOfBirth = "";
    private String gender = "gender";
    private String roleId = "roleId";
    private String roleName = "roleName";
    private String hospitalName = "hospitalName";
    private String hospitalId = "hospitalId";
    private String doctorName = "doctorName";
    private String imageUrl = "imageUrl";
    private String userId = "userId";
    private String lastApiTiming = "lastApiTiming";
    private String latitude = "latitude";
    private String longitude = "longitude";
    private String loginTime = "loginTime";
    private String versionInfoModel = "versionInfoModel";
    private String isUpdateAvailable = "isUpdateAvailable";

    private String country = "country_name";
    private String state = "state_name";
    private String district = "district_name";
    private String taluka = "taluka_name";
    private String village = "village_name";
    private String place = "place";

    private String stateId = "state_id";
    private String districtId = "district_id";
    private String talukaId = "taluka_id";
    private String pinCode = "pincode";

    private String isAfterLogin = "isAfterLogin";

    private String isAppInBackground = "isAppInBackground";

    private String toShowPatientHabits = "toShowPatientHabits";
    private String toShowCheckups = "toShowCheckups";
    private String toShowPatientAllergies = "toShowPatientAllergies";
    private String toShowLabReports = "toShowLabReports";
    private String toShowImagesNonDiacom = "toShowImagesNonDiacom";
    private String toShowClinicalOrder = "toShowClinicalOrder";
    private String toShowPrescription = "toShowPrescription";
    private String toShowPatientAdvice = "toShowPatientAdvice";
    private String toShowPatientProgressNotes = "toShowPatientProgressNotes";
    private String toShowPatientData = "toShowPatientData";
    private String toShowBillingInformation = "toShowBillingInformation";

    private String hospital_place = "hospital_place";
    private String are_terms_and_conditions_accepted = "are_terms_and_conditions_accepted";

//    private boolean toShowPatientHabits;
//    private boolean toShowCheckups;
//    private boolean toShowPatientAllergies;
//    private boolean toShowLabReports;
//    private boolean toShowImagesNonDiacom;
//    private boolean toShowClinicalOrder;
//    private boolean toShowPrescription;
//    private boolean toShowPatientAdvice;
//    private boolean toShowPatientData;
//    private boolean toShowBillingInformation;




    public AppPreferenceManager(Activity activity) {
        super();
        appPreference = AppPreference.getAppPreferences(activity);
    }

    public AppPreferenceManager(Context context) {
        appPreference = AppPreference.getAppPreferences(context);
    }

    public String getAppVersion() {
        return appPreference.getString(AppVersion, "");
    }

    public void setAppVersion(String appVersion) {
        appPreference.putString(this.AppVersion, appVersion);
    }

    public String getAPISessionKey() {
        return appPreference.getString(APISessionKey, "");
    }

    public void setAPISessionKey(String aPISessionKey) {
        appPreference.putString(this.APISessionKey, aPISessionKey);
    }

    public AppPreference getAppPreference() {
        return appPreference;
    }

    public void setAppPreference(AppPreference appPreference) {
        this.appPreference = appPreference;
    }

    public String getFirstName() {
        return appPreference.getString(firstName, "");
    }

    public void setFirstName(String firstName) {
        appPreference.putString(this.firstName, firstName);
    }

    public String getMiddleName() {
        return appPreference.getString(middleName, "");
    }

    public void setMiddleName(String address) {
        appPreference.putString(this.middleName, address);
    }

    public String getLastName() {
        return appPreference.getString(lastName, "");
    }

    public void setLastName(String lastName) {
        appPreference.putString(this.lastName, lastName);
    }

    public String getPhone() {
        return appPreference.getString(phone, "");
    }

    public void setPhone(String phone) {
        appPreference.putString(this.phone, phone);
    }

    public long getDateOfBirth() {
        return appPreference.getLong(dateOfBirth, 0);
    }

    public void setDateOfBirth(long dateOfBirth) {
        appPreference.putLong(this.dateOfBirth, dateOfBirth);
    }

    public String getCountry() {
        return appPreference.getString(country, "");
    }

    public void setCountry(String country) {
        appPreference.putString(this.country, country);
    }

    public String getState() {
        return appPreference.getString(state, "");
    }

    public void setState(String state) {
        appPreference.putString(this.state, state);
    }

    public String getDistrict() {
        return appPreference.getString(district, "");
    }

    public void setDistrict(String district) {
        appPreference.putString(this.district, district);
    }

    public String getTaluka() {
        return appPreference.getString(taluka, "");
    }

    public void setTaluka(String taluka) {
        appPreference.putString(this.taluka, taluka);
    }

    public String getVillage() {
        return appPreference.getString(village, "");
    }

    public void setVillage(String village) {
        appPreference.putString(this.village, village);
    }

    public String getPlace() {
        return appPreference.getString(place, "");
    }

    public void setPlace(String place) {
        appPreference.putString(this.place, place);
    }

    public String getImageUrl() {
        return appPreference.getString(imageUrl, "");
    }

    public void setImageUrl(String imageUrl) {
        appPreference.putString(this.imageUrl, imageUrl);
    }

    public String getUserId() {
        return appPreference.getString(userId, "");
    }

    public void setUserId(String userId) {
        appPreference.putString(this.userId, userId);
    }

    public String getGender() {
        return appPreference.getString(gender, "");
    }

    public void setGender(String gender) {
        appPreference.putString(this.gender, gender);
    }

    public String getRoleName() {
        return appPreference.getString(roleName, "");
    }

    public void setRoleName(String roleName) {
        appPreference.putString(this.roleName, roleName);
    }

    public String getRoleId() {
        return appPreference.getString(roleId, "");
    }

    public void setRoleId(String roleId) {
        appPreference.putString(this.roleId, roleId);
    }

    public void removeApiSessionKey() {
        appPreference.remove(APISessionKey);
    }

    public String getUserEmail() {
        return appPreference.getString(UserEmail, "");
    }

    public void setUserEmail(String userEmail) {
        appPreference.putString(this.UserEmail, userEmail);
    }

    public String getHospitalName() {
        return appPreference.getString(hospitalName, "");
    }

    public void setHospitalName(String hospitalName) {
        appPreference.putString(this.hospitalName, hospitalName);
    }

    public long getLastApiTiming() {
        return appPreference.getLong(lastApiTiming, 0);
    }

    public void setLastApiTiming(long lastApiTiming) {
        appPreference.putLong(this.lastApiTiming, lastApiTiming);
    }

    public void removeUserEmail() {
        appPreference.remove(UserEmail);
    }

    public String getDoctorName() {
        return appPreference.getString(doctorName, "");
    }

    public void setDoctorName(String doctorName) {
        appPreference.putString(this.doctorName, doctorName);
    }

    public String getLatitude() {
        return appPreference.getString(latitude, "");
    }

    public void setLatitude(String latitude) {
        appPreference.putString(this.latitude, latitude);
    }

    public String getLongitude() {
        return appPreference.getString(longitude, "");
    }

    public void setLongitude(String longitude) {
        appPreference.putString(this.longitude, longitude);
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void clearAllPreferences() {
        boolean termsAndConditionsAccepted = appPreference.getBoolean(this.are_terms_and_conditions_accepted, false);
        appPreference.clearPreferences();
        appPreference.putBoolean(this.are_terms_and_conditions_accepted, termsAndConditionsAccepted);

    }

    public String getHospitalId() {
        return appPreference.getString(hospitalId, "");
    }

    public void setHospitalId(String hospitalId) {
        appPreference.putString(this.hospitalId, hospitalId);
    }

    public boolean isAfterLogin() {
        return appPreference.getBoolean(this.isAfterLogin, false);
    }

    public void setIsAfterLogin(boolean value) {
        appPreference.putBoolean(this.isAfterLogin, value);
    }

    public boolean isAppInBackground() {
        return appPreference.getBoolean(this.isAppInBackground, false);
    }

    public void setIsAppInBackground(boolean isAppInBackground) {
        appPreference.putBoolean(this.isAppInBackground, isAppInBackground);
    }

    public long getStateId() {
        return appPreference.getLong(stateId, 0);
    }

    public void setStateId(long stateId) {
        appPreference.putLong(this.stateId, stateId);
    }

    public long getDistrictId() {
        return appPreference.getLong(districtId, 0);
    }

    public void setDistrictId(long districtId) {
        appPreference.putLong(this.districtId, districtId);
    }

    public long getTalukaId() {
        return appPreference.getLong(talukaId, 0);
    }

    public void setTalukaId(long talukaId) {
        appPreference.putLong(this.talukaId, talukaId);
    }

    public String getPinCode() {
        return appPreference.getString(pinCode, null);
    }

    public void setPinCode(String pinCode) {
        appPreference.putString(this.pinCode, pinCode);
    }

    public boolean isToShowPatientHabits() {
        return appPreference.getBoolean(this.toShowPatientHabits, true);
    }

    public void setToShowPatientHabits(boolean toShowPatientHabits) {
        appPreference.putBoolean(this.toShowPatientHabits, toShowPatientHabits);
    }

    public boolean isToShowCheckups() {
        return appPreference.getBoolean(this.toShowCheckups, true);
    }

    public void setToShowCheckups(boolean toShowCheckups) {
        appPreference.putBoolean(this.toShowCheckups, toShowCheckups);
    }

    public boolean isToShowPatientAllergies() {
        return appPreference.getBoolean(this.toShowPatientAllergies, true);
    }

    public void setToShowPatientAllergies(boolean toShowPatientAllergies) {
        appPreference.putBoolean(this.toShowPatientAllergies, toShowPatientAllergies);
    }

    public boolean isToShowLabReports() {
        return appPreference.getBoolean(this.toShowLabReports, true);
    }

    public void setToShowLabReports(boolean toShowLabReports) {
        appPreference.putBoolean(this.toShowLabReports, toShowLabReports);
    }

    public boolean isToShowImagesNonDiacom() {
        return appPreference.getBoolean(this.toShowImagesNonDiacom, true);
    }

    public void setToShowImagesNonDiacom(boolean toShowImagesNonDiacom) {
        appPreference.putBoolean(this.toShowImagesNonDiacom, toShowImagesNonDiacom);
    }

    public boolean isToShowClinicalOrder() {
        return appPreference.getBoolean(this.toShowClinicalOrder, true);
    }

    public void setToShowClinicalOrder(boolean toShowClinicalOrder) {
        appPreference.putBoolean(this.toShowClinicalOrder, toShowClinicalOrder);
    }

    public boolean isToShowPrescription() {
        return appPreference.getBoolean(this.toShowPrescription, true);
    }

    public void setToShowPrescription(boolean toShowPrescription) {
        appPreference.putBoolean(this.toShowPrescription, toShowPrescription);
    }

    public boolean isToShowPatientAdvice() {
        return appPreference.getBoolean(this.toShowPatientAdvice, true);
    }

    public void setToShowPatientAdvice(boolean toShowPatientAdvice) {
        appPreference.putBoolean(this.toShowPatientAdvice, toShowPatientAdvice);
    }

    public boolean isToShowPatientProgressNotes() {
        return appPreference.getBoolean(this.toShowPatientProgressNotes, true);
    }

    public void setToShowPatientProgressNotes(boolean toShowPatientProgressNotes) {
        appPreference.putBoolean(this.toShowPatientProgressNotes, toShowPatientProgressNotes);
    }

    public boolean isToShowPatientData() {
        return appPreference.getBoolean(this.toShowPatientData, true);
    }

    public void setToShowPatientData(boolean toShowPatientData) {
        appPreference.putBoolean(this.toShowPatientData, toShowPatientData);
    }

    public boolean isToShowBillingInformation() {
        return appPreference.getBoolean(this.toShowBillingInformation, true);
    }

    public void setToShowBillingInformation(boolean toShowBillingInformation) {
        appPreference.putBoolean(this.toShowBillingInformation, toShowBillingInformation);
    }

    public String getHospitalPlace() {
        return appPreference.getString(this.hospital_place, "");
    }

    public void setHospitalPlace(String hospitalPlace) {
        appPreference.putString(this.hospital_place, hospitalPlace);
    }

    public boolean areTermsAndConditionsAccepted() {
        return appPreference.getBoolean(this.are_terms_and_conditions_accepted, false);
    }

    public void setAreTermsAndConditionsAccepted(boolean areTermsAndConditionsAccepted) {
        appPreference.putBoolean(this.are_terms_and_conditions_accepted, areTermsAndConditionsAccepted);
    }
    public boolean IsUpdateAvailable() {
        return appPreference.getBoolean(this.isUpdateAvailable,false);
    }

    public void setIsUpdateAvailable(boolean isUpdateAvailable) {
        appPreference.putBoolean(this.isUpdateAvailable,isUpdateAvailable);
    }

    public VersionInfo getVersionInfoModel(){
        return appPreference.getVersionInfo(this.versionInfoModel);
    }

    public void setVersionInfoModel(VersionInfo versionInfo){
        appPreference.putVersionInfo(this.versionInfoModel,versionInfo);
    }
}
