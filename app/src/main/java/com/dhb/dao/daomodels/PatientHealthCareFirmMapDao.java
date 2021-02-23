package com.dhb.dao.daomodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.PatientHealthCareFirmMap;
import com.dhb.utils.AppConstants;
import com.dhb.utils.Logger;

import java.util.ArrayList;

public class PatientHealthCareFirmMapDao {
    private SQLiteDatabase db;

    // DB TABLE NAME
    public static String TABLE_NAME = "patient_healthcare_firm_map";

    // DB TABLE COLUMN INFO

    private static final String ID = "id";

    public static final String PATIENT_ID = "patient_id";
    public static final String HEALTHCARE_FIRM_ID = "healthcare_firm_id";
    public static final String OLD_HIN = "old_hin";
    public static final String REF_TAG = "reference_tag";
    public static final String REF_NUMBER = "ref_number";

    private static final String CREATED_AT = "created_at";
    private static final String CREATED_BY = "created_by";
    private static final String UPDATED_AT = "updated_at";
    private static final String UPDATED_BY = "updated_by";

    private static final String RECORD_STATUS = "record_status";
    private static final String SYNC_STATUS = "sync_status";
    private static final String SYNC_ACTION = "sync_action";

    // Constructors
    public PatientHealthCareFirmMapDao(SQLiteDatabase db) {
        this.db = db;
    }

    // get model from cursor
    private PatientHealthCareFirmMap getPatientHealthCareFirmMapModelFromCursor(Cursor cursor) {
        PatientHealthCareFirmMap userHealthcareFirmMap = new PatientHealthCareFirmMap();
        userHealthcareFirmMap.setId(cursor.getString(cursor.getColumnIndex(ID)));

        userHealthcareFirmMap.setPatientId(cursor.getString(cursor.getColumnIndex(PATIENT_ID)));
        userHealthcareFirmMap.setHealthCareFirmId(cursor.getString(cursor.getColumnIndex(HEALTHCARE_FIRM_ID)));
        userHealthcareFirmMap.setOldHin(cursor.getString(cursor.getColumnIndex(OLD_HIN)));
        userHealthcareFirmMap.setRefTag(cursor.getString(cursor.getColumnIndex(REF_TAG)));
        userHealthcareFirmMap.setRefNumber(cursor.getString(cursor.getColumnIndex(REF_NUMBER)));

        userHealthcareFirmMap.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
        userHealthcareFirmMap.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
        userHealthcareFirmMap.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
        userHealthcareFirmMap.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
        userHealthcareFirmMap.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
        userHealthcareFirmMap.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
        userHealthcareFirmMap.setSyncAction(cursor.getString(cursor.getColumnIndex(SYNC_ACTION)));

        return userHealthcareFirmMap;
    }

    public ContentValues getContentValuesFromModel(PatientHealthCareFirmMap userHealthcareFirmMap) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(ID, userHealthcareFirmMap.getId());
        initialValues.put(PATIENT_ID, userHealthcareFirmMap.getPatientId());
        initialValues.put(HEALTHCARE_FIRM_ID, userHealthcareFirmMap.getHealthCareFirmId());
        initialValues.put(REF_TAG, userHealthcareFirmMap.getRefTag());
        initialValues.put(REF_NUMBER, userHealthcareFirmMap.getRefNumber());
        initialValues.put(OLD_HIN, userHealthcareFirmMap.getOldHin());
        initialValues.put(CREATED_AT, userHealthcareFirmMap.getCreatedAt());
        initialValues.put(CREATED_BY, userHealthcareFirmMap.getCreatedBy());
        initialValues.put(UPDATED_AT, userHealthcareFirmMap.getUpdatedAt());
        initialValues.put(UPDATED_BY, userHealthcareFirmMap.getUpdatedBy());
        initialValues.put(RECORD_STATUS, userHealthcareFirmMap.getRecordStatus());
        initialValues.put(SYNC_STATUS, userHealthcareFirmMap.getSyncStatus());
        initialValues.put(SYNC_ACTION, userHealthcareFirmMap.getSyncAction());

        return initialValues;
    }

    public void insertOrUpdatePatientHealthCareMapAfterApiCall(PatientHealthCareFirmMap patientHealthcareFirmMap) {
        ContentValues contentValues = new ContentValues();
        PatientHealthCareFirmMap patientHealthcareFirmMapModel;
        patientHealthcareFirmMapModel = this.getPatientHealthCareFirmMapModelWithQueueId(patientHealthcareFirmMap.getId());
        if (patientHealthcareFirmMapModel == null){
            contentValues = this.getContentValuesFromModel(patientHealthcareFirmMap);
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
            Logger.debug("insertOrUpdatePatientHealthCareMapAfterApiCall : insert : " + insertValue);
        } else {
            contentValues = this.getContentValuesFromModel(patientHealthcareFirmMap);
            contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {patientHealthcareFirmMap.getId()});
            Logger.debug("insertOrUpdatePatientHealthCareMapAfterApiCall : update : " + updateValue);
        }
    }

    public PatientHealthCareFirmMap getPatientHealthCareFirmMapModelWithQueueId(String id) {

        PatientHealthCareFirmMap patientHealthcareFirmMap = null;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            do {
                patientHealthcareFirmMap = getPatientHealthCareFirmMapModelFromCursor(cursor);
            } while ((cursor.moveToNext()));

        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientHealthcareFirmMap;
    }

    // get all PatientHealthCareFirmMap from db
    public ArrayList<PatientHealthCareFirmMap> getAllPatientHealthcareFirmMap() {

        ArrayList<PatientHealthCareFirmMap> userHealthcareFirmMapList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=?";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                PatientHealthCareFirmMap userHealthcareFirmMap = getPatientHealthCareFirmMapModelFromCursor(cursor);
                if (userHealthcareFirmMap != null){
                    userHealthcareFirmMapList.add(userHealthcareFirmMap);
                }

            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return userHealthcareFirmMapList;
    }

    public ArrayList<PatientHealthCareFirmMap> getAllPatientHealthcareFirmMap(String userId) {

        ArrayList<PatientHealthCareFirmMap> userHealthcareFirmMapList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=? AND " + PATIENT_ID + "=?";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS, userId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                PatientHealthCareFirmMap userHealthcareFirmMap = getPatientHealthCareFirmMapModelFromCursor(cursor);
                if (userHealthcareFirmMap != null){
                    userHealthcareFirmMapList.add(userHealthcareFirmMap);
                }

            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return userHealthcareFirmMapList;
    }

    public void insertOrUpdatePatientHealthcareFirmMapAfterApiCall(PatientHealthCareFirmMap userHealthcareFirmMap) {
        ContentValues contentValues = new ContentValues();
        PatientHealthCareFirmMap userHealthcareFirmMapModel;
        userHealthcareFirmMapModel = this.getPatientHealthcareFirmMapWithId(userHealthcareFirmMap.getId());
        if (userHealthcareFirmMapModel == null){
            contentValues = this.getContentValuesFromModel(userHealthcareFirmMap);
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
            Logger.debug("insertOrUpdateHealthCareFirmAfterApiCall : insert : " + insertValue);
        } else {
            contentValues = this.getContentValuesFromModel(userHealthcareFirmMap);
            contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {userHealthcareFirmMap.getId()});
            Logger.debug("insertOrUpdateHealthCareFirmAfterApiCall : update : " + updateValue);
        }
    }

    public PatientHealthCareFirmMap getPatientHealthcareFirmMapWithId(String id) {

        PatientHealthCareFirmMap userHealthcareFirmMapModel = null;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                userHealthcareFirmMapModel = getPatientHealthCareFirmMapModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return userHealthcareFirmMapModel;
    }

    public int getNonSyncedPatientHealthcareFirmMapCount() {
        int count = 0;
        String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=?";
        String[] whereParams = new String[] {AppConstants.STATUS_NON_SYNC};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null){
            count = cursor.getCount();
            cursor.close();
        }

        return count;
    }

    // update if exists or insert PatientHealthCareFirmMap to db

    public void insertPatientHealthcareFirmMap(PatientHealthCareFirmMap userHealthcareFirmMap) {

        ContentValues initialValues = new ContentValues();
        initialValues = this.getContentValuesFromModel(userHealthcareFirmMap);
        initialValues.put(SYNC_ACTION, AppConstants.UPDATE);
        int id = db.update(TABLE_NAME, initialValues, ID + "=?", new String[] {String.valueOf(userHealthcareFirmMap.getId())});

        if (id == 0){
//			initialValues.put(ID, DeviceUtils.getRandomUUID());
//			initialValues.put(CREATED_AT, Calendar.getInstance().getTimeInMillis());
//			initialValues.put(CREATED_BY, userHealthcareFirmMap.getCreatedBy());
            initialValues.put(SYNC_ACTION, AppConstants.INSERT);
            int newId = (int) db.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        }

    }

    public PatientHealthCareFirmMap getPatientHealthCareFirmMapWithUserId(String userId, String healthCareFirmId) {

        PatientHealthCareFirmMap userHealthCareFirmMap = new PatientHealthCareFirmMap();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + HEALTHCARE_FIRM_ID + " = '" + healthCareFirmId + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                userHealthCareFirmMap = getPatientHealthCareFirmMapModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return userHealthCareFirmMap;
    }

    public String[] getRefNoAndOldHinAndRefTagWithUserId(String userId, String healthCareFirmId) {

        String[] arrVal =  {"", "", ""};

        String query = "SELECT " + REF_NUMBER + "," + OLD_HIN + "," + REF_TAG + " FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + HEALTHCARE_FIRM_ID + " = '" + healthCareFirmId + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                if(cursor.getString(cursor.getColumnIndex(REF_NUMBER)) == null){
                    arrVal[0] = "";
                }
                else {
                    arrVal[0] = cursor.getString(cursor.getColumnIndex(REF_NUMBER));
                }
                if(cursor.getString(cursor.getColumnIndex(OLD_HIN)) == null){
                    arrVal[1] = "";
                }
                else {
                    arrVal[1] = cursor.getString(cursor.getColumnIndex(OLD_HIN));
                }
                if(cursor.getString(cursor.getColumnIndex(REF_TAG)) == null){
                    arrVal[2] = "";
                }
                else {
                    arrVal[2] = cursor.getString(cursor.getColumnIndex(REF_TAG));
                }
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return arrVal;
    }

    private PatientHealthCareFirmMap getPatientHealthcareFirmMapModelFromCursorForCount(Cursor cursor) {
        PatientHealthCareFirmMap userHealthcareFirmMap = new PatientHealthCareFirmMap();

        userHealthcareFirmMap.setHealthCareFirmId(cursor.getString(cursor.getColumnIndex(HEALTHCARE_FIRM_ID)));

        return userHealthcareFirmMap;
    }

    public void deletePatientHealthcareFirmMap(String userId) {

        int id = db.delete(TABLE_NAME, PATIENT_ID + "=?", new String[] {String.valueOf(userId)});
        Logger.debug(id + "");
    }

    public String getReferenceNumberByPatientId(String patientId, String healthCareFirmId) {

        String referenceNumber = "";

        String query = "SELECT " + REF_NUMBER + " FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + patientId + "' AND " + HEALTHCARE_FIRM_ID + " = '" + healthCareFirmId + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            do {
                referenceNumber = cursor.getString(cursor.getColumnIndex(REF_NUMBER));
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return referenceNumber;
    }
}
