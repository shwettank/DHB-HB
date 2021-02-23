package com.dhb.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.activity.LoginActivity;
import com.dhb.application.ApplicationController;
import com.dhb.request_model.BusinessErrorModel;
import com.dhb.request_model.BusinessErrorType;
import com.dhb.request_model.ErrorModel;
import com.dhb.request_model.ErrorResponseModel;
import com.dhb.request_model.MessageModel;
import com.dhb.request_model.SessionExpireModel;
import com.dhb.request_model.VersionInfoResultModel;
import com.dhb.utils.AlertDialogMessage;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.DeviceUtils;
import com.dhb.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;

public class ResponseParser  implements AppConstants {


    private Activity activity;

    private Context context;

    private Gson gson;

    private AlertDialogMessage alertDialogMessage;

    private AppPreferenceManager appPreferenceManager;

    private TextView txtErrorMsg;

    private boolean isToSwitchActivity = true;

    private MessageModel messageModel;

    public void setTxtErrorMsg(TextView txtErrorMsg) {
        this.txtErrorMsg = txtErrorMsg;
    }

    private boolean isToShowErrorDailog = true;

    private boolean isToShowToast = true;

    private static final int SPACE_COUNT_FOR_TOAST_TIME = 10;

    public void setToShowErrorDailog(boolean isToShowErrorDailog) {
        this.isToShowErrorDailog = isToShowErrorDailog;
    }

    public void setToShowToast(boolean isToShowToast) {
        this.isToShowToast = isToShowToast;
    }

    public ResponseParser(Activity activity) {

        this.activity = activity;

        this.context = activity.getApplicationContext();

        gson = new Gson();

        alertDialogMessage = new AlertDialogMessage();

        appPreferenceManager = new AppPreferenceManager(activity);

    }

    public ResponseParser(Context context) {

        this.context = context;

        gson = new Gson();

        alertDialogMessage = new AlertDialogMessage();

        appPreferenceManager = new AppPreferenceManager(context);

    }

    private boolean parseIntoError(String json) {

        Logger.debug("Response for checking error- " + json);

        this.messageModel = null;
        try {
            this.messageModel = gson.fromJson(json, MessageModel.class );
        } catch (JsonSyntaxException e){
            e.printStackTrace();
            return false;

        }

		/*   if (messageModel != null
		   && messageModel.get() != null
		           && (messageModel.get().equalsIgnoreCase("error") || messageModel
		           .get().equalsIgnoreCase(AppConstants.ERROR_MESSAGE)))*/

        if (this.messageModel != null
                && this.messageModel.getMessages() != null
                && (this.messageModel.getMessages().length > 0)){

            String message = "";

            if (this.messageModel.getMessages() != null){

				/* for (MessageModel.FieldError msg : messageModel.getMessages()) {

				     if (msg != null) {
				         message = message + msg.getMessage() + ",";
				     }

				   }*/


                MessageModel.FieldError msg[] = this.messageModel.getMessages();

                MessageModel.FieldError fieldError = msg[0];


                if (msg != null){
                    message = msg[0].getMessage();

                }

            }

            if (message.lastIndexOf(',') != -1){

                message = message.substring(0, message.lastIndexOf(','));

            }

            if (this.messageModel.getStatusCode() == 401){

//                alertDialogMessage
//                        .setAlertDialogOkListener(new InvalidSessionAlertDialogOkButtonListener());

                if (activity != null){

                    //  alertDialogMessage.showSweptAlert(activity, message, false);

                }

                return true;

            }

            if (activity != null && isToShowErrorDailog){
                //  alertDialogMessage
                //          .setAlertDialogOkListener(alertDialogListener);
                // alertDialogMessage.showSweptAlert(activity, message, false);
                //  alertDialogMessage.showAlertTwoButtonAlert(activity,message,"yes","No",  false);
            } else if (txtErrorMsg != null){

                txtErrorMsg.setVisibility(View.VISIBLE);
                txtErrorMsg.setText(message);

            }

            return true;

        }

        return false;


    }

    public boolean isSessionExpired(String json, int statusCode) {
        if (statusCode == 401){
            Gson gson = new Gson();
            BusinessErrorModel busiError = gson.fromJson(json, BusinessErrorModel.class );
            if (busiError != null && busiError.getMessages() != null){
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public int countSpacesInMessage(String message) {
        int counter = 0;
        for (int i = 0; i < message.length(); i++){
            if (message.charAt(i) == ' '){
                counter++;
            }
        }
        return counter;
    }

    public boolean parseIntoErrorWithoutPopUp(String json, int statusCode) {

        //For beep
        startTimerForBeep();

        try {
            switch (statusCode){
                case 401:
                    BusinessErrorModel busiError = gson.fromJson(json, BusinessErrorModel.class );
                    if (busiError != null && busiError.getMessages() != null){
                        return false; //parseAfterError(json, busiError);
                    } else {
                        SessionExpireModel sessionExpireModel = gson.fromJson(json, SessionExpireModel.class );
                        if (sessionExpireModel != null){
                            if (activity != null){
                                return parseAfterError(json, sessionExpireModel, activity, false);
                            } else {
                                return parseAfterError(json, sessionExpireModel, context, false);
                            }
                        }
                    }

                    break;
                case 400:
                    BusinessErrorModel businessErrorModel = gson.fromJson(json, BusinessErrorModel.class );
                    if (businessErrorModel != null && businessErrorModel.getMessages() != null){
                        return false; //parseAfterError(json, businessErrorModel);
                    }

                    ErrorResponseModel errorResponseModel = gson.fromJson(json, ErrorResponseModel.class );
                    if (errorResponseModel != null && errorResponseModel.getMessage() != null){
                        Toast.makeText(activity, errorResponseModel.getMessage().toString(), Toast.LENGTH_LONG).show();
                        return false;
                    }

                    MessageModel messageModel = null;
                    try {
                        messageModel = gson.fromJson(json, MessageModel.class );
                    } catch (JsonSyntaxException e){
                        e.printStackTrace();
                        return false;

                    }
                    if (messageModel != null
                            && messageModel.getMessages() != null
                            && (messageModel.getMessages().length > 0)){

                        String message = "";

                        if (messageModel.getMessages() != null){

                            for (MessageModel.FieldError msg : messageModel.getMessages()){

                                if (msg != null){
                                    message = message + msg.getMessage() + ",";
                                }

                            }

                        }

                        MessageModel.FieldError msg[] = messageModel.getMessages();

                        MessageModel.FieldError fieldError = msg[0];


                        //if (msg != null && msg.length>1) {
                        if (msg != null){
                            message = msg[0].getMessage();
                            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
                                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                                alertDialogMessage1.showAlert(activity, message.toString(), true);
                                alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());
                            } else if (isToShowToast && !DeviceUtils.isAppIsInBackground(context)){
                                if (message.toString().contains(" ") && countSpacesInMessage(message.toString()) >= SPACE_COUNT_FOR_TOAST_TIME){
                                    Toast.makeText(activity, message.toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(activity, message.toString(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }


                        if (message.lastIndexOf(',') != -1){
                            message = message.substring(0, message.lastIndexOf(','));
                            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
                                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                                alertDialogMessage1.showAlert(activity, message.toString(), true);
                                alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());
                            } else if (isToShowToast && !DeviceUtils.isAppIsInBackground(context)){
                                if (message.toString().contains(" ") && countSpacesInMessage(message.toString()) >= SPACE_COUNT_FOR_TOAST_TIME){
                                    Toast.makeText(activity, message.toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(activity, message.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    break;

                case 200:
                    ErrorModel errorModel = gson.fromJson(json, ErrorModel.class );
                    if (errorModel != null && errorModel.getMessage() != null && errorModel.getType() != null && (errorModel.getType().equalsIgnoreCase(SUCCESS_MESSAGE) || errorModel.getType().equalsIgnoreCase(ERROR_MESSAGE))){
                        parseAfterError(json, errorModel);
                    } else {
                        errorModel = null;
                        return false;
                    }
                    break;

                case 500:

                    BusinessErrorModel businessErrorModel1 = gson.fromJson(json, BusinessErrorModel.class );
                    if (businessErrorModel1 != null){
                        return parseInternalServerError(json, businessErrorModel1);
                    }
                    break;

                case 404:

                    BusinessErrorModel businessErrorModel2 = gson.fromJson(json, BusinessErrorModel.class );
                    if (businessErrorModel2 != null){
                        return parseInternalServerError(json, businessErrorModel2);
                    }
                    break;
                default:
                    ErrorModel defaultErrorModel = gson.fromJson(json, ErrorModel.class );
                    if (defaultErrorModel != null && defaultErrorModel.getMessage() != null){
                        if (defaultErrorModel.getMessage().equalsIgnoreCase("User logged out successfully")){
                            //parseLogoutApi(json);
                            return true;
                        } else {

                            return parseAfterError(json, defaultErrorModel);
                        }
                    } else {
                        return parseAfterError(json, defaultErrorModel);
                    }
            }

        } catch (JsonSyntaxException js){

            return true;

        }
        return false;

    }

    public boolean parseIntoError(String json, int statusCode) {

        //For beep
//		startTimerForBeep();


        try {

            switch (statusCode){
                case 401:
                    BusinessErrorModel busiError = gson.fromJson(json, BusinessErrorModel.class );
                    if (busiError != null && busiError.getMessages() != null){
                        return parseAfterError(json, busiError);
                    } else {
                        SessionExpireModel sessionExpireModel = gson.fromJson(json, SessionExpireModel.class );
                        if (sessionExpireModel != null){
                            if (activity != null){
                                return parseAfterError(json, sessionExpireModel, activity, false);
                            } else {
                                return parseAfterError(json, sessionExpireModel, context, false);
                            }
                        }
                    }

                    break;
                case 400:
                    BusinessErrorModel businessErrorModel = gson.fromJson(json, BusinessErrorModel.class );
                    if (businessErrorModel != null && businessErrorModel.getMessages() != null){
                        return parseAfterError(json, businessErrorModel);
                    }

                    ErrorResponseModel errorResponseModel = gson.fromJson(json, ErrorResponseModel.class );
                    if (errorResponseModel != null && errorResponseModel.getMessage() != null){
                        if (isToShowToast && !DeviceUtils.isAppIsInBackground(context)){
                            if (errorResponseModel.getMessage().toString().contains(" ") && countSpacesInMessage(errorResponseModel.getMessage().toString()) >= SPACE_COUNT_FOR_TOAST_TIME){
                                // if (!appPreferenceManager.isAppInBackground()) {
                                Toast.makeText(activity, errorResponseModel.getMessage().toString(), Toast.LENGTH_LONG).show();
                                // }
                            } else {
                                //  if (!appPreferenceManager.isAppInBackground()) {
                                Toast.makeText(context, errorResponseModel.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                //  }
                            }
                        }
                        return true;
                    }

                    MessageModel messageModel = null;
                    try {
                        messageModel = gson.fromJson(json, MessageModel.class );
                    } catch (JsonSyntaxException e){
                        e.printStackTrace();
                        return false;

                    }
                    if (messageModel != null
                            && messageModel.getMessages() != null
                            && (messageModel.getMessages().length > 0)){

                        String message = "";

                        if (messageModel.getMessages() != null){

                            for (MessageModel.FieldError msg : messageModel.getMessages()){

                                if (msg != null){
                                    message = message + msg.getMessage() + ",";
                                }

                            }

                        }

                        MessageModel.FieldError msg[] = messageModel.getMessages();

                        MessageModel.FieldError fieldError = msg[0];


                        //if (msg != null && msg.length>1) {
                        if (msg != null && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
                            message = msg[0].getMessage();

                            AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                            if (activity != null){
                                alertDialogMessage1.showAlert(activity, message.toString(), true);
                            } else {
                                alertDialogMessage1.showAlert(context.getApplicationContext(), message.toString(), true);
                            }
                            //alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());


                        }


                        if (message.lastIndexOf(',') != -1 && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){

                            message = message.substring(0, message.lastIndexOf(','));
                            AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();

                            if (activity != null){
                                alertDialogMessage1.showAlert(activity, message.toString(), true);
                            } else {
                                alertDialogMessage1.showAlert(context.getApplicationContext(), message.toString(), true);
                            }
                            alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

                        }
                    }

                    break;

                case 200:
                    ErrorModel errorModel = gson.fromJson(json, ErrorModel.class );
                    if (errorModel != null && errorModel.getMessage() != null && errorModel.getType() != null && (errorModel.getType().equalsIgnoreCase(ERROR_MESSAGE)
                            || errorModel.getType().equals(ERROR_MESSAGE_OPTIONAL))){
                        parseAfterError(json, errorModel);
                    } else {
                        errorModel = null;
                        return false;
                    }

                case 500:

                    BusinessErrorModel businessErrorModel1 = gson.fromJson(json, BusinessErrorModel.class );
                    if (businessErrorModel1 != null){
                        return parseInternalServerError(json, businessErrorModel1);
                    }
                    break;

                case 404:

                    BusinessErrorModel businessErrorModel2 = gson.fromJson(json, BusinessErrorModel.class );
                    if (businessErrorModel2 != null){
                        return parseInternalServerError(json, businessErrorModel2);
                    }
                    break;
                default:
                    ErrorModel defaultErrorModel = gson.fromJson(json, ErrorModel.class );
                    if (defaultErrorModel != null && defaultErrorModel.getMessage() != null){
                        if (defaultErrorModel.getMessage().equalsIgnoreCase("User logged out successfully")){
                            //parseLogoutApi(json);
                            return true;
                        } else {

                            return parseAfterError(json, defaultErrorModel);
                        }
                    } else {
                        return parseAfterError(json, defaultErrorModel);
                    }
            }

        } catch (JsonSyntaxException js){

            return true;

        }
        return true;

    }

    private boolean parseAfterError(String json, ErrorModel errorModel) {
        if (errorModel != null && errorModel.getType() != null && errorModel.getMessage() != null){
            if (errorModel.getMessage().equals(MSG_SERVER_EXCEPTION)){

                if (errorModel.getMessage().equals(MSG_INTERNET_CONNECTION_SLOW)){

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getMessage().equals(MSG_COMMUNICATION_PROBLEM)){

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getMessage().equals(MSG_NETWORK_ERROR)){

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getMessage().equals(MSG_UNKNOW_ERROR)){

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getMessage().equals("Please check your network connection.")){

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getType().equalsIgnoreCase("ERR-BUSS")){
                    onApiFailed(errorModel);
                } else if (errorModel.getType().equals(ERROR_MESSAGE) || errorModel.getType().equals(ERROR_MESSAGE_OPTIONAL) || (errorModel.getMessage() != null)){
                    if (!errorModel.getType().equals(SUCCESS_MESSAGE) || !errorModel.getType().equals(SUCCESS_MESSAGE_OPTIONAL)){
                        onApiFailed(errorModel);
                        return false;
                    } else {
                        return false;
                    }
                }
            } else {

                return true;
            }

        }

        return true;

    }

    private boolean parseAfterError(String json, BusinessErrorModel businessErrorModel) {
        if (businessErrorModel != null && businessErrorModel.getType() != null && businessErrorModel.getMessages() != null){

            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
                if (activity != null){
                    alertDialogMessage.showAlert(activity, businessErrorModel.getMessages().getMessage().toString(), true);
                } else {
                    alertDialogMessage.showAlert(context.getApplicationContext(), businessErrorModel.getMessages().getMessage().toString(), true);
                }

                alertDialogMessage.setAlertDialogOkListener(new DialogOkButtonListener());
            }

            return false;

        }
        return true;
    }

    private boolean parseAfterError(String json, SessionExpireModel sessionExpireModel, Activity activity, boolean isDialogCancelable) {
        if (sessionExpireModel != null && sessionExpireModel.getMessage() != null){
            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                alertDialogMessage1.showAlert(activity, sessionExpireModel.getMessage().toString(), isDialogCancelable);
                alertDialogMessage1.setAlertDialogOkListener(new InvalidSessionAlertDialogOkButtonListener());
            }
            return false;
        }
        return true;
    }

    private boolean parseAfterError(String json, SessionExpireModel sessionExpireModel, Context context, boolean isDialogCancelable) {
        if (sessionExpireModel != null && sessionExpireModel.getMessage() != null){
            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                alertDialogMessage1.showAlert(context, sessionExpireModel.getMessage().toString(), true);
                alertDialogMessage1.setAlertDialogOkListener(new InvalidSessionAlertDialogOkButtonListener());
            }
            return false;
        }
        return true;
    }

    private boolean parseInternalServerError(String json, BusinessErrorModel businessErrorModel) {
        if (businessErrorModel != null && businessErrorModel.getType() != null && businessErrorModel.getMessages() != null
                && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
            // onApiFailed(businessErrorModel);
            AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
            alertDialogMessage1.showAlert(activity, businessErrorModel.getMessages().getMessage().toString(), true);
            //alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

            return false;


        }
        return true;
    }

   /* public MedicineResponseModel getUserMedicineMapModel(String json, int statusCode) {
        MedicineResponseModel medicineResponseModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                medicineResponseModel = gson.fromJson(json, MedicineResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return medicineResponseModel;

    }*/

   /* public SuccessDeleteMedicineResponseModel getDeleteMedicineModel(String json, int statusCode) {
        SuccessDeleteMedicineResponseModel successDeleteMedicineResponseModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                successDeleteMedicineResponseModel = gson.fromJson(json, SuccessDeleteMedicineResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return successDeleteMedicineResponseModel;
    }*/

    /*public CreateTemplateResponseModel getCreatePrescriptionModel(String json, int statusCode) {
        CreateTemplateResponseModel createTemplateResponseModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                createTemplateResponseModel = gson.fromJson(json, CreateTemplateResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return createTemplateResponseModel;
    }*/

    private class DialogOkButtonListener implements AlertDialogMessage.AlertDialogOkListener {

        @Override
        public void onAlertDialogOkButtonListener() {

        }

    }

    /*public SuccessResponseModel addPatientHabitsResponseModel(String json, int statusCode) {
        SuccessResponseModel successResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            successResponseModel = gson.fromJson(json, SuccessResponseModel.class );
        }
        return successResponseModel;
    }

    public AllAllergiesDetailModel getAllAllergiesResponseModel(String json, int statusCode) {
        AllAllergiesDetailModel allAllergiesDetailModel = null;
        if (!parseIntoError(json, statusCode)){
            allAllergiesDetailModel = gson.fromJson(json, AllAllergiesDetailModel.class );
        }
        return allAllergiesDetailModel;
    }*/

    public void onApiFailed(BusinessErrorModel businessErrorModel) {
        if (businessErrorModel != null){

            BusinessErrorType list = businessErrorModel.getMessages();
            if (list != null){
                if (activity != null){
                    if (!activity.isFinishing() && context != null && !DeviceUtils.isAppIsInBackground(context)){
                //        AlertMessage.showAlert(activity, businessErrorModel.getType(), list.getMessage(), true);

                    }
                }
            }
        }

    }

    public void onApiFailed(ErrorModel errorModel) {

        if (errorModel != null){

            if (activity != null && context != null && !DeviceUtils.isAppIsInBackground(context)){
                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                alertDialogMessage1.showAlert(activity, errorModel.getMessage().toString(), true);
                // alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

            }

        }

        Logger.debug("inside onapifailed");

    }

    private class InvalidSessionAlertDialogOkButtonListener implements
            AlertDialogMessage.AlertDialogOkListener {

        @Override
        public void onAlertDialogOkButtonListener() {

            AppPreferenceManager appPreferenceManager = new AppPreferenceManager(
                    context);

            appPreferenceManager.clearAllPreferences();
            /*DhbDao dhbDao = new DhbDao(activity);
            dhbDao.deleteDb();*/


            Intent intent = new Intent(context, LoginActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }

    }

    //Login Response parse:
    /*public LoginResponseModel getLoginResponseModel(String json, int statusCode) {
        LoginResponseModel loginUserModel = null;
        if (!parseIntoError(json, statusCode)){
            loginUserModel = gson.fromJson(json, LoginResponseModel.class );
            Logger.debug("After login model parsing");
        }
        return loginUserModel;
    }*/

    //Login Response parse new:
   /* public LoginResponseModelNew getLoginResponseModelNew(String json, int statusCode) {
        LoginResponseModelNew loginUserModel = null;
        if (!parseIntoError(json, statusCode)){
            loginUserModel = gson.fromJson(json, LoginResponseModelNew.class );
            Logger.debug("After login model parsing");
        }
        return loginUserModel;
    }

    //Logout Response parse:
    public SuccessResponseModel getSuccessResponseModel(String json, int statusCode) {
        SuccessResponseModel successResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            successResponseModel = gson.fromJson(json, SuccessResponseModel.class );
        }
        return successResponseModel;
    }*/

   /* public PatientDetailModel getPatientDetailsResponseModel(String json, int statusCode) {
        PatientDetailModel patientDetailModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                patientDetailModel = gson.fromJson(json, PatientDetailModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return patientDetailModel;
    }

    public SearchPatientResultModel getPatientSearchResponseModel(String json, int statusCode) {
        SearchPatientResultModel searchPatientResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                searchPatientResultModel = gson.fromJson(json, SearchPatientResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return searchPatientResultModel;
    }*/

    /*public PatientDetailModelNew getPatientDetailsResponseModelNew(String json, int statusCode) {
        PatientDetailModelNew patientDetailModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                patientDetailModel = gson.fromJson(json, PatientDetailModelNew.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return patientDetailModel;
    }

    public SyncPatientRegistrationResultModel getSyncPatientRegistrationResultModel(String json, int statusCode) {
        SyncPatientRegistrationResultModel syncPatientRegistrationResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                syncPatientRegistrationResultModel = gson.fromJson(json, SyncPatientRegistrationResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return syncPatientRegistrationResultModel;
    }*/

    /*public SyncPatientQueueResultModel getSyncPatientQueueResultModel(String json, int statusCode) {
        SyncPatientQueueResultModel syncPatientQueueResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                syncPatientQueueResultModel = gson.fromJson(json, SyncPatientQueueResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return syncPatientQueueResultModel;
    }

    public SyncHealthCareFirmPatientBillResultModel getSyncHealthCareFirmPatientBillResultModel(String json, int statusCode) {
        SyncHealthCareFirmPatientBillResultModel syncHealthCareFirmPatientBillResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                syncHealthCareFirmPatientBillResultModel = gson.fromJson(json, SyncHealthCareFirmPatientBillResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return syncHealthCareFirmPatientBillResultModel;
    }*/

    /*public SyncQueueBillMappingResultModel getSyncQueueBillMappingResultModel(String json, int statusCode) {
        SyncQueueBillMappingResultModel syncQueueBillMappingResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                syncQueueBillMappingResultModel = gson.fromJson(json, SyncQueueBillMappingResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return syncQueueBillMappingResultModel;
    }

    public SyncPatientVisitListResultModel getSyncPatientVisitListResultModel(String json, int statusCode) {
        SyncPatientVisitListResultModel syncPatientVisitListResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                syncPatientVisitListResultModel = gson.fromJson(json, SyncPatientVisitListResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return syncPatientVisitListResultModel;
    }*/

    /*public SyncDocumentUploadResultModel getSyncDocumentUploadResultModel(String json, int statusCode) {
        SyncDocumentUploadResultModel syncDocumentUploadResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                syncDocumentUploadResultModel = gson.fromJson(json, SyncDocumentUploadResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return syncDocumentUploadResultModel;
    }

    public SyncPatientVisitInfoResultModel getSyncPatientVisitInfoResultModel(String json, int statusCode) {
        SyncPatientVisitInfoResultModel syncPatientVisitInfoResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                syncPatientVisitInfoResultModel = gson.fromJson(json, SyncPatientVisitInfoResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return syncPatientVisitInfoResultModel;
    }*/

    /*public MasterTableUpdateResponseModel getMasterTableUpdateResponseModel(String json, int statusCode) {
        MasterTableUpdateResponseModel masterTableUpdateResponseModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                masterTableUpdateResponseModel = gson.fromJson(json, MasterTableUpdateResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return masterTableUpdateResponseModel;
    }

    public MasterTableCountUpdateResponseModel getMasterTableCountUpdateResponseModel(String json, int statusCode) {
        MasterTableCountUpdateResponseModel masterTableCountUpdateResponseModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                masterTableCountUpdateResponseModel = gson.fromJson(json, MasterTableCountUpdateResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return masterTableCountUpdateResponseModel;
    }*/

    /*public PatientQueueModelNew getPatientQueueResponseModelNew(String json, int statusCode) {
        PatientQueueModelNew patientQueueModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                patientQueueModel = gson.fromJson(json, PatientQueueModelNew.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return patientQueueModel;
    }

    public DoctorReceptionMapResultModel getDoctorReceptionMapResultModel(String json, int statusCode) {
        DoctorReceptionMapResultModel doctorReceptionMapResultModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                doctorReceptionMapResultModel = gson.fromJson(json, DoctorReceptionMapResultModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return doctorReceptionMapResultModel;
    }*/

   /* public NewPatientRegistrationResponseModel getNewPatientRegistrationResponseModel(String json, int statusCode) {

        NewPatientRegistrationResponseModel newPatientRegistrationResponseModel = null;
        try {
            if (!parseIntoError(json, statusCode)){
                newPatientRegistrationResponseModel = gson.fromJson(json, NewPatientRegistrationResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return newPatientRegistrationResponseModel;
    }

    public FileUploadSuccessResponseModel getFileUploadSuccessResponseModel(String json, int statusCode) {
        FileUploadSuccessResponseModel fileUploadSuccessResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            fileUploadSuccessResponseModel = gson.fromJson(json, FileUploadSuccessResponseModel.class );
        }
        return fileUploadSuccessResponseModel;
    }*/

    /*public SearchPatientDetailModel getSearchPatientResponseModel(String json, int statusCode) {
        SearchPatientDetailModel searchPatientDetailModel = null;
        if (!parseIntoError(json, statusCode)){
            searchPatientDetailModel = gson.fromJson(json, SearchPatientDetailModel.class );
        }
        return searchPatientDetailModel;
    }

    public SearchPatientDetailModel getViewPatientResponseModel(String json, int statusCode) {
        SearchPatientDetailModel searchPatientDetailModel = null;
        if (!parseIntoError(json, statusCode)){
            searchPatientDetailModel = gson.fromJson(json, SearchPatientDetailModel.class );
        }
        return searchPatientDetailModel;
    }

    public ArrayList<Visit> getPatientVisitsModel(String json, int statusCode) throws JSONException {
        ArrayList<Visit> visits = null;
        if (!parseIntoErrorWithoutPopUp(json, statusCode)){
            String visitsArray = new JSONObject(json).getJSONArray("result").toString();
            visits = gson.fromJson(visitsArray, new TypeToken<List<Visit> >() {
            }.getType());
        }
        return visits;
    }*/

    /*public PatientVitalsModel getPatientVitalsModel(String json, int statusCode) throws JSONException {
        PatientVitalsModel patientVitalsModels = null;
        if (!parseIntoErrorWithoutPopUp(json, statusCode)){
            String patientVitals = new JSONObject(json).getJSONObject("patient_vitals").toString();
            patientVitalsModels = gson.fromJson(patientVitals, new TypeToken<PatientVitalsModel>() {
            }.getType());
        }
        return patientVitalsModels;
    }

    public FilterImagesResponseModelNew getUserDocumentModel(String json, int statusCode) throws JSONException {
        FilterImagesResponseModelNew userDocumentModels = null;
        if (!parseIntoErrorWithoutPopUp(json, statusCode)){
            userDocumentModels = gson.fromJson(json, FilterImagesResponseModelNew.class );
        }
        return userDocumentModels;
    }*/

    /*public ArrayList<PatientComplaintModel> getCheifComplaints(String json, int statusCode) throws JSONException {
        ArrayList<PatientComplaintModel> patientComplaintModel = null;
        if (!parseIntoError(json, statusCode)){
            String complaintArray = new JSONObject(json).getJSONArray("complaint_list").toString();
            patientComplaintModel = gson.fromJson(complaintArray, new TypeToken<List<PatientComplaintModel> >() {
            }.getType());
        }
        return patientComplaintModel;
    }

    public DoctorTimingModel getDoctorTimings(String json, int statusCode) throws JSONException {
        DoctorTimingModel doctorTimingsModel = null;

        if (!parseIntoError(json, statusCode)){
            doctorTimingsModel = gson.fromJson(json, DoctorTimingModel.class );
        }

        return doctorTimingsModel;
    }*/

    /*public ArrayList<DiagnosisModel> getDiagnosisModel(String json, int statusCode) throws JSONException {
        ArrayList<DiagnosisModel> diagnosisModel = null;
        if (!parseIntoError(json, statusCode)){
            String complaintArray = new JSONObject(json).getJSONArray("patientDiagnosis").toString();
            diagnosisModel = gson.fromJson(complaintArray, new TypeToken<List<DiagnosisModel> >() {
            }.getType());
        }
        return diagnosisModel;
    }

    public DepartmentListApiResponseModel getDepartmentResponseModel(String json, int statusCode) throws JSONException {
        DepartmentListApiResponseModel departmentListApiResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            departmentListApiResponseModel = gson.fromJson(json, DepartmentListApiResponseModel.class );
        }
        return departmentListApiResponseModel;
    }*/

    /*public GetDoctorListResponseModel getDoctorResponseModel(String json, int statusCode) throws JSONException {
        GetDoctorListResponseModel getDoctorListResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            getDoctorListResponseModel = gson.fromJson(json, GetDoctorListResponseModel.class );
        }
        return getDoctorListResponseModel;
    }

    public AddDoctorReferralResponseModel addDoctorReferralResponseModel(String json, int statusCode) throws JSONException {
        AddDoctorReferralResponseModel addDoctorReferralResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            addDoctorReferralResponseModel = gson.fromJson(json, AddDoctorReferralResponseModel.class );
        }
        return addDoctorReferralResponseModel;
    }*/

   /* public GetDoctorReferralResponseModel getDoctorReferralResponseModel(String json, int statusCode) throws JSONException {
        GetDoctorReferralResponseModel getDoctorReferralResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            getDoctorReferralResponseModel = gson.fromJson(json, GetDoctorReferralResponseModel.class );
        }
        return getDoctorReferralResponseModel;
    }

    public ArrayList<ImmunizationModel> getImmunizationList(String json, int statusCode) throws JSONException {
        ArrayList<ImmunizationModel> immunizationModels = null;
        if (!parseIntoErrorWithoutPopUp(json, statusCode)){
            String immunizations = new JSONObject(json).getJSONArray("immunizations").toString();
            immunizationModels = gson.fromJson(immunizations, new TypeToken<List<ImmunizationModel> >() {
            }.getType());
        }
        return immunizationModels;
    }*/

    /*public PatientHabitDetailsModel getPatientHabitsResponseModel(String json, int statusCode) {
        PatientHabitDetailsModel patientHabitDetailsModel = null;
        if (!parseIntoError(json, statusCode)){
            patientHabitDetailsModel = gson.fromJson(json, PatientHabitDetailsModel.class );
        }
        return patientHabitDetailsModel;
    }

    public PatientAllergiesDetailModel getPatientAllergiesResponseModel(String json, int statusCode) {
        PatientAllergiesDetailModel patientAllergiesDetailModel = null;
        if (!parseIntoError(json, statusCode)){
            patientAllergiesDetailModel = gson.fromJson(json, PatientAllergiesDetailModel.class );
        }
        return patientAllergiesDetailModel;
    }*/

    /*public AddDocumentRequestModel getDocumentRequestModel(String json, int statusCode) throws JSONException {
        AddDocumentRequestModel addDocumentRequestModel = null;
        if (!parseIntoErrorWithoutPopUp(json, statusCode)){
            addDocumentRequestModel = gson.fromJson(json, new TypeToken<AddDocumentRequestModel>() {
            }.getType());
        }
        return addDocumentRequestModel;
    }

    public FamilyHistoryResponseModel getFamilyHistoryResponseModel(String json, int statusCode) throws JSONException {
        FamilyHistoryResponseModel familyHistoryResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            familyHistoryResponseModel = gson.fromJson(json, FamilyHistoryResponseModel.class );
        }
        return familyHistoryResponseModel;
    }*/

    /*public MedicalSurgeryHistoryResponseModel getMedicalSurgeryHistoryResponseModel(String json, int statusCode) throws JSONException {
        MedicalSurgeryHistoryResponseModel medicalSurgeryHistoryResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            medicalSurgeryHistoryResponseModel = gson.fromJson(json, MedicalSurgeryHistoryResponseModel.class );
        }
        return medicalSurgeryHistoryResponseModel;
    }

    public ObservationResponseModel getObservationResponseModel(String json, int statusCode) {
        ObservationResponseModel observationResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            observationResponseModel = gson.fromJson(json, ObservationResponseModel.class );
        }
        return observationResponseModel;
    }*/

   /* public PatientAllergiesDetailModel addPatientAllergiesResponseModel(String json, int statusCode) {
        PatientAllergiesDetailModel patientAllergiesDetailModel = null;
        if (!parseIntoError(json, statusCode)){
            patientAllergiesDetailModel = gson.fromJson(json, PatientAllergiesDetailModel.class );
        }
        return patientAllergiesDetailModel;
    }

    public ArrayList<DiseaseModel> getDiseaseList(String json, int statusCode) {
        ArrayList<DiseaseModel> diseaseModels = null;
        if (!parseIntoError(json, statusCode)){

            JSONObject jobj = null;
            try {

                String diseaseList = new JSONObject(json).getJSONArray("diseaseDetails").toString();
                diseaseModels = gson.fromJson(diseaseList, new TypeToken<List<DiseaseModel> >() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        return diseaseModels;
    }*/

    /*public ArrayList<TemplateDetailsInnerModel> getDiseaseTemplateList(String json, int statusCode) {
        ArrayList<TemplateDetailsInnerModel> diseaseTemplateModels = null;
        if (!parseIntoError(json, statusCode)){

            JSONObject jobj = null;
            try {

                String diseaseList = new JSONObject(json).getJSONArray("templateDetails").toString();
                diseaseTemplateModels = gson.fromJson(diseaseList, new TypeToken<List<TemplateDetailsInnerModel> >() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        return diseaseTemplateModels;
    }*/

    private void startTimerForBeep() {
        ApplicationController.beepCouner = 0;

        appPreferenceManager.setLastApiTiming(Calendar.getInstance().getTimeInMillis());

        //		Intent intent1 = new Intent(this.context, AlertBeepService.class);
//		context.stopService(intent1);
       // AlertBeepService.searchAndStopBeepTimerService(context);

        if (!appPreferenceManager.getAPISessionKey().trim().isEmpty()
                && appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){

            /*Intent intent1 = new Intent(this.context, AlertBeepService.class );
            context.startService(intent1);*/
        }
    }

   /* public AddFamilyMemberResponseModel getAddNewMemberToFamilyResponseModel(String json, int statusCode) {

        AddFamilyMemberResponseModel addFamilyMemberResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            addFamilyMemberResponseModel = gson.fromJson(json, AddFamilyMemberResponseModel.class );
        }
        return addFamilyMemberResponseModel;
    }*/

   /* public ChangePasswordRequestModel getChangePasswordResponseModel(String json, int statusCode) {
        ChangePasswordRequestModel changePassewordModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String jsonStr = new JSONObject(json).getJSONObject("result").toString();
                changePassewordModel = gson.fromJson(jsonStr, ChangePasswordRequestModel.class );
                changePassewordModel.setMessage(new JSONObject(json).getString("message"));

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return changePassewordModel;
    }*/

    /*public ForgotPasswordResponseModel getForgotPasswordSuccessResponseModel(String json, int statusCode) {
        ForgotPasswordResponseModel successResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            successResponseModel = gson.fromJson(json, ForgotPasswordResponseModel.class );
        }
        return successResponseModel;
    }*/

    /*public ArrayList<Template> getTemplatesList(String json, int statusCode) {
        ArrayList<Template> templates = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String templatesJson = new JSONObject(json).getJSONArray("result").toString();
                templates = gson.fromJson(templatesJson, new TypeToken<List<Template> >() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        return templates;
    }*/

    /*public ArrayList<Template> getTemplateAndMedicinesList(String json, int statusCode) {
        ArrayList<Template> templateMedicineModels = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String templatesJson = new JSONObject(json).getJSONArray("result").toString();
                templateMedicineModels = gson.fromJson(templatesJson, new TypeToken<List<Template> >() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        return templateMedicineModels;
    }*/

    /*public ArrayList<PreviousPrescriptionResponseModel> getPreviousPrescriptionsList(String json, int statusCode) {
        ArrayList<PreviousPrescriptionResponseModel> prescriptionResponseModels = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String listJson = new JSONObject(json).getJSONArray("result").toString();
                prescriptionResponseModels = gson.fromJson(listJson, new TypeToken<List<PreviousPrescriptionResponseModel> >() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        return prescriptionResponseModels;
    }*/

    /*public SearchPatientDetailApiResponseModel getSearchPatientAPIResponseModel(String json, int statusCode) {
        SearchPatientDetailApiResponseModel searchPatientDetailModel = null;
        if (!parseIntoError(json, statusCode)){
            searchPatientDetailModel = gson.fromJson(json, SearchPatientDetailApiResponseModel.class );
        }
        return searchPatientDetailModel;
    }*/

    /*public DoctorDepartmentListResponseModel getDepartmentDoctorListResponseModel(String json, int statusCode) throws JSONException {
        DoctorDepartmentListResponseModel doctorDepartmentListResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            doctorDepartmentListResponseModel = gson.fromJson(json, DoctorDepartmentListResponseModel.class );
        }
        return doctorDepartmentListResponseModel;
    }*/

    /*public GetDoctorTimingResponseModel getAllDoctorTimingsResponseModel(String json, int statusCode) throws JSONException {
        GetDoctorTimingResponseModel doctorTimingResponseModel = null;
        doctorTimingResponseModel = gson.fromJson(json, GetDoctorTimingResponseModel.class );
        return doctorTimingResponseModel;
    }*/

    /*public DoctorTimingsResultModel getDoctorTimingsResponseModel(String json, int statusCode) throws JSONException {
        DoctorTimingsResultModel doctorTimingsResultModel = null;
        if (!parseIntoError(json, statusCode)){
            doctorTimingsResultModel = gson.fromJson(json, DoctorTimingsResultModel.class );
        }
        return doctorTimingsResultModel;
    }*/

    /*public DoctorTimingsResponseModel getSavedDoctorTimingsResponseModel(String json, int statusCode) throws JSONException {
        DoctorTimingsResponseModel doctorTimingsResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            doctorTimingsResponseModel = gson.fromJson(json, DoctorTimingsResponseModel.class );
        }
        return doctorTimingsResponseModel;
    }*/

   /* public SavePrescriptionRequestModelNew getSavePrescriptionResponseModel(String json, int statusCode) {
        SavePrescriptionRequestModelNew savePrescriptionRequestModelNew = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String listJson = new JSONObject(json).getJSONObject("result").toString();
                savePrescriptionRequestModelNew = gson.fromJson(listJson, new TypeToken<SavePrescriptionRequestModelNew>() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        return savePrescriptionRequestModelNew;
    }*/

    public ArrayList<Queue> getPatientQueueResponseModel(String json, int statusCode) {
        ArrayList<Queue> queues = null;
        if (!parseIntoErrorWithoutPopUp(json, statusCode)){
            String queuesArray = null;
            try {
                queuesArray = new JSONObject(json).getJSONArray("result").toString();
            } catch (JSONException e){
                e.printStackTrace();
            }
            queues = gson.fromJson(queuesArray, new TypeToken<List<Queue>>() {
            }.getType());
        }
        return queues;
    }

    //Login Response parse new:
    /*public PatientQueueCountResponseModel getPatientQueueCountResponseModel(String json, int statusCode) {
        PatientQueueCountResponseModel patientQueueCountResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            patientQueueCountResponseModel = gson.fromJson(json, PatientQueueCountResponseModel.class );
        }
        return patientQueueCountResponseModel;
    }*/

    /*public RecentDocumentsResponseModel getRecentDocuments(String json, int statusCode) {
        RecentDocumentsResponseModel RecentDocumentsResponseModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                RecentDocumentsResponseModel = gson.fromJson(json, RecentDocumentsResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return RecentDocumentsResponseModel;
    }*/

    /*public SavePatientBillResponseModel getSavePatientBillResponseModel(String json, int statusCode) {
        SavePatientBillResponseModel savePatientBillResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String listJson = new JSONObject(json).getJSONObject("result").toString();
                savePatientBillResponseModel = gson.fromJson(listJson, new TypeToken<SavePatientBillResponseModel>() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        return savePatientBillResponseModel;
    }*/

    /*public GetPatientBillByQueueIdResponseModel getGetPatientBillByQueueIdResponseModel(String json, int statusCode) {
        GetPatientBillByQueueIdResponseModel patientBillByQueueIdResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String listJson = new JSONObject(json).getJSONObject("result").toString();
                patientBillByQueueIdResponseModel = gson.fromJson(listJson, new TypeToken<GetPatientBillByQueueIdResponseModel>() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        return patientBillByQueueIdResponseModel;
    }*/

    /*public ReceptionListResponseModel getReceptionListResponseModel(String json, int statusCode) throws JSONException {
        ReceptionListResponseModel receptionListResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String listJson = new JSONObject(json).getJSONObject("result").toString();
                receptionListResponseModel = gson.fromJson(listJson, new TypeToken<ReceptionListResponseModel>() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }

        return receptionListResponseModel;
    }*/

    public VersionInfoResultModel getVersionInfoResponseModel(String json, int statusCode) throws JSONException {
        VersionInfoResultModel versionInfoResultModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                versionInfoResultModel = gson.fromJson(json, VersionInfoResultModel.class );
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return versionInfoResultModel;
    }

   /* public DoctorProfileResponseModel getDoctorProfileResponseModel(String json, int statusCode) throws JSONException {
        DoctorProfileResponseModel doctorProfileResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String listJson = new JSONObject(json).getJSONObject("result").toString();
                doctorProfileResponseModel = gson.fromJson(listJson, new TypeToken<DoctorProfileResponseModel>() {
                }.getType());

            } catch (JSONException e){
                e.printStackTrace();
            }

        }

        return doctorProfileResponseModel;
    }*/

   /* public DeletedTemplateModel deleteTemplateModel(String json, int statusCode) {
        DeletedTemplateModel deletedTemplateModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                String templatesJson = new JSONObject(json).getJSONObject("result").toString();
                deletedTemplateModel = gson.fromJson(templatesJson, DeletedTemplateModel.class );

            } catch (JSONException e){
                e.printStackTrace();
            }

        }
        return deletedTemplateModel;
    }*/

   /* public HoldVisitResponseModel getHoldVisitResponseModel(String json, int statusCode) {
        HoldVisitResponseModel holdVisitResponseModel = null;
        if (!parseIntoError(json, statusCode)){
            try {
                holdVisitResponseModel = gson.fromJson(json, HoldVisitResponseModel.class );
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return holdVisitResponseModel;
    }*/

    /*public AddToQueueViaHoldVisitResponseModel getAddToQueueViaHoldVisitResponseModel(String json, int statusCode) {
        AddToQueueViaHoldVisitResponseModel addToQueueViaHoldVisitResponseModel = null;

        try {
            if (!parseIntoError(json, statusCode)){
                addToQueueViaHoldVisitResponseModel = gson.fromJson(json, AddToQueueViaHoldVisitResponseModel.class );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return addToQueueViaHoldVisitResponseModel;
    }*/

}
