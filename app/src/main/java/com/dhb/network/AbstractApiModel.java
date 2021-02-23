package com.dhb.network;

import com.dhb.utils.AppConstants;

import java.util.HashMap;
import java.util.List;

public class AbstractApiModel implements AppConstants {

    /* Requset urlt_layout of api call */
    private String requestUrl;

    /* Json string to post/put method */
    private String postData = "";

    /* Headers to be added as list */
    private HashMap<String, String> headerMap;

    public int methodType;


    // DEVELOPMENT SERVER LINK
    // public static String SERVER_BASE_API_URL = "http://66.228.48.88/dhbMob";

    // DHB DEMO SERVER LINK (18th Sep 2015)
    // public static String SERVER_BASE_API_URL = "http://66.228.48.88/dhbDemo";

    // TESTER LINK
    // public static String SERVER_BASE_API_URL = "http://66.228.48.88/dhbTest";

    // PATANJALI
    // public static String SERVER_BASE_API_URL = "http://66.228.48.88/patanjali";

    // PATANJALI DEMO
    //  public static String SERVER_BASE_API_URL = "http://66.228.48.88/patanjaliDemo";

    // PATANJALI TIME AND MATERIAL
    // public static String SERVER_BASE_API_URL = "http://66.228.48.88/patanjalitnm";

    // New DB changes Development
    //public static String SERVER_BASE_API_URL = "http://66.228.48.88/dhbapi";

    // New DB changes Demo
    // public static String SERVER_BASE_API_URL = "http://66.228.48.88/dhbnewdemo";

    // New DB changes Production
    // public static String SERVER_BASE_API_URL = "http://66.228.48.88/dhbapiproduction";

    // New DB changes Amazon Dev
    //public static String SERVER_BASE_API_URL = "http://apidev.digitalhealthbook.in";

//	New DB changes Demo Amazon
//	public static String SERVER_BASE_API_URL = "http://apidemo.digitalhealthbook.in";

    // New DB changes Amazon Production
    public static String SERVER_BASE_API_URL = "https://api.digitalhealthbook.in";

//	public static String SERVER_BASE_API_URL = BuildConfig.BASE_URL;

    /**
     * RESPECTIVE URLS
     */

    public static String DEVICE_TYPE = "android";

    public static String APP_NAME = "dhb";

    // Version Specific Base URL
//	public static String VERSION_API_URL = "/api/v1";

    public static String VERSION_API_URL = "/v1";

    public static String LOGIN = VERSION_API_URL + "/index.php/user/login";

    public static String GET_VERSION = VERSION_API_URL + "/index.php/app/version";

    public String FORGOT_LOGIN = VERSION_API_URL + "/index.php/user/forgotpassword";

    public String LOG_OUT = VERSION_API_URL + "/index.php/user/logout";

    public String CHANGE_PASSWORD = VERSION_API_URL + "/index.php/user/changepassword";

    public String GET_ALL_DOC_TIMINGS = VERSION_API_URL + "/index.php/user/get/doctor/time";

    public String GET_PATIENT_DETAILS = VERSION_API_URL + "/index.php/queue";

    public String GET_UPDATE_QUEUE_STATUS = VERSION_API_URL + "/index.php/queue/";

    public String SEARCH_PATIENT = VERSION_API_URL + "/index.php/patients/healthcare_firm_id/";

    public String VIEW_PATIENT_LIST = VERSION_API_URL + "/index.php/queue/list/";

    public String PATIENT_REGISTRATION = VERSION_API_URL + "/index.php/patients/details";

    public String UPDATE_PATIENT_INFO = VERSION_API_URL + "/index.php/patients/";

    public String VERIFY_OTP = VERSION_API_URL + "/index.php/user/verifyotp";

    public String UPDATE_NEW_PASSWORD = VERSION_API_URL + "/index.php/user/updatenewpassword";

    public String SEND_SUGGESTION = VERSION_API_URL + "/index.php/user/addsuggestion";

    public static String FILE_UPLOAD = VERSION_API_URL + "/index.php/patients/document/upload";

    public String ADD_PATIENT_DOCUMENT = VERSION_API_URL + "/addPatientDocument";

    public String GET_RECENT_DOCUMENT = VERSION_API_URL + "/index.php/patients/alldocuments/";

    public String GET_PATIENT_VISITS_AND_FOLLOWUPS = VERSION_API_URL + "/index.php/patients";

    public String GET_PATIENT_DATA_BY_VISIT_WISE = VERSION_API_URL + "/index.php/patients/visit";

    public String GET_PATIENT_LATEST_DATA_PRE = VERSION_API_URL + "/index.php/patients/";

    public String GET_PATIENT_LATEST_DATA_POST = "/visit/latest/info";

    public String ADD_PATIENT_VITALS = VERSION_API_URL + "/addPatientVitals";

    public String FILTER_DOCUMENT_LIST = VERSION_API_URL + "/index.php/patients";

    public String GET_CHIEF_COMPLAINTS = VERSION_API_URL + "/getChiefComplaints";

    public String ADD_DOCTOR_TIMING = VERSION_API_URL + "/addDocterTiming";

    public String GET_DOCTOR_TIMING = VERSION_API_URL + "/getDocterTiming";

    public String SAVE_DOCTOR_TIME_SLOT = VERSION_API_URL + "/index.php/user/timeslot";

    public String ADD_PATIENT_DIAGNOSIS = VERSION_API_URL + "/addPatientDiagnosis";

    public String GET_PATIENT_VITALS = VERSION_API_URL + "/getPatientVitals";

    public String GET_PATIENT_DIAGNOSIS = VERSION_API_URL + "/getPatientDiagnosisByVisit";

//	public String GET_DOCTOR_AND_DEPARTMENT_LIST = VERSION_API_URL+"/getDoctorAndDepartmentList";

    public String GET_DOCTOR_AND_DEPARTMENT_LIST = VERSION_API_URL + "/index.php/user/reception/doctorinfo";

    public String GET_DEPARTMENT_LIST = VERSION_API_URL + "/index.php/healthcarefirm/departments/list";

    public String GET_DOCTOR_TIMINGS = VERSION_API_URL + "/index.php/user/timing/doctor/";

    public String SAVE_DOCTOR_TIMINGS = VERSION_API_URL + "/index.php/user/doctor/time";

    public String Add_LOCATION = VERSION_API_URL + "/addLocation";

    public String GET_CHIEF_OTHER_COMPLAINTS = VERSION_API_URL + "/getPatientChiefOtherComplaints";

    public String ADD_CHIEF_OTHER_COMPLAINTS = VERSION_API_URL + "/addPatientcomplaints";

    public String ADD_PATIENT_IMMUNIZATION = VERSION_API_URL + "/addPatientImmunization";

    public String GET_PATIENT_IMMUNIZATION = VERSION_API_URL + "/getPatientImmunizations";

    public String DELETE_PATIENT_IMMUNIZATION = VERSION_API_URL + "/deletePatientImmunization";

    public String GET_VACCINE_LIST = VERSION_API_URL + "/getVaccineDose";

    public String GET_PATIENT_HABITS_BY_PATIENT_ID = VERSION_API_URL + "/getPatientHabitsByPatientId/";

    public String GET_PATIENT_HABITS_BY_VISIT_ID = VERSION_API_URL + "/getPatientHabitsByVisitId/";

    public String ADD_PATIENT_FAMILY_HISTORY = VERSION_API_URL + "/addPatientDiseaseHistory";

    public String ADD_PATIENT_MEDICAL_SURGERY_HISTORY = VERSION_API_URL + "/addPatientSurgicalHistory";

    public String GET_PATIENT_FAMILY_HISTORY = VERSION_API_URL + "/getPatientDiseaseHistory/";

    public String GET_PATIENT_MEDICAL_SURGERY_HISTORY = VERSION_API_URL + "/getPatientSurgicalHistory/";

    public String GET_IMMUNIZATION_SCHEDULE = VERSION_API_URL + "/getImmunizationSchedule/Immunization";

    public String ADD_IMMUNIZATION_SCHEDULE = VERSION_API_URL + "/addImmunizationSchedule";

    public String ADD_PATIENT_HABITS = VERSION_API_URL + "/addPatientHabits";

    public String GET_ALL_ALLERGIES_LIST = VERSION_API_URL + "/getAllergies";

    public String GET_PATIENT_ALLERGIES_LIST = VERSION_API_URL + "/getPatientAllergies/";

    public String ADD_OBSERVATION = VERSION_API_URL + "/addObservation";

    public String GET_DISTRICT_WISE_DOCTOR_LIST = VERSION_API_URL + "/getDoctorListLocationWise";

    public String ADD_PATIENT_ALLERGIES = VERSION_API_URL + "/addPatientAllergies";

    public String ADD_DOCTOR_REFERRAL = VERSION_API_URL + "/addDoctorRef";

    public String GET_DOCTOR_REFERRAL = VERSION_API_URL + "/getDoctorRef";

    public String GET_PATIENT_OBSERVATION = VERSION_API_URL + "/getObservation/";

    public String GET_DISEASE_LIST = VERSION_API_URL + "/getDisease/";

    public String GET_MEDICINES_LIST = VERSION_API_URL + "/getTemplate";

    //public String SAVE_PRESCRIPTION = VERSION_API_URL+"/savePrescription";

    public String ADD_NEW_MEMBER_IN_FAMILY = VERSION_API_URL + "/index.php/patients/family";

    public String GET_ALL_FAMILY_MEMEBERS_PRE = VERSION_API_URL + "/index.php/patients/";

    public String GET_ALL_FAMILY_MEMEBERS_POST = "/family/search";

    public String REMOVE_FAMILY_MEMEBER_PRE = VERSION_API_URL + "/index.php/patients/";

    public String REMOVE_FAMILY_MEMEBER_POST = "/family/";

    public String GET_PATIENT_INFO_BY_MISCALL = VERSION_API_URL + "/index.php/patients/misscall/";

    public String SYNC_PATIENT_REGISTRATION = VERSION_API_URL + "/index.php/patients/register/sync";

    public String SYNC_PATIENT_QUEUE = VERSION_API_URL + "/index.php/patients/queue/sync";

    public String SYNC_BILL_ENTRY = VERSION_API_URL + "/index.php/patients/healthcarefirmpatientbill/sync";

    public String SYNC_QUEUE_BILL_MAPPING = VERSION_API_URL + "/index.php/patients/queuebillmap/sync";

    public String SYNC_PATIENT_VISIT_LIST = VERSION_API_URL + "/index.php/patients/visit/sync";

    public String SYNC_PATIENT_VISIT_INFO = VERSION_API_URL + "/index.php/patients/visitdata/sync";

    public String MASTER_TABLE_SYNC = VERSION_API_URL + "/index.php/master/updated/sync";

    public String MASTER_TABLE_COUNT_SYNC = VERSION_API_URL + "/index.php/master/sync/count";

    public String GET_RECEIPTION_FOR_DOCTOR = VERSION_API_URL + "/index.php/user/getreception/";

    public String SYNC_DOCUMENTS = VERSION_API_URL + "/index.php/patients/document/sync";

    public String GET_TEMPLATES_LIST = VERSION_API_URL + "/index.php/template";

    public String GET_TEMPLATE_MEDICINES_LIST = VERSION_API_URL + "/index.php/template/medicine";

    public String GET_HOSPITAL_WISE_PATIENT_COUNT = VERSION_API_URL + "/index.php/patientcount/queue";

    public String SAVE_PRESCRIPTION_PRE = VERSION_API_URL + "/index.php/patient/";

    public String GET_PREVIOUS_PRESCRIPTIONS_PRE = VERSION_API_URL + "/index.php/patients/";

    public String SAVE_PRESCRIPTION_POST = "/prescription";

    public String GET_PREVIOUS_PRESCRIPTIONS_POST = "/prescription/";

    public String SAVE_BILL_PRE = VERSION_API_URL + "/index.php/patients/";

    public String SAVE_BILL_POST = "/bill";

    public String GET_PATIENT_BILL_BY_QUEUE_ID = VERSION_API_URL + "/index.php/patients/bill/";

    public String GET_DOCTOR_PRESENT_QUEUE_PRE = VERSION_API_URL + "/index.php/queue/list/status/";

    public String GET_DOCTOR_PRESENT_QUEUE_MID_PAGE = "/page/";

    public String GET_DOCTOR_PRESENT_QUEUE_MID_FIRM_ID = "/healthcare_firm_id/";

    public String GET_DOCTOR_PRESENT_QUEUE_POST = "/doctor/";

    public String UPDATE_DOCTOR_PROFILE = VERSION_API_URL + "/index.php/user/profile/update";

    public String GET_DOCTOR_PROFILE = VERSION_API_URL + "/index.php/user/profile";

    public String ADD_OR_UPDATE_MEDICINE = VERSION_API_URL + "/index.php/user/medicine";

    public String DELETE_MEDICINE = VERSION_API_URL + "/index.php/medicine/";

    public String CREATE_TEMPLATE = VERSION_API_URL + "/index.php/doctor/template/online";

    public String DELETE_TEMPLATE = VERSION_API_URL + "/index.php/doctor/template/";

    public String CREATE_NEW_USER_SEND_OTP = VERSION_API_URL + "/index.php/user/registration/otp/phone_number/";

    public String CREATE_NEW_USER_VERIFY_OTP = VERSION_API_URL + "/index.php/user/registration/otpvarify/otp/";

    public String USER_REGISTRATION = VERSION_API_URL + "/index.php/user/registration";

    public String GET_HOLD_VISIT_QUEUE_PRE = VERSION_API_URL + "/index.php/queue/healthcare_firm_id/";

    public String GET_HOLD_VISIT_QUEUE_POST = "/is_hold/";

    public String ADD_TO_QUEUE_VIA_HOLD_VISIT = VERSION_API_URL + "/index.php/queue/holdvisit";

    public static final String X_API_KEY = "x-api-key";
    /* Json string to post/put method */
    private String postJsonString;
    /* Headers to be added as list */
    private List<HeaderData> header;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String rquestUrl) {
        this.requestUrl = rquestUrl;
    }

    public String getPostJsonString() {
        return postJsonString;
    }

    public void setPostJsonString(String postJsonString) {
        this.postJsonString = postJsonString;
    }

    public List<HeaderData> getHeader() {
        return header;
    }

    public void setHeader(List<HeaderData> header) {
        this.header = header;
    }

    public void createJson() {

    }

    public void setParam(String key, String value) {

        if (this.requestUrl == null){
            return;
        } else if (!this.requestUrl.contains("?")){
            this.requestUrl = this.requestUrl + "?" + key + "=" + value;
        } else if (key.equals("action")){
            this.requestUrl = this.requestUrl + key + "=" + value;
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + value;
        }
    }

    public void setParam(String key, float value) {

        if (this.requestUrl == null){
            return;
        } else if (!this.requestUrl.contains("?")){
            this.requestUrl = this.requestUrl + "?" + key + "=" + (value);
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + (value);
        }
    }

    public void setParam(String key, int value) {

        if (this.requestUrl == null){
            return;
        } else if (!this.requestUrl.contains("?")){
            this.requestUrl = this.requestUrl + "?" + key + "=" + (value);
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + (value);
        }
    }

    public AbstractApiModel() {
        super();
        headerMap = new HashMap<String, String>();
    }

    public String getRequestUrlt_layout() {
        return requestUrl;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public void putHeader(String key, String value) {

        headerMap.put(key, value);
    }

    public HashMap<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(HashMap<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }
}
