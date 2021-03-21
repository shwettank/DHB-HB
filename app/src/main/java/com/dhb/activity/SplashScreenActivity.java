package com.dhb.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.application.ApplicationController;
import com.dhb.dao.CreateOrUpgradeDbTask;
import com.dhb.dao.DbHelper;
import com.dhb.models.VersionInfo;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.request_model.VersionInfoResultModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.GeoLocationDialogDelegate;
import com.dhb.utils.GeoLocationUtils;
import com.dhb.utils.InputUtils;
import com.dhb.utils.Logger;
import com.dhb.network.ResponseParser;

import com.dhb.utils.NetworkUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import static com.google.android.gms.common.GooglePlayServicesUtil.getErrorDialog;


import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.os.Handler;

public class SplashScreenActivity extends AbstractActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GeoLocationDialogDelegate {


    private Handler nextScreenHandler;
    private Runnable showNextScreenTask;

    private SplashScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    TextView txtName;
    TextView txtLoading;
    ProgressBar progress;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private boolean isAfterGooglePlayServiceIntent = false;
    private boolean isAfterLocationDialog = false;

    private boolean isDbCreationInProgress = false;
    private boolean isVersionApiInProgress = false;
    private boolean isOkToShowNextScreen = false;
    private TextView txtDigital;
    private TextView txtHealthbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initUI();
   //     isOkToShowNextScreen = false;

    //    showNextScreen(true);

        isDbCreationInProgress = true;
        isVersionApiInProgress = false;
        /*DbHelper.init(activity.getApplicationContext());
        setVisibilityOfProgress(true);
        txtLoading.setText(getString(R.string.loading_db_msg));*/
      //  new CreateOrUpgradeDbTask(new DhbDbDelegate(), this.getApplicationContext()).execute();

        /*if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Reception")){
            showScreenId(getResources().getString(R.string.screen_no_rec_splash_screen));
        } else if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Doctor")){
            showScreenId(getResources().getString(R.string.screen_no_doc_splash_screen));
        }*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *After 3 seconds the User Logged In Status is checked. If true Call Re-Login API
                 * else navigate to Login Activity
                 */
                switchToActivity(activity, LoginActivity.class, null);

            }
        }, 3000);
    }

    private void setFont() {

        txtHealthbook.setTypeface(fontArialBold);
        txtDigital.setTypeface(fontArialBold);
    }

    public void initUI() {
        super.initUI();
        activity = this;

        progress = (ProgressBar) findViewById(R.id.progress_db);
        txtLoading = (TextView) findViewById(R.id.txt_loading_db);

        appPreferenceManager = new AppPreferenceManager(activity);

        txtDigital = (TextView) findViewById(R.id.txt_power_digital);
        txtHealthbook = (TextView) findViewById(R.id.txt_power_heath_book);

        setFont();
//		if (appPreferenceManager.getAPISessionKey().trim().isEmpty()){
        isOkToShowNextScreen = true;
      //  showNextScreen(true);
//		} else {
//
//			isGooglePlayServicesAvailable();
//			buildGoogleApiClient();
//		}
    }



    private void showNextScreen(boolean isDelayedSwitchScreen) {

        if (nextScreenHandler != null){
            nextScreenHandler.removeCallbacks(showNextScreenTask);
        }

        nextScreenHandler = new Handler();
        showNextScreenTask = new Runnable() {
            @Override
            public void run() {

                isOkToShowNextScreen = true;
                if (!isDbCreationInProgress && !isVersionApiInProgress){
                    if (appPreferenceManager.getAPISessionKey().trim().isEmpty()){
                        if (appPreferenceManager.areTermsAndConditionsAccepted()){
                            switchToActivity(activity, LoginActivity.class, null);
                        } else {
                            switchToActivity(activity, TermsAndConditionsActivity.class, null);
                        }
                    } else {
                        Logger.debug("api key: " + appPreferenceManager.getAPISessionKey() + " " + isDbCreationInProgress);
                        appPreferenceManager.setIsAfterLogin(false);
                        switchToActivity(activity, HomeScreenActivity.class, null);
                    }
                }
            }

        };

        nextScreenHandler.postDelayed(showNextScreenTask, isDelayedSwitchScreen ? AppConstants.SPLASH_SCREEN_TIMEOUT : 0);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class DhbDbDelegate implements CreateOrUpgradeDbTask.DbTaskDelegate {
        @Override
        public void dbTaskCompletedWithResult(Boolean result) {

            setVisibilityOfProgress(false);

            isDbCreationInProgress = false;

//			isVersionApiInProgress = false;
//			if (isOkToShowNextScreen){
//				showNextScreen(false);
//			}

            if (NetworkUtils.isNetworkAvailable(activity)){

                isVersionApiInProgress = true;

                setVisibilityOfProgress(true);
                txtLoading.setText(getString(R.string.loading_version_api_msg));

                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
               /* ApiCallAsyncTask apiCallAsyncTask = asyncTaskForRequest.getVersionAPIAsyncTask(AppConstants.LOGIN_TYPE);
                apiCallAsyncTask.setApiCallAsyncTaskDelegate(new VersionInfoApiCallAsyncTaskDelegate());
                apiCallAsyncTask.setProgressBarVisible(false);
                apiCallAsyncTask.execute(apiCallAsyncTask);*/

            } else {

                setVisibilityOfProgress(false);
                isVersionApiInProgress = false;
                Toast.makeText(activity, getString(R.string.loading_version_api_msg_fail), Toast.LENGTH_SHORT).show();
                if (isOkToShowNextScreen){
                    showNextScreen(false);
                }

            }

        }

    }

    private class VersionInfoApiCallAsyncTaskDelegate implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            ResponseParser responseParser = new ResponseParser(activity);
            VersionInfoResultModel versionInfoResultModel = responseParser.getVersionInfoResponseModel(json, statusCode);

            if (versionInfoResultModel == null
                    || (versionInfoResultModel != null && versionInfoResultModel.getVersionInfo() == null)
                    || (versionInfoResultModel != null && versionInfoResultModel.getVersionInfo() != null
                    && versionInfoResultModel.getVersionInfo().getMinimumSupportedVersion() == null)){
                isVersionApiInProgress = false;
                setVisibilityOfProgress(false);
                Toast.makeText(activity, getString(R.string.loading_version_api_msg_fail), Toast.LENGTH_SHORT).show();
                showNextScreen(false);
                return;
            }

            if (versionInfoResultModel.getVersionInfo().getVersion().equals(AppConstants.ANDROID_APP_VERSION)){
                isVersionApiInProgress = false;
                setVisibilityOfProgress(false);
//				Toast.makeText(activity, getString(R.string.loading_version_api_msg_latest), Toast.LENGTH_SHORT).show();
                showNextScreen(false);
            } else {
				/*setVisibilityOfProgress(false);
				if (nextScreenHandler != null){
					nextScreenHandler.removeCallbacks(showNextScreenTask);
				}
				showDialog(versionInfoResultModel.getVersionInfo());*/
                if(!InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                    appPreferenceManager.setIsUpdateAvailable(true);
                    isVersionApiInProgress = false;
                    setVisibilityOfProgress(false);
                    showNextScreen(false);
                    appPreferenceManager.setVersionInfoModel(versionInfoResultModel.getVersionInfo());
                }
                else{
                    setVisibilityOfProgress(false);
                    if (nextScreenHandler != null){
                        nextScreenHandler.removeCallbacks(showNextScreenTask);
                    }
                    showDialog(versionInfoResultModel.getVersionInfo());
                }
            }
        }

        @Override
        public void onApiCancelled() {
            isVersionApiInProgress = false;
            showNextScreen(false);
        }

    }

    public void setVisibilityOfProgress(boolean isVisible) {
        if (isVisible){
            if (progress.getVisibility() == View.INVISIBLE || progress.getVisibility() == View.GONE){
                progress.setVisibility(View.VISIBLE);
            }
            if (txtLoading.getVisibility() == View.INVISIBLE || txtLoading.getVisibility() == View.GONE){
                txtLoading.setVisibility(View.VISIBLE);
            }
        } else {
            if (txtLoading.getVisibility() == View.VISIBLE){
                txtLoading.setVisibility(View.INVISIBLE);
            }
            if (progress.getVisibility() == View.VISIBLE){
                progress.setVisibility(View.INVISIBLE);
            }
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
    public void onConnected(Bundle connectionHint) {

        if (GeoLocationUtils.getLocationSetting(this)){
            /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);*/

            if (mLastLocation != null){
//			Toast.makeText(activity, "Latitude : " + String.valueOf(mLastLocation.getLatitude()) + "\nLongitude : " + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();

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
                            + (address == null ? "" : (address + ", "))
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
                    Toast.makeText(SplashScreenActivity.this, addressGenerated, Toast.LENGTH_LONG).show();
                }

                isOkToShowNextScreen = true;
                showNextScreen(false);
            } else {
                if (!isAfterLocationDialog){
                    isAfterLocationDialog = true;
                    GeoLocationUtils.showGPSDisabledAlertToUser(this, this);
                } else {
                    isOkToShowNextScreen = true;
                    showNextScreen(false);
                }
            }
        } else {
            if (!isAfterLocationDialog){
                isAfterLocationDialog = true;
                GeoLocationUtils.showGPSDisabledAlertToUser(this, this);
            } else {
                isOkToShowNextScreen = true;
                showNextScreen(false);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }

        if (isAfterGooglePlayServiceIntent){
            isGooglePlayServicesAvailable();
//			showNextScreen();
            isAfterGooglePlayServiceIntent = false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (nextScreenHandler != null){
            nextScreenHandler.removeCallbacks(showNextScreenTask);
        }
        if (mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status){
            return true;
        } else {
            getErrorDialog(status, this, 111).show();
            if (nextScreenHandler != null){
                nextScreenHandler.removeCallbacks(showNextScreenTask);
            }
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//		showNextScreen();

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
    public void onGeoLocDialogCancelPressed() {
        isAfterLocationDialog = false;
        isOkToShowNextScreen = true;
        showNextScreen(false);
    }

    @SuppressLint("NewApi")
    public void showDialog(final VersionInfo versionInfo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.upgrade));

        builder.setCancelable(false);

        String appVersion = AppConstants.ANDROID_APP_VERSION;
        String minVersionAllowed = versionInfo.getMinimumSupportedVersion().toString();


        String[]str,str2;
        str=appVersion.split("\\.");
        str2=minVersionAllowed.split("\\.");
        Log.d("versions","versions=="+str.length);
        Log.d("versions","versions=="+str2.length);
        Log.d("minversions","versions=="+minVersionAllowed);
        if (Integer.parseInt(str[0]) < Integer.parseInt(str2[0])){
            Log.d("versions","versions=="+str[0]+str2[0]);
            builder.setMessage(getString(R.string.loading_version_api_msg_unsupported)
                    + System.lineSeparator()
                    + getString(R.string.loading_version_api_msg_upgrade));

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    isVersionApiInProgress = false;
                    ApplicationController.clearActivityStack();
                }
            });

//			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//					dialog.cancel();
//					isVersionApiInProgress = false;
//					ApplicationController.clearActivityStack();
//				}
//			});

        }else if(Integer.parseInt(str[1]) < Integer.parseInt(str2[1])) {

            builder.setMessage(getString(R.string.loading_version_api_msg_unsupported)
                    + System.lineSeparator()
                    + getString(R.string.loading_version_api_msg_upgrade));

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    isVersionApiInProgress = false;
                    ApplicationController.clearActivityStack();
                }
            });

        }else if(str2.length!=2 && str2.length==3){
            if(Integer.parseInt(str[2]) < Integer.parseInt(str2[2])){
                Log.d("versions","versions=="+str[2]+str2[2]);
                builder.setMessage(getString(R.string.loading_version_api_msg_unsupported)
                        + System.lineSeparator()
                        + getString(R.string.loading_version_api_msg_upgrade));

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        isVersionApiInProgress = false;
                        ApplicationController.clearActivityStack();
                    }
                });
            }
            else{
                builder.setMessage(versionInfo.getReleaseNotes()
                        + System.lineSeparator()
                        + getString(R.string.loading_version_api_msg_upgrade));

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        isVersionApiInProgress = false;
                        showNextScreen(false);
                    }
                });
            }
        }
        else {

            builder.setMessage(versionInfo.getReleaseNotes()
                    + System.lineSeparator()
                    + getString(R.string.loading_version_api_msg_upgrade));

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    isVersionApiInProgress = false;
                    showNextScreen(false);
                }
            });

//			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//				        dialog.cancel();
//				        isVersionApiInProgress = false;
//				        showNextScreen(false);
//				}
//			});

        }

        builder.setPositiveButton(getString(R.string.upgrade), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isVersionApiInProgress = false;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(versionInfo.getAppLink()));
                startActivity(i);
                finish();
            }
        });

        builder.create().show();
    }

}
