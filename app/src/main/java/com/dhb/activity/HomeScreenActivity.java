package com.dhb.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dhb.R;
import com.dhb.application.ApplicationController;
import com.dhb.dao.DhbDao;
import com.dhb.dao.daomodels.PatientQueueCountDao;
import com.dhb.models.HealthCareFirm;
import com.dhb.models.PatientQueueCountModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.AlertDialogMessage;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.GeoLocationDialogDelegate;
import com.dhb.utils.Logger;
import com.dhb.utils.NetworkUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class HomeScreenActivity extends AbstractActivity/* implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GeoLocationDialogDelegate*/

{
/*    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private DrawerLayout dlDrawerLayoutprivate FrameLayout fmLayoutContainer;
    private ListView lvDrawerList;
    private TextView txtUserName;
    private TextView txtUserRoleName;
    private TextView txtUserAddress;
    private RoundedImageView imgUserPicture;
    private Toolbar tlToolBar;
    private ImageView imgMenu;
    private ImageButton ibtnMenu;
    private TextView txtTitle;
    private boolean isVersionApiInProgress = false;
    private TextView txtDoctorName;
    private TextView txtHospitalName;
    private ImageButton ibtnAddPatient;
    private ImageButton ibtnSearchPatient;
    private MenuItemsAdapter menuItemsAdapter;
    private Fragment patientQueueFragment;
    private AppPreferenceManager appPreferenceManager;
    private ApiCallAsyncTask logoutApiCallAsyncTask;
    Activity activity;

    SyncStatusReceiver syncStatusReceiver;

    public boolean isSearchClicked = false;
    public boolean isAddClicked = false;

    private ProgressDialog syncBarProgressDialog;

    private Fragment familyQueueFragment;

    private static final int REQUSET_CODE_ACTIVITY_RESULT = 9999;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private boolean isAfterGooglePlayServiceIntent = false;
    private boolean isAfterLocationDialog = false;
    private String latitude;
    private String longitude;

    private boolean isAfterOnCreate = false;

    PatientQueueCountResponseModel patientQueueCountResponseModel;

    private Dialog dialogLogoutReason;
    private Toolbar tlToolBarForFamilyQueue;
    private ImageButton ibtnSearchFamilyMember;
    private ImageButton ibtnBack;
    private ImageButton ibtnAddFamilyMember;
    private TextView txtFamilyQueueHeader;
    private TextView txtDone;
    public String familyId;
    public DhbDao dhbDao;
    private String selectedHospital = "";
    private LinearLayout lnHospList;
    private ResponseParser responseParser;
    private int eximinedPatientCount;
    private int registeredPatientCount;
    private TextView txtRegisteredCount1;
    private TextView txtRegisteredCount2;
    private TextView txtRegisteredCount3;
    private TextView txtExaminedCount7;
    private TextView txtExaminedCount6;
    private TextView txtExaminedCount5;
    private TextView txtExaminedCount4;
    private TextView txtExaminedCount3;
    private TextView txtExaminedCount2;
    private TextView txtExaminedCount1;
    private TextView txtRegisteredCount7;
    private TextView txtRegisteredCount6;
    private TextView txtRegisteredCount5;

    private TextView txtRegisteredCount4;
    private View footerViewCounter;
    private RelativeLayout rltLayoutDrawer;
    private TextView txtDigital;
    private TextView txtHealthbook;
    private boolean isSingOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        bringDrawerOnTop();
        initFragment();
        initUI();
        isSingOut = false;
        setFont();
        setData();
        if(appPreferenceManager.IsUpdateAvailable()){
            showDialog(appPreferenceManager.getVersionInfoModel());
        }

        isAfterOnCreate = true;

//		showScreenId("R-101");

        // pushFragments(patientQueueFragment, false, true, FragmentConstant.FRAGMENT_PATIENT_QUEUE);
    }

    private void initFragment() {
        patientQueueFragment = PatientQueueFragment.getInstance();
//        familyQueueFragment = FamilyQueueFragment.getInstance();
    }

    public void initUI() {
        super.initUI();
        activity = this;
        appPreferenceManager = new AppPreferenceManager(this);
        Logger.debug("api key: " + appPreferenceManager.getAPISessionKey());
        dhbDao = new DhbDao(activity);
        if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
            if (appPreferenceManager.isAfterLogin()){
                appPreferenceManager.setIsAfterLogin(false);
                showHospitalList();
            }
        } else {
            HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
            ArrayList<HealthCareFirm> healthCareFirms = healthCareFirmDao.getAllHealthCareFirmsFromUserId(appPreferenceManager.getUserId());

            if (healthCareFirms != null && healthCareFirms.size() > 0){
                appPreferenceManager.setHospitalName(healthCareFirms.get(0).getName().toString());
                appPreferenceManager.setHospitalId(healthCareFirms.get(0).getId().toString());
                appPreferenceManager.setHospitalPlace(healthCareFirms.get(0).getPlace());
            } else {
                activity.finish();
            }
        }
        fmLayoutContainer = (FrameLayout) findViewById(R.id.fm_layout_container);
        dlDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        lvDrawerList = (ListView) dlDrawerLayout.findViewById(R.id.lv_drawer_list);
//        footerViewCounter = dlDrawerLayout.findViewById(R.id.rlt_layout_drawer);
        tlToolBar = (Toolbar) findViewById(R.id.tl_toolbar);
        tlToolBarForFamilyQueue = (Toolbar) findViewById(R.id.tl_toolbar_family_queue);
        tlToolBarForFamilyQueue.setVisibility(View.GONE);

        //  imgMenu = (ImageView) tlToolBar.findViewById(R.id.img_toolbar_left_action_button);
        ibtnMenu = (ImageButton) tlToolBar.findViewById(R.id.ibtn_toolbar_left_action_button);
        txtTitle = (TextView) tlToolBar.findViewById(R.id.txt_title);
        txtTitle.setVisibility(View.GONE);
        txtDoctorName = (TextView) tlToolBar.findViewById(R.id.txt_doctor_name);
        txtHospitalName = (TextView) tlToolBar.findViewById(R.id.txt_hospital_name);
        ibtnAddPatient = (ImageButton) tlToolBar.findViewById(R.id.ibtn_add_patient);
        ibtnSearchPatient = (ImageButton) tlToolBar.findViewById(R.id.ibtn_search);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ibtnSearchFamilyMember = (ImageButton) tlToolBarForFamilyQueue.findViewById(R.id.ibtn_search);
        ibtnBack = (ImageButton) tlToolBarForFamilyQueue.findViewById(R.id.ibtn_back_arrow_icon);
        ibtnAddFamilyMember = (ImageButton) tlToolBarForFamilyQueue.findViewById(R.id.ibtn_add_member);
        txtFamilyQueueHeader = (TextView) tlToolBarForFamilyQueue.findViewById(R.id.txt_header);
        txtDone = (TextView) tlToolBarForFamilyQueue.findViewById(R.id.txt_done);
        txtDigital = (TextView) findViewById(R.id.txt_power_digital);
        txtHealthbook = (TextView) findViewById(R.id.txt_power_heath_book);
        setListener();
        setMenuHeader();
        setSupportActionBar(tlToolBar);

    }

    private void setMenuHeader() {
        lvDrawerList.invalidateViews();
        View headerView = getLayoutInflater().inflate(R.layout.item_drawer_menu_header, null);

        lvDrawerList.addHeaderView(headerView);

        txtUserName = (TextView) headerView.findViewById(R.id.txt_user_name);
        txtUserRoleName = (TextView) headerView.findViewById(R.id.txt_user_role_name);
        txtUserAddress = (TextView) headerView.findViewById(R.id.txt_user_address);
        imgUserPicture = (RoundedImageView) headerView.findViewById(R.id.img_user_picture);

        View headerViewHosp = getLayoutInflater().inflate(R.layout.item_hosp_list_menu_header, null);
        lnHospList = (LinearLayout) headerViewHosp.findViewById(R.id.ln_hosp_list);
        if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
            setHospitalList(null);
            setFooterCounter(eximinedPatientCount, registeredPatientCount);
        }
        lvDrawerList.addHeaderView(headerViewHosp);

//        if (healthCareFirmsList == null) {
        setMenuAdapter(true);
//        }else{
//            setMenuAdapter(false);
//        }
    }

    public void setFooterCounter(int eximinedPatientCount, int registeredPatientCount) {
//        lvDrawerList.removeFooterView(footerViewCounter);
//        footerViewCounter = getLayoutInflater().inflate(R.layout.item_drawer_counter, null);
        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        rltLayoutDrawer = (RelativeLayout) dlDrawerLayout.findViewById(R.id.rlt_layout_drawer);
        if (!hasMenuKey && !hasBackKey){
            // Do whatever you need to do, this device has a navigation bar
            rltLayoutDrawer.setPadding(0, 0, 0, 105);
        } else {
            rltLayoutDrawer.setPadding(0, 0, 0, 0);
        }

        txtRegisteredCount1 = (TextView) dlDrawerLayout.findViewById(R.id.txt_registered_count1);
        txtRegisteredCount2 = (TextView) dlDrawerLayout.findViewById(R.id.txt_registered_count2);
        txtRegisteredCount3 = (TextView) dlDrawerLayout.findViewById(R.id.txt_registered_count3);
        txtRegisteredCount4 = (TextView) dlDrawerLayout.findViewById(R.id.txt_registered_count4);
        txtRegisteredCount5 = (TextView) dlDrawerLayout.findViewById(R.id.txt_registered_count5);
        txtRegisteredCount6 = (TextView) dlDrawerLayout.findViewById(R.id.txt_registered_count6);
        txtRegisteredCount7 = (TextView) dlDrawerLayout.findViewById(R.id.txt_registered_count7);

        txtExaminedCount1 = (TextView) dlDrawerLayout.findViewById(R.id.txt_examined_count1);
        txtExaminedCount2 = (TextView) dlDrawerLayout.findViewById(R.id.txt_examined_count2);
        txtExaminedCount3 = (TextView) dlDrawerLayout.findViewById(R.id.txt_examined_count3);
        txtExaminedCount4 = (TextView) dlDrawerLayout.findViewById(R.id.txt_examined_count4);
        txtExaminedCount5 = (TextView) dlDrawerLayout.findViewById(R.id.txt_examined_count5);
        txtExaminedCount6 = (TextView) dlDrawerLayout.findViewById(R.id.txt_examined_count6);
        txtExaminedCount7 = (TextView) dlDrawerLayout.findViewById(R.id.txt_examined_count7);

        String countRegistered = registeredPatientCount + "";
        char[] registeredCountArray = countRegistered.toCharArray();


        if (registeredCountArray.length == 7){
            txtRegisteredCount1.setText(registeredCountArray[0] + "");
            txtRegisteredCount2.setText(registeredCountArray[1] + "");
            txtRegisteredCount3.setText(registeredCountArray[2] + "");
            txtRegisteredCount4.setText(registeredCountArray[3] + "");
            txtRegisteredCount5.setText(registeredCountArray[4] + "");
            txtRegisteredCount6.setText(registeredCountArray[5] + "");
            txtRegisteredCount7.setText(registeredCountArray[6] + "");
        } else if (registeredCountArray.length == 6){
            txtRegisteredCount1.setText("0");
            txtRegisteredCount2.setText(registeredCountArray[0] + "");
            txtRegisteredCount3.setText(registeredCountArray[1] + "");
            txtRegisteredCount4.setText(registeredCountArray[2] + "");
            txtRegisteredCount5.setText(registeredCountArray[3] + "");
            txtRegisteredCount6.setText(registeredCountArray[4] + "");
            txtRegisteredCount7.setText(registeredCountArray[5] + "");
        } else if (registeredCountArray.length == 5){
            txtRegisteredCount1.setText("0");
            txtRegisteredCount2.setText("0");
            txtRegisteredCount3.setText(registeredCountArray[0] + "");
            txtRegisteredCount4.setText(registeredCountArray[1] + "");
            txtRegisteredCount5.setText(registeredCountArray[2] + "");
            txtRegisteredCount6.setText(registeredCountArray[3] + "");
            txtRegisteredCount7.setText(registeredCountArray[4] + "");
        } else if (registeredCountArray.length == 4){
            txtRegisteredCount1.setText("0");
            txtRegisteredCount2.setText("0");
            txtRegisteredCount3.setText("0");
            txtRegisteredCount4.setText(registeredCountArray[0] + "");
            txtRegisteredCount5.setText(registeredCountArray[1] + "");
            txtRegisteredCount6.setText(registeredCountArray[2] + "");
            txtRegisteredCount7.setText(registeredCountArray[3] + "");
        } else if (registeredCountArray.length == 3){
            txtRegisteredCount1.setText("0");
            txtRegisteredCount2.setText("0");
            txtRegisteredCount3.setText("0");
            txtRegisteredCount4.setText("0");
            txtRegisteredCount5.setText(registeredCountArray[0] + "");
            txtRegisteredCount6.setText(registeredCountArray[1] + "");
            txtRegisteredCount7.setText(registeredCountArray[2] + "");
        } else if (registeredCountArray.length == 2){
            txtRegisteredCount1.setText("0");
            txtRegisteredCount2.setText("0");
            txtRegisteredCount3.setText("0");
            txtRegisteredCount4.setText("0");
            txtRegisteredCount5.setText("0");
            txtRegisteredCount6.setText(registeredCountArray[0] + "");
            txtRegisteredCount7.setText(registeredCountArray[1] + "");
        } else if (registeredCountArray.length == 1 && eximinedPatientCount != 0){
            txtRegisteredCount1.setText("0");
            txtRegisteredCount2.setText("0");
            txtRegisteredCount3.setText("0");
            txtRegisteredCount4.setText("0");
            txtRegisteredCount5.setText("0");
            txtRegisteredCount6.setText("0");
            txtRegisteredCount7.setText(registeredCountArray[0] + "");
        } else {
            txtRegisteredCount1.setText("0");
            txtRegisteredCount2.setText("0");
            txtRegisteredCount3.setText("0");
            txtRegisteredCount4.setText("0");
            txtRegisteredCount5.setText("0");
            txtRegisteredCount6.setText("0");
            txtRegisteredCount7.setText("0");
        }


        String countEximined = eximinedPatientCount + "";
        char[] examinedCountArray = countEximined.toCharArray();
        if (examinedCountArray.length == 7){
            txtExaminedCount1.setText(examinedCountArray[0] + "");
            txtExaminedCount2.setText(examinedCountArray[1] + "");
            txtExaminedCount3.setText(examinedCountArray[2] + "");
            txtExaminedCount4.setText(examinedCountArray[3] + "");
            txtExaminedCount5.setText(examinedCountArray[4] + "");
            txtExaminedCount6.setText(examinedCountArray[5] + "");
            txtExaminedCount7.setText(examinedCountArray[6] + "");
        } else if (examinedCountArray.length == 6){
            txtExaminedCount1.setText("0");
            txtExaminedCount2.setText(examinedCountArray[0] + "");
            txtExaminedCount3.setText(examinedCountArray[1] + "");
            txtExaminedCount4.setText(examinedCountArray[2] + "");
            txtExaminedCount5.setText(examinedCountArray[3] + "");
            txtExaminedCount6.setText(examinedCountArray[4] + "");
            txtExaminedCount7.setText(examinedCountArray[5] + "");
        } else if (examinedCountArray.length == 5){
            txtExaminedCount1.setText("0");
            txtExaminedCount2.setText("0");
            txtExaminedCount3.setText(examinedCountArray[0] + "");
            txtExaminedCount4.setText(examinedCountArray[1] + "");
            txtExaminedCount5.setText(examinedCountArray[2] + "");
            txtExaminedCount6.setText(examinedCountArray[3] + "");
            txtExaminedCount7.setText(examinedCountArray[4] + "");
        } else if (examinedCountArray.length == 4){
            txtExaminedCount1.setText("0");
            txtExaminedCount2.setText("0");
            txtExaminedCount3.setText("0");
            txtExaminedCount4.setText(examinedCountArray[0] + "");
            txtExaminedCount5.setText(examinedCountArray[1] + "");
            txtExaminedCount6.setText(examinedCountArray[2] + "");
            txtExaminedCount7.setText(examinedCountArray[3] + "");
        } else if (examinedCountArray.length == 3){
            txtExaminedCount1.setText("0");
            txtExaminedCount2.setText("0");
            txtExaminedCount3.setText("0");
            txtExaminedCount4.setText("0");
            txtExaminedCount5.setText(examinedCountArray[0] + "");
            txtExaminedCount6.setText(examinedCountArray[1] + "");
            txtExaminedCount7.setText(examinedCountArray[2] + "");
        } else if (examinedCountArray.length == 2){
            txtExaminedCount1.setText("0");
            txtExaminedCount2.setText("0");
            txtExaminedCount3.setText("0");
            txtExaminedCount4.setText("0");
            txtExaminedCount5.setText("0");
            txtExaminedCount6.setText(examinedCountArray[0] + "");
            txtExaminedCount7.setText(examinedCountArray[1] + "");
        } else if (examinedCountArray.length == 1 && eximinedPatientCount != 0){
            txtExaminedCount1.setText("0");
            txtExaminedCount2.setText("0");
            txtExaminedCount3.setText("0");
            txtExaminedCount4.setText("0");
            txtExaminedCount5.setText("0");
            txtExaminedCount6.setText("0");
            txtExaminedCount7.setText(examinedCountArray[0] + "");
        } else {
            txtExaminedCount1.setText("0");
            txtExaminedCount2.setText("0");
            txtExaminedCount3.setText("0");
            txtExaminedCount4.setText("0");
            txtExaminedCount5.setText("0");
            txtExaminedCount6.setText("0");
            txtExaminedCount7.setText("0");
        }
        txtExaminedCount1.setTypeface(fontOpenRobotoRegular);
        txtExaminedCount2.setTypeface(fontOpenRobotoRegular);
        txtExaminedCount3.setTypeface(fontOpenRobotoRegular);
        txtExaminedCount4.setTypeface(fontOpenRobotoRegular);
        txtExaminedCount5.setTypeface(fontOpenRobotoRegular);
        txtExaminedCount6.setTypeface(fontOpenRobotoRegular);
        txtExaminedCount7.setTypeface(fontOpenRobotoRegular);
        txtRegisteredCount1.setTypeface(fontOpenRobotoRegular);
        txtRegisteredCount2.setTypeface(fontOpenRobotoRegular);
        txtRegisteredCount3.setTypeface(fontOpenRobotoRegular);
        txtRegisteredCount4.setTypeface(fontOpenRobotoRegular);
        txtRegisteredCount5.setTypeface(fontOpenRobotoRegular);
        txtRegisteredCount6.setTypeface(fontOpenRobotoRegular);
        txtRegisteredCount7.setTypeface(fontOpenRobotoRegular);
//        lvDrawerList.addFooterView(footerViewCounter);
//        dlDrawerLayout.addView(footerViewCounter);
        if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
            rltLayoutDrawer.setVisibility(View.VISIBLE);
        } else {
            rltLayoutDrawer.setVisibility(View.GONE);
        }
    }

    public void setHospitalList(ArrayList<HealthCareFirm> healthCareFirmsList) {
        lnHospList.removeAllViews();
        if (healthCareFirmsList == null){
            HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
            ArrayList<HealthCareFirm> healthCareFirms = healthCareFirmDao.getAllHealthCareFirmsFromUserId(appPreferenceManager.getUserId());
            healthCareFirmsList = healthCareFirms;
        }

        for (HealthCareFirm healthCareFirm : healthCareFirmsList){

            PatientQueueCountDao patientQueueCountDao = new PatientQueueCountDao(dhbDao.getDb());
            PatientQueueCountModel patientQueueCountModel = patientQueueCountDao.getpatientQueueCountModelWithId(healthCareFirm.getId());

            View hospItem = getLayoutInflater().inflate(R.layout.item_drawer_hospital_list, null);
            RelativeLayout layHospName = (RelativeLayout) hospItem.findViewById(R.id.rlt_layout_drawer_hosp);
            TextView txtHospName = (TextView) hospItem.findViewById(R.id.txt_hosp_list_name);
            TextView txtHospPatientCount = (TextView) hospItem.findViewById(R.id.txt_hosp_list_count);
            hospItem.setOnClickListener(new DrawerHospNameOnClickListener(healthCareFirm.getName(), healthCareFirm.getId()));
            txtHospName.setText(healthCareFirm.getName());
//			if (healthCareFirm.getPatientCount() != null){
//				txtHospPatientCount.setText(healthCareFirm.getPatientCount());
//			}
            if (patientQueueCountModel != null && patientQueueCountModel.getPatientCountQueue() != null){
                txtHospPatientCount.setText(patientQueueCountModel.getPatientCountQueue());
            } else {
                txtHospPatientCount.setText("0");
            }
            if (healthCareFirm.getName().trim().equals(appPreferenceManager.getHospitalName())){
                layHospName.setBackgroundColor(0xFFE0E0E0);
            }
            txtHospName.setTypeface(fontOpenRobotoRegular);
            txtHospPatientCount.setTypeface(fontOpenRobotoRegular);
            lnHospList.addView(hospItem);

        }
        if (menuItemsAdapter != null){
            menuItemsAdapter.notifyDataSetChanged();
        }
    }

    private void setData() {
        if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
            txtDoctorName.setText(appPreferenceManager.getDoctorName());
            txtUserName.setText(appPreferenceManager.getDoctorName());
        } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
            txtDoctorName.setText("Dr. " + appPreferenceManager.getDoctorName());
            txtUserName.setText("Dr. " + appPreferenceManager.getDoctorName());
        }

        txtHospitalName.setText(appPreferenceManager.getHospitalName() + ", " + appPreferenceManager.getHospitalPlace());
//        txtUserName.setText(appPreferenceManager.getDoctorName());
        txtUserRoleName.setText(appPreferenceManager.getRoleName());

        String imageUrl = appPreferenceManager.getImageUrl();
        if (imageUrl != null){
            ApplicationController.imageLoader.displayImage(imageUrl, imgUserPicture);
        }


    }

    private void setListener() {
        ibtnMenu.setOnClickListener(new MenuBtnClickListener());
        ibtnAddPatient.setOnClickListener(new AddNewPatientOnClickListener());
        ibtnSearchPatient.setOnClickListener(new SearchButtonOnclickListener());
        lvDrawerList.setOnItemClickListener(new ListOnItemClickListener());

        ibtnSearchFamilyMember.setOnClickListener(new SearchButtonOnclickListener());
        ibtnAddFamilyMember.setOnClickListener(new AddNewPatientOnClickListener());
        ibtnBack.setOnClickListener(new BackButtonClickListener());
        txtDone.setOnClickListener(new DoneBtnClickListener());

        dlDrawerLayout.setDrawerListener(new DrawerActionListener());
    }

    private void setFont() {
        txtUserName.setTypeface(fontOpenRobotoRegular);
        txtUserRoleName.setTypeface(fontOpenRobotoRegular);
        txtUserAddress.setTypeface(fontOpenRobotoRegular);
        txtTitle.setTypeface(fontOpenRobotoRegular);
        txtDoctorName.setTypeface(fontOpenRobotoRegular);
        txtHospitalName.setTypeface(fontOpenRobotoRegular);
        txtHealthbook.setTypeface(fontArialBold);
        txtDigital.setTypeface(fontArialBold);

    }

    private void setMenuAdapter(boolean isFirstTime) {
        ArrayList<Integer> menuItemIcon = new ArrayList<>();
        ArrayList<String> menuNames = new ArrayList<>();

        if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
//            menuItemIcon.add(R.drawable.patient_queue);
//			menuItemIcon.add(R.drawable.doctor_referal_icon);
            menuItemIcon.add(R.drawable.doctors_present_queue);
            menuItemIcon.add(R.drawable.change_password);
            menuItemIcon.add(R.drawable.sync_data);
            menuItemIcon.add(R.drawable.suggestion);
//			menuItemIcon.add(R.drawable.doctor_timing);
//			menuItemIcon.add(R.drawable.settings_icon);
//			menuItemIcon.add(R.drawable.help_icon);
            menuItemIcon.add(R.drawable.settings_icon);
            menuItemIcon.add(R.drawable.logout);

//            menuNames.add(getResources().getString(R.string.patient_queue));
//			menuNames.add(getResources().getString(R.string.referred_to_me));
            menuNames.add(getResources().getString(R.string.on_hold_patient_queue));
            menuNames.add(getResources().getString(R.string.change_password));
            menuNames.add(getResources().getString(R.string.sync_data));
            menuNames.add(getResources().getString(R.string.suggestion));
//			menuNames.add(getResources().getString(R.string.doctor_timing));
//			menuNames.add(getResources().getString(R.string.settings));
//			menuNames.add(getResources().getString(R.string.help));
            menuNames.add(getResources().getString(R.string.settings));
            menuNames.add(getResources().getString(R.string.logout));

        } else {

//            menuItemIcon.add(R.drawable.patient_queue);
            menuItemIcon.add(R.drawable.doctors_present_queue);
            menuItemIcon.add(R.drawable.change_password);
            menuItemIcon.add(R.drawable.sync_data);
            menuItemIcon.add(R.drawable.suggestion);
//			menuItemIcon.add(R.drawable.doctor_timing);
//			menuItemIcon.add(R.drawable.settings_icon);
//			menuItemIcon.add(R.drawable.help_icon);
            menuItemIcon.add(R.drawable.logout);

//            menuNames.add(getResources().getString(R.string.patient_queue));
            menuNames.add(getResources().getString(R.string.doctor_present_queue));
            menuNames.add(getResources().getString(R.string.change_password));
            menuNames.add(getResources().getString(R.string.sync_data));
            menuNames.add(getResources().getString(R.string.suggestion));
//			menuNames.add(getResources().getString(R.string.doctor_timing));
//			menuNames.add(getResources().getString(R.string.settings));
//			menuNames.add(getResources().getString(R.string.help));
            menuNames.add(getResources().getString(R.string.logout));
        }

        menuItemsAdapter = new MenuItemsAdapter(HomeScreenActivity.this, menuNames, menuItemIcon);
        lvDrawerList.setAdapter(menuItemsAdapter);

        if (isFirstTime){
            lvDrawerList.performItemClick(lvDrawerList, 1, 0);
        }
//        pushFragments(patientQueueFragment, false, true, FragmentConstant.FRAGMENT_PATIENT_QUEUE);
    }

    private class MenuBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // hideKeyboard(v);
            dlDrawerLayout.openDrawer(Gravity.LEFT);
        }

    }

    private void bringDrawerOnTop() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.layout_drawer, null);
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.ln_drawer_content);
        container.addView(child, 0);
        decor.addView(drawer);

    }

    private class ListOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            dlDrawerLayout.closeDrawer(Gravity.LEFT);

            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                switch (i)

                {
                    case 1 :
                        pushFragments(patientQueueFragment, false, true, FragmentConstant.FRAGMENT_PATIENT_QUEUE);
                        //pushFragments(familyQueueFragment, false, true, FragmentConstant.FRAGMENT_FAMILY_QUEUE);
                        if (patientQueueFragment != null){
                            if (((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue != null){
                                ((PatientQueueFragment) patientQueueFragment).isOkToFireApiForRefreshQueue = false;
                                ((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue.performClick();
                            }
                        }
                        break;
                    case 2:
                        Intent intent = new Intent(activity, HoldVisitQueueActivity.class );
                        startActivity(intent);
                        break;
                    case 3:
                        callChangesPasswordActivity();
                        break;
                    case 4:
                        if (NetworkUtils.isNetworkAvailable(activity)){
                            if (isOfflineSyncServiceIsInProgress()){
                                Toast.makeText(activity, getString(R.string.sync_image_in_progress), Toast.LENGTH_LONG).show();
                            } else {
                                callOfflineSyncService();
                            }
                        } else {
                            Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 5:
                        callSuggestionActivity();
                        break;
//				case 5:
//					if (NetworkUtils.isNetworkAvailable(activity)){
//						Intent intentDoctorTiming = new Intent(activity, DoctorsTimingActivity.class );
//						startActivityForResult(intentDoctorTiming, REQUSET_CODE_ACTIVITY_RESULT, null);
//					} else {
//						Toast.makeText(activity, getString(R.string.network_error), Toast.LENGTH_LONG).show();
//					}
//					break;
//				case 7:
//					callSettingsActivity();
//					break;
//				case 6:
//					callHelpActivity();
//					break;
                    case 6:
                        goToActivity(activity, SettingsActivity.class, null);
                        break;

                    case 7:
                        if (NetworkUtils.isNetworkAvailable(activity)){
//						if (isOfflineSyncServiceIsInProgress()){
//							Toast.makeText(activity, getString(R.string.sync_in_progress_wait), Toast.LENGTH_LONG).show();
//						} else {
                            AlertDialogMessage alertDialogMessage = new AlertDialogMessage();
                            alertDialogMessage.showAlertTwoButtonAlert(activity, getResources().getString(R.string.alert_you_want_to_logOut), getResources().getString(R.string.yes), getResources().getString(R.string.no), false);
                            alertDialogMessage.setAlertDialogOkListener(new AlertDialogPositiveListener());
//						}
                        } else {
                            Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();
                        }
                        break;

                }

            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                switch (i)

                {
                    case 1:
                        pushFragments(patientQueueFragment, false, true, FragmentConstant.FRAGMENT_PATIENT_QUEUE);
                        // pushFragments(familyQueueFragment, false, true, FragmentConstant.FRAGMENT_FAMILY_QUEUE);
                        if (patientQueueFragment != null){
                            if (((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue != null){
                                ((PatientQueueFragment) patientQueueFragment).isOkToFireApiForRefreshQueue = false;
                                ((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue.performClick();
                            }
                        }
                        break;
                    case 2:
                        Intent intentDoctor = new Intent(activity, DoctorPresentQueueActivity.class );
                        startActivity(intentDoctor);
                        break;
                    case 3:
                        callChangesPasswordActivity();
                        break;
                    case 4:
                        if (NetworkUtils.isNetworkAvailable(activity)){
                            if (isOfflineSyncServiceIsInProgress()){
                                Toast.makeText(activity, getString(R.string.sync_image_in_progress), Toast.LENGTH_LONG).show();
                            } else {
                                callOfflineSyncService();
                            }
                        } else {
                            Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 5:
                        callSuggestionActivity();
                        break;
//				case 6:
//					if (NetworkUtils.isNetworkAvailable(activity)){
//						Intent intentDoctorTiming = new Intent(activity, DoctorsTimingActivity.class );
//						startActivityForResult(intentDoctorTiming, REQUSET_CODE_ACTIVITY_RESULT, null);
//					} else {
//						Toast.makeText(activity, getString(R.string.network_error), Toast.LENGTH_LONG).show();
//					}
//					break;
//				case 7:
//					callSettingsActivity();
//					break;
//				case 7:
//					callHelpActivity();
//					break;
                    case 6:
                        if (NetworkUtils.isNetworkAvailable(activity)){
//                            if (isOfflineSyncServiceIsInProgress()) {
//                                Toast.makeText(activity, getString(R.string.sync_in_progress_wait), Toast.LENGTH_LONG).show();
//                            } else {
                            AlertDialogMessage alertDialogMessage = new AlertDialogMessage();
                            alertDialogMessage.showAlertTwoButtonAlert(activity, getResources().getString(R.string.alert_you_want_to_logOut), getResources().getString(R.string.yes), getResources().getString(R.string.no), false);
                            alertDialogMessage.setAlertDialogOkListener(new AlertDialogPositiveListener());
//                            }
                        } else {
                            Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();
                        }
                        break;

                }
            }

        }

    }

    public boolean isOfflineSyncServiceIsInProgress() {
        if (isServiceRunning(OfflineSyncService.class )){
            return true;
        } else {
            return false;
        }

    }

    public boolean isMasterTableSyncServiceIsInProgress() {
        if (isServiceRunning(MasterTableSyncService.class )){
            return true;
        } else {
            return false;
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

    public void stopOfflineSyncService() {
        stopService(new Intent(HomeScreenActivity.this, OfflineSyncService.class ));
    }

    public void stopMasterTableSyncService() {
        stopService(new Intent(HomeScreenActivity.this, MasterTableSyncService.class ));
    }

    private void callSettingsActivity() {

        return;
    }

    private void callHelpActivity() {
        Intent intentHelp = new Intent(activity, HelpScreenActivity.class );
        startActivity(intentHelp);
        return;
    }

    private void callOfflineSyncService() {
        Intent intent = new Intent(HomeScreenActivity.this, OfflineSyncService.class );
        startService(intent);
        return;
    }

    private void callMasterTableSyncService() {
        Intent intent = new Intent(HomeScreenActivity.this, MasterTableSyncService.class );
        startService(intent);
        return;
    }

    private void callSuggestionActivity() {
        if (!NetworkUtils.isNetworkAvailable(activity)){
            Toast.makeText(activity, "This feature is only available in online mode.", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intentSuggestion = new Intent(activity, SuggestionActivity.class );
        startActivity(intentSuggestion);
        return;
    }

    private void callChangesPasswordActivity() {
        if (!NetworkUtils.isNetworkAvailable(activity)){
            Toast.makeText(activity, "This feature is only available in online mode.", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(activity, ChangePasswordActivity.class );
        startActivity(intent);

        return;
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
            syncBarProgressDialog = new ProgressDialog(HomeScreenActivity.this);
            syncBarProgressDialog.setTitle("Please wait");
            syncBarProgressDialog.setMessage("Sync in Progress");
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setProgress(0);
//			syncBarProgressDialog.setMax(100);
            syncBarProgressDialog.setCancelable(false);
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
        if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)
                || appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
//			if (requestCode == REQUSET_CODE_ACTIVITY_RESULT && resultCode == RESULT_OK){
            if (requestCode == REQUSET_CODE_ACTIVITY_RESULT){
                if (patientQueueFragment != null){
                    if (((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue != null){
                        ((PatientQueueFragment) patientQueueFragment).isOkToFireApiForRefreshQueue = false;
                        ((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue.performClick();
                    }
                }
            }
        }

    }

    private class AlertDialogPositiveListener implements AlertDialogMessage.AlertDialogOkListener {

        @Override
        public void onAlertDialogOkButtonListener() {

            if (!isOfflineSyncServiceIsInProgress()){
                isSingOut = true;
                callOfflineSyncService();
            } else {
                isSingOut = false;
                Toast.makeText(activity, getString(R.string.sync_image_in_progress_sign_out), Toast.LENGTH_LONG).show();
            }
//			if(isOfflineSyncServiceIsInProgress()){
//				stopOfflineSyncService();
//			}
//			if(isMasterTableSyncServiceIsInProgress()){
//				stopMasterTableSyncService();
//			}
//            callLogout(null, false);

        }

    }

    public void callLogout(String reason, boolean showDialog) {
        //delete hospital mapping
        UserHealthCareFirmMapDao userHealthCareFirmMapDao = new UserHealthCareFirmMapDao(dhbDao.getDb());
        userHealthCareFirmMapDao.deleteUserHealthcareFirmMap(appPreferenceManager.getUserId());

        //Logout API call
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        }
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        logoutApiCallAsyncTask = asyncTaskForRequest.getLogOutAsyncTask(latitude, longitude, reason,
                appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR));
        LogOutApiCallResult logOutApiCallResult = new LogOutApiCallResult();
        logOutApiCallResult.setShowErrorDialog(showDialog);
        logoutApiCallAsyncTask.setApiCallAsyncTaskDelegate(logOutApiCallResult);
        logoutApiCallAsyncTask.execute(logoutApiCallAsyncTask);
    }

    private class SearchButtonOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (!isSearchClicked){

                isSearchClicked = true;

                Intent intent = new Intent(activity, SearchPatientActivity.class );
                Bundle bundle = new Bundle();
                if (familyQueueFragment != null && familyQueueFragment.isVisible()){
                    bundle.putBoolean(BundleConstants.TO_BE_ADDED_IN_FAMILY, true);
                    bundle.putBoolean(BundleConstants.IS_FAMILY, true);
                    bundle.putString(BundleConstants.FAMILY_ID, familyId);
                    bundle.putBoolean(BundleConstants.IS_FROM_FAMILY_FRAGMENT, true);
                } else {
                    if (((PatientQueueFragment) patientQueueFragment).isFirstTabQueue){
                        bundle.putBoolean(BundleConstants.IS_FROM_PRESENT, true);
                    } else {
                        bundle.putBoolean(BundleConstants.IS_FROM_PRESENT, false);
                    }
                }
                intent.putExtras(bundle);
//				startActivity(intent);
                startActivityForResult(intent, REQUSET_CODE_ACTIVITY_RESULT, null);

            }

        }

    }


    private class LogOutApiCallResult implements ApiCallAsyncTaskDelegate {

        private boolean showDialog = true;

        public void setShowErrorDialog(boolean showDialog) {
            this.showDialog = showDialog;
        }

        @Override
        public void apiCallResult(String json, int statusCode) {

            ResponseParser responseParser = new ResponseParser(activity);
            responseParser.setToShowErrorDailog(showDialog);
            SuccessResponseModel successResponseModel = responseParser.getSuccessResponseModel(json, statusCode);

            if (successResponseModel != null && successResponseModel.getStatus() == 200){

                appPreferenceManager.clearAllPreferences();

                //appPreferenceManager.setIsAfterLogin(false);
                if (dhbDao == null){
                    dhbDao = new DhbDao(activity);
                }
                dhbDao.deleteDb();

//				Intent i = new Intent(activity, LoginActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(i);

                if (dialogLogoutReason != null && dialogLogoutReason.isShowing()){
                    dialogLogoutReason.dismiss();
                }

                ApplicationController.clearActivityStack();
            } else if (successResponseModel != null && successResponseModel.getStatus() == 400
                    && successResponseModel.getMessage().equals("Provide the reason for logout.")){

                if (dialogLogoutReason != null && dialogLogoutReason.isShowing()){
                    dialogLogoutReason.dismiss();
                }

                dialogLogoutReason = new LogoutReasonDialog(activity, new LogoutReasonSelectionDelegate());
                dialogLogoutReason.show();

            } else if (successResponseModel != null && successResponseModel.getStatus() == 401
                    && successResponseModel.getMessage().equals("Session Expired, Please Sign in again!")){

                appPreferenceManager.clearAllPreferences();
                if (dhbDao == null){
                    dhbDao = new DhbDao(activity);
                }
                dhbDao.deleteDb();

                //appPreferenceManager.setIsAfterLogin(false);

//				Intent i = new Intent(activity, LoginActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(i);

                if (dialogLogoutReason != null && dialogLogoutReason.isShowing()){
                    dialogLogoutReason.dismiss();
                }

                ApplicationController.clearActivityStack();

            } else {
                if (dialogLogoutReason != null && dialogLogoutReason.isShowing()){
                    dialogLogoutReason.dismiss();
                }
            }
        }

        @Override
        public void onApiCancelled() {

        }

    }

    private class LogoutReasonSelectionDelegate implements SelectorDialogResult {

        @Override
        public void setSelector(String logoutReason, int reasonId) {

            if (isOfflineSyncServiceIsInProgress()){
                stopOfflineSyncService();
            }
            if (isMasterTableSyncServiceIsInProgress()){
                stopMasterTableSyncService();
            }
            callLogout(logoutReason, true);
        }

        @Override
        public void setSelector(String Selector, String id) {

        }

    }

    public void pushFragments(Fragment fragment, boolean shouldAnimate,
                              boolean shouldAdd, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate){
            // ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
            // R.animator.fragment_slide_left_exit,
            // R.animator.fragment_slide_right_enter,
            // R.animator.fragment_slide_right_exit);
        }

        ft.replace(R.id.fm_layout_container, fragment, TAG_FRAGMENT);

        //ft.add(R.id.fr_layout_container, fragment, TAG_FRAGMENT);

        if (shouldAdd){

            ft.addToBackStack(tag);
        } else {

            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        ft.commit();
    }

    private class AddNewPatientOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (!isAddClicked){


                isAddClicked = true;

                Intent intent = new Intent(activity, PatientRegistrationActivity.class );
                if (familyQueueFragment != null && familyQueueFragment.isVisible()){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(BundleConstants.TO_BE_ADDED_IN_FAMILY, true);
                    bundle.putBoolean(BundleConstants.IS_FAMILY, true);
                    bundle.putString(BundleConstants.FAMILY_ID, familyId);
                    bundle.putBoolean(BundleConstants.IS_FROM_PRESENT, true);
                    intent.putExtras(bundle);
                }
//				activity.startActivity(intent);
                startActivityForResult(intent, REQUSET_CODE_ACTIVITY_RESULT, null);
            }
        }

    }

    public class SyncStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getAction() != null){

                if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_IN_PROGRESS)){

                    int totalUploadCount = 0, uploadedCount = 0;

                    if (intent != null && intent.getExtras() != null){
                        if (intent.getExtras().containsKey(AppConstants.OFFLINE_TOTAL_COUNT)){
                            totalUploadCount = intent.getExtras().getInt(AppConstants.OFFLINE_TOTAL_COUNT);
                        }
                        if (intent.getExtras().containsKey(AppConstants.OFFLINE_COMPLETED_COUNT)){
                            uploadedCount = intent.getExtras().getInt(AppConstants.OFFLINE_COMPLETED_COUNT);
                        }

                    }

                    initSyncProgressBarDialog();
                    syncBarProgressDialog.setMax(totalUploadCount);

                    if (uploadedCount <= totalUploadCount){
                        syncBarProgressDialog.setProgress(uploadedCount);
                        showSyncProgressBarDialog();
                    } else {

                        syncBarProgressDialog.setProgress(totalUploadCount);
                        showSyncProgressBarDialog();

//						if(ApplicationController.isOfflineSyncServiceIsInProgress(HomeScreenActivity.this)){
//							stopOfflineSyncService(HomeScreenActivity.this);
//						}
                    }

                } else if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_DONE)){

                    hideSyncProgressBarDialog();
                    syncBarProgressDialog = null;

                    if (intent != null && intent.getExtras() != null){
                        boolean isIssueFoundInSync = false;
                        if (intent.getExtras().containsKey(AppConstants.OFFLINE_ISSUE_FOUND)){
                            isIssueFoundInSync = intent.getExtras().getBoolean(AppConstants.OFFLINE_ISSUE_FOUND);
                        }

                        boolean isUploadImagesComplete = false;
                        if (intent.getExtras().containsKey(AppConstants.OFFLINE_UPLOAD_IMAGE_COMPLETE)){
                            isUploadImagesComplete = intent.getExtras().getBoolean(AppConstants.OFFLINE_UPLOAD_IMAGE_COMPLETE, false);
                        }

                        boolean showImageUploadToast = false;
                        if (intent.getExtras().containsKey(AppConstants.OFFLINE_UPLOAD_IMAGE_SHOW_TOAST)){
                            showImageUploadToast = intent.getExtras().getBoolean(AppConstants.OFFLINE_UPLOAD_IMAGE_SHOW_TOAST, false);
                        }

                        if (isUploadImagesComplete){

                            if (isOfflineSyncServiceIsInProgress()){
                                stopOfflineSyncService();
                            }

                            if (isIssueFoundInSync){
                                Toast.makeText(HomeScreenActivity.this, getResources().getString(R.string.sync_error), Toast.LENGTH_SHORT).show();
                            } else {
                                if (isSingOut){
                                    AlertDialogMessage alertDialogMessage = new AlertDialogMessage();
                                    alertDialogMessage.showAlertTwoButtonAlert(activity, getResources().getString(R.string.alert_you_want_to_signOut), getResources().getString(R.string.yes), getResources().getString(R.string.no), false);
                                    alertDialogMessage.setAlertDialogOkListener(new AlertDialogSignOutListener());

                                }
                                isSingOut = false;
                                Toast.makeText(HomeScreenActivity.this, getResources().getString(R.string.sync_done), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (showImageUploadToast){
                                Toast.makeText(HomeScreenActivity.this, getResources().getString(R.string.sync_image_in_progress), Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (patientQueueFragment != null && !isSingOut){
                            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                                ((PatientQueueFragment) patientQueueFragment).setTabContent(AppConstants.DOCTOR_PRESENT);
                            }
                            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                                ((PatientQueueFragment) patientQueueFragment).setTabContent(AppConstants.RECEPTION_PRESENT);
                            }
                            ((PatientQueueFragment) patientQueueFragment).setTabContent(AppConstants.EXAMINED);
                        }
                    }


                } else if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_NO_DATA)){
                    if (isSingOut){
                        callLogout(null, false);
                    }
                    isSingOut = false;
                    Toast.makeText(HomeScreenActivity.this, getResources().getString(R.string.sync_no_data), Toast.LENGTH_SHORT).show();

                } else if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_ISSUE)){

                    Toast.makeText(HomeScreenActivity.this, getResources().getString(R.string.sync_error), Toast.LENGTH_SHORT).show();

                }
            }
        }

    }

    @Override
    public void onBackPressed() {

        isAfterLocationDialog = false;

        if (dlDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            dlDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount == 0){
                AlertDialogMessage alertDialogMessage = new AlertDialogMessage();
                alertDialogMessage.showAlertTwoButtonAlert(activity, getResources().getString(R.string.alert_message_confirm_exit), getResources().getString(R.string.yes), getResources().getString(R.string.no), false);
                alertDialogMessage.setAlertDialogOkListener(new AlertExitOkButtonListener());
            } else {

                FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
                String str = backEntry.getName();
                Logger.debug("Str1-" + str);


                if (str != null){
                    if (str.equalsIgnoreCase(FragmentConstant.FRAGMENT_PATIENT_QUEUE)){

                        AlertDialogMessage alertDialogMessage = new AlertDialogMessage();
                        alertDialogMessage.showAlertTwoButtonAlert(activity, getResources().getString(R.string.alert_message_confirm_exit), getResources().getString(R.string.yes), getResources().getString(R.string.no), false);
                        alertDialogMessage.setAlertDialogOkListener(new AlertExitOkButtonListener());

                        //super.onBackPressed();
                    } else {
                        if (str.equalsIgnoreCase(FragmentConstant.FRAGMENT_FAMILY_QUEUE)){
                            tlToolBar.setVisibility(View.VISIBLE);
                            tlToolBarForFamilyQueue.setVisibility(View.GONE);
                            setSupportActionBar(tlToolBar);
                        }
                        super.onBackPressed();
                    }


                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    private class AlertExitOkButtonListener implements AlertDialogMessage.AlertDialogOkListener {

        @Override
        public void onAlertDialogOkButtonListener() {
            ApplicationController.clearActivityStack();
            getSupportFragmentManager().popBackStack();
            finish();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null){
//			Toast.makeText(activity, "Latitude : " + String.valueOf(mLastLocation.getLatitude()) + "\nLongitude : " + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();

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

            if (addresses != null && addresses.size() > 0){
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

//				Logger.debug(addressGenerated);
//				Toast.makeText(HomeScreenActivity.this, addressGenerated, Toast.LENGTH_LONG).show();
            }

//			showNextScreen();
        } else {
            if (!isAfterLocationDialog){
                isAfterLocationDialog = true;
//				GeoLocationUtils.showGPSDisabledAlertToUser(this, this);
            } else {
//				showNextScreen();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filterAutoUploadProgress = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_IN_PROGRESS);
        IntentFilter filterAutoUploadDone = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_DONE);
        IntentFilter filterAutoUploadNoData = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_NO_DATA);
        IntentFilter filterAutoUploadIssue = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_ISSUE);

        IntentFilter filterMasterSyncProgress = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS);
        IntentFilter filterMasterSyncDone = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_DONE);
        IntentFilter filterMasterSyncNoData = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA);
        IntentFilter filterMasterSyncIssue = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_ISSUE);

        syncStatusReceiver = new SyncStatusReceiver();

        registerReceiver(syncStatusReceiver, filterAutoUploadProgress);
        registerReceiver(syncStatusReceiver, filterAutoUploadDone);
        registerReceiver(syncStatusReceiver, filterAutoUploadNoData);
        registerReceiver(syncStatusReceiver, filterAutoUploadIssue);

        registerReceiver(syncStatusReceiver, filterMasterSyncProgress);
        registerReceiver(syncStatusReceiver, filterMasterSyncDone);
        registerReceiver(syncStatusReceiver, filterMasterSyncNoData);
        registerReceiver(syncStatusReceiver, filterMasterSyncIssue);

        isSearchClicked = false;
        isAddClicked = false;

        if (isAfterOnCreate){
            isAfterOnCreate = false;
//			isGooglePlayServicesAvailable();
            buildGoogleApiClient();

        }

        mGoogleApiClient.connect();

        if (isAfterGooglePlayServiceIntent){
            isGooglePlayServicesAvailable();
//			showNextScreen();
            isAfterGooglePlayServiceIntent = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (syncStatusReceiver != null){
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (syncStatusReceiver != null){
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }
        if (dhbDao != null){
            dhbDao.closeDatabase();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status){
            return true;
        } else {
//			getErrorDialog(status, this, 111).show();
            return false;
        }
    }

    @Override
    public void onGeoLocDialogCancelPressed() {
        isAfterLocationDialog = false;
//		showNextScreen();
    }

    public void showPatientQueueFragment() {
        tlToolBar.setVisibility(View.VISIBLE);
        tlToolBarForFamilyQueue.setVisibility(View.GONE);
        setSupportActionBar(tlToolBar);
        pushFragments(patientQueueFragment, false, true, FragmentConstant.FRAGMENT_PATIENT_QUEUE);
    }

    public void showFamilyQueueFragment(Bundle bundle, int familyCount) {
        tlToolBar.setVisibility(View.GONE);
        tlToolBarForFamilyQueue.setVisibility(View.VISIBLE);
        if (familyCount <= 10){
            ibtnSearchFamilyMember.setVisibility(View.VISIBLE);
            ibtnAddFamilyMember.setVisibility(View.VISIBLE);
        } else {
            ibtnSearchFamilyMember.setVisibility(View.INVISIBLE);
            ibtnAddFamilyMember.setVisibility(View.INVISIBLE);
        }


        setSupportActionBar(tlToolBarForFamilyQueue);
        if (!familyQueueFragment.isVisible()){
            familyQueueFragment.setArguments(bundle);
        }
        pushFragments(familyQueueFragment, false, true, FragmentConstant.FRAGMENT_FAMILY_QUEUE);
    }

    public void showFamilyTitleButtons(boolean value) {
        if (value){
            ibtnSearchFamilyMember.setVisibility(View.VISIBLE);
            ibtnAddFamilyMember.setVisibility(View.VISIBLE);
        } else {
            ibtnSearchFamilyMember.setVisibility(View.INVISIBLE);
            ibtnAddFamilyMember.setVisibility(View.INVISIBLE);
        }
    }

    private class BackButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            onBackPressed();
        }

    }

    private class DoneBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Should come back to the patient queue
        }

    }

    private void showHospitalList() {
//        HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
//        ArrayList<HealthCareFirm> healthCareFirms = healthCareFirmDao.getAllHealthCareFirmsFromUserId(appPreferenceManager.getUserId());

        Dialog dialog = new HospitalSelectorDialog(this, selectedHospital, appPreferenceManager.getUserId());
        ((HospitalSelectorDialog) dialog).setDelegate(new HospitalSelectionDelegate());
        dialog.setCancelable(false);
        dialog.show();
    }

    private class HospitalSelectionDelegate implements SelectorDialogResult {
        @Override
        public void setSelector(String Selector, int id) {
            selectedHospital = Selector;
        }

        @Override
        public void setSelector(String selector, String id) {
            selectedHospital = selector;
            appPreferenceManager.setHospitalName(selector);
            appPreferenceManager.setHospitalId(id);
            HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
            HealthCareFirm healthCareFirm = healthCareFirmDao.getHealthCareFirmWithId(id);
            appPreferenceManager.setHospitalPlace(healthCareFirm.getPlace());
            setData();

            if (patientQueueFragment != null){
                if (((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue != null){
                    ((PatientQueueFragment) patientQueueFragment).isOkToFireApiForRefreshQueue = true;
                    ((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue.performClick();
                }
            }

        }

    }

    private class DrawerHospNameOnClickListener implements View.OnClickListener {
        String name;
        String id;

        public DrawerHospNameOnClickListener(String name, String id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public void onClick(View view) {
            appPreferenceManager.setHospitalName(name);
            appPreferenceManager.setHospitalId(id);
            if (dlDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                dlDrawerLayout.closeDrawer(Gravity.LEFT);
            }
            HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
            HealthCareFirm healthCareFirm = healthCareFirmDao.getHealthCareFirmWithId(id);
            appPreferenceManager.setHospitalPlace(healthCareFirm.getPlace());
            setData();

            ArrayList<HealthCareFirm> healthCareFirms;
//			HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
            healthCareFirms = healthCareFirmDao.getAllHealthCareFirmsFromUserId(appPreferenceManager.getUserId());

            PatientQueueCountDao patientQueueCountDao = new PatientQueueCountDao(dhbDao.getDb());

            for (PatientQueueCountModel patientQueueCountModel : patientQueueCountDao.getAllPatientQueueCountModel()){
                for (int i = 0; i < healthCareFirms.size(); i++){
                    if (patientQueueCountModel.getHealthcareFirmId().equalsIgnoreCase(healthCareFirms.get(i).getId())){
                        healthCareFirms.get(i).setPatientCount(patientQueueCountModel.getPatientCountQueue());
                    }
                }
            }
            setHospitalList(healthCareFirms);
            setFooterCounter(eximinedPatientCount, registeredPatientCount);

//			pushFragments(patientQueueFragment, false, true, FragmentConstant.FRAGMENT_PATIENT_QUEUE);
            getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);

            if (patientQueueFragment != null){
                if (((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue != null){
                    ((PatientQueueFragment) patientQueueFragment).isOkToFireApiForRefreshQueue = true;
                    ((PatientQueueFragment) patientQueueFragment).rltLayoutPresentQueue.performClick();
                }
            }

//			try {
//
//				if (((PatientQueueFragment) patientQueueFragment).isFirstTabQueue){
//
//					if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
//						((PatientQueueFragment) patientQueueFragment).setUpdateQueueStatusApiCall(AppConstants.RECEPTION_PRESENT);
//					} else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
//						((PatientQueueFragment) patientQueueFragment).setUpdateQueueStatusApiCall(AppConstants.DOCTOR_PRESENT);
//					}
//				} else {
//					((PatientQueueFragment) patientQueueFragment).setUpdateQueueStatusApiCall(AppConstants.EXAMINED);
//				}
//
//			} catch (Exception e){
//				e.printStackTrace();
//			}
        }

    }

    private class DrawerActionListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                if (NetworkUtils.isNetworkAvailable(activity)){
                    UserHealthCareFirmMapDao healthCareFirmDao = new UserHealthCareFirmMapDao(dhbDao.getDb());
                    ArrayList<UserHealthCareFirmMap> healthCareFirms = healthCareFirmDao.getUserHealthcareFirmMapWithUserId(appPreferenceManager.getUserId());

                    AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);

                    ApiCallAsyncTask hospitalwisePatientCountApiCallAsyncTask = asyncTaskForRequest.hospitalWisePatientCountApi(healthCareFirms);
                    hospitalwisePatientCountApiCallAsyncTask.setApiCallAsyncTaskDelegate(new HospitalWisePatientCountApiCallResult());
                    hospitalwisePatientCountApiCallAsyncTask.execute(hospitalwisePatientCountApiCallAsyncTask);
                } else {
                    ArrayList<HealthCareFirm> healthCareFirms;
                    HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
                    healthCareFirms = healthCareFirmDao.getAllHealthCareFirmsFromUserId(appPreferenceManager.getUserId());

                    PatientQueueCountDao patientQueueCountDao = new PatientQueueCountDao(dhbDao.getDb());

                    for (PatientQueueCountModel patientQueueCountModel : patientQueueCountDao.getAllPatientQueueCountModel()){
                        for (int i = 0; i < healthCareFirms.size(); i++){
                            if (patientQueueCountModel.getHealthcareFirmId().equalsIgnoreCase(healthCareFirms.get(i).getId())){
                                healthCareFirms.get(i).setPatientCount(patientQueueCountModel.getPatientCountQueue());
                            }
                        }
                    }
                    setHospitalList(healthCareFirms);
                    setFooterCounter(eximinedPatientCount, registeredPatientCount);
                }
            }
        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }

    }

    private class HospitalWisePatientCountApiCallResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            responseParser = new ResponseParser(activity);
            if (!responseParser.parseIntoError(json, statusCode)){

                patientQueueCountResponseModel = responseParser.getPatientQueueCountResponseModel(json, statusCode);

                if (patientQueueCountResponseModel == null){
                    return;
                }
                if (patientQueueCountResponseModel.getPatientCountResponseModel() != null){
                    eximinedPatientCount = patientQueueCountResponseModel.getPatientCountResponseModel().getTotalExaminedPatient();
                    registeredPatientCount = patientQueueCountResponseModel.getPatientCountResponseModel().getTotalRegisteredPatient();
                    setFooterCounter(eximinedPatientCount, registeredPatientCount);
                }
                if (patientQueueCountResponseModel.getPatientCountResponseModel() != null && patientQueueCountResponseModel.getPatientCountResponseModel().getPatientQueueCountModelList() != null){
                    for (PatientQueueCountModel patientQueueCountModel : patientQueueCountResponseModel.getPatientCountResponseModel().getPatientQueueCountModelList()){
                        PatientQueueCountDao patientQueueCountDao = new PatientQueueCountDao(dhbDao.getDb());
                        patientQueueCountModel.setId(DeviceUtils.getRandomUUID());
                        patientQueueCountModel.setCreatedBy(appPreferenceManager.getUserId());
                        patientQueueCountModel.setCreatedAt(DateUtils.getCurrentTimeInDefault());
                        patientQueueCountModel.setUpdatedBy(appPreferenceManager.getUserId());
                        patientQueueCountModel.setUpdatedAt(DateUtils.getCurrentTimeInDefault());
                        patientQueueCountModel.setRecordStatus(AppConstants.ACTIVE_RECORD_STATUS);
                        patientQueueCountModel.setSyncStatus(AppConstants.STATUS_SYNCED);
                        patientQueueCountModel.setSyncAction(AppConstants.INSERT);
                        patientQueueCountDao.insertOrUpdatePatientQueueCountModelAfterApiCall(patientQueueCountModel);
                    }
                }
                if (patientQueueCountResponseModel.getPatientCountResponseModel() != null && patientQueueCountResponseModel.getPatientCountResponseModel().getHealthcareFirms() != null){
                    for (HealthCareFirm healthCareFirmAPIModel : patientQueueCountResponseModel.getPatientCountResponseModel().getHealthcareFirms()){
                        HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
                        healthCareFirmDao.insertOrUpdateHealthCareFirmAfterApiCall(healthCareFirmAPIModel);
                    }
                }

                setHospitalList(null);
            }
        }

        @Override
        public void onApiCancelled() {

        }

    }

    private class AlertDialogSignOutListener implements AlertDialogMessage.AlertDialogOkListener {
        @Override
        public void onAlertDialogOkButtonListener() {
            callLogout(null, false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
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
        boolean isnew=false;
        if(Integer.parseInt(str[0])>Integer.parseInt(str2[0]))
        {
            isnew=true;
        }
        else if(Integer.parseInt(str[1])>Integer.parseInt(str2[1])){
            isnew=true;
        }
        if (Integer.parseInt(str[0]) < Integer.parseInt(str2[0])){
            Log.d("versions", "versions==" + str[0] + str2[0]);
            builder.setMessage(getString(R.string.loading_version_api_msg_unsupported)
                    + System.lineSeparator()
                    + getString(R.string.loading_version_api_msg_upgrade));

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lvDrawerList.performItemClick(lvDrawerList,7,7);
              //dialog.cancel();
              // ApplicationController.clearActivityStack();
                }
            });

//       builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//          @Override
//          public void onDismiss(DialogInterface dialog) {
//             dialog.cancel();
//             isVersionApiInProgress = false;
//             ApplicationController.clearActivityStack();
//          }
//       });

        }else if(Integer.parseInt(str[1]) < Integer.parseInt(str2[1]) && isnew==false) {

            builder.setMessage(getString(R.string.loading_version_api_msg_unsupported)
                    + System.lineSeparator()
                    + getString(R.string.loading_version_api_msg_upgrade));

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("versions","versions=="+which);
                    lvDrawerList.performItemClick(lvDrawerList,7,7);
               //dialog.cancel();
               //ApplicationController.clearActivityStack();
                }
            });

        }
        else  if(Integer.parseInt(str[2]) < Integer.parseInt(str2[2]) && isnew==false){
            Log.d("versions","versions=="+str[2]+str2[2]);
            builder.setMessage(getString(R.string.loading_version_api_msg_unsupported)
                    + System.lineSeparator()
                    + getString(R.string.loading_version_api_msg_upgrade));

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lvDrawerList.performItemClick(lvDrawerList,7,7);
               //dialog.cancel();
              // ApplicationController.clearActivityStack();
                }
            });
        }
        else {

            builder.setMessage(versionInfo.getReleaseNotes()
                    + System.lineSeparator()
                    + getString(R.string.loading_version_api_msg_upgrade));

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

//       builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//          @Override
//          public void onDismiss(DialogInterface dialog) {
//                  dialog.cancel();
//                  isVersionApiInProgress = false;
//                  showNextScreen(false);
//          }
//       });

        }

        builder.setPositiveButton(getString(R.string.upgrade), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //callLogout("Version Upgrade",true);
                lvDrawerList.performItemClick(lvDrawerList,7,7);
            }
        });

        builder.create().show();
    }*/

        }
