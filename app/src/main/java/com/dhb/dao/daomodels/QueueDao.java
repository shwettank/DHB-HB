package com.dhb.dao.daomodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.Patients;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class QueueDao {
    private SQLiteDatabase db;


    // DB TABLE NAME

    public static String TABLE_NAME = "queue";


    // DB TABLE COLUMN INFO

    public static final String ID = "id";
    private static final String PATIENT_ID = "patient_id";
    private static final String SENDER_ID = "sender_id";
    private static final String RECEIVER_ID = "receiver_id";
    private static final String STATUS = "status";
    private static final String MISSCALL_TIME = "misscall_time";
    private static final String APPOINTMENT_START_TIME = "appointment_start_time";
    private static final String APPOINTMENT_END_TIME = "appointment_end_time";
    private static final String PARENT_QUEUE_ID = "parent_queue_id";
    private static final String SENDER_COMMENTS = "sender_comments";
    private static final String RECEIVER_COMMENTS = "receiver_comments";
    private static final String HEALTHCARE_FIRM_ID = "healthcare_firm_id";
    private static final String CREATED_AT = "created_at";
    private static final String CREATED_BY = "created_by";
    private static final String UPDATED_AT = "updated_at";
    private static final String UPDATED_BY = "updated_by";
    public static final String RECORD_STATUS = "record_status";
    public static final String SYNC_STATUS = "sync_status";
    private static final String SYNC_ACTION = "sync_action";

    private int batchCount = 4;
    private AppPreferenceManager appPreferenceManager;

    public QueueDao(SQLiteDatabase db, AppPreferenceManager appPreferenceManager) {
        this.db = db;
        this.appPreferenceManager = appPreferenceManager;
    }

    // get model from cursor

    private Queue getQueueModelFromCursor(Cursor cursorQueue) {

        Queue queue = new Queue();

        queue.setId(cursorQueue.getString(cursorQueue.getColumnIndex(ID)));
        String patientId = cursorQueue.getString(cursorQueue.getColumnIndex(PATIENT_ID));
        queue.setPatientId(cursorQueue.getString(cursorQueue.getColumnIndex(PATIENT_ID)));
        queue.setSenderId(cursorQueue.getString(cursorQueue.getColumnIndex(SENDER_ID)));
        queue.setReceiverID(cursorQueue.getString(cursorQueue.getColumnIndex(RECEIVER_ID)));
        queue.setStatus(cursorQueue.getInt(cursorQueue.getColumnIndex(STATUS)));
        queue.setMissCallTime(cursorQueue.getLong(cursorQueue.getColumnIndex(MISSCALL_TIME)));
        queue.setAppointmentStartTime(cursorQueue.getLong(cursorQueue.getColumnIndex(APPOINTMENT_START_TIME)));
        queue.setAppointmentEndTime(cursorQueue.getLong(cursorQueue.getColumnIndex(APPOINTMENT_END_TIME)));
        queue.setParentQueuId(cursorQueue.getString(cursorQueue.getColumnIndex(PARENT_QUEUE_ID)));
        queue.setSenderComments(cursorQueue.getString(cursorQueue.getColumnIndex(SENDER_COMMENTS)));
        queue.setReceiverComments(cursorQueue.getString(cursorQueue.getColumnIndex(RECEIVER_COMMENTS)));
        queue.setHealthcareFirmId(cursorQueue.getString(cursorQueue.getColumnIndex(HEALTHCARE_FIRM_ID)));
        queue.setCreatedAt(cursorQueue.getLong(cursorQueue.getColumnIndex(CREATED_AT)));
        queue.setCreatedBy(cursorQueue.getString(cursorQueue.getColumnIndex(CREATED_BY)));
        queue.setUpdatedAt(cursorQueue.getLong(cursorQueue.getColumnIndex(UPDATED_AT)));
        queue.setUpdatedBy(cursorQueue.getString(cursorQueue.getColumnIndex(UPDATED_BY)));
        queue.setRecordStatus(cursorQueue.getString(cursorQueue.getColumnIndex(RECORD_STATUS)));
        queue.setSyncStatus(cursorQueue.getString(cursorQueue.getColumnIndex(SYNC_STATUS)));
        queue.setSyncAction(cursorQueue.getString(cursorQueue.getColumnIndex(SYNC_ACTION)));

        String query = "SELECT * FROM " + PatientsDao.TABLE_NAME + " WHERE " + PatientsDao.ID + " = '" + patientId + "'";
        String[] whereParams = new String[] {};
        Cursor cursorPatient = this.db.rawQuery(query, whereParams);
        if (cursorPatient != null && (cursorPatient.moveToFirst())){

            PatientsDao patientDao = new PatientsDao(db);

            Patients patient = patientDao.getPatientModelFromCursor(cursorPatient);
            queue.setPatient(patient);

        }
        if (cursorPatient != null && !cursorPatient.isClosed()){
            cursorPatient.close();
        }

        return queue;
    }

    public void deleteQueue(Queue queue) {
        String whereClause = ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(queue.getId())};
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

// get queue patients from db for Receptions

    public List<Queue> getPatientFromQueue(String userId, String status) {

        List<Queue> queueArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECEIVER_ID + " = '" + userId + "'"
                + " AND " + HEALTHCARE_FIRM_ID + " = '" + appPreferenceManager.getHospitalId() + "'"
                + " AND " + RECORD_STATUS + " ='" + AppConstants.ACTIVE_RECORD_STATUS + "' AND "
                + STATUS + " = " + status + " AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() +
                " AND " + DateUtils.getEndOfDayInDefaultTimeZone() + " ORDER BY " + MISSCALL_TIME + " ASC";

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Queue queue = getQueueModelFromCursor(cursor);
                if (queue != null){
                    queueArrayList.add(queue);
                }

            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return queueArrayList;
    }

    public ArrayList<String> getNonSyncedQueueIds() {
        ArrayList<String> ids = new ArrayList<>();
        String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=?";
        String[] whereParams = new String[] {AppConstants.STATUS_NON_SYNC};
        Cursor cursor = this.db.rawQuery(query, whereParams);
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

// Check patient is already exist or not in queue

    public Queue getQueueModelFromPatientIdForCurrentDate(String patientId) {

        Queue queue = null;
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        String status = "";
        if (appPreferenceManager.getRoleName().equals(AppConstants.RECEPTION)){
            status = AppConstants.RECEPTION_PRESENT;
        } else if (appPreferenceManager.getRoleName().equals(AppConstants.DOCTOR)){
            status = AppConstants.DOCTOR_PRESENT;
        }
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + patientId
                + "' AND " + RECORD_STATUS + " ='" + AppConstants.ACTIVE_RECORD_STATUS + "' AND " + STATUS + " = " + status + " AND "
                + HEALTHCARE_FIRM_ID + " = " + "\"" + appPreferenceManager.getHospitalId() + "\""
                + " AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone()
                + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                queue = getQueueModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return queue;
    }

    public Queue getQueueModelFromPatientId(String patientId) {

        Queue queue = null;
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        String status = "";
        if (appPreferenceManager.getRoleName().equals(AppConstants.RECEPTION)){
            status = AppConstants.RECEPTION_PRESENT;
        } else if (appPreferenceManager.getRoleName().equals(AppConstants.DOCTOR)){
            status = AppConstants.DOCTOR_PRESENT;
        }
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + patientId
                + "' AND " + RECORD_STATUS + " ='" + AppConstants.ACTIVE_RECORD_STATUS + "' AND " + STATUS + " = " + status + " AND "
                + HEALTHCARE_FIRM_ID + " = " + "\"" + appPreferenceManager.getHospitalId() + "\"";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                queue = getQueueModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return queue;
    }

    public Queue getQueueModelWithQueueId(String queueId) {

        Queue queue = null;
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + queueId + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                queue = getQueueModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return queue;
    }

    // get all patients from db

    public ArrayList<Queue> getTodaysPatientQueue() {

        ArrayList<Queue> queueArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=?";
        String[] whereParams = new String[] {"A"};
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && (cursor.moveToFirst())){

            do {
                Queue queue = getQueueModelFromCursor(cursor);
                if (queue != null){
                    queueArrayList.add(queue);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return queueArrayList;
    }

    // update if exists or insert patients to db

    public int insertQueue(Queue queue) {
        ContentValues initialValues = new ContentValues();
        Queue queueModel;
        queueModel = this.getQueueModelFromPatientIdForCurrentDate(queue.getPatientId());
        if (queueModel == null){
            initialValues = this.getContentValuesFromModel(queue);
            initialValues.put(SYNC_ACTION, AppConstants.INSERT);
            int id = (int) db.insert(TABLE_NAME, null, initialValues);
            return id;
        }
        return 0;
    }

    public Queue getPatientQueueFromMobileNumber(String mobileNumber, String status) {

        Queue queue = null;

        String query = "SELECT T1.* FROM " + TABLE_NAME + " T1"
                + " JOIN " + PatientsDao.TABLE_NAME + " T2"
                + " ON " + "T1." + PATIENT_ID + "=" + "T2." + PatientsDao.ID
                + " WHERE T1." + RECORD_STATUS + "='" + AppConstants.ACTIVE_RECORD_STATUS + "'"
                + " AND T2." + RECORD_STATUS + "='" + AppConstants.ACTIVE_RECORD_STATUS + "'"
                + " AND T1." + STATUS + "='" + status + "'"
                + " AND (" + "T2." + PatientsDao.MOBILE1 + "='" + mobileNumber + "'"
                + " OR T2." + PatientsDao.MOBILE2 + "='" + mobileNumber + "'"
                + " OR T2." + PatientsDao.MOBILE3 + "='" + mobileNumber + "')";

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                queue = getQueueModelFromCursor(cursor);
            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return queue;
    }

    public int updateQueueWithSenderComments(Queue queue) {
        ContentValues initialValues = new ContentValues();
        Queue queueModel;
        queueModel = this.getQueueModelWithQueueId(queue.getId());
        if (queueModel != null){
            initialValues.put(ID, queueModel.getId());
            initialValues.put(PATIENT_ID, queueModel.getPatientId());
            initialValues.put(SENDER_ID, queueModel.getSenderId());
            initialValues.put(RECEIVER_ID, queueModel.getReceiverID());
            initialValues.put(STATUS, queueModel.getStatus());
            initialValues.put(MISSCALL_TIME, queueModel.getMissCallTime());
            initialValues.put(APPOINTMENT_START_TIME, queueModel.getAppointmentStartTime());
            initialValues.put(APPOINTMENT_END_TIME, queueModel.getAppointmentEndTime());
            initialValues.put(PARENT_QUEUE_ID, queueModel.getParentQueuId());
            initialValues.put(SENDER_COMMENTS, queue.getSenderComments());
            initialValues.put(RECEIVER_COMMENTS, queueModel.getReceiverComments());
            initialValues.put(HEALTHCARE_FIRM_ID, appPreferenceManager.getHospitalId());
            initialValues.put(CREATED_AT, queueModel.getCreatedAt());
            initialValues.put(CREATED_BY, queueModel.getCreatedBy());
            initialValues.put(RECORD_STATUS, queueModel.getRecordStatus());

            initialValues.put(SYNC_ACTION, AppConstants.UPDATE);
            initialValues.put(SYNC_STATUS, AppConstants.STATUS_NON_SYNC);
            initialValues.put(UPDATED_AT, DateUtils.getCurrentTimeInDefault());
            initialValues.put(UPDATED_BY, appPreferenceManager.getUserId());
            int updateValue = db.update(TABLE_NAME, initialValues, ID + "=?", new String[] {String.valueOf(queueModel.getId())});
            Logger.debug("sender comment update value" + updateValue);
            return updateValue;
        }
        return 0;
    }

    public void insertOrUpdateQueueAfterApiCall(Queue queue, boolean syncStatus) {
        ContentValues contentValues = new ContentValues();
        Queue queueModel;
        queueModel = this.getQueueModelWithQueueId(queue.getId());
        if (queueModel == null){
            contentValues = this.getContentValuesFromModel(queue);
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, syncStatus ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
            Logger.debug("insertOrUpdatePatientHealthCareMapAfterApiCall : insert : " + insertValue);
        } else {
            contentValues = this.getContentValuesFromModel(queue);
            contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
            contentValues.put(SYNC_STATUS, syncStatus ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
            int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {queue.getId()});
            Logger.debug("insertOrUpdatePatientHealthCareMapAfterApiCall : update : " + updateValue);
        }
    }

    public int insertInQueueWithoutCheck(Queue queue) {
        ContentValues contentValues = getContentValuesFromModel(queue);
        int id = (int) db.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public ContentValues getContentValuesFromModel(Queue queue) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, queue.getId());
        initialValues.put(PATIENT_ID, queue.getPatientId());
        initialValues.put(SENDER_ID, queue.getSenderId());
        initialValues.put(RECEIVER_ID, queue.getReceiverID());
        initialValues.put(STATUS, queue.getStatus());
        initialValues.put(MISSCALL_TIME, queue.getMissCallTime());
        initialValues.put(APPOINTMENT_START_TIME, queue.getAppointmentStartTime());
        initialValues.put(APPOINTMENT_END_TIME, queue.getAppointmentEndTime());
        initialValues.put(PARENT_QUEUE_ID, queue.getParentQueuId());
        initialValues.put(SENDER_COMMENTS, queue.getSenderComments());
        initialValues.put(RECEIVER_COMMENTS, queue.getReceiverComments());
        initialValues.put(HEALTHCARE_FIRM_ID, appPreferenceManager.getHospitalId());
        initialValues.put(CREATED_AT, queue.getCreatedAt());
        initialValues.put(CREATED_BY, queue.getCreatedBy());
        initialValues.put(UPDATED_AT, queue.getUpdatedAt());
        initialValues.put(UPDATED_BY, queue.getUpdatedBy());
        initialValues.put(RECORD_STATUS, queue.getRecordStatus());
        initialValues.put(SYNC_STATUS, queue.getSyncStatus());
        initialValues.put(SYNC_ACTION, queue.getSyncAction());
        return initialValues;
    }

    public List<Queue> getExaminedPatientFromQueue(String userId) {

        List<Queue> queueArrayList = new ArrayList<>();
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECEIVER_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        String query = "";

        if (appPreferenceManager.getRoleName().equals(AppConstants.RECEPTION)){
            query = "SELECT * FROM " + TABLE_NAME + " WHERE ("
                    + RECEIVER_ID + " = '" + userId + "' OR " + RECEIVER_ID + " = '' ) AND "
                    + HEALTHCARE_FIRM_ID + " = '" + appPreferenceManager.getHospitalId()
                    + "' AND " + RECORD_STATUS + " ='A' AND " +
                    STATUS + " = " + AppConstants.EXAMINED
                    + " AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() +
                    " AND " + DateUtils.getEndOfDayInDefaultTimeZone() + " ORDER BY " + MISSCALL_TIME + " ASC";
            ;
        } else if (appPreferenceManager.getRoleName().equals(AppConstants.DOCTOR)){
            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + SENDER_ID + " = '" + userId + "' AND " + HEALTHCARE_FIRM_ID + " = '" +
                    appPreferenceManager.getHospitalId() + "' AND " + RECORD_STATUS + " ='A' AND " +
                    STATUS + " = " + AppConstants.EXAMINED
                    + " AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() +
                    " AND " + DateUtils.getEndOfDayInDefaultTimeZone() + " ORDER BY " + MISSCALL_TIME + " ASC";
        }


        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Queue queue = getQueueModelFromCursor(cursor);
                if (queue != null){
                    queueArrayList.add(queue);
                }

            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return queueArrayList;
    }

    public boolean updateRecordStatus(String queueId, String recordStatus, String syncStatus, int fromStatus, int toStatus) {

        ContentValues contentValues = new ContentValues();
        if (toStatus != -1){
            contentValues.put(QueueDao.STATUS, toStatus);
        }
        contentValues.put(QueueDao.RECORD_STATUS, recordStatus);
        contentValues.put(QueueDao.SYNC_STATUS, syncStatus);

        int id = db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {queueId});

        if (id == -1){
            return false;
        } else {
            return true;
        }
    }

    public void updatePatientId(String id, String changedId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PATIENT_ID, changedId);
        db.update(TABLE_NAME, contentValues, PATIENT_ID + "=?", new String[] {id});
    }

    public ArrayList<Queue> getLocalQueueBatch() {

        ArrayList<Queue> queueList = new ArrayList<>();
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=?"
//				+ " LIMIT " + batchCount;

//		get all queue entries for patient who are registered
//		SELECT q.* FROM queue q JOIN patient p ON q.patient_id=p.id WHERE p.registration_status='1' LIMIT 5

        String query = "SELECT T1.* FROM " + TABLE_NAME + " T1"
                + " JOIN " + PatientsDao.TABLE_NAME + " T2"
                + " ON " + "T1." + QueueDao.PATIENT_ID + "=" + "T2." + PatientsDao.ID
                + " WHERE T1." + QueueDao.SYNC_STATUS + "='" + AppConstants.STATUS_NON_SYNC + "'"
                + " AND T2." + PatientsDao.SYNC_STATUS + "='" + AppConstants.STATUS_SYNCED + "'"
                + " ORDER BY T1." + QueueDao.MISSCALL_TIME + " ASC"
                + " LIMIT " + batchCount;

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Queue queue = getQueueModelFromCursor(cursor);
                if (queue != null){
                    queueList.add(queue);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return queueList;
    }

    public void deleteAll() {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public String getDoctorPresentParentQueueId(String queueId) {

        String parentQueueId = "";

        String query = "SELECT " + PARENT_QUEUE_ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + queueId + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            do {
                parentQueueId = cursor.getString(cursor.getColumnIndex(PARENT_QUEUE_ID));
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return parentQueueId;
    }

    public ArrayList<Queue> getUnSyncedLocalQueueFromPatientId(String patientId) {

        ArrayList<Queue> queueList = new ArrayList<>();

        String query = "SELECT T1.* FROM " + TABLE_NAME + " T1"
                + " JOIN " + PatientsDao.TABLE_NAME + " T2"
                + " ON " + "T1." + QueueDao.PATIENT_ID + "=" + "T2." + PatientsDao.ID
                + " WHERE T1." + QueueDao.SYNC_STATUS + "='" + AppConstants.STATUS_NON_SYNC + "'"
                + " AND T1." + PATIENT_ID + "='" + patientId + "'"
                + " AND T2." + PatientsDao.SYNC_STATUS + "='" + AppConstants.STATUS_SYNCED + "'"
                + " ORDER BY T1." + QueueDao.MISSCALL_TIME + " ASC"
                + " LIMIT " + batchCount;

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Queue queue = getQueueModelFromCursor(cursor);
                if (queue != null){
                    queueList.add(queue);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return queueList;
    }

    public Queue getLastQueueEntryForPatientId(String patientId) {
        Queue queue = null;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + HEALTHCARE_FIRM_ID + " = '" + appPreferenceManager.getHospitalId() + "'"
                + " AND " + PATIENT_ID + " = '" + patientId + "'"
                + " AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone()
                + " AND " + DateUtils.getEndOfDayInDefaultTimeZone()
                + " ORDER BY " + UPDATED_AT + " DESC " + "LIMIT 1";
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            queue = getQueueModelFromCursor(cursor);
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return queue;
    }

    public ArrayList<Queue> getHoldVisitQueues(int onHoldStatus, long startTime, long endTime) {

        ArrayList<Queue> queueArrayList = new ArrayList<>();
        String query = "SELECT T1.* FROM " + TABLE_NAME
                + " T1 JOIN " + VisitDao.TABLE_NAME + " T2"
                + " ON " + "T1." + ID + "=" + "T2." + VisitDao.QUEUE_ID
                + " WHERE T1." + HEALTHCARE_FIRM_ID + " = '" + appPreferenceManager.getHospitalId() + "'"
                + " AND T2." + VisitDao.DOCTOR_ID + " = '" + appPreferenceManager.getUserId() + "'"
                + " AND T2." + VisitDao.IS_HOLD + " = " + onHoldStatus
                + " AND T1." + MISSCALL_TIME + " BETWEEN " + startTime
                + " AND " + endTime + " ORDER BY T1." + MISSCALL_TIME + " ASC";

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Queue queue = getQueueModelFromCursor(cursor);
                if (queue != null){
                    queueArrayList.add(queue);
                }

            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return queueArrayList;
    }

}
