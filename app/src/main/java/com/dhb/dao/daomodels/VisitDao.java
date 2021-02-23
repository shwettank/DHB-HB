package com.dhb.dao.daomodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.Visit;
import com.dhb.utils.AppConstants;
import com.dhb.utils.DateUtils;
import com.dhb.utils.Logger;
import com.google.gson.Gson;

import java.util.ArrayList;

public class VisitDao {

    private SQLiteDatabase db;


    // DB TABLE NAME

    public static String TABLE_NAME = "visit";


    // DB TABLE COLUMN INFO

    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String DOCTOR_ID = "doctor_id";
    public static final String QUEUE_ID = "queue_id";
    public static final String HEALTHCARE_FIRM_ID = "healthcare_firm_id";
    public static final String TITLE = "title";
    public static final String VISIT_DATE = "visit_date";
    public static final String INSPECTION_TYPE = "inspection_type";
    public static final String IS_HOLD = "is_hold";
    public static final String PARENT_ID = "parent_id";
    public static final String RECORD_STATUS = "record_status";
    public static final String CREATED_AT = "created_at";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_AT = "updated_at";
    public static final String UPDATED_BY = "updated_by";
    public static final String SYNC_STATUS = "sync_status";
    public static final String SYNC_ACTION = "sync_action";

    private int batchCount = 5;


    private Gson gson;

    // Constructors

    public VisitDao() {
        this.gson = new Gson();
    }

    public VisitDao(SQLiteDatabase db) {
        this.db = db;
        this.gson = new Gson();
    }

    // get all visits from db


    public ArrayList<Visit> getAllVisits(String doctorId, String patientId) {

        ArrayList<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + DOCTOR_ID + "=? AND " + PATIENT_ID + "=? AND " + RECORD_STATUS + "=?"
                + " ORDER BY " + UPDATED_AT + " DESC";
        String[] whereParams = new String[] {doctorId, patientId, AppConstants.ACTIVE_RECORD_STATUS};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Visit visit = getVisitModelFromCursor(cursor);
                if (visit != null){
                    visits.add(visit);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return visits;
    }

    public ArrayList<String> getNonSyncedVisitIds(String userId) {
        String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=? AND " + DOCTOR_ID + "=?";
        String[] whereParams = new String[] {AppConstants.STATUS_NON_SYNC, userId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        ArrayList<String> ids = new ArrayList<>();
        if (cursor != null && (cursor.moveToFirst())){
            do {
                String id = cursor.getString(cursor.getColumnIndex(ID));
                if (id != null && !id.isEmpty()){
                    ids.add(id);
                }
            } while ((cursor.moveToNext()));
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return ids;
    }

    public Visit getVisitModelWithQueueId(String visitId) {

        Visit visit = null;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + visitId + "'";
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            do {
                visit = getVisitModelFromCursor(cursor);
            } while ((cursor.moveToNext()));
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return visit;
    }

    public String getPatientIdWithVisitId(String visitId) {

        String query = "SELECT " + PATIENT_ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + visitId + "'";
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            do {
                String patientId = cursor.getString(cursor.getColumnIndex(PATIENT_ID));
                cursor.close();
                return patientId;
            } while ((cursor.moveToNext()));
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return null;
    }

    public ArrayList<Visit> getAllMainVisits(String doctorId, String patientId, String healthCareFirmId) {

        ArrayList<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + DOCTOR_ID + "=? AND " + PATIENT_ID + "=? AND "
                + HEALTHCARE_FIRM_ID + "=? AND "
                + INSPECTION_TYPE + "=? AND " + RECORD_STATUS + "=?"
                + " ORDER BY " + CREATED_AT + " ASC";
        String[] whereParams = new String[] {
                doctorId, patientId, healthCareFirmId,
                "" + AppConstants.INSPECTION_TYPE_VISIT, AppConstants.ACTIVE_RECORD_STATUS
        };
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Visit visit = getVisitModelFromCursor(cursor);
                if (visit != null){
                    visits.add(visit);
                }

            } while ((cursor.moveToNext()));

        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return visits;
    }

    public ArrayList<Visit> getAllFollowupVisits(String doctorId, String patientId, String parentId, String healthCareFirmId) {

        ArrayList<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + DOCTOR_ID + "=? AND " + PATIENT_ID + "=? AND "
                + PARENT_ID + "=? AND " + HEALTHCARE_FIRM_ID + "=? AND " + INSPECTION_TYPE + "=? AND "
                + RECORD_STATUS + "=?" + " ORDER BY " + CREATED_AT + " ASC";
        String[] whereParams = new String[] {
                doctorId, patientId, parentId, healthCareFirmId,
                "" + AppConstants.INSPECTION_TYPE_FOLLOWUP, AppConstants.ACTIVE_RECORD_STATUS
        };
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Visit visit = getVisitModelFromCursor(cursor);
                if (visit != null){
                    visits.add(visit);
                }

            } while ((cursor.moveToNext()));

        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return visits;
    }

    // update if exists or insert patients to db

    public void insertVisit(Visit visit) {

//		ContentValues initialValues;
//		initialValues = this.getContentValuesFromModel(visit);
//		initialValues.put(SYNC_ACTION, "I");
//		int id = (int) db.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
//		if (id == -1){
//			initialValues.put(SYNC_ACTION, "U");
//			db.update(TABLE_NAME, initialValues, "_id=?", new String[] {String.valueOf(visit.getId())});
//		}

        ContentValues contentValues = getContentValuesFromModel(visit);
        contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
        int id = db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {String.valueOf(visit.getId())});

        if (id == 0){
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            int newId = (int) db.insert(TABLE_NAME, null, contentValues);
//			int newId = (int) db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Logger.debug("" + newId);
        }

    }

    public void insertOrUpdateVisitAfterApiCall(Visit visit) {
        ContentValues contentValues = new ContentValues();
        Visit visitModel;
        visitModel = this.getVisitModelWithQueueId(visit.getId());
        if (visitModel == null){
            contentValues = this.getContentValuesFromModel(visit);
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
            Logger.debug("insertOrUpdateVisitAfterApiCall : insert : " + insertValue);
        } else {
            contentValues = this.getContentValuesFromModel(visit);
            contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {visit.getId()});
            Logger.debug("insertOrUpdateVisitAfterApiCall : update : " + updateValue);
        }
    }

    public ArrayList<Visit> getLocalVisitsBatch(String userId) {

        ArrayList<Visit> visitList = new ArrayList<>();

        String query = "SELECT T1.* FROM " + TABLE_NAME + " T1"
                + " JOIN " + PatientsDao.TABLE_NAME + " T2"
                + " ON " + "T1." + VisitDao.PATIENT_ID + "=" + "T2." + PatientsDao.ID
                + " WHERE T1." + VisitDao.SYNC_STATUS + "='" + AppConstants.STATUS_NON_SYNC + "'"
                + " AND T2." + PatientsDao.SYNC_STATUS + "='" + AppConstants.STATUS_SYNCED + "'"
                + " AND T1." + VisitDao.UPDATED_BY + "='" + userId + "'"
                + " LIMIT " + batchCount;

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Visit visit = getVisitModelFromCursor(cursor);
                if (visit != null){
                    visitList.add(visit);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return visitList;
    }

    public ArrayList<Visit> getLocalVisitsFromVisitId(String userId, String visitId) {

        ArrayList<Visit> visitList = new ArrayList<>();

        String query = "SELECT T1.* FROM " + TABLE_NAME + " T1"
                + " JOIN " + PatientsDao.TABLE_NAME + " T2"
                + " ON " + "T1." + VisitDao.PATIENT_ID + "=" + "T2." + PatientsDao.ID
                + " WHERE T1." + VisitDao.SYNC_STATUS + "='" + AppConstants.STATUS_NON_SYNC + "'"
                + " AND T1." + ID + "='" + visitId + "'"
                + " AND T2." + PatientsDao.SYNC_STATUS + "='" + AppConstants.STATUS_SYNCED + "'"
                + " AND T1." + VisitDao.UPDATED_BY + "='" + userId + "'"
                + " LIMIT " + batchCount;

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Visit visit = getVisitModelFromCursor(cursor);
                if (visit != null){
                    visitList.add(visit);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return visitList;
    }

    // get model from cursor

    private Visit getVisitModelFromCursor(Cursor cursor) {
        Visit visit = new Visit();
        visit.setId(cursor.getString(cursor.getColumnIndex(ID)));
        visit.setPatientId(cursor.getString(cursor.getColumnIndex(PATIENT_ID)));
        visit.setDoctorId(cursor.getString(cursor.getColumnIndex(DOCTOR_ID)));
        visit.setQueueId(cursor.getString(cursor.getColumnIndex(QUEUE_ID)));
        visit.setHealthcareFirmId(cursor.getString(cursor.getColumnIndex(HEALTHCARE_FIRM_ID)));
        visit.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        visit.setVisitDate(cursor.getLong(cursor.getColumnIndex(VISIT_DATE)));
        visit.setInspectionType(cursor.getInt(cursor.getColumnIndex(INSPECTION_TYPE)));
        visit.setIsHold(cursor.getInt(cursor.getColumnIndex(IS_HOLD)));
        visit.setParentId(cursor.getString(cursor.getColumnIndex(PARENT_ID)));
        visit.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
        visit.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
        visit.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
        visit.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
        visit.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
        visit.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
        visit.setSyncAction(cursor.getString(cursor.getColumnIndex(SYNC_ACTION)));

        return visit;
    }

    public ContentValues getContentValuesFromModel(Visit visit) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, visit.getId());
        initialValues.put(PATIENT_ID, visit.getPatientId());
        initialValues.put(DOCTOR_ID, visit.getDoctorId());
        initialValues.put(QUEUE_ID, visit.getQueueId());
        initialValues.put(HEALTHCARE_FIRM_ID, visit.getHealthcareFirmId());
        initialValues.put(TITLE, visit.getTitle());
        initialValues.put(VISIT_DATE, visit.getVisitDate());
        initialValues.put(INSPECTION_TYPE, visit.getInspectionType());
        initialValues.put(IS_HOLD, visit.getIsHold());
        initialValues.put(PARENT_ID, visit.getParentId());
        initialValues.put(RECORD_STATUS, visit.getRecordStatus());
        initialValues.put(CREATED_AT, visit.getCreatedAt());
        initialValues.put(CREATED_BY, visit.getCreatedBy());
        initialValues.put(UPDATED_AT, visit.getUpdatedAt());
        initialValues.put(UPDATED_BY, visit.getUpdatedBy());
        initialValues.put(SYNC_STATUS, visit.getSyncStatus());
        initialValues.put(SYNC_ACTION, visit.getSyncAction());

        return initialValues;
    }

    public void updatePatientId(String id, String changedId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PATIENT_ID, changedId);
        db.update(TABLE_NAME, contentValues, PATIENT_ID + "=?", new String[] {id});
    }

    public void updateValueOfUpdatedAt(String visitId, long updatedAt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_AT, updatedAt);
        contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
        contentValues.put(SYNC_STATUS, AppConstants.NO);
        db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {visitId});
    }

    public void updateValueOfIsHold(String visitId, int isHoldStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_HOLD, isHoldStatus);
        contentValues.put(UPDATED_AT, DateUtils.getCurrentTimeInDefault());
        contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
        contentValues.put(SYNC_STATUS, AppConstants.NO);
        db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {visitId});
    }

    public void deleteAll() {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public Visit getHoldVisitsFromQueueId(String queueId) {
        Visit visit = null;

        String query = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + QUEUE_ID + " =?";
//                + " AND T1." + IS_HOLD + " = 1"
//                + " LIMIT 1";

        String[] whereParams = new String[] {queueId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                visit = getVisitModelFromCursor(cursor);


            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return visit;
    }
}
