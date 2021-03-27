package com.dhb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.PatientDetailInformationActivity;
import com.dhb.activity.PatientVisitsActivity;
import com.dhb.adapter.PatientQueueRecycleAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.daomodels.DocumentDao;
import com.dhb.dao.daomodels.PatientHealthCareFirmMapDao;
import com.dhb.dao.daomodels.PatientsDao;
import com.dhb.dao.daomodels.QueueDao;
import com.dhb.models.PatientHealthCareFirmMap;
import com.dhb.models.Patients;
import com.dhb.models.Queue;
import com.dhb.network.AbstractApiModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.AppConstants;
import com.dhb.utils.BundleConstants;
import com.dhb.utils.DateUtils;
import com.dhb.utils.DeviceUtils;
import com.dhb.utils.NetworkUtils;
import com.dhb.uiutils.AbstractFragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import org.json.JSONException;


public class PatientQueueFragment extends AbstractFragment implements AppConstants, LoaderManager.LoaderCallbacks<Cursor> {

    private Activity activity;
    public static Fragment fragment;

    DhbDao dhbDao;

    private View rootView;

    public RelativeLayout rltLayoutPresentQueue;
    private RelativeLayout rltLayoutExaminedQueue;

    private TextView txtDate;

    private RecyclerView rvSecondTabRecycleView;
    private RecyclerView rvFirstTabRecycleView;

    private LinearLayoutManager mLayoutManagerFirstTab;
    private LinearLayoutManager mLayoutManagerSecondTab;

    public PatientQueueRecycleAdapter patientPresentQueueRecycleAdapter;
    public PatientQueueRecycleAdapter patientExaminedQueueRecycleAdapter;

    public SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener;

    public List<Queue> patientPresentQueueModelList = new ArrayList<>();
    public List<Queue> patientExaminedQueueModelList = new ArrayList<>();

    private PatientQueueContentObserver patientQueueContentObserver;

    private ApiCallAsyncTask updateQueueStatusApiCallAsyncTask;
    private ApiCallAsyncTask doctorListApiCallAsyncTask;

    private TextView txtPresentCount1, txtPresentCount2, txtPresentCount3;
    private TextView txtExaminedCount1, txtExaminedCount2, txtExaminedCount3;
    private TextView txtPresent;
    private TextView txtExamined;
    private TextView txtNoRecordTabOne;
    private TextView txtNoRecordTabTwo;

    public boolean isFirstTabQueue = true;
    private boolean isSecondTabQueue = false;

    public SwipeRefreshLayout swipeViewFirstTab;
    public SwipeRefreshLayout swipeViewSecondTab;

    public boolean isPresentQueueInProgress = false;
    public boolean isExaminedQueueInProgress = false;


    public boolean isAfterOnCreateView = false;
    public boolean isOkToFireApiForRefreshQueue = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_patient_queue, null);
        initUI();
        setListener();
//		isOkToFireApiForRefreshQueue = true;
//		rltLayoutPresentQueue.performClick();
        setData();
        setFont();
        /*if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Reception")){
            ((AbstractActivity) getActivity()).showScreenId(getResources().getString(R.string.screen_no_rec_queue));
        } else if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Doctor")){
            ((AbstractActivity) getActivity()).showScreenId(getResources().getString(R.string.screen_no_doc_queue));
        }*/

        isAfterOnCreateView = true;

        return rootView;
    }

    public static Fragment getInstance() {
        fragment = new PatientQueueFragment();
        return fragment;

    }

    @Override
    public void initUI() {
        super.initUI();
        activity = getActivity();

        dhbDao = ((HomeScreenActivity) activity).dhbDao;
        patientQueueContentObserver = new PatientQueueContentObserver(new Handler());

        activity.getContentResolver().registerContentObserver(AppConstants.PATIENT_QUEUE_CONTENT_URI, true, patientQueueContentObserver);
        rltLayoutPresentQueue = (RelativeLayout) rootView.findViewById(R.id.rlt_layout_present);
        rltLayoutExaminedQueue = (RelativeLayout) rootView.findViewById(R.id.rlt_layout_examine);
        txtDate = (TextView) rootView.findViewById(R.id.txt_date);
        mLayoutManagerFirstTab = new LinearLayoutManager(activity);
        mLayoutManagerSecondTab = new LinearLayoutManager(activity);
        rvFirstTabRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_first_tab_list);
        rvFirstTabRecycleView.setLayoutManager(mLayoutManagerFirstTab);

        rvSecondTabRecycleView = (RecyclerView) rootView.findViewById(R.id.rv_second_tab_list);
        rvSecondTabRecycleView.setLayoutManager(mLayoutManagerSecondTab);

        txtPresent = (TextView) rootView.findViewById(R.id.txt_present);
        txtPresentCount1 = (TextView) rootView.findViewById(R.id.txt_present_count1);
        txtPresentCount2 = (TextView) rootView.findViewById(R.id.txt_present_count2);
        txtPresentCount3 = (TextView) rootView.findViewById(R.id.txt_present_count3);

        txtExamined = (TextView) rootView.findViewById(R.id.txt_examine);
        txtExaminedCount1 = (TextView) rootView.findViewById(R.id.txt_examine_count1);
        txtExaminedCount2 = (TextView) rootView.findViewById(R.id.txt_examine_count2);
        txtExaminedCount3 = (TextView) rootView.findViewById(R.id.txt_examine_count3);
        txtNoRecordTabOne = (TextView) rootView.findViewById(R.id.txt_no_records_first_tab);
        txtNoRecordTabTwo = (TextView) rootView.findViewById(R.id.txt_no_records_second_tab);
        swipeViewFirstTab = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_view_first_tab);
        swipeViewSecondTab = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_view_second_tab);

        patientPresentQueueRecycleAdapter = new PatientQueueRecycleAdapter(activity, patientPresentQueueModelList, appPreferenceManager.getRoleName(), false);
        rvFirstTabRecycleView.setAdapter(patientPresentQueueRecycleAdapter);

        patientExaminedQueueRecycleAdapter = new PatientQueueRecycleAdapter(activity, patientExaminedQueueModelList, appPreferenceManager.getRoleName(), false);
        rvSecondTabRecycleView.setAdapter(patientExaminedQueueRecycleAdapter);

//		swipeViewFirstTab.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeViewFirstTab.setColorScheme(android.R.color.white);
        swipeViewFirstTab.setProgressBackgroundColor(android.R.color.holo_blue_dark);

//		swipeViewSecondTab.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeViewSecondTab.setColorScheme(android.R.color.white);
        swipeViewSecondTab.setProgressBackgroundColor(android.R.color.holo_blue_dark);

    }

    private void setData() {
        txtDate.setText(DateUtils.getDateInDefault(Calendar.getInstance().getTimeInMillis()).toUpperCase());
    }

    private void setListener() {
        rltLayoutPresentQueue.setOnClickListener(new PresentQueueOnclickListener());
        rltLayoutExaminedQueue.setOnClickListener(new ExaminedQueueOnclickListener());

        swipeViewFirstTab.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isOkToFireApiForRefreshQueue = true;
                rltLayoutPresentQueue.performClick();
            }
        });


        swipeViewSecondTab.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isOkToFireApiForRefreshQueue = true;
                rltLayoutExaminedQueue.performClick();
            }
        });


        /*touchListener =
                new SwipeToDismissTouchListener<>(
                        new RecyclerViewAdapter(rvFirstTabRecycleView),
                        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerViewAdapter view, int position) {
                                patientPresentQueueRecycleAdapter.removePatientInformationArrayElements(position);
                                patientPresentQueueRecycleAdapter.remove(position);
                            }
                        }, appPreferenceManager.getRoleName());*/

        rvFirstTabRecycleView.setOnTouchListener(touchListener);
        rvFirstTabRecycleView.setOnScrollListener((RecyclerView.OnScrollListener)touchListener.makeScrollListener());
        /*rvFirstTabRecycleView.addOnItemTouchListener(new SwipeableItemClickListener(activity,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        //NO SHOW
                        if (view.getId() == R.id.txt_delete || view.getId() == R.id.txt_delete_hindi){

                            if (NetworkUtils.isNetworkAvailable(getActivity())
                                    && patientPresentQueueModelList.get(position).getSyncStatus().equals(AppConstants.STATUS_SYNCED)
                                    && patientPresentQueueModelList.get(position).getPatient().getSyncStatus().equals(AppConstants.STATUS_SYNCED)){

                                sendToNoShow(patientPresentQueueModelList.get(position),
                                        new UpdateQueueInterface() {
                                            @Override
                                            public void deleteRow(Queue queue) {

                                                touchListener.undoPendingDismiss();


                                                QueueDao queueDao = new QueueDao(dhbDao.getDb(), appPreferenceManager);
                                                if (appPreferenceManager.getRoleName().equals(AppConstants.RECEPTION)){
                                                    queueDao.updateRecordStatus(patientPresentQueueModelList.get(position).getId(),
                                                            AppConstants.DELETED_RECORD_STATUS, AppConstants.STATUS_SYNCED,
                                                            -1, -1);
                                                } else if (appPreferenceManager.getRoleName().equals(AppConstants.DOCTOR)){
                                                    queueDao.updateRecordStatus(patientPresentQueueModelList.get(position).getId(),
                                                            AppConstants.DELETED_RECORD_STATUS, AppConstants.STATUS_SYNCED, -1, -1);
                                                }

                                                if (patientPresentQueueModelList.get(position).getPatient() != null
                                                        && patientPresentQueueModelList.get(position).getPatient().getRegistrationStatus() != null
                                                        && patientPresentQueueModelList.get(position).getPatient().getRegistrationStatus().equals(AppConstants.NO)
                                                        && patientPresentQueueModelList.get(position).getPatient().getSyncStatus() != null
                                                        && patientPresentQueueModelList.get(position).getPatient().getSyncStatus().equals(AppConstants.STATUS_NON_SYNC)){
                                                    PatientsDao patientsDao = new PatientsDao(dhbDao.getDb());
                                                    patientsDao.deletePatient(patientPresentQueueModelList.get(position).getPatient().getId());
                                                }

                                                if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                                                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.RECEPTION_PRESENT);
                                                    setPresentQueueAsyncTask.execute();
                                                } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                                                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.DOCTOR_PRESENT);
                                                    setPresentQueueAsyncTask.execute();
                                                }

                                            }
                                        });

                            } else {
                                touchListener.undoPendingDismiss();
                                updateStatus(patientPresentQueueModelList.get(position), AppConstants.NO_SHOW);

                                if (patientPresentQueueModelList.get(position).getPatient() != null
                                        && patientPresentQueueModelList.get(position).getPatient().getRegistrationStatus() != null
                                        && patientPresentQueueModelList.get(position).getPatient().getRegistrationStatus().equals(AppConstants.NO)
                                        && patientPresentQueueModelList.get(position).getPatient().getSyncStatus() != null
                                        && patientPresentQueueModelList.get(position).getPatient().getSyncStatus().equals(AppConstants.STATUS_NON_SYNC)){
                                    PatientsDao patientsDao = new PatientsDao(dhbDao.getDb());
                                    patientsDao.deletePatient(patientPresentQueueModelList.get(position).getPatient().getId());
                                }

                                if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.RECEPTION_PRESENT);
                                    setPresentQueueAsyncTask.execute();
//										setPresentQueue(AppConstants.RECEPTION_PRESENT);
                                } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
//							                setPresentQueue(AppConstants.DOCTOR_PRESENT);
                                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.DOCTOR_PRESENT);
                                    setPresentQueueAsyncTask.execute();
                                }
                            }

                        }
                        // UNDO
                        else if (view.getId() == R.id.txt_undo || view.getId() == R.id.txt_undo_hindi){
                            touchListener.undoPendingDismiss();
                        }
                        // PATIENT VISITS / ADD AS EXAMINED
                        // OR
                        // SEND TO DOCTOR
                        else if (view.getId() == R.id.txt_action || view.getId() == R.id.txt_action_hindi){

                            if (patientPresentQueueModelList.get(position).getPatient().getRegistrationStatus().equals(AppConstants.YES)){

                                // SEND TO DOCTOR
                                if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){

                                    if (NetworkUtils.isNetworkAvailable(getActivity())){
                                        if (patientPresentQueueModelList.get(position).getSyncStatus().equals(AppConstants.STATUS_SYNCED)
                                                && patientPresentQueueModelList.get(position).getPatient().getSyncStatus().equals(AppConstants.STATUS_SYNCED)){
                                            //SEND TO DOCTOR IF NETWORK AVAILABLE

                                            sendToDoctor(patientPresentQueueModelList.get(position),
                                                    new UpdateQueueInterface() {
                                                        @Override
                                                        public void deleteRow(Queue queue) {

                                                            touchListener.undoPendingDismiss();

                                                            QueueDao queueDao = new QueueDao(dhbDao.getDb(), appPreferenceManager);
                                                            queueDao.updateRecordStatus(patientPresentQueueModelList.get(position).getId(),
                                                                    AppConstants.DELETED_RECORD_STATUS, AppConstants.STATUS_SYNCED, -1, -1);

                                                            queue.setSyncStatus(AppConstants.STATUS_SYNCED);
                                                            queue.setSyncAction(AppConstants.INSERT);
                                                            queueDao.insertInQueueWithoutCheck(queue);

                                                            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                                                                setTabContent(AppConstants.RECEPTION_PRESENT);
                                                            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                                                                setTabContent(AppConstants.DOCTOR_PRESENT);
                                                                setTabContent(AppConstants.EXAMINED);
                                                            }
                                                        }
                                                    });
                                        } else {
                                            //SEND TO DOCTOR IF PATIENT NOT SYNCED
                                            Toast.makeText(activity, getResources().getString(R.string.sync_error_msg), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        //SEND TO DOCTOR IF NETWORK NOT AVAILABLE
                                        Toast.makeText(activity, "This feature is only available in online mode.", Toast.LENGTH_SHORT).show();
                                    }
//                                    }
                                }
                                // EXAMINED
                                else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                                    touchListener.undoPendingDismiss(SwipeToDismissTouchListener.mAnimationTimeReset);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(BundleConstants.LIST_ID, position);
                                    bundle.putParcelable(BundleConstants.PATIENT_QUEUE_MODEL, patientPresentQueueModelList.get(position));
                                    bundle.putBoolean(BundleConstants.IS_FROM_PRESENT, true);
                                    goToActivity(activity, PatientVisitsActivity.class, bundle);

                                    activity.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);
                                }
                            } else {
                                Toast.makeText(activity, getResources().getString(R.string.alert_register_send_to_doctor), Toast.LENGTH_LONG).show();
//			                        AlertDialogMessage alertDialogMessage = new AlertDialogMessage();
//			                        alertDialogMessage.showAlert(activity, getResources().getString(R.string.alert_register_send_to_doctor), true);
                            }

//							patientPresentQueueRecycleAdapter.notifyDataSetChanged();
//							patientExaminedQueueRecycleAdapter.notifyDataSetChanged();

                        } else if (view.getId() == R.id.txt_undo_action || view.getId() == R.id.txt_undo_action_hindi){
                            touchListener.undoPendingDismiss();
                        } else if (view.getId() == R.id.ibtn_view_details_register_user){
                            addPatientDetails(position, 1);
                        } else if (view.getId() == R.id.ibtn_view_details_unregister_user){
                            if (touchListener.existPendingDismisses()){
//			                        touchListener.undoPendingDismiss(SwipeToDismissTouchListener.mAnimationTimeReset);
                            } else {
                                addPatientDetails(position, 1);
                            }
                        } else {
                            if (touchListener.existPendingDismisses()){
//			                        touchListener.undoPendingDismiss(SwipeToDismissTouchListener.mAnimationTimeReset);
                            } else {
                                addPatientDetails(position, 1);
                            }
                        }
                    }
                }

        ));*/

        rvSecondTabRecycleView.addOnItemTouchListener((RecyclerView.OnItemTouchListener) new

                SwipeableItemClickListener(activity,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.ibtn_view_details_register_user){
                            addPatientDetails(position, 2);
                        } else if (view.getId() == R.id.ibtn_view_details_unregister_user){
                            addPatientDetails(position, 2);
                        } else {
                            addPatientDetails(position, 2);
                        }
                    }
                }

        ));

    }

    public Queue updateStatus(Queue queueFromList, String status) {

        QueueDao queueDao = new QueueDao(dhbDao.getDb(), appPreferenceManager);
        boolean isSuccessful = queueDao.updateRecordStatus(queueFromList.getId(),
                AppConstants.DELETED_RECORD_STATUS, AppConstants.STATUS_NON_SYNC, -1, -1);

        Queue queue = null;

        if (isSuccessful){
            queue = new Queue();
            queue.setId(DeviceUtils.getRandomUUID());
            queue.setPatientId(queueFromList.getPatientId());
            queue.setSenderId(appPreferenceManager.getUserId());
            if (queueFromList.getSenderId() != null){
                queue.setReceiverID(queueFromList.getSenderId());
            } else {
                queue.setReceiverID("");
            }
            queue.setStatus(Integer.parseInt(status));
            queue.setMissCallTime(queueFromList.getMissCallTime());
            queue.setAppointmentStartTime(queueFromList.getAppointmentStartTime());
            queue.setAppointmentEndTime(queueFromList.getAppointmentEndTime());
            queue.setParentQueuId(queueFromList.getId());
            queue.setSenderComments(queueFromList.getSenderComments());
            queue.setReceiverComments("");
            queue.setCreatedAt(DateUtils.getCurrentTimeInDefault());
            queue.setCreatedBy(appPreferenceManager.getUserId());
            queue.setUpdatedAt(DateUtils.getCurrentTimeInDefault());
            queue.setUpdatedBy(appPreferenceManager.getUserId());
            queue.setSyncStatus(AppConstants.STATUS_NON_SYNC);
            queue.setSyncAction(AppConstants.INSERT);
            queue.setRecordStatus(AppConstants.ACTIVE_RECORD_STATUS);
            queueDao.insertInQueueWithoutCheck(queue);
        }
        return queue;
    }

    private void setFont() {
        txtPresent.setTypeface(fontOpenRobotoMedium);
        txtExamined.setTypeface(fontOpenRobotoMedium);
        txtDate.setTypeface(fontOpenRobotoRegular);
        txtPresentCount1.setTypeface(fontOpenRobotoMedium);
        txtPresentCount2.setTypeface(fontOpenRobotoMedium);
        txtPresentCount3.setTypeface(fontOpenRobotoMedium);
        txtExaminedCount1.setTypeface(fontOpenRobotoMedium);
        txtExaminedCount2.setTypeface(fontOpenRobotoMedium);
        txtExaminedCount3.setTypeface(fontOpenRobotoMedium);
        txtNoRecordTabOne.setTypeface(fontOpenRobotoRegular);
        txtNoRecordTabTwo.setTypeface(fontOpenRobotoRegular);
    }

    private void addPatientDetails(int position, int forTab) {

        if (position != -1){

//            if (patientPresentQueueModelList.get(position).getFamilyCount() > 1) {
//           Bundle bundle = new Bundle();
//                bundle.putString(BundleConstants.FAMILY_ID, patientPresentQueueModelList.get(position).getFamilyId());
//                ((HomeScreenActivity) activity).showFamilyQueueFragment(bundle, patientPresentQueueModelList.get(position).getFamilyCount());
//            } else {
//
            Intent i = new Intent(activity, PatientDetailInformationActivity.class );
            Bundle bundle = new Bundle();

//                if (patientPresentQueueModelList.get(position).getFamilyCount() > 1) {
//                    bundle.putBoolean(BundleConstants.IS_FAMILY, true);
//                } else if (patientPresentQueueModelList.get(position).getRegistrationStatus() == 1) {
//                    bundle.putBoolean(BundleConstants.IS_REGISTERED_PATIENT, true);
//                    bundle.putString(BundleConstants.FAMILY_ID, patientPresentQueueModelList.get(position).getFamilyId());
//                    bundle.putString(BundleConstants.PATIENT_MOBILE_1, patientPresentQueueModelList.get(position).getMobile1());
//                }

            if (forTab == 1){
                bundle.putInt(BundleConstants.LIST_ID, position);
                bundle.putParcelable(BundleConstants.PATIENT_QUEUE_MODEL, patientPresentQueueModelList.get(position));
                bundle.putBoolean(BundleConstants.IS_FROM_PRESENT, true);
            } else if (forTab == 2){
                bundle.putInt(BundleConstants.LIST_ID, position);
                bundle.putParcelable(BundleConstants.PATIENT_QUEUE_MODEL, patientExaminedQueueModelList.get(position));
                bundle.putBoolean(BundleConstants.IS_FROM_PRESENT, false);
            }
            i.putExtras(bundle);
            activity.startActivity(i);
//
//            }
        }
    }

   /* public void setUpdateQueueStatusApiCall(String status) throws JSONException {
        if (NetworkUtils.isNetworkAvailable(activity) && appPreferenceManager.getHospitalId() != null && !appPreferenceManager.getHospitalId().isEmpty()){
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            ApiCallAsyncTask getPresentPatientQueueApiCallAsyncTask = asyncTaskForRequest.getViewPatientListFirmWiseAsyncTask(status, 1, appPreferenceManager.getHospitalId());
            if (status.equalsIgnoreCase(AppConstants.RECEPTION_PRESENT) || status.equalsIgnoreCase(AppConstants.DOCTOR_PRESENT)){
                swipeViewFirstTab.setRefreshing(true);
                swipeViewSecondTab.setRefreshing(false);
                getPresentPatientQueueApiCallAsyncTask.setApiCallAsyncTaskDelegate(new PatientQueueApiCallDelegate());
            } else if (status.equalsIgnoreCase(AppConstants.EXAMINED)){
                swipeViewFirstTab.setRefreshing(false);
                swipeViewSecondTab.setRefreshing(true);
                getPresentPatientQueueApiCallAsyncTask.setApiCallAsyncTaskDelegate(new ExaminedPatientQueueApiCallDelegate());
            }
            getPresentPatientQueueApiCallAsyncTask.execute(getPresentPatientQueueApiCallAsyncTask);
        } else {
            if (status.equalsIgnoreCase(AppConstants.RECEPTION_PRESENT) || status.equalsIgnoreCase(AppConstants.DOCTOR_PRESENT)){
                if (swipeViewFirstTab != null){
                    swipeViewFirstTab.setRefreshing(false);
                }
                isPresentQueueInProgress = false;
            } else if (status.equalsIgnoreCase(AppConstants.EXAMINED)){
                if (swipeViewSecondTab != null){
                    swipeViewSecondTab.setRefreshing(false);
                }
                isExaminedQueueInProgress = false;
            }
        }
    }*/

    private class PresentQueueOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            try {
                swipeViewFirstTab.setRefreshing(true);
                swipeViewSecondTab.setRefreshing(false);
                isFirstTabQueue = true;
                isSecondTabQueue = false;

                txtNoRecordTabOne.setVisibility(View.INVISIBLE);
                txtNoRecordTabTwo.setVisibility(View.INVISIBLE);

                if (!isPresentQueueInProgress && isOkToFireApiForRefreshQueue){
                    isPresentQueueInProgress = true;
                    getPresentPatientQueue();
                }

            } catch (JSONException e){
                swipeViewFirstTab.setRefreshing(false);
                e.printStackTrace();
            }
            rvFirstTabRecycleView.setVisibility(View.VISIBLE);
            rvSecondTabRecycleView.setVisibility(View.INVISIBLE);
            swipeViewSecondTab.setVisibility(View.INVISIBLE);
            swipeViewFirstTab.setVisibility(View.VISIBLE);
            rltLayoutPresentQueue.setEnabled(false);
            rltLayoutExaminedQueue.setEnabled(true);

            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                /*SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.RECEPTION_PRESENT);
                setPresentQueueAsyncTask.execute();*/
            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                /*SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.DOCTOR_PRESENT);
                setPresentQueueAsyncTask.execute();*/
            }

            isOkToFireApiForRefreshQueue = true;

        }

    }

    private class ExaminedQueueOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            try {
                if (swipeViewFirstTab != null){
                    swipeViewFirstTab.setRefreshing(false);
                }
                if (swipeViewSecondTab != null){
                    swipeViewSecondTab.setRefreshing(true);
                }
                String status = "";
                isFirstTabQueue = false;
                isSecondTabQueue = true;

                txtNoRecordTabOne.setVisibility(View.INVISIBLE);
                txtNoRecordTabTwo.setVisibility(View.INVISIBLE);

//				if(patientExaminedQueueModelList == null || (patientExaminedQueueModelList != null && patientExaminedQueueModelList.size() == 0)){
//					txtNoRecord.setVisibility(View.VISIBLE);
//				}
//				else{
//					txtNoRecord.setVisibility(View.INVISIBLE);
//				}

                status = AppConstants.EXAMINED;

                if (!isExaminedQueueInProgress){
                    isExaminedQueueInProgress = true;
                    getExaminedPatientQueue();
                }

            } catch (JSONException e){
                if (swipeViewSecondTab != null){
                    swipeViewSecondTab.setRefreshing(false);
                }
                e.printStackTrace();
            }
            rvFirstTabRecycleView.setVisibility(View.INVISIBLE);
            rvSecondTabRecycleView.setVisibility(View.VISIBLE);
            rltLayoutPresentQueue.setEnabled(true);
            rltLayoutExaminedQueue.setEnabled(false);
            swipeViewSecondTab.setVisibility(View.VISIBLE);
            swipeViewFirstTab.setVisibility(View.INVISIBLE);

            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
               /* SetExaminedQueueAsyncTask setExaminedQueueAsyncTask = new SetExaminedQueueAsyncTask(AppConstants.EXAMINED);
                setExaminedQueueAsyncTask.execute();*/
            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
               /* SetExaminedQueueAsyncTask setExaminedQueueAsyncTask = new SetExaminedQueueAsyncTask(AppConstants.EXAMINED);
                setExaminedQueueAsyncTask.execute();*/
            }

            isOkToFireApiForRefreshQueue = true;

        }

    }

    /*private void sendToDoctor(Queue queue, UpdateQueueInterface delegate) {
        doctorListApiCallAsyncTask = new ApiCallAsyncTask(activity);
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        doctorListApiCallAsyncTask = asyncTaskForRequest.getDepartmentDoctorList();
        doctorListApiCallAsyncTask.setApiCallAsyncTaskDelegate(new DepartmentAndDoctorListAsyncTaskDelegate(queue, delegate));
        doctorListApiCallAsyncTask.execute(doctorListApiCallAsyncTask);
    }*/

    /*private void sendToNoShow(Queue queue, UpdateQueueInterface delegate) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        UpdateQueueStatusModelNew updateQueueStatusModel = new UpdateQueueStatusModelNew();
        updateQueueStatusModel.setQueueId(queue.getId());
        updateQueueStatusModel.setFromStatus("" + queue.getStatus());
        updateQueueStatusModel.setHealthcareFirmId(appPreferenceManager.getHospitalId());
        updateQueueStatusModel.setToStatus("" + AppConstants.NO_SHOW);
        updateQueueStatusApiCallAsyncTask = asyncTaskForRequest.getUpdateQueueStatusAsyncTask(updateQueueStatusModel, queue.getPatient().getId());
        updateQueueStatusApiCallAsyncTask.setApiCallAsyncTaskDelegate(new SendToNoShowApiCallAsyncTaskDelegate(queue, delegate));
        updateQueueStatusApiCallAsyncTask.execute(updateQueueStatusApiCallAsyncTask);
    }*/

    /*private void sendToExamined(Queue queue, UpdateQueueInterface delegate) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        UpdateQueueStatusModelNew updateQueueStatusModel = new UpdateQueueStatusModelNew();
        updateQueueStatusModel.setFromStatus("" + queue.getStatus());
        updateQueueStatusModel.setQueueId(queue.getId());
        updateQueueStatusModel.setHealthcareFirmId(appPreferenceManager.getHospitalId());
        updateQueueStatusModel.setToStatus(AppConstants.EXAMINED);
        updateQueueStatusApiCallAsyncTask = asyncTaskForRequest.getUpdateQueueStatusAsyncTask(updateQueueStatusModel, queue.getPatient().getId());
        updateQueueStatusApiCallAsyncTask.setApiCallAsyncTaskDelegate(new SendToDoctorApiCallAsyncTaskDelegate(queue, delegate));
        updateQueueStatusApiCallAsyncTask.execute(updateQueueStatusApiCallAsyncTask);
    }*/

   /* private class SendToDoctorApiCallAsyncTaskDelegate implements ApiCallAsyncTaskDelegate {

        private Queue queue;
        UpdateQueueInterface delegate;

        SendToDoctorApiCallAsyncTaskDelegate(Queue queue, UpdateQueueInterface delegate) {
            this.queue = queue;
            this.delegate = delegate;

        }

        @Override
        public void apiCallResult(String json, int statusCode) {

            ResponseParser responseParser = new ResponseParser(activity);
            PatientQueueModelNew successResponseModel = responseParser.getPatientQueueResponseModelNew(json, statusCode);
            if (successResponseModel != null && successResponseModel.getStatusCode() == 200 && successResponseModel != null
                    && successResponseModel.getMessage() != null && successResponseModel.getQueue() != null){

                delegate.deleteRow(successResponseModel.getQueue());
                Toast.makeText(activity, successResponseModel.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public void onApiCancelled() {

        }

    }*/

 /*   private class SendToNoShowApiCallAsyncTaskDelegate implements ApiCallAsyncTaskDelegate {

        private Queue queue;
        private UpdateQueueInterface delegate;


        SendToNoShowApiCallAsyncTaskDelegate(Queue queue, UpdateQueueInterface delegate) {
            this.queue = queue;
            this.delegate = delegate;

        }

        @Override
        public void apiCallResult(String json, int statusCode) {

            ResponseParser responseParser = new ResponseParser(activity);
            PatientQueueModelNew successResponseModel = responseParser.getPatientQueueResponseModelNew(json, statusCode);
            if (successResponseModel != null && successResponseModel.getStatusCode() == 200 && successResponseModel != null
                    && successResponseModel.getMessage() != null && successResponseModel.getQueue() != null){

                delegate.deleteRow(successResponseModel.getQueue());
                Toast.makeText(activity, successResponseModel.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public void onApiCancelled() {

        }

    }*/

    /*private class DepartmentAndDoctorListAsyncTaskDelegate implements ApiCallAsyncTaskDelegate {

        Queue queue;
        UpdateQueueInterface delegate;

        DepartmentAndDoctorListAsyncTaskDelegate(Queue queue, UpdateQueueInterface delegate) {
            this.queue = queue;
            this.delegate = delegate;


        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            ResponseParser responseParser = new ResponseParser(activity);
            DoctorDepartmentListResponseModel departmentDoctorListResponseModel = responseParser.getDepartmentDoctorListResponseModel(json, statusCode);
            if (departmentDoctorListResponseModel == null){
                return;
            }

            if (departmentDoctorListResponseModel.getDeptDoctorList().getUsersList() == null){
                return;
            }

            if (departmentDoctorListResponseModel.getDeptDoctorList() != null){
                if (departmentDoctorListResponseModel.getDeptDoctorList().getUserHealthCareFirmMapList() != null){
                    for (UserHealthCareFirmMap userHealthCareFirmMap : departmentDoctorListResponseModel.getDeptDoctorList().getUserHealthCareFirmMapList()){
                        UserHealthCareFirmMapDao userHealthCareFirmMapDao = new UserHealthCareFirmMapDao(dhbDao.getDb());
                        userHealthCareFirmMapDao.insertOrUpdateUserHealthcareFirmMapAfterApiCall(userHealthCareFirmMap);
                    }
                }

                if (departmentDoctorListResponseModel.getDeptDoctorList().getHealthCareFirmList() != null){
                    for (HealthCareFirm healthCareFirm : departmentDoctorListResponseModel.getDeptDoctorList().getHealthCareFirmList()){
                        HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
                        healthCareFirmDao.insertOrUpdateHealthCareFirmAfterApiCall(healthCareFirm);
                    }
                }
                if (departmentDoctorListResponseModel.getDeptDoctorList().getDepartmentList() != null){
                    for (Department department : departmentDoctorListResponseModel.getDeptDoctorList().getDepartmentList()){
                        DepartmentDao departmentDao = new DepartmentDao(dhbDao.getDb());
                        departmentDao.insertOrUpdateDepartmentAfterApiCall(department);
                    }
                }
                if (departmentDoctorListResponseModel.getDeptDoctorList().getHealthCareFirmDepartmentMapList() != null){
                    for (HealthCareFirmDepartmentMap healthCareFirmDepartmentMap : departmentDoctorListResponseModel.getDeptDoctorList().getHealthCareFirmDepartmentMapList()){
                        HealthCareFirmDepartmentMapDao healthCareFirmDepartmentMapDao = new HealthCareFirmDepartmentMapDao(dhbDao.getDb());
                        healthCareFirmDepartmentMapDao.insertOrUpdateHealthcareFirmDepartmentMapAfterApiCall(healthCareFirmDepartmentMap);
                    }
                }


                if (departmentDoctorListResponseModel.getDeptDoctorList().getUsersList() != null){
                    for (Users users : departmentDoctorListResponseModel.getDeptDoctorList().getUsersList()){
                        UsersDao usersDao = new UsersDao(dhbDao.getDb());
                        usersDao.insertOrUpdateUsersAfterApiCall(users);
                    }
                }

                if (departmentDoctorListResponseModel.getDeptDoctorList().getDoctorReceptionMapList() != null){
                    for (DoctorReceptionMap doctorReceptionMap : departmentDoctorListResponseModel.getDeptDoctorList().getDoctorReceptionMapList()){
                        DoctorReceptionMapDao doctorReceptionMapDao = new DoctorReceptionMapDao(dhbDao.getDb());
                        doctorReceptionMapDao.insertOrUpdatedoctorReceiptionMapAfterApiCall(doctorReceptionMap);
                    }
                }

                if (departmentDoctorListResponseModel.getDeptDoctorList().getDoctorTimingList() != null){
                    for (DoctorTiming doctorTiming : departmentDoctorListResponseModel.getDeptDoctorList().getDoctorTimingList()){
                        DoctorTimingDao doctorTimingDao = new DoctorTimingDao(dhbDao.getDb());
                        doctorTimingDao.insertOrUpdateDoctorTimingAfterApiCall(doctorTiming);
                    }
                }
                if (departmentDoctorListResponseModel.getDeptDoctorList().getDocuments() != null){
                    for (Document document : departmentDoctorListResponseModel.getDeptDoctorList().getDocuments()){
                        DocumentDao documentDao = new DocumentDao(dhbDao.getDb());
                        documentDao.insertOrUpdateDocumentAfterApiCall(document, true);
                    }
                }
            }


            if (departmentDoctorListResponseModel.getDeptDoctorList().getDepartmentList() != null
                    && departmentDoctorListResponseModel.getDeptDoctorList().getUsersList() != null){

                DepartmentDoctorListSelectorDialog departmentListSelectorDialog = new DepartmentDoctorListSelectorDialog(activity, getDoctorAndDepartmentList(departmentDoctorListResponseModel), new DefaultSelectorDialogResult() {
                    @Override
                    public void setSelector(String receiverId, String stateCode) {

                        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                        UpdateQueueStatusModelNew updateQueueStatusModel = new UpdateQueueStatusModelNew();
                        updateQueueStatusModel.setQueueId(queue.getId());
                        updateQueueStatusModel.setReceiverId(receiverId);
                        updateQueueStatusModel.setHealthcareFirmId(appPreferenceManager.getHospitalId());
                        updateQueueStatusModel.setFromStatus("" + queue.getStatus());
                        if (stateCode != null && stateCode.equalsIgnoreCase("isDoctor")){
                            updateQueueStatusModel.setToStatus(AppConstants.DOCTOR_PRESENT);
                        } else {
                            updateQueueStatusModel.setToStatus(AppConstants.DEPARTMENT_PRESENT);
                        }

                        updateQueueStatusApiCallAsyncTask = asyncTaskForRequest.getUpdateQueueStatusAsyncTask(updateQueueStatusModel, queue.getPatient().getId());
                        updateQueueStatusApiCallAsyncTask.setApiCallAsyncTaskDelegate(new SendToOtherUserApiCallAsyncTaskDelegate(queue, delegate));
                        updateQueueStatusApiCallAsyncTask.execute(updateQueueStatusApiCallAsyncTask);
                    }
                }, R.style.Theme_Transparent, DOCTOR_LIST);

                departmentListSelectorDialog.show();
            }
        }

        @Override
        public void onApiCancelled() {

        }

    }*/

   /* private class SendToOtherUserApiCallAsyncTaskDelegate implements ApiCallAsyncTaskDelegate {

        private Queue queue;
        UpdateQueueInterface delegate;

        public SendToOtherUserApiCallAsyncTaskDelegate(Queue queue, UpdateQueueInterface delegate) {
            this.queue = queue;
            this.delegate = delegate;
        }

        @Override
        public void apiCallResult(String json, int statusCode) {

            ResponseParser responseParser = new ResponseParser(activity);
            PatientQueueModelNew successResponseModel = responseParser.getPatientQueueResponseModelNew(json, statusCode);
            if (successResponseModel == null){
                return;
            }
            if (successResponseModel != null && successResponseModel.getStatusCode() == 200 && successResponseModel != null
                    && successResponseModel.getMessage() != null && successResponseModel.getQueue() != null){

                delegate.deleteRow(successResponseModel.getQueue());
                Toast.makeText(activity, successResponseModel.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public void onApiCancelled() {

        }

    }*/

   /* private ArrayList<Department> getDoctorAndDepartmentList(DoctorDepartmentListResponseModel departmentDoctorListResponseModel) {
        ArrayList<Department> doctorDepartmentListModel = new ArrayList<>();

        if (departmentDoctorListResponseModel.getDeptDoctorList().getUsersList() != null && departmentDoctorListResponseModel.getDeptDoctorList().getUsersList().size() > 0){

            UsersDao usersDao = new UsersDao(dhbDao.getDb());
            ArrayList<Users> doctors = usersDao.getAllDoctorsForReception(appPreferenceManager.getUserId(), appPreferenceManager.getHospitalId());

            for (Users users : doctors){
                Department department = new Department();
                department.setId(users.getId());
                department.setName("Dr. " + users.getFirstName() + " " + users.getLastName());

                DocumentDao documentDao = new DocumentDao(dhbDao.getDb());
                Document document = null;
                if (users.getInfo() != null && users.getInfo().getProfileImageThumbnail() != null){
                    document = documentDao.getDocumentWithId(users.getInfo().getProfileImageThumbnail());
                }
                String imageUrl = null;
                if (document != null && document.getFileLocalPath() != null && !document.getFileLocalPath().isEmpty()){
                    imageUrl = document.getFileLocalPath();

                } else if (document != null && document.getFileThumb() != null && !document.getFileThumb().isEmpty()){
                    imageUrl = document.getFileThumb();
                }
                if (imageUrl != null){
                    department.setImgUrl(imageUrl);
                }

                department.setIsDepartment(false);
                department.setSelected(true);
                doctorDepartmentListModel.add(department);
            }

        }

        return doctorDepartmentListModel;
    }*/

    private void getPresentPatientQueue() throws JSONException {

        /*if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
            setUpdateQueueStatusApiCall(AppConstants.RECEPTION_PRESENT);
        } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
            setUpdateQueueStatusApiCall(AppConstants.DOCTOR_PRESENT);
        }*/

    }

    private void getExaminedPatientQueue() throws JSONException {

      //  setUpdateQueueStatusApiCall(AppConstants.EXAMINED);

    }

    /*private class ExaminedPatientQueueApiCallDelegate implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) {

            ResponseParser responseParser = new ResponseParser(activity);
            ArrayList<Queue> queue = responseParser.getPatientQueueResponseModel(json, statusCode);
            if (queue == null){
                if (swipeViewSecondTab != null){

                    swipeViewSecondTab.setRefreshing(false);
                }
                isExaminedQueueInProgress = false;

                return;
            }

            if (queue.size() != 0){
                for (int i = 0; i < queue.size(); i++){
                    QueueDao queueDao = new QueueDao(dhbDao.getDb(), appPreferenceManager);

                    PatientsDao patientsDao = new PatientsDao(dhbDao.getDb());
                    Patients patientModel = patientsDao.getPatientFromID(queue.get(i).getPatient().getId());
                    if (patientModel != null && patientModel.getInfo() != null && patientModel.getInfo().getBloodGroup() != null
                            && queue != null && queue.get(i) != null && queue.get(i).getPatient() != null
                            && queue.get(i).getPatient().getInfo() != null){
                        if (queue.get(i).getPatient().getInfo().getBloodGroup() != null
                                && patientModel.getInfo().getBloodGroup().equals(queue.get(i).getPatient().getInfo().getBloodGroup())){
                            patientsDao.insertOrUpdatePatientAfterApiCall(queue.get(i).getPatient(), true, false);
                        } else {
                            queue.get(i).getPatient().getInfo().setBloodGroup(patientModel.getInfo().getBloodGroup());
                            patientsDao.insertOrUpdatePatientAfterApiCall(queue.get(i).getPatient(), false, false);
                        }

                    } else {
                        patientsDao.insertOrUpdatePatientAfterApiCall(queue.get(i).getPatient(), true, false);
                    }

                    if (queue.get(i).getDocuments() != null){
                        DocumentDao documentDao = new DocumentDao(dhbDao.getDb());
                        documentDao.insertOrUpdateDocumentAfterApiCall(queue.get(i).getDocuments(), true);
                    }

                    if (queue.get(i).getPatientHealthCareFirmMap() != null){

                        PatientHealthCareFirmMap patientHealthcareFirmMap = queue.get(i).getPatientHealthCareFirmMap();
                        patientHealthcareFirmMap.setSyncStatus(AbstractApiModel.STATUS_SYNCED);
                        patientHealthcareFirmMap.setSyncAction(AppConstants.INSERT);

                        if (patientHealthcareFirmMap != null){
                            PatientHealthCareFirmMapDao patientHealthcareFirmMapDao = new PatientHealthCareFirmMapDao(dhbDao.getDb());
                            patientHealthcareFirmMapDao.insertOrUpdatePatientHealthcareFirmMapAfterApiCall(patientHealthcareFirmMap);
                        }
                    }

                    Queue currentQueueModel = queueDao.getQueueModelWithQueueId(queue.get(i).getId());
                    if (currentQueueModel == null || (currentQueueModel != null && currentQueueModel.getSyncStatus().equals(AppConstants.STATUS_SYNCED))){
                        if (currentQueueModel != null && currentQueueModel.getSenderComments() != null){
                            if (currentQueueModel.getSenderComments().equals(queue.get(i).getSenderComments())){
                                queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), true);
                            } else {
                                if (!currentQueueModel.getSenderComments().isEmpty()){
                                    queue.get(i).setSenderComments(currentQueueModel.getSenderComments());
                                }
                                queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), false);
                            }
                        } else {
                            queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), true);
                        }
                    } else {
                        if (currentQueueModel.getUpdatedAt() < queue.get(i).getUpdatedAt()){
                            queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), true);
                        }
                    }
                }

                if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                    SetExaminedQueueAsyncTask setExaminedQueueAsyncTask = new SetExaminedQueueAsyncTask(AppConstants.EXAMINED);
                    setExaminedQueueAsyncTask.execute();
                } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                    SetExaminedQueueAsyncTask setExaminedQueueAsyncTask = new SetExaminedQueueAsyncTask(AppConstants.EXAMINED);
                    setExaminedQueueAsyncTask.execute();
                }
            }
            isExaminedQueueInProgress = false;
        }

        @Override
        public void onApiCancelled() {
            if (swipeViewSecondTab != null){
                swipeViewSecondTab.setRefreshing(false);
            }
            isExaminedQueueInProgress = false;
        }

    }*/

    /*
     * Content Observer*/
    private class PatientQueueContentObserver extends ContentObserver {

        public PatientQueueContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);

        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {

            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){

                setTabContent(AppConstants.RECEPTION_PRESENT);
                setTabContent(AppConstants.EXAMINED);

            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){

                setTabContent(AppConstants.DOCTOR_PRESENT);
                setTabContent(AppConstants.EXAMINED);
            }


        }

    }

    public void setTabContent(String queueType) {

        if (queueType != null && !queueType.trim().isEmpty()){

            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                if (queueType.equals(AppConstants.RECEPTION_PRESENT)){
                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.RECEPTION_PRESENT);
                    setPresentQueueAsyncTask.execute();
                } else if (queueType.equals(AppConstants.EXAMINED)){
                    SetExaminedQueueAsyncTask setExaminedQueueAsyncTask = new SetExaminedQueueAsyncTask(AppConstants.EXAMINED);
                    setExaminedQueueAsyncTask.execute();
                }
            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                if (queueType.equals(AppConstants.DOCTOR_PRESENT)){
                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.DOCTOR_PRESENT);
                    setPresentQueueAsyncTask.execute();
                } else if (queueType.equals(AppConstants.EXAMINED)){
                    SetExaminedQueueAsyncTask setExaminedQueueAsyncTask = new SetExaminedQueueAsyncTask(AppConstants.EXAMINED);
                    setExaminedQueueAsyncTask.execute();
                }
            }
        }

    }

    public void setPresentQueue(String queueType) {

        QueueDao queueDao = new QueueDao(dhbDao.getDb(), appPreferenceManager);
        List<Queue> queues = new ArrayList<>();

        if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
            queues = queueDao.getPatientFromQueue(appPreferenceManager.getUserId(), AppConstants.RECEPTION_PRESENT);
        } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
            queues = queueDao.getPatientFromQueue(appPreferenceManager.getUserId(), AppConstants.DOCTOR_PRESENT);
        }

//		patientPresentQueueModelList.clear();
//		patientPresentQueueModelList.addAll(queues);
//		patientPresentQueueRecycleAdapter.setPatientInformationArrays(patientPresentQueueModelList);

//		if(isFirstTabQueue) {
//			if (queues != null && queues.size() == 0) {
//				txtNoRecord.setVisibility(View.VISIBLE);
//			} else {
//				txtNoRecord.setVisibility(View.INVISIBLE);
//			}
//		}
//		patientPresentQueueRecycleAdapter.notifyDataSetChanged();
//		updateCount(patientPresentQueueModelList.size(), queueType);

        activity.runOnUiThread(new UiRunnableForPresentQueue(queues, queueType));

//		if (touchListener.existPendingDismisses()){
//			touchListener.undoPendingDismiss(SwipeToDismissTouchListener.mAnimationTimeReset);
//		}

    }

    private final class UiRunnableForPresentQueue implements Runnable {
        private final List<Queue> queues;
        private final String queueType;

        protected UiRunnableForPresentQueue(List<Queue> queues, String queueType) {
            this.queues = queues;
            this.queueType = queueType;
        }

        @Override
        public void run() {

            if (touchListener.existPendingDismisses()){
                touchListener.undoPendingDismiss(SwipeToDismissTouchListener.mAnimationTimeReset);
            }

            if (isFirstTabQueue){
                if (queues != null && queues.size() == 0){
                    txtNoRecordTabOne.setVisibility(View.VISIBLE);
                } else {
                    txtNoRecordTabOne.setVisibility(View.INVISIBLE);
                }
            }

            patientPresentQueueModelList.clear();
            patientPresentQueueModelList.addAll(queues);
            patientPresentQueueRecycleAdapter.setPatientInformationArrays(patientPresentQueueModelList);

            patientPresentQueueRecycleAdapter.notifyDataSetChanged();
            updateCount(patientPresentQueueModelList.size(), queueType);

            swipeViewFirstTab.setRefreshing(false);

        }

    }

    public void setExaminedQueue(String queueType) {

        QueueDao queueDao = new QueueDao(dhbDao.getDb(), appPreferenceManager);
        List<Queue> queues = new ArrayList<>();
        queues = queueDao.getExaminedPatientFromQueue(appPreferenceManager.getUserId());

//		patientExaminedQueueModelList.clear();
//		patientExaminedQueueModelList.addAll(queues);
//		patientExaminedQueueRecycleAdapter.setPatientInformationArrays(patientExaminedQueueModelList);

//		if(isSecondTabQueue) {
//			if (queues != null && queues.size() == 0) {
//				txtNoRecord.setVisibility(View.VISIBLE);
//			} else {
//				txtNoRecord.setVisibility(View.INVISIBLE);
//			}
//		}
//		patientExaminedQueueRecycleAdapter.notifyDataSetChanged();
//		updateCount(patientExaminedQueueModelList.size(), queueType);

        activity.runOnUiThread(new UiRunnableForExaminedQueue(queues, queueType));

//		if (touchListener.existPendingDismisses()){
//			touchListener.undoPendingDismiss(SwipeToDismissTouchListener.mAnimationTimeReset);
//		}
    }

    private final class UiRunnableForExaminedQueue implements Runnable {
        private final List<Queue> queues;
        private final String queueType;

        protected UiRunnableForExaminedQueue(List<Queue> queues, String queueType) {
            this.queues = queues;
            this.queueType = queueType;
        }

        @Override
        public void run() {

            if (touchListener.existPendingDismisses()){
                touchListener.undoPendingDismiss(SwipeToDismissTouchListener.mAnimationTimeReset);
            }

            if (isSecondTabQueue){
                if (queues != null && queues.size() == 0){
                    txtNoRecordTabTwo.setVisibility(View.VISIBLE);
                } else {
                    txtNoRecordTabTwo.setVisibility(View.INVISIBLE);
                }
            }

            patientExaminedQueueModelList.clear();
            patientExaminedQueueModelList.addAll(queues);
            patientExaminedQueueRecycleAdapter.setPatientInformationArrays(patientExaminedQueueModelList);

            patientExaminedQueueRecycleAdapter.notifyDataSetChanged();
            updateCount(patientExaminedQueueModelList.size(), queueType);

            swipeViewSecondTab.setRefreshing(false);

        }

    }

    private class SetPresentQueueAsyncTask extends AsyncTask<Void, Void, Void> {

        String queueType;

        public SetPresentQueueAsyncTask(String queueType) {
            this.queueType = queueType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void...params) {
            setPresentQueue(queueType);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    private class SetExaminedQueueAsyncTask extends AsyncTask<Void, Void, Void> {

        String queueType;

        public SetExaminedQueueAsyncTask(String queueType) {
            this.queueType = queueType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void...params) {
            setExaminedQueue(queueType);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    private void updateCount(int size, String queueType) {
        String count = size + "";
        char[] presentCountArray = count.toCharArray();
//		txtNoRecord.setVisibility(View.INVISIBLE);

        if (queueType.equals(AppConstants.RECEPTION_PRESENT) || queueType.equals(AppConstants.DOCTOR_PRESENT)){
            if (presentCountArray.length == 3){
                txtPresentCount1.setText(presentCountArray[0] + "");
                txtPresentCount2.setText(presentCountArray[1] + "");
                txtPresentCount3.setText(presentCountArray[2] + "");
            } else if (presentCountArray.length == 2){
                txtPresentCount1.setText("0");
                txtPresentCount2.setText(presentCountArray[0] + "");
                txtPresentCount3.setText(presentCountArray[1] + "");
            } else if (presentCountArray.length == 1 && size != 0){
                txtPresentCount1.setText("0");
                txtPresentCount2.setText("0");
                txtPresentCount3.setText(presentCountArray[0] + "");
            } else {
                txtPresentCount1.setText("0");
                txtPresentCount2.setText("0");
                txtPresentCount3.setText("0");
                if (isFirstTabQueue){
//					txtNoRecord.setVisibility(View.VISIBLE);
                }
            }
        }
        if (queueType.equals(AppConstants.EXAMINED)){
            if (presentCountArray.length == 3){
                txtExaminedCount1.setText(presentCountArray[0] + "");
                txtExaminedCount2.setText(presentCountArray[1] + "");
                txtExaminedCount3.setText(presentCountArray[2] + "");
            } else if (presentCountArray.length == 2){
                txtExaminedCount1.setText("0");
                txtExaminedCount2.setText(presentCountArray[0] + "");
                txtExaminedCount3.setText(presentCountArray[1] + "");
            } else if (presentCountArray.length == 1 && size != 0){
                txtExaminedCount1.setText("0");
                txtExaminedCount2.setText("0");
                txtExaminedCount3.setText(presentCountArray[0] + "");
            } else {
                txtExaminedCount1.setText("0");
                txtExaminedCount2.setText("0");
                txtExaminedCount3.setText("0");
                if (isSecondTabQueue){
//					txtNoRecord.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String status = "";

        if(isAfterOnCreateView){
            isOkToFireApiForRefreshQueue = true;
        }
        else{
            isOkToFireApiForRefreshQueue = false;
        }

        if (isFirstTabQueue){
//			try {
            if (!isPresentQueueInProgress){
                isPresentQueueInProgress = false;
                rltLayoutPresentQueue.performClick();
//					getPresentPatientQueue();
            }
//			} catch (JSONException e){
//				e.printStackTrace();
//			}
        } else {
//			try {
            if (!isExaminedQueueInProgress){
                isExaminedQueueInProgress = true;
                rltLayoutExaminedQueue.performClick();
//					getExaminedPatientQueue();
            }
//			} catch (JSONException e){
//				e.printStackTrace();
//			}
        }

        isAfterOnCreateView = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (patientQueueContentObserver != null){
            activity.getContentResolver().unregisterContentObserver(patientQueueContentObserver);
            patientQueueContentObserver = null;
        }
        if (dhbDao != null){
            dhbDao.closeDatabase();
        }
    }

    /*private class PatientQueueApiCallDelegate implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            ResponseParser responseParser = new ResponseParser(activity);
            ArrayList<Queue> queue = responseParser.getPatientQueueResponseModel(json, statusCode);
            if (queue == null){
                if (swipeViewFirstTab != null){

                    swipeViewFirstTab.setRefreshing(false);
                }
                isPresentQueueInProgress = false;

                return;
            }

            if (queue.size() != 0){
                for (int i = 0; i < queue.size(); i++){
                    QueueDao queueDao = new QueueDao(dhbDao.getDb(), appPreferenceManager);

                    PatientsDao patientsDao = new PatientsDao(dhbDao.getDb());
                    Patients patientModel = patientsDao.getPatientFromID(queue.get(i).getPatient().getId());
                    if (patientModel != null && patientModel.getInfo() != null && patientModel.getInfo().getBloodGroup() != null
                            && queue != null && queue.get(i) != null && queue.get(i).getPatient() != null
                            && queue.get(i).getPatient().getInfo() != null){
                        if (queue.get(i).getPatient().getInfo().getBloodGroup() != null
                                && patientModel.getInfo().getBloodGroup().equals(queue.get(i).getPatient().getInfo().getBloodGroup())){
                            patientsDao.insertOrUpdatePatientAfterApiCall(queue.get(i).getPatient(), true, false);
                        } else {
                            queue.get(i).getPatient().getInfo().setBloodGroup(patientModel.getInfo().getBloodGroup());
                            patientsDao.insertOrUpdatePatientAfterApiCall(queue.get(i).getPatient(), false, false);
                        }
                    } else {
                        patientsDao.insertOrUpdatePatientAfterApiCall(queue.get(i).getPatient(), true, false);
                    }

                    if (queue.get(i).getDocuments() != null){
                        DocumentDao documentDao = new DocumentDao(dhbDao.getDb());
                        documentDao.insertOrUpdateDocumentAfterApiCall(queue.get(i).getDocuments(), true);
                    }

                    if (queue.get(i).getPatientHealthCareFirmMap() != null){

                        PatientHealthCareFirmMap patientHealthcareFirmMap = new PatientHealthCareFirmMap();
                        patientHealthcareFirmMap = queue.get(i).getPatientHealthCareFirmMap();
                        patientHealthcareFirmMap.setSyncStatus(AbstractApiModel.STATUS_SYNCED);
                        patientHealthcareFirmMap.setSyncAction(AppConstants.INSERT);

                        PatientHealthCareFirmMapDao patientHealthcareFirmMapDao = new PatientHealthCareFirmMapDao(dhbDao.getDb());
                        patientHealthcareFirmMapDao.insertOrUpdatePatientHealthcareFirmMapAfterApiCall(patientHealthcareFirmMap);
                    }

                    Queue currentQueueModel = queueDao.getQueueModelWithQueueId(queue.get(i).getId());
                    if (currentQueueModel == null || (currentQueueModel != null && currentQueueModel.getSyncStatus().equals(AppConstants.STATUS_SYNCED))){
                        if (currentQueueModel != null && currentQueueModel.getSenderComments() != null){
                            if (currentQueueModel.getSenderComments().equalsIgnoreCase(queue.get(i).getSenderComments())){
                                queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), true);
                            } else {
                                if (!currentQueueModel.getSenderComments().isEmpty()){
                                    queue.get(i).setSenderComments(currentQueueModel.getSenderComments());
                                }
                                queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), false);
                            }
                        } else {
                            queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), true);
                        }
                    } else {
                        if (currentQueueModel.getUpdatedAt() < queue.get(i).getUpdatedAt()){
                            queueDao.insertOrUpdateQueueAfterApiCall(queue.get(i), true);
                        }
                    }
                }

                if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.RECEPTION_PRESENT);
                    setPresentQueueAsyncTask.execute();
                } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                    SetPresentQueueAsyncTask setPresentQueueAsyncTask = new SetPresentQueueAsyncTask(AppConstants.DOCTOR_PRESENT);
                    setPresentQueueAsyncTask.execute();
                }
            }

            isPresentQueueInProgress = false;
        }

        @Override
        public void onApiCancelled() {

        }

    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
