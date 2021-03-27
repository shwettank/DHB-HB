package com.dhb.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.SettingsActivity;
import com.dhb.adapter.HospitalListAdapter;
import com.dhb.dao.daomodels.HealthCareFirmDao;
import com.dhb.models.HealthCareFirm;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.SelectorDialogResult;

import java.util.ArrayList;

public class HospitalSelectorDialog extends Dialog {
    private SettingsActivity settingActivity;
    private AppPreferenceManager appPreferenceManager;
    private HomeScreenActivity activity;
    private ArrayList<HealthCareFirm> districtModelList = new ArrayList<HealthCareFirm>();
    private ArrayList<HealthCareFirm> mainDistrictModelList = new ArrayList<HealthCareFirm>();

    private SelectorDialogResult delegate;

    private EditText etSelectDistrict;
    private TextView txtDialogTitle;
    private ListView lvDistrict;
    // private ArrayAdapter<DistrictModel> stateModelArrayAdapter;
    private HospitalListAdapter districtListAdapter;
    private String selectedDistrict;
    private RelativeLayout rltDialog;


    public HospitalSelectorDialog(HomeScreenActivity activity, String selectedString, String userId) {
        super(activity);

        this.activity = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
        this.mainDistrictModelList.clear();
        this.districtModelList.clear();
        this.selectedDistrict = selectedString;
        HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(activity.dhbDao.getDb());
        ArrayList<HealthCareFirm> healthCareFirms = healthCareFirmDao.getAllHealthCareFirmsFromUserId(appPreferenceManager.getUserId());


        this.mainDistrictModelList.addAll(healthCareFirms);
        this.districtModelList.addAll(this.mainDistrictModelList);

    }

    public HospitalSelectorDialog(SettingsActivity activity, String selectedString, String userId) {
        super(activity);

        this.settingActivity = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
        this.mainDistrictModelList.clear();
        this.districtModelList.clear();
        this.selectedDistrict = selectedString;
       // HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(settingActivity.dhbDao.getDb());
        /*HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(activity.dhbDao.getDb());
        ArrayList<HealthCareFirm> healthCareFirms = healthCareFirmDao.getAllHealthCareFirmsFromUserId(appPreferenceManager.getUserId());


        this.mainDistrictModelList.addAll(healthCareFirms);
        this.districtModelList.addAll(this.mainDistrictModelList);*/

    }

    public void setDelegate(SelectorDialogResult delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_state_selector);
        initUI();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initUI() {
        etSelectDistrict = (EditText) findViewById(R.id.et_selector);
        txtDialogTitle = (TextView) findViewById(R.id.txt_dialog_title);
        rltDialog = (RelativeLayout) findViewById(R.id.rlt_dialog);
        txtDialogTitle.setText("Select Hospital");
        etSelectDistrict.setVisibility(View.GONE);
        lvDistrict = (ListView) findViewById(R.id.lv_selector);
        setListener();
        setAdapter();
    }

    private void setAdapter() {
        if (activity != null){
            districtListAdapter = new HospitalListAdapter(activity, districtModelList, selectedDistrict);
            lvDistrict.setAdapter(districtListAdapter);
        } else {
            districtListAdapter = new HospitalListAdapter(settingActivity, districtModelList, selectedDistrict);
            lvDistrict.setAdapter(districtListAdapter);
        }
    }

    private void setListener() {
        lvDistrict.setOnItemClickListener(new StatesListViewOnItemClickListener());
//		rltDialog.setOnClickListener(new HideKeybordOnClickListener());
    }

    Handler handler = new Handler();

    private class StatesListViewOnItemClickListener implements
            AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            districtListAdapter.setSelectedString(districtModelList.get(position).getName().toString());
            districtListAdapter.notifyDataSetChanged();

            final Runnable r = new Runnable() {
                public void run() {
                    delegate.setSelector(districtModelList.get(position).getName().toString(), districtModelList.get(position).getId());
                    HospitalSelectorDialog.this.dismiss();
                }

            };

            handler.postDelayed(r, 250);

        }

    }

    private class HideKeybordOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
