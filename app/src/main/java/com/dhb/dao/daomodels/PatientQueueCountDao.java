package com.dhb.dao.daomodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.PatientQueueCountModel;
import com.dhb.utils.AppConstants;
import com.dhb.utils.DeviceUtils;
import com.dhb.utils.DateUtils;
import com.dhb.utils.Logger;

import java.util.ArrayList;

public class PatientQueueCountDao {
    private SQLiteDatabase db;

    // DB TABLE NAME

    public static String TABLE_NAME = "patient_queue_count";

    // DB TABLE COLUMN INFO

    private static final String ID = "id";
    private static final String HEALTHCARE_FIRM_ID = "healthcare_firm_id";
    private static final String PATIENT_COUNT_IN_QUEUE = "patient_count_in_queue";
    private static final String TOTAL_PATIENT = "total_patient";
    private static final String CREATED_AT = "created_at";
    private static final String CREATED_BY = "created_by";
    private static final String UPDATED_AT = "updated_at";
    private static final String UPDATED_BY = "updated_by";
    private static final String RECORD_STATUS = "record_status";
    private static final String SYNC_STATUS = "sync_status";
    private static final String SYNC_ACTION = "sync_action";

    public PatientQueueCountDao() {
    }

    public PatientQueueCountDao(SQLiteDatabase db) {
        this.db = db;
    }

    // get model from cursor

    private PatientQueueCountModel getPatientQueueCountModelFromCursor(Cursor cursor) {
        PatientQueueCountModel patientQueueCountModel = new PatientQueueCountModel();
        patientQueueCountModel.setId(cursor.getString(cursor.getColumnIndex(ID)));
        patientQueueCountModel.setHealthcareFirmId(cursor.getString(cursor.getColumnIndex(HEALTHCARE_FIRM_ID)));
        patientQueueCountModel.setPatientCountQueue(cursor.getString(cursor.getColumnIndex(PATIENT_COUNT_IN_QUEUE)));
        patientQueueCountModel.setTotalPatient(cursor.getString(cursor.getColumnIndex(TOTAL_PATIENT)));
        patientQueueCountModel.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
        patientQueueCountModel.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
        patientQueueCountModel.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
        patientQueueCountModel.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
        patientQueueCountModel.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
        patientQueueCountModel.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
        patientQueueCountModel.setSyncAction((cursor.getString(cursor.getColumnIndex(SYNC_ACTION))));

        return patientQueueCountModel;
    }

    public ContentValues getContentValuesFromModel(PatientQueueCountModel patientQueueCountModel) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, patientQueueCountModel.getId());
        initialValues.put(PATIENT_COUNT_IN_QUEUE, patientQueueCountModel.getPatientCountQueue());
        initialValues.put(HEALTHCARE_FIRM_ID, patientQueueCountModel.getHealthcareFirmId());
        initialValues.put(TOTAL_PATIENT, patientQueueCountModel.getTotalPatient());
        initialValues.put(CREATED_AT, patientQueueCountModel.getCreatedAt());
        initialValues.put(CREATED_BY, patientQueueCountModel.getCreatedBy());
        initialValues.put(UPDATED_AT, DateUtils.getCurrentTimeInDefault());
        initialValues.put(UPDATED_BY, patientQueueCountModel.getUpdatedBy());
        initialValues.put(RECORD_STATUS, AppConstants.ACTIVE_RECORD_STATUS);
        initialValues.put(SYNC_STATUS, patientQueueCountModel.getSyncStatus());
        initialValues.put(SYNC_ACTION, patientQueueCountModel.getSyncAction());
        return initialValues;
    }

    // get all PatientQueueCountModel from db

    public ArrayList<PatientQueueCountModel> getAllPatientQueueCountModel() {

        ArrayList<PatientQueueCountModel> patientQueueCountModels = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=?";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                PatientQueueCountModel patientQueueCountModel = getPatientQueueCountModelFromCursor(cursor);
                if (patientQueueCountModel != null){
                    patientQueueCountModels.add(patientQueueCountModel);
                }

            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return patientQueueCountModels;
    }

    public void insertOrUpdatePatientQueueCountModelAfterApiCall(PatientQueueCountModel patientQueueCountModel) {
        ContentValues contentValues = new ContentValues();
        PatientQueueCountModel patientQueueCountModelFromDB;
        patientQueueCountModelFromDB = this.getpatientQueueCountModelWithId(patientQueueCountModel.getHealthcareFirmId());

        if (patientQueueCountModelFromDB == null){
            contentValues = this.getContentValuesFromModel(patientQueueCountModel);
            contentValues.put(ID, DeviceUtils.getRandomUUID());
            contentValues.put(CREATED_AT, DateUtils.getCurrentTimeInDefault());
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
        } else {
            contentValues = this.getContentValuesFromModel(patientQueueCountModel);
            contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int updateValue = db.update(TABLE_NAME, contentValues, HEALTHCARE_FIRM_ID + "=?", new String[] {patientQueueCountModelFromDB.getHealthcareFirmId()});
        }
    }

    public PatientQueueCountModel getpatientQueueCountModelWithId(String id) {

        PatientQueueCountModel patientQueueCountModel = null;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + HEALTHCARE_FIRM_ID + " = '" + id + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                patientQueueCountModel = getPatientQueueCountModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientQueueCountModel;
    }

    public void removeCountData() {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
