package com.dhb.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.request_model.LoginRequestModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.AlertDialogMessage;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.GeoLocationDialogDelegate;
import com.dhb.utils.GeoLocationUtils;
import com.dhb.utils.Logger;
import com.dhb.utils.NetworkUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import static com.google.android.gms.common.GooglePlayServicesUtil.getErrorDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LoginActivity  extends AbstractActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GeoLocationDialogDelegate {
private Button btnLogin;
private ImageButton ibtnForgotPassword;
private LoginActivity activity;
private EditText edtPassword;
private EditText edtMobile;
private ApiCallAsyncTask loginApiCallAsyncTask;
private ApiCallAsyncTask getAllDoctorTimingsApiCallAsyncTask;
private TextView txtMobile;
private TextView txtPassword;
private TextView txtPowrDigital;
private TextView txtPowerhealthBook;
private ResponseParser responseParser;
private GoogleApiClient mGoogleApiClient;
private Location mLastLocation;

private boolean isAfterGooglePlayServiceIntent = false;
private boolean isAfterLocationDialog = false;
private String latitude;
private String longitude;
private DhbDao dhbDao;

private ProgressDialog syncBarProgressDialog;

        SyncStatusReceiver syncStatusReceiver;

private boolean isAfterMasterSyncDone = false;

        PowerManager.WakeLock wakeLock;
private ImageButton ibtnCreateNewUser;

private boolean isAfterOnCreate = false;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_login_screen);
        appPreferenceManager = new AppPreferenceManager(activity);
        // AbstractDao abstractDao= new

        initUI();
        setListener();
        setFont();

        if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Reception")){
        showScreenId(getResources().getString(R.string.screen_no_rec_sign_in));
        } else if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Doctor")){
        showScreenId(getResources().getString(R.string.screen_no_doc_sign_in));
        }

        isAfterOnCreate = true;

        }

public void initUI() {
        super.initUI();
        dhbDao = new DhbDao(activity);
        edtMobile = (EditText) findViewById(R.id.et_mobile);
        edtPassword = (EditText) findViewById(R.id.et_password);
        txtPassword = (TextView) findViewById(R.id.txt_password);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        btnLogin = (Button) findViewById(R.id.btn_sign_in);
        ibtnForgotPassword = (ImageButton) findViewById(R.id.ibtn_forgot_password);
        txtPowrDigital = (TextView) findViewById(R.id.txt_power_digital);
        txtPowerhealthBook = (TextView) findViewById(R.id.txt_power_heath_book);
        ibtnCreateNewUser = (ImageButton) findViewById(R.id.ibtn_create_new_user);
//        edtMobile.requestFocus();
		/*edtMobile.setText("8055978471");
		                           edtPassword.setText("2345!");*/
        }

private void setListener() {
        btnLogin.setOnClickListener(new LoginActionOnClickListener());
        ibtnForgotPassword.setOnClickListener(new ForgotPasswordOnclickListener());
        ibtnCreateNewUser.setOnClickListener(new CreateNewUserOnclickListener());
        edtPassword.setOnEditorActionListener(new EditorActionListener());
//        edtMobile.setOnTouchListener(new ShowKeyboardListener());
        }

private void setFont() {
        edtPassword.setTypeface(fontOpenRobotoRegular);
        edtMobile.setTypeface(fontOpenRobotoRegular);
        btnLogin.setTypeface(fontOpenRobotoRegular);
        txtMobile.setTypeface(fontOpenRobotoRegular);
        txtPassword.setTypeface(fontOpenRobotoRegular);

        txtPowrDigital.setTypeface(fontArialBold);
        txtPowerhealthBook.setTypeface(fontArialBold);


        }

private class EditorActionListener implements TextView.OnEditorActionListener {

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
            btnLogin.performClick();
        }
        return false;
    }

}

private class ForgotPasswordOnclickListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        goToActivity(activity, ForgotPasswordActivity.class, null);
    }

}

private class LoginActionOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {

        if (isGooglePlayServicesAvailable())
            if (validate()){
                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                LoginRequestModel loginRequestModel = new LoginRequestModel();
                loginRequestModel.setMobile(edtMobile.getText().toString());
                loginRequestModel.setPassword(edtPassword.getText().toString());
                loginRequestModel.setRole(AppConstants.LOGIN_TYPE);

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null){
                    latitude = String.valueOf(mLastLocation.getLatitude());
                    longitude = String.valueOf(mLastLocation.getLongitude());
                }

                if (latitude != null && !latitude.isEmpty()){
                    loginRequestModel.setLatitude(latitude);
                }
                if (longitude != null && !longitude.isEmpty()){
                    loginRequestModel.setLongitude(longitude);
                }
                loginApiCallAsyncTask = asyncTaskForRequest.getLoginAsyncTask(loginRequestModel);
                loginApiCallAsyncTask.setApiCallAsyncTaskDelegate(new LoginApiCallResult());
                loginApiCallAsyncTask.execute(loginApiCallAsyncTask);

            }
    }

}

/*private class LoginApiCallResult implements ApiCallAsyncTaskDelegate {
    @Override
    public void apiCallResult(String json, int statusCode) {
        responseParser = new ResponseParser(activity);
        if (!responseParser.parseIntoError(json, statusCode)){

            LoginResponseModelNew loginResponseModelNew = responseParser.getLoginResponseModelNew(json, statusCode);

            if (loginResponseModelNew != null && loginResponseModelNew.getStatus() == 200 && loginResponseModelNew.getUser() != null){

                if (!loginResponseModelNew.getUser().getRoleName().equalsIgnoreCase(AppConstants.LOGIN_TYPE)){
                    Toast.makeText(activity, "Invalid Credentials. Please login with valid " + AppConstants.LOGIN_TYPE.toLowerCase() + " credentials.", Toast.LENGTH_SHORT).show();
                    return;
                }

                appPreferenceManager.setAPISessionKey(loginResponseModelNew.getUser().getSession().getSessionKey());
                appPreferenceManager.setUserId(loginResponseModelNew.getUser().getId());
//					appPreferenceManager.setPhone(loginResponseModel.getUser_details().getMobile());

                appPreferenceManager.setFirstName(loginResponseModelNew.getUser().getFirstName());
                appPreferenceManager.setMiddleName(loginResponseModelNew.getUser().getMiddleName());
                appPreferenceManager.setLastName(loginResponseModelNew.getUser().getLastName());

                appPreferenceManager.setDoctorName(loginResponseModelNew.getUser().getFirstName() + " " + loginResponseModelNew.getUser().getLastName());
                appPreferenceManager.setHospitalName(loginResponseModelNew.getUser().getHospitalName());
                appPreferenceManager.setRoleId("" + loginResponseModelNew.getUser().getRoleId());
                appPreferenceManager.setRoleName(loginResponseModelNew.getUser().getRoleName());
                appPreferenceManager.setStateId(loginResponseModelNew.getUser().getStateId());
                appPreferenceManager.setDistrictId(loginResponseModelNew.getUser().getDistrictId());
                appPreferenceManager.setTalukaId(loginResponseModelNew.getUser().getTalukaId());

                if (loginResponseModelNew.getUser().getInfo() != null){
                    if (loginResponseModelNew.getUser().getInfo().getProfileImage() != null){
                        appPreferenceManager.setImageUrl(loginResponseModelNew.getUser().getInfo().getProfileImage());
                    } else {
                        appPreferenceManager.setImageUrl("");
                    }
                    if (loginResponseModelNew.getUser().getInfo().getGender() != null){
                        appPreferenceManager.setGender(loginResponseModelNew.getUser().getInfo().getGender());
                    } else {
                        appPreferenceManager.setGender("");
                    }
                    if (loginResponseModelNew.getUser().getInfo().getPincode() != null){
                        appPreferenceManager.setPinCode(loginResponseModelNew.getUser().getInfo().getPincode());
                    } else {
                        appPreferenceManager.setPinCode("");
                    }
                }

                dhbDao = new DhbDao(activity);
                UsersDao usersDao = new UsersDao(dhbDao.getDb());
                //insert data to user table

                loginResponseModelNew.getUser().setSyncStatus(AppConstants.STATUS_SYNCED);
                loginResponseModelNew.getUser().setSyncAction(AppConstants.INSERT);

                if (usersDao.insertUser(loginResponseModelNew.getUser()) >= 1){

                    UserRoleMapDao userRoleMap = new UserRoleMapDao(dhbDao.getDb());
                    userRoleMap.setUserId(loginResponseModelNew.getUser().getId());
                    userRoleMap.setRoleId("" + loginResponseModelNew.getUser().getRoleId());
                    userRoleMap.setCreatedAt(System.currentTimeMillis());
                    userRoleMap.setSyncStatus(AppConstants.STATUS_SYNCED);
                    userRoleMap.setSyncAction(AppConstants.INSERT);
                    if (userRoleMap.insertUserRoleMap(userRoleMap) >= 1){
                        SessionDao sessionDao = new SessionDao(dhbDao.getDb());
                        sessionDao.insertSession(loginResponseModelNew.getUser().getSession());
                    }
//						if (loginResponseModelNew.getUser().getHealthCareFirmDetails().size() > 0){
                    if (loginResponseModelNew.getUser().getLoginHealthCareFirmDetail() != null){
                        if (loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getUserHealthCareFirmMapList() != null){
                            for (UserHealthCareFirmMap userHealthCareFirmMap : loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getUserHealthCareFirmMapList()){
                                UserHealthCareFirmMapDao userHealthCareFirmMapDao = new UserHealthCareFirmMapDao(dhbDao.getDb());
                                userHealthCareFirmMapDao.insertOrUpdateUserHealthcareFirmMapAfterApiCall(userHealthCareFirmMap);
                            }
                        }

                        if (loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getHealthCareFirmList() != null){
                            for (HealthCareFirm healthCareFirm : loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getHealthCareFirmList()){
                                HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
                                healthCareFirmDao.insertOrUpdateHealthCareFirmAfterApiCall(healthCareFirm);
                            }
                        }
                        if (loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getDepartmentList() != null){
                            for (Department department : loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getDepartmentList()){
                                DepartmentDao departmentDao = new DepartmentDao(dhbDao.getDb());
                                departmentDao.insertOrUpdateDepartmentAfterApiCall(department);
                            }
                        }
                        if (loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getHealthCareFirmDepartmentMapList() != null){
                            for (HealthCareFirmDepartmentMap healthCareFirmDepartmentMap : loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getHealthCareFirmDepartmentMapList()){
                                HealthCareFirmDepartmentMapDao healthCareFirmDepartmentMapDao = new HealthCareFirmDepartmentMapDao(dhbDao.getDb());
                                healthCareFirmDepartmentMapDao.insertOrUpdateHealthcareFirmDepartmentMapAfterApiCall(healthCareFirmDepartmentMap);
                            }
                        }
                        if (loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getSpecialities() != null){
                            for (Speciality speciality : loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getSpecialities()){
                                SpecialityDao specialityDao = new SpecialityDao(dhbDao.getDb());
                                specialityDao.insertOrUpdateSpecialityAfterApiCall(speciality);
                            }
                        }
                        if (loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getDoctorSpecialityMaps() != null){
                            for (DoctorSpecialityMap doctorSpecialityMap : loginResponseModelNew.getUser().getLoginHealthCareFirmDetail().getDoctorSpecialityMaps()){
                                DoctorSpecialityMapDao doctorSpecialityMapDao = new DoctorSpecialityMapDao(dhbDao.getDb());
                                doctorSpecialityMapDao.insertOrUpdateDoctorSpecialityMapAfterApiCall(doctorSpecialityMap);
                            }
                        }
                    }


//						}

                    if (NetworkUtils.isNetworkAvailable(activity)){
                        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                        getAllDoctorTimingsApiCallAsyncTask = asyncTaskForRequest.getAllDoctorTimingsAsyncTask();
                        getAllDoctorTimingsApiCallAsyncTask.setApiCallAsyncTaskDelegate(new GetAllDoctorTimingsApiCallResult());
                        getAllDoctorTimingsApiCallAsyncTask.execute(getAllDoctorTimingsApiCallAsyncTask);
                    } else {

//							appPreferenceManager.setIsAfterLogin(true);
//							switchToActivity(activity, HomeScreenActivity.class, null);

                        callMasterSync();

                    }
                }
            }
        }
    }

    @Override
    public void onApiCancelled() {

    }

}*/

    public void callMasterSync() {
        if (NetworkUtils.isNetworkAvailable(activity)){
            if (isMasterTableSyncServiceIsInProgress()){
                Toast.makeText(activity, getString(R.string.sync_in_progress), Toast.LENGTH_LONG).show();
            } else {
                callMasterTableSyncService();
            }
        } else {
            Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();

            if (appPreferenceManager != null){
                appPreferenceManager.clearAllPreferences();
            }

            if (dhbDao == null){
                dhbDao = new DhbDao(activity);
            }
            dhbDao.deleteDb();
        }
    }

/*private class GetAllDoctorTimingsApiCallResult implements ApiCallAsyncTaskDelegate {
    @Override
    public void apiCallResult(String json, int statusCode) {
        ResponseParser responseParser = new ResponseParser(activity);
        try {
            GetDoctorTimingResponseModel doctorTimingResponseModel = responseParser.getAllDoctorTimingsResponseModel(json, statusCode);
            if (doctorTimingResponseModel != null && doctorTimingResponseModel.getResult() != null){

                dhbDao = new DhbDao(activity);

                if (doctorTimingResponseModel.getResult().getDoctorTiming() != null){
                    for (DoctorTiming doctorTiming : doctorTimingResponseModel.getResult().getDoctorTiming()){
                        DoctorTimingDao doctorTimingDao = new DoctorTimingDao(dhbDao.getDb());
                        doctorTimingDao.insertOrUpdateDoctorTimingAfterApiCall(doctorTiming);
                    }
                }
                if (doctorTimingResponseModel.getResult().getDoctorReceptionMap() != null){
                    for (DoctorReceptionMap doctorReceptionMap : doctorTimingResponseModel.getResult().getDoctorReceptionMap()){
                        DoctorReceptionMapDao doctorReceptionMapDao = new DoctorReceptionMapDao(dhbDao.getDb());
                        doctorReceptionMapDao.insertOrUpdatedoctorReceiptionMapAfterApiCall(doctorReceptionMap);
                    }
                }
                if (doctorTimingResponseModel.getResult().getUsers() != null){
                    for (Users users : doctorTimingResponseModel.getResult().getUsers()){
                        UsersDao usersDao = new UsersDao(dhbDao.getDb());
                        usersDao.insertOrUpdateUsersAfterApiCall(users);
                    }
                }
                if (doctorTimingResponseModel.getResult().getUserHealthCareFirmMap() != null){
                    for (UserHealthCareFirmMap userHealthCareFirmMap : doctorTimingResponseModel.getResult().getUserHealthCareFirmMap()){
                        UserHealthCareFirmMapDao userHealthCareFirmMapDao = new UserHealthCareFirmMapDao(dhbDao.getDb());
                        userHealthCareFirmMapDao.insertOrUpdateUserHealthcareFirmMapAfterApiCall(userHealthCareFirmMap);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();

//				appPreferenceManager.setIsAfterLogin(true);
//				switchToActivity(activity, HomeScreenActivity.class, null);

            callMasterSync();
        }

//			appPreferenceManager.setIsAfterLogin(true);
//			switchToActivity(activity, HomeScreenActivity.class, null);

        callMasterSync();

    }

    @Override
    public void onApiCancelled() {

    }

}*/


    private boolean validate() {
        String password = edtPassword.getText().toString();
        String mobile = edtMobile.getText().toString();
        AlertDialogMessage alertDialogMessage = new AlertDialogMessage();

        if (mobile == null || (mobile != null && mobile.isEmpty())){
            Toast.makeText(activity, getResources().getString(R.string.alert_enter_Mobile), Toast.LENGTH_LONG).show();
            //alertDialogMessage.showAlert(activity, getResources().getString(R.string.alert_enter_Mobile), true);
            return false;
        } else if (password == null || (password != null && password.isEmpty())){
            Toast.makeText(activity, getResources().getString(R.string.alert_enter_password), Toast.LENGTH_LONG).show();
            //alertDialogMessage.showAlert(activity, getResources().getString(R.string.alert_enter_password), true);
            return false;
        } else if (password != null && password.length() < 5){
            Toast.makeText(activity, getResources().getString(R.string.alert_password_length_should_be_5_digit), Toast.LENGTH_LONG).show();
            //alertDialogMessage.showAlert(activity, getResources().getString(R.string.alert_password_length_should_be_5_digit), true);
            return false;
        } else {
            return true;
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (GeoLocationUtils.getLocationSetting(this)){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null){
//            Toast.makeText(activity, String.valueOf(mLastLocation.getLatitude()) + "\n" + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();

                latitude = String.valueOf(mLastLocation.getLatitude());
                longitude = String.valueOf(mLastLocation.getLongitude());

                Logger.debug(String.valueOf(mLastLocation.getLatitude()));
                Logger.debug(String.valueOf(mLastLocation.getLongitude()));

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e){
                    e.printStackTrace();
                }

                if (addresses != null){
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only i

                    String addressGenerated = "Address : "
                            + (address == null ? "" : address)
                            + (city == null ? "" : (city + ", "))
                            + (state == null ? "" : (state + ", "))
                            + (country == null ? "" : (country + ", "))
                            + (postalCode == null ? "" : (postalCode + ", "));
//						+ (knownName == null ? "" : (knownName));

                    if (addressGenerated.trim().endsWith(",")){
                        addressGenerated = addressGenerated.trim();
                        addressGenerated = addressGenerated.substring(0, addressGenerated.length() - 1);
                    }

                    Logger.debug(addressGenerated);
                    Toast.makeText(LoginActivity.this, addressGenerated, Toast.LENGTH_LONG).show();
                }

//            showNextScreen();
            }
//		else {
//			if (!isAfterLocationDialog){
//				isAfterLocationDialog = true;
//				GeoLocationUtils.showGPSDisabledAlertToUser(this, this);
//			} else {
////                showNextScreen();
//			}
//		}
        } else {
            if (!isAfterLocationDialog){
                isAfterLocationDialog = true;
                GeoLocationUtils.showGPSDisabledAlertToUser(this, this);
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onGeoLocDialogCancelPressed() {
        isAfterLocationDialog = false;
//        showNextScreen();
//        edtMobile.clearFocus();
        edtMobile.requestFocus();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isAfterOnCreate){
            isAfterOnCreate = false;
            buildGoogleApiClient();
        }

        mGoogleApiClient.connect();

        if (wakeLock != null){
            acquirewakeLock();
        }

        if (isAfterGooglePlayServiceIntent){
            isGooglePlayServicesAvailable();
            isAfterGooglePlayServiceIntent = false;
        }

        IntentFilter filterMasterSyncProgress = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS);
        IntentFilter filterMasterSyncDone = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_DONE);
        IntentFilter filterMasterSyncNoData = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA);
        IntentFilter filterMasterSyncIssue = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_ISSUE);

        syncStatusReceiver = new SyncStatusReceiver();

        registerReceiver(syncStatusReceiver, filterMasterSyncProgress);
        registerReceiver(syncStatusReceiver, filterMasterSyncDone);
        registerReceiver(syncStatusReceiver, filterMasterSyncNoData);
        registerReceiver(syncStatusReceiver, filterMasterSyncIssue);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

        if (wakeLock != null){
            releaseWakeLock();
        }

        if (syncStatusReceiver != null){
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }

        stopMasterTableSyncService();

        if (!isAfterMasterSyncDone){
            if (appPreferenceManager != null){
                appPreferenceManager.clearAllPreferences();
            }
            if (dhbDao == null){
                dhbDao = new DhbDao(activity);
            }
            dhbDao.deleteDb();
        }

        hideSyncProgressBarDialog();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!isAfterMasterSyncDone){
            if (appPreferenceManager != null){
                appPreferenceManager.clearAllPreferences();
            }
        }

        stopMasterTableSyncService();

        hideSyncProgressBarDialog();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status){
            return true;
        } else {
            getErrorDialog(status, this, 111).show();
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 111){
            isAfterGooglePlayServiceIntent = true;
        } else {
            isAfterGooglePlayServiceIntent = false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isAfterLocationDialog = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isAfterMasterSyncDone){
            if (appPreferenceManager != null){
                appPreferenceManager.clearAllPreferences();
            }
            if (dhbDao == null){
                dhbDao = new DhbDao(activity);
            }
            dhbDao.deleteDb();
        }

        if (dhbDao != null){
            dhbDao.closeDatabase();
        }

        if (syncStatusReceiver != null){
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }

        stopMasterTableSyncService();

        hideSyncProgressBarDialog();

    }

    public void showSyncProgressBarDialog() {
        if (!syncBarProgressDialog.isShowing()){
            syncBarProgressDialog.show();
        }
    }

    public void hideSyncProgressBarDialog() {
        if (syncBarProgressDialog != null && syncBarProgressDialog.isShowing()){
            syncBarProgressDialog.dismiss();
        }
    }

    public void initSyncProgressBarDialog() {
        if (syncBarProgressDialog == null){
            syncBarProgressDialog = new ProgressDialog(LoginActivity.this);
            syncBarProgressDialog.setTitle("Please wait");
            syncBarProgressDialog.setMessage("Master Table Sync in Progress");
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setProgress(0);
//			syncBarProgressDialog.setMax(100);
            syncBarProgressDialog.setCancelable(false);
        }
    }

    public void setSyncProgressDialogTypeToHorizontalOrSpinner(boolean isHorizontalMode) {
        if (isHorizontalMode){
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setMessage("Master Table Sync in Progress");
        } else {
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public boolean isMasterTableSyncServiceIsInProgress() {
       /* if (isServiceRunning(MasterTableSyncService.class)){
            return true;
        } else {
            return false;
        }*/

    }

    private void callMasterTableSyncService() {
  /*      Intent intent = new Intent(LoginActivity.this, MasterTableSyncService.class);
        startService(intent);*/
        return;
    }

    public void stopMasterTableSyncService() {
      //  stopService(new Intent(LoginActivity.this, MasterTableSyncService.class));
    }

public class SyncStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction() != null){

            if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_IN_PROGRESS)
                    || intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS)){

                int totalUploadCount = 0, uploadedCount = 0;

                if (intent != null && intent.getExtras() != null){
                    if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT)){
                        totalUploadCount = intent.getExtras().getInt(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT);
                    }
                    if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT)){
                        uploadedCount = intent.getExtras().getInt(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT);
                    }
                }

                initSyncProgressBarDialog();

                if (totalUploadCount == 0 && uploadedCount == 0){
                    setSyncProgressDialogTypeToHorizontalOrSpinner(false);
                    showSyncProgressBarDialog();
                } else {
                    setSyncProgressDialogTypeToHorizontalOrSpinner(true);

                    syncBarProgressDialog.setMax(totalUploadCount);

                    if (uploadedCount <= totalUploadCount){
                        syncBarProgressDialog.setProgress(uploadedCount);
                        showSyncProgressBarDialog();
                    } else {

                        syncBarProgressDialog.setProgress(totalUploadCount);
                        showSyncProgressBarDialog();

//						if(ApplicationController.isOfflineSyncServiceIsInProgress(LoginActivity.this)){
//							stopOfflineSyncService(LoginActivity.this);
//						}
                    }
                }

            } else if (intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_DONE)){

                if (intent != null && intent.getExtras() != null){
                    boolean isIssueFoundInSync = false;
                    if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_ISSUE_FOUND)){
                        isIssueFoundInSync = intent.getExtras().getBoolean(AppConstants.MASTER_TABLE_UPDATE_ISSUE_FOUND);
                    }

                    if (isMasterTableSyncServiceIsInProgress()){
                        stopMasterTableSyncService();
                    }

                    if (isIssueFoundInSync){
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                        if (appPreferenceManager != null){
                            appPreferenceManager.clearAllPreferences();
                        }

                        //appPreferenceManager.setIsAfterLogin(false);
                        if (dhbDao == null){
                            dhbDao = new DhbDao(activity);
                        }
                        dhbDao.deleteDb();

                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.sync_done_master_table), Toast.LENGTH_SHORT).show();

                        appPreferenceManager.setIsAfterLogin(true);
                        isAfterMasterSyncDone = true;
                        switchToActivity(activity, HomeScreenActivity.class, null);

                    }

                    hideSyncProgressBarDialog();
                    syncBarProgressDialog = null;
                }

            } else if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_NO_DATA)
                    || intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA)){

                Toast.makeText(LoginActivity.this, getResources().getString(R.string.sync_no_data), Toast.LENGTH_SHORT).show();

            }
        }
    }

}

    public void acquirewakeLock() {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Login Wakelock");
        wakeLock.acquire();
    }

    public void releaseWakeLock() {
        wakeLock.release();
    }

private class CreateNewUserOnclickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        if (!NetworkUtils.isNetworkAvailable(activity)){
            Toast.makeText(activity, getString(R.string.feature_available_in_online_mode), Toast.LENGTH_SHORT).show();
            return;
        }
        goToActivity(activity, CreateNewUserActivity.class, null);
    }

}

    private void showKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

private class ShowKeyboardListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        showKeyboard(v);
        return false;
    }

}
}
