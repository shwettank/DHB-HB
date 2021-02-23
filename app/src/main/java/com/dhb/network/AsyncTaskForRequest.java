package com.dhb.network;

import android.app.Activity;
import android.content.Context;

import com.dhb.R;
import com.dhb.request_model.LoginRequestModel;
import com.dhb.utils.AppPreferenceManager;
import com.google.gson.Gson;

import org.json.JSONException;

public class AsyncTaskForRequest {
    private AppPreferenceManager appPreferenceManager;
    private Activity activity;
    private AbstractApiModel abstractApiModel;

    private Context context;

    public AsyncTaskForRequest(Activity activity) {
        super();
        this.context = activity;
        this.activity = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public AsyncTaskForRequest(Context context) {
        super();
        this.context = context;
        appPreferenceManager = new AppPreferenceManager(context);
        return;

    }

    /*
     * Login Api Integration*/

    public ApiCallAsyncTask getLoginAsyncTask(LoginRequestModel loginRequestModel) {

        Gson gson = new Gson();
        String loginJson = gson.toJson(loginRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.LOGIN);
        abstractApiModel.setPostData(loginJson);
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        if (activity != null){
            apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                    .getString(R.string.progress_message_authenticating_please_wait));
        } else if (context != null){
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_authenticating_please_wait));
        }

        return apiCallAsyncTask;
    }

    /*public ApiCallAsyncTask getLogOutAsyncTask(String latitude, String longitude, String reason, boolean isDoctor) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.LOG_OUT);
        abstractApiModel.setHeader(getHeader());

        if (isDoctor){
            LogoutRequestModel logoutModel = new LogoutRequestModel();
            if (latitude != null)
                logoutModel.setLatitude(latitude);
            if (longitude != null)
                logoutModel.setLongitude(longitude);
            if (reason != null)
                logoutModel.setReason(reason);

            Gson gson = new Gson();
            String logoutJson = gson.toJson(logoutModel);

            if ((latitude != null && longitude != null) || reason != null){
                abstractApiModel.setPostData(logoutJson);
            }
        }

        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getChangePassword(ChangePasswordRequestModel changePasswordRequestModel) {

        Gson gson = new Gson();
        String changePasswordJson = gson.toJson(changePasswordRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.CHANGE_PASSWORD);
        abstractApiModel.setPostData(changePasswordJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAllDoctorTimingsAsyncTask() {

        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_ALL_DOC_TIMINGS);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAddToPatientQueue(Context activity, AddToPatientQueueRequestModel addToPatientQueueRequestModel) {

        Gson gson = new Gson();
        String addToPatientJson = gson.toJson(addToPatientQueueRequestModel);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.PATIENT_REGISTRATION);
        abstractApiModel.setPostData(addToPatientJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;

    }

    public ApiCallAsyncTask getAddToPatientQueueViaSearch(AddToPatientQueueAfterSearchRequestModel addToPatientQueueRequestModel) {
        Gson gson = new Gson();
        String addToPatientJson = gson.toJson(addToPatientQueueRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(context);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.GET_PATIENT_DETAILS);
        abstractApiModel.setPostData(addToPatientJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setProgressBarVisible(true);
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(context.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAddToPatientQueueViaHoldVisit(AddToPatientQueueFromHoldVisitRequestModel addToPatientQueueFromHoldVisitRequestModel) {
        Gson gson = new Gson();
        String addToPatientJson = gson.toJson(addToPatientQueueFromHoldVisitRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(context);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.ADD_TO_QUEUE_VIA_HOLD_VISIT);
        abstractApiModel.setPostData(addToPatientJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setProgressBarVisible(true);
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(context.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getUpdateQueueStatusAsyncTask(UpdateQueueStatusModelNew status, String patientId) {
        Gson gson = new Gson();
        String updateQueueStatusJson = gson.toJson(status);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.GET_UPDATE_QUEUE_STATUS + patientId);
        abstractApiModel.setPostData(updateQueueStatusJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getReceptionForDoctorAsyncTask(String healthCareFrimId) {
        Gson gson = new Gson();
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.GET_RECEIPTION_FOR_DOCTOR + healthCareFrimId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getSearchPatientAsyncTask(SearchRequestModel search) {
        Gson gson = new Gson();
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SEARCH_PATIENT + appPreferenceManager.getHospitalId() + "/search/" + search.getSearchQuery());
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));

        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getSearchPaginationPatientAsyncTask(SearchRequestModel search, int offset) {
        Gson gson = new Gson();
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SEARCH_PATIENT + appPreferenceManager.getHospitalId() + "/searchwithpagination/" + search.getSearchQuery() + "/" + offset);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));

        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getViewPatientListAsyncTask(String status, int pageNo) throws JSONException {
        Gson gson = new Gson();

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.VIEW_PATIENT_LIST + "status/" + status + "/page/" + pageNo);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);

        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getViewPatientListFirmWiseAsyncTask(String status, int pageNo, String firmId) throws JSONException {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.VIEW_PATIENT_LIST + "status/" + status + "/page/" + pageNo + "/healthcare_firm_id/" + firmId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getForgotLoginAsyncTask(String mobile) throws JSONException {

        JSONObject mobileJson = new JSONObject();
        mobileJson.put("mobile", mobile);
        mobileJson.put("role", AppConstants.LOGIN_TYPE);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);

        AbstractApiModel abstractApiModel = new AbstractApiModel();

        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.FORGOT_LOGIN);
        abstractApiModel.setPostData(mobileJson.toString());
        apiCallAsyncTask.setHttpMethod(APICall.POST_METHOD);
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    private List<HeaderData> getHeader() {
        HeaderData headerData = new HeaderData();
        headerData.setHeaderKey(AbstractApiModel.X_API_KEY);
        headerData.setHeaderValue(appPreferenceManager.getAPISessionKey());

        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Content-Type");
        headerData1.setHeaderValue("application/json");


        List<HeaderData> header = new ArrayList<HeaderData>();
        header.add(headerData);
        header.add(headerData1);
        return header;
    }

    private List<HeaderData> getHeaderContentType() {

        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Content-Type");
        headerData1.setHeaderValue("application/json");


        List<HeaderData> header = new ArrayList<HeaderData>();
        header.add(headerData1);
        return header;
    }

    public ApiCallAsyncTask addNewPatientRegistration(NewPatientRegistrationRequestModel patientUserInfoModel) {

        Gson gson = new Gson();
        String addToPatientJson = gson.toJson(patientUserInfoModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.PATIENT_REGISTRATION);
        abstractApiModel.setPostData(addToPatientJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask updatePatientInformation(NewPatientRegistrationRequestModel patientRegistrationRequestModel, String patientID) {

        Gson gson = new Gson();
        String addToPatientJson = gson.toJson(patientRegistrationRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.UPDATE_PATIENT_INFO + patientID + "/update");
        abstractApiModel.setPostData(addToPatientJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask patientRegistrationForSync(SyncPatientRegistrationRequestModel syncPatientRegistrationRequestModel) {

        Gson gson = new Gson();
        String patientsJson = gson.toJson(syncPatientRegistrationRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SYNC_PATIENT_REGISTRATION);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask patientQueueUploadForSync(SyncPatientQueueRequestModel syncPatientQueueRequestModel) {

        Gson gson = new Gson();
        String patientsJson = gson.toJson(syncPatientQueueRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SYNC_PATIENT_QUEUE);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask healthCareFirmPatientBillUploadForSync(SyncHealthCareFirmPatientBillRequestModel syncHealthCareFirmPatientBillRequestModel) {

        Gson gson = new Gson();
        String patientsJson = gson.toJson(syncHealthCareFirmPatientBillRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SYNC_BILL_ENTRY);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask queueBillMapUploadForSync(SyncQueueBillMappingRequestModel syncQueueBillMappingRequestModel) {

        Gson gson = new Gson();
        String patientsJson = gson.toJson(syncQueueBillMappingRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SYNC_QUEUE_BILL_MAPPING);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask patientVisitListUploadForSync(SyncPatientVisitListRequestModel syncPatientVisitRequestModel) {

        Gson gson = new Gson();
        String patientsJson = gson.toJson(syncPatientVisitRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SYNC_PATIENT_VISIT_LIST);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask patientVisitInfoUploadForSync(SyncPatientVisitInfoRequestModel syncPatientVisitInfoRequestModel) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        String patientsJson = gson.toJson(syncPatientVisitInfoRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SYNC_PATIENT_VISIT_INFO);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask documentEntryUploadForSync(Document document) {

        Gson gson = new Gson();
        String documentJson = gson.toJson(document);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SYNC_DOCUMENTS);
        abstractApiModel.setPostData(documentJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask masterTableUpdate(MasterTableUpdateRequestModel masterTableUpdateRequestModel) {

        Gson gson = new GsonBuilder().serializeNulls().create();
        String patientsJson = gson.toJson(masterTableUpdateRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.MASTER_TABLE_SYNC);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask masterTableUpdateCount(MasterTableUpdateRequestModel masterTableUpdateRequestModel) {

        Gson gson = new Gson();
        String patientsJson = gson.toJson(masterTableUpdateRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = null;
        if (activity != null){
            apiCallAsyncTask = new ApiCallAsyncTask(activity);
        } else if (context != null){
            apiCallAsyncTask = new ApiCallAsyncTask(context);
        }
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.MASTER_TABLE_COUNT_SYNC);
        abstractApiModel.setPostData(patientsJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarVisible(false);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addDoctorReferralWithVisitId(DoctorReferralWithVisitIdRequestModel doctorReferralWithVisitIdRequestModel) {

        Gson gson = new Gson();
        String addDoctorReferralJson = gson.toJson(doctorReferralWithVisitIdRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.ADD_DOCTOR_REFERRAL);
        abstractApiModel.setPostData(addDoctorReferralJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addDoctorReferralWithOutVisitId(DoctorReferralWithOutVisitIdRequestModel doctorReferralWithOutVisitIdRequestModel) {

        Gson gson = new Gson();
        String addDoctorReferralJson = gson.toJson(doctorReferralWithOutVisitIdRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.ADD_DOCTOR_REFERRAL);
        abstractApiModel.setPostData(addDoctorReferralJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDoctorReferral(GetDoctorReferralRequestModel getDoctorReferralRequestModel) {

        Gson gson = new Gson();
        String getDoctorReferralJson = gson.toJson(getDoctorReferralRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.GET_DOCTOR_REFERRAL);
        abstractApiModel.setPostData(getDoctorReferralJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask sendSuggestion(SuggestionRequestModel suggestionRequestModel) {

        Gson gson = new Gson();
        String addSuggestionJson = gson.toJson(suggestionRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.SEND_SUGGESTION);
        abstractApiModel.setPostData(addSuggestionJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addDocument(AddDocumentRequestModel addDocumentRequestModel) {

        Gson gson = new Gson();
        String addDocumentJson = gson.toJson(addDocumentRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_DOCUMENT);
        abstractApiModel.setPostData(addDocumentJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientVisitsAndFollowups(String patientId, String healthCareId) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_VISITS_AND_FOLLOWUPS + "/" + patientId + "/visit/healthcare_firm_id/" + healthCareId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getVisitsData(String visitId) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_DATA_BY_VISIT_WISE + "/" + visitId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addPatientVitals(PatientVitalsModel patientVitalsModel) {

        Gson gson = new Gson();
        String addDocumentJson = gson.toJson(patientVitalsModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_VITALS);
        abstractApiModel.setPostData(addDocumentJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask filterDocumentList(String patientId, FilterImageRequestModel filterImageRequestModel) {
        Gson gson = new Gson();
        String requestJson = gson.toJson(filterImageRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        String url = AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FILTER_DOCUMENT_LIST + "/" + patientId + "/document";
        abstractApiModel.setRequestUrl(url);
        abstractApiModel.setPostData(requestJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getChiefComplaint() {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_CHIEF_COMPLAINTS);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask updateDoctorTimingAsyncTask(DoctorTimingModel doctorTimingModel) {
        Gson gson = new Gson();
        String addDocumentJson = gson.toJson(doctorTimingModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_DOCTOR_TIMING);
        abstractApiModel.setPostData(addDocumentJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;

    }

    public ApiCallAsyncTask addPatientDiagnosisAsyncTask(DiagnosisModel diagnosisModel) {

        Gson gson = new Gson();
        String addDocumentJson = gson.toJson(diagnosisModel);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();

        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_DIAGNOSIS);
        abstractApiModel.setPostData(addDocumentJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientVitals(String patientId, String visitId) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_VITALS + "/" + patientId + "/" + visitId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientDiagnosisAsyncTask(String patientId, String visitId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_DIAGNOSIS + "/" + patientId + "/" + visitId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDepartmentDoctorList() {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_DOCTOR_AND_DEPARTMENT_LIST);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDepartmentList() {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_DEPARTMENT_LIST);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDoctorListFromDistrict(String districtName) {
        DoctorListRequestModel doctorListRequestModel = new DoctorListRequestModel();
        doctorListRequestModel.setDistrictName(districtName);
        Gson gson = new Gson();
        String districtJson = gson.toJson(doctorListRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_DISTRICT_WISE_DOCTOR_LIST);
        abstractApiModel.setPostData(districtJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDoctorTimings(String doctorId, String healthcareFirmId) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_DOCTOR_TIMINGS + doctorId);
//		                               + "/health_care_firm_id/" + healthcareFirmId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask saveDoctorTimings(ArrayList<DoctorTiming> doctorTimings) {

        Gson gson = new Gson();
        String doctorTimingsJson = gson.toJson(doctorTimings);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SAVE_DOCTOR_TIMINGS);
        abstractApiModel.setHeader(getHeader());
        abstractApiModel.setPostData(doctorTimingsJson);
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addLocationAsyncTask(String latitude, String longitude) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.Add_LOCATION);

        if (latitude != null && longitude != null){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
            } catch (JSONException e){
                e.printStackTrace();
                return null;
            }
            abstractApiModel.setPostData(jsonObject.toString());
        }
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources().getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getChiefComplaint(String complaintId, String patientId, String visitId) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_CHIEF_OTHER_COMPLAINTS + "/" + complaintId + "/" + patientId + "/" + visitId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addPatientImmunization(AddImmunizationRequestModel requestModel) {
        Gson gson = new Gson();
        String immunizationJson = gson.toJson(requestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_IMMUNIZATION);
        abstractApiModel.setPostData(immunizationJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientImmunization(String patientId, String visitId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_IMMUNIZATION + "/" + patientId *//*+ "/" + visitId*//*);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask deletePatientImmunization(String patientId, String immunizationId) {

        JSONObject jobj = new JSONObject();
        try {
            jobj.put("patient_id", patientId);
            jobj.put("immunization_id", immunizationId);
        } catch (JSONException e){
            e.printStackTrace();
        }
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.DELETE_PATIENT_IMMUNIZATION);
        abstractApiModel.setPostData(jobj.toString());
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getVaccineList() {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_VACCINE_LIST);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getImmunizationSchedule() {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_IMMUNIZATION_SCHEDULE);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addImmunizationSchedule(AddDocumentRequestModel model) {

        Gson gson = new Gson();
        String addDocModel = gson.toJson(model);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_IMMUNIZATION_SCHEDULE);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        abstractApiModel.setPostData(addDocModel);
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addChiefComplaint() {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_CHIEF_OTHER_COMPLAINTS);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientHabits(String id) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
//        if (isVisitId) {
//            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_HABITS_BY_VISIT_ID + id);
//        } else {
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_HABITS_BY_PATIENT_ID + id);
//        }
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addPatientFamilyHistory(FamilyHistoryRequestModel familyHistoryRequestModel) {
        Gson gson = new Gson();
        String familyHistory = gson.toJson(familyHistoryRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_FAMILY_HISTORY);
        abstractApiModel.setPostData(familyHistory);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientFamilyHistory(String patientId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_FAMILY_HISTORY + patientId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addPatientMedicalSurgeryHistory(MedicalSurgeryHistoryRequestModel medicalSurgeryHistoryRequestModel) {
        Gson gson = new Gson();
        String familyHistory = gson.toJson(medicalSurgeryHistoryRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_MEDICAL_SURGERY_HISTORY);
        abstractApiModel.setPostData(familyHistory);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientMedicalSurgeryHistory(String patientId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_MEDICAL_SURGERY_HISTORY + patientId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask updatePatientHabits(HabitsUpdateModel habitsUpdateModel) {
        Gson gson = new Gson();
        String habitJson = gson.toJson(habitsUpdateModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_HABITS);
        abstractApiModel.setPostData(habitJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAllergiesListDetails() {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_ALL_ALLERGIES_LIST);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientAllergiesListDetails(String patientId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_ALLERGIES_LIST + patientId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask updatePatientAllergies(AddAllergiesModel addAllergiesModel) {
        Gson gson = new Gson();
        String habitJson = gson.toJson(addAllergiesModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_PATIENT_ALLERGIES);
        abstractApiModel.setPostData(habitJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask addPatientObservation(ObservationRequestModel observationRequestModel) {
        Gson gson = new Gson();
        String immunizationJson = gson.toJson(observationRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_OBSERVATION);
        abstractApiModel.setPostData(immunizationJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPatientObservation(String patientId, String visitId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_OBSERVATION + patientId + "/" + visitId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDiseaseList(int parameter) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_DISEASE_LIST + parameter);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getMedicinesList(DiseaseListModel diseaseListModel) {

        Gson gson = new Gson();
        String diseaseJson = gson.toJson(diseaseListModel);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_MEDICINES_LIST);
        abstractApiModel.setPostData(diseaseJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask savePrescription(SavePrescriptionModel savePrescriptionModel) {

        Gson gson = new Gson();
        String savePrescriptionJson = gson.toJson(savePrescriptionModel);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SAVE_PRESCRIPTION_PRE);
        abstractApiModel.setPostData(savePrescriptionJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAddNewMemberToFamily(AddFamilyMemberInputModel addFamilyMemberInputModel) {
        Gson gson = new Gson();
        String addToFamilyJson = gson.toJson(addFamilyMemberInputModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(context);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.ADD_NEW_MEMBER_IN_FAMILY);
        abstractApiModel.setPostData(addToFamilyJson);
        Logger.debug("post json: " + addToFamilyJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setProgressBarVisible(true);
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(context.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAllFamilyMembers(String searchParameter) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_ALL_FAMILY_MEMEBERS_PRE + searchParameter + abstractApiModel.GET_ALL_FAMILY_MEMEBERS_POST);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask removeFamilyMember(String patientId, String familyId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.REMOVE_FAMILY_MEMEBER_PRE + patientId + abstractApiModel.REMOVE_FAMILY_MEMEBER_POST + familyId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.DELETE_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getTemplateList() {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_TEMPLATES_LIST);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask callGetMedicinesTemplatesListApi(ArrayList<TemplateMedicinesRequestModel> templates) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_TEMPLATE_MEDICINES_LIST);

        Gson gson = new Gson();
        String templatesJson = gson.toJson(templates);

//        JSONArray jsonObject = new JSONArray();
//        try {
//            jsonObject.put("templates", templatesJson);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        abstractApiModel.setPostData(templatesJson);

        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask verifyOTP(String transactionId, String otp) {

        JSONObject jobj = new JSONObject();
        try {
            jobj.put("otptransactionid", transactionId);
            jobj.put("otp", otp);
        } catch (JSONException e){
            e.printStackTrace();
        }
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.VERIFY_OTP);
        abstractApiModel.setPostData(jobj.toString());
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask verifyOTPChangePassword(String newpassword, String userId, String mobileNo) {

        JSONObject jobj = new JSONObject();
        try {
            jobj.put("newpassword", newpassword);
            jobj.put("user_id", userId);
            jobj.put("mobile", mobileNo);
            jobj.put("role", AppConstants.LOGIN_TYPE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.UPDATE_NEW_PASSWORD);
        abstractApiModel.setPostData(jobj.toString());
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask callSavePrescriptionApi(SavePrescriptionRequestModelNew savePrescriptionRequestModel, String patientId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SAVE_PRESCRIPTION_PRE + patientId + abstractApiModel.SAVE_PRESCRIPTION_POST);

        Gson gson = new Gson();
        String templatesJson = gson.toJson(savePrescriptionRequestModel);

        abstractApiModel.setPostData(templatesJson);

        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getPreviousPrescriptions(String patientId, int limit) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PREVIOUS_PRESCRIPTIONS_PRE + patientId + abstractApiModel.GET_PREVIOUS_PRESCRIPTIONS_POST + limit);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask hospitalWisePatientCountApi(ArrayList<UserHealthCareFirmMap> healthCareFirms) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_HOSPITAL_WISE_PATIENT_COUNT);

        Gson gson = new Gson();
        String healthCareFirmIdJson = gson.toJson(healthCareFirms);

//        JSONArray jsonObject = new JSONArray();
//		for(i=0;i<healthCareFirms.size();i++){
//        try {
//            jsonObject.put("healthcare_firm_id", healthCareFirms.get(i).getHealthCareFirmId());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }}

        abstractApiModel.setPostData(healthCareFirmIdJson);

        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setProgressBarVisible(false);
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getLatestVisitsData(String patientId) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_LATEST_DATA_PRE + patientId + abstractApiModel.GET_PATIENT_LATEST_DATA_POST);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setProgressBarVisible(false);
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getRecentDocuments(String patientId) {

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_RECENT_DOCUMENT + patientId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask callSaveBillApi(HealthCareFirmPatientBill healthCareFirmPatientBill, String patientId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SAVE_BILL_PRE + patientId + abstractApiModel.SAVE_BILL_POST);

        Gson gson = new Gson();
        String billJson = gson.toJson(healthCareFirmPatientBill);

        abstractApiModel.setPostData(billJson);

        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask callGetPatientBillByQueueIdApi(String queueId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_PATIENT_BILL_BY_QUEUE_ID + queueId);

        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDoctorPresentQueue(String status, int pageNo, String healthCareFirmId, String doctorId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_DOCTOR_PRESENT_QUEUE_PRE + status + abstractApiModel.GET_DOCTOR_PRESENT_QUEUE_MID_PAGE + pageNo + abstractApiModel.GET_DOCTOR_PRESENT_QUEUE_MID_FIRM_ID + healthCareFirmId + abstractApiModel.GET_DOCTOR_PRESENT_QUEUE_POST + doctorId);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask saveDoctorTimeSlot(DoctorTimeSlotRequestModel doctorTimeSlotRequestModel) {

        Gson gson = new Gson();
        String doctorTimingsJson = gson.toJson(doctorTimeSlotRequestModel);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SAVE_DOCTOR_TIME_SLOT);
        abstractApiModel.setHeader(getHeader());
        abstractApiModel.setPostData(doctorTimingsJson);
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getVersionAPIAsyncTask(String roleName) {

        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setVersion(AppConstants.ANDROID_APP_VERSION);
        versionInfo.setDeviceType("android");
        versionInfo.setRoleName(roleName);
        Gson gson = new Gson();
        String versionRequestJson = gson.toJson(versionInfo);

        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + AbstractApiModel.GET_VERSION);
        abstractApiModel.setHeader(getHeader());
        abstractApiModel.setPostData(versionRequestJson);
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getUpdateDoctorProfileAPIAsyncTask(DoctorProfileRequestModel doctorProfileRequestModel) {

        Gson gson = new Gson();
        String doctorProfileRequestJson = gson.toJson(doctorProfileRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.UPDATE_DOCTOR_PROFILE);
        abstractApiModel.setPostData(doctorProfileRequestJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAddOrEditMedicine(MedicineRequestModel medicineRequestModel) {
        Gson gson = new Gson();
        String medicineRequestJson = gson.toJson(medicineRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.ADD_OR_UPDATE_MEDICINE);
        abstractApiModel.setPostData(medicineRequestJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;

    }

    public ApiCallAsyncTask getDeleteMedicine(String medicineToBeDeleted) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.DELETE_MEDICINE + medicineToBeDeleted);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.DELETE_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getDoctorProfileAPIAsyncTask() {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.GET_DOCTOR_PROFILE);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask createPrescriptionTemplate(CreatePrescriptionTemplateModel createPrescriptionTemplateModel) {
        Gson gson = new Gson();
        String createPrescriptionTemplateRequestJson = gson.toJson(createPrescriptionTemplateModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.CREATE_TEMPLATE);
        abstractApiModel.setHeader(getHeader());
        abstractApiModel.setPostData(createPrescriptionTemplateRequestJson);
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask deleteTemplate(String templateToBeDeleted) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.DELETE_TEMPLATE + templateToBeDeleted);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.DELETE_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask createNewUserSendOTPAPIAsyncTask(String mobileNo) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.CREATE_NEW_USER_SEND_OTP + mobileNo);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask createNewUserVerifyOTPAPIAsyncTask(String otp, String mobileNo) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.CREATE_NEW_USER_VERIFY_OTP + otp + "/phone_number/" + mobileNo);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask createNewUserAsyncTask(DoctorProfileRequestModel doctorProfileRequestModel) {
        Gson gson = new Gson();
        String requestJson = gson.toJson(doctorProfileRequestModel);
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL
                + abstractApiModel.USER_REGISTRATION);
        abstractApiModel.setPostData(requestJson);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.PUT_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        apiCallAsyncTask.setProgressBarMessage(activity.getResources()
                .getString(R.string.progress_message_authenticating_please_wait));
        return apiCallAsyncTask;

    }

    public ApiCallAsyncTask getHoldVisitQueue(String status, String healthCareFirmId) {
        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(activity);
        AbstractApiModel abstractApiModel = new AbstractApiModel();
        abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_HOLD_VISIT_QUEUE_PRE + healthCareFirmId + abstractApiModel.GET_HOLD_VISIT_QUEUE_POST + status);
        abstractApiModel.setHeader(getHeader());
        apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
        apiCallAsyncTask.setApiModel(abstractApiModel);
        return apiCallAsyncTask;
    }*/

}
