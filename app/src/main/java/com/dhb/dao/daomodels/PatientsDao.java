package com.dhb.dao.daomodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.Patients;
import com.dhb.models.Queue;
import com.dhb.request_model.GeneralUserInfoModel;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.DateUtils;
import com.dhb.utils.DeviceUtils;
import com.dhb.utils.Logger;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientsDao {


    private SQLiteDatabase db;


    // DB TABLE NAME

    public static String TABLE_NAME = "patient";


    // DB TABLE COLUMN INFO

    public static final String ID = "id";
    private static final String FIRST_NAME = "first_name";
    private static final String MIDDLE_NAME = "middle_name";
    private static final String LAST_NAME = "last_name";
    private static final String DOB = "date_of_birth";
    private static final String DHB_ID = "dhb_id";
    private static final String FAMILY_ID = "family_id";
    public static final String MOBILE1 = "mobile1";
    public static final String MOBILE2 = "mobile2";
    public static final String MOBILE3 = "mobile3";
    private static final String PASSWORD = "password";
    private static final String INFO = "info";
    private static final String CREATED_AT = "created_at";
    private static final String CREATED_BY = "created_by";
    private static final String UPDATED_AT = "updated_at";
    private static final String UPDATED_BY = "updated_by";
    private static final String RECORD_STATUS = "record_status";
    private static final String REGISTRATION_STATUS = "registration_status";
    private static final String IS_MR = "is_mr";
    public static final String SYNC_STATUS = "sync_status";
    private static final String SYNC_ACTION = "sync_action";
    private static final String REGISTRATION_PLAN = "registration_plan";


    private int batchCount = 4;

    private Gson gson;

    // Constructors

    public PatientsDao(SQLiteDatabase db) {
        this.db = db;
        this.gson = new Gson();
    }

    // get model from cursor

    public Patients getPatientModelFromCursor(Cursor cursor) {
        Patients patients = new Patients();
        patients.setId(cursor.getString(cursor.getColumnIndex(ID)));
        patients.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
        patients.setMiddleName(cursor.getString(cursor.getColumnIndex(MIDDLE_NAME)));
        patients.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
        patients.setDob(cursor.getLong(cursor.getColumnIndex(DOB)));
        patients.setDhbId(cursor.getString(cursor.getColumnIndex(DHB_ID)));
        patients.setFamilyId(cursor.getString(cursor.getColumnIndex(FAMILY_ID)));
        patients.setMobile1(cursor.getString(cursor.getColumnIndex(MOBILE1)));
        patients.setMobile2(cursor.getString(cursor.getColumnIndex(MOBILE2)));
        patients.setMobile3(cursor.getString(cursor.getColumnIndex(MOBILE3)));
        patients.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        patients.setInfo(gson.fromJson(cursor.getString(cursor.getColumnIndex(INFO)), GeneralUserInfoModel.class ));
        patients.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
        patients.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
        patients.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
        patients.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
        patients.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
        patients.setRegistrationStatus(cursor.getString(cursor.getColumnIndex(REGISTRATION_STATUS)));
        patients.setIsMr(cursor.getString(cursor.getColumnIndex(IS_MR)));
        patients.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
        patients.setSyncAction(cursor.getString(cursor.getColumnIndex(SYNC_ACTION)));
        patients.setRegistrationPlan(cursor.getString(cursor.getColumnIndex(REGISTRATION_PLAN)));

        return patients;
    }

    // get all patients from db

    public List<Patients> getAllPatients() {

        List<Patients> patientsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Patients patient = getPatientModelFromCursor(cursor);
                if (patient != null){
                    patientsList.add(patient);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return patientsList;
    }

    public ArrayList<String> getNonSyncedPatientIds() {
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

    public String getPatientFromMobileNumber(String mobileNo1, String mobileNo2, String mobileNo3) {
        if (mobileNo1.trim().equalsIgnoreCase("") && mobileNo2.trim().equalsIgnoreCase("") && mobileNo3.trim().equalsIgnoreCase("")){
            //All mobile are empty
            return "";
        }
        String query = "";
        String[] whereParams = null;
        if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){

            // All 3 mobile number present

            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + "(" + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?"
                    + " OR " + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?"
                    + " OR " + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){

            // 2 mobile number present : 1st and 2nd

            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + "(" + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?"
                    + " OR " + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){

            // 2 mobile number present : 1st and 3rd

            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + "(" + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?"
                    + " OR " + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo3, mobileNo3, mobileNo3
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){

            // 2 mobile number present : 2nd and 3rd

            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + "(" + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?"
                    + " OR " + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){

            // 1 mobile number present : 1st

            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + "(" + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?" + ")";
            whereParams = new String[] {mobileNo1, mobileNo1, mobileNo1};
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){

            // 1 mobile number present : 2nd

            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + "(" + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?" + ")";
            whereParams = new String[] {mobileNo2, mobileNo2, mobileNo2};
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){

            // 1 mobile number present : 3rd

            query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                    + "(" + MOBILE1 + "=?" + " OR " + MOBILE2 + "=?" + " OR " + MOBILE3 + "=?" + ")";
            whereParams = new String[] {mobileNo3, mobileNo3, mobileNo3};
        } else {
            return "";
        }
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            String patientId = cursor.getString(cursor.getColumnIndex(ID));
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            return patientId;
        }

        return "";
    }

    public Patients getPatientFromID(String patientId) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=?";
        String[] whereParams = new String[] {patientId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        Patients patient = new Patients();
        if (cursor != null && (cursor.moveToFirst())){
            patient = getPatientModelFromCursor(cursor);
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        if (patient != null){
            return patient;
        } else {
            return null;
        }
    }

    // update if exists or insert patients to db

    public int insertOrUpdatePatient(Patients patient, String userId, AppPreferenceManager appPreferenceManager) {

        ContentValues initialValues = new ContentValues();
        initialValues = this.getContentValuesFromModel(patient);
        initialValues.put(SYNC_ACTION, AppConstants.UPDATE);
        int id = db.update(TABLE_NAME, initialValues, ID + "=?", new String[] {String.valueOf(patient.getId())});

        if (id == 0){
            initialValues.put(SYNC_ACTION, AppConstants.INSERT);
            int newId = (int) db.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
            if (newId != 0){
                Queue queue = new Queue();
                queue.setId(DeviceUtils.getRandomUUID());
                queue.setPatientId(patient.getId());
                queue.setSenderId("");
                queue.setReceiverID(userId);
                if (appPreferenceManager.getRoleName().equals(AppConstants.RECEPTION)){
                    queue.setStatus(Integer.parseInt(AppConstants.RECEPTION_PRESENT));
                }
                if (appPreferenceManager.getRoleName().equals(AppConstants.DOCTOR)){
                    queue.setStatus(Integer.parseInt(AppConstants.DOCTOR_PRESENT));
                }
                queue.setMissCallTime(DateUtils.getCurrentTimeInDefault());
                queue.setAppointmentStartTime(0);
                queue.setAppointmentEndTime(0);
                queue.setParentQueuId("");
                queue.setSenderComments("");
                queue.setReceiverComments("");
                queue.setCreatedAt(DateUtils.getCurrentTimeInDefault());
                queue.setCreatedBy(userId);
                queue.setUpdatedAt(DateUtils.getCurrentTimeInDefault());
                queue.setUpdatedBy(userId);
                queue.setSyncStatus(AppConstants.STATUS_NON_SYNC);
                queue.setSyncAction(AppConstants.INSERT);
                queue.setRecordStatus(AppConstants.ACTIVE_RECORD_STATUS);
                QueueDao queueDao = new QueueDao(db, appPreferenceManager);
                queueDao.insertQueue(queue);
                return newId;
            }
            return id;
        }


        return id;
    }

    public ContentValues getContentValuesFromModel(Patients patient) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, patient.getId());
        initialValues.put(FIRST_NAME, patient.getFirstName());
        initialValues.put(MIDDLE_NAME, patient.getMiddleName());
        initialValues.put(LAST_NAME, patient.getLastName());
        initialValues.put(DOB, patient.getDob());
        initialValues.put(DHB_ID, patient.getDhbId());
        initialValues.put(FAMILY_ID, patient.getFamilyId());
        initialValues.put(MOBILE1, patient.getMobile1());
        initialValues.put(MOBILE2, patient.getMobile2());
        initialValues.put(MOBILE3, patient.getMobile3());
        initialValues.put(PASSWORD, patient.getPassword());
        initialValues.put(INFO, gson.toJson(patient.getInfo()));
        initialValues.put(CREATED_AT, patient.getCreatedAt());
        initialValues.put(CREATED_BY, patient.getCreatedBy());
        initialValues.put(UPDATED_AT, Calendar.getInstance().getTimeInMillis());
        initialValues.put(UPDATED_BY, patient.getUpdatedBy());
        initialValues.put(RECORD_STATUS, patient.getRecordStatus());
        initialValues.put(REGISTRATION_STATUS, patient.getRegistrationStatus());
        initialValues.put(IS_MR, patient.getIsMr());
        initialValues.put(SYNC_STATUS, patient.getSyncStatus());
        initialValues.put(SYNC_ACTION, patient.getSyncAction());
        initialValues.put(REGISTRATION_PLAN, patient.getRegistrationPlan());

        return initialValues;
    }

    // update if exists or insert patients to db
//    public void insertPatient(Patients patient) {
//        ContentValues initialValues;
//        initialValues = this.getContentValuesFromModel(patient);
//        initialValues.put(SYNC_ACTION, "I");
//        int id = (int) db.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
//        if (id == -1) {
//            initialValues.put(SYNC_ACTION, AppConstants.UPDATE);
//            db.update(TABLE_NAME, initialValues, "_id=?", new String[]{String.valueOf(patient.id)});
//        }
//    }

    public void updateBloodGroup(String patientId, String bloodGroup, String userId) {

        Patients patient = getPatientFromID(patientId);
        if (patient != null){
            if (patient.getInfo() != null){
                GeneralUserInfoModel generalUserInfoModel = patient.getInfo();
                generalUserInfoModel.setBloodGroup(bloodGroup);

                ContentValues values = new ContentValues();
                String whereClause = ID + "='" + patientId + "'";
                values.put(INFO, gson.toJson(patient.getInfo()));
                values.put(SYNC_ACTION, AppConstants.UPDATE);
                values.put(SYNC_STATUS, AppConstants.STATUS_NON_SYNC);
                values.put(UPDATED_AT, Calendar.getInstance().getTimeInMillis());
                values.put(UPDATED_BY, userId);

                int id = db.update(TABLE_NAME, values, whereClause, null);
            }
        }

    }

    public ArrayList<Patients> getUnregisteredPatientsBatch() {

        ArrayList<Patients> patientsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=?" + " LIMIT " + batchCount;
        String[] whereParams = new String[] {AppConstants.STATUS_NON_SYNC};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Patients patient = getPatientModelFromCursor(cursor);
                if (patient != null){
                    patientsList.add(patient);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return patientsList;
    }

    public void updatePatientId(String id, String changedId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, changedId);
        db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {id});
    }

    public void insertOrUpdatePatientAfterApiCall(Patients patient, boolean syncStatus, boolean checkPatientMobile) {
        ContentValues contentValues = new ContentValues();
        Patients patientModel;
        patientModel = this.getPatientWithId(patient.getId());

        if (patientModel == null){
//			if(checkPatientMobile){
//				String patientIdWithMobile = getPatientFromMobileNumber(patient.getMobile1(), patient.getMobile2(), patient.getMobile3());
//				if(patientIdWithMobile == null || (patientIdWithMobile != null && patientIdWithMobile.isEmpty())){
//					contentValues = this.getContentValuesFromModel(patient);
//					contentValues.put(UPDATED_AT, patient.getUpdatedAt());
//					contentValues.put(SYNC_ACTION, AppConstants.INSERT);
//					contentValues.put(SYNC_STATUS, syncStatus ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
//					int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
//					Logger.debug("insertOrUpdatePatientAfterApiCall : insert after mobile number check : " + insertValue);
//				}
//				else{
//					Patients patientModelWithMobile = getPatientWithId(patientIdWithMobile);
//					if(patientIdWithMobile != null && patient != null && patientModelWithMobile.getUpdatedAt() < patient.getUpdatedAt()){
//						contentValues = this.getContentValuesFromModel(patient);
//						contentValues.put(UPDATED_AT, patient.getUpdatedAt());
//						contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
//						contentValues.put(SYNC_STATUS, syncStatus ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
//						int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {patientModelWithMobile.getId()});
//						Logger.debug("insertOrUpdatePatientAfterApiCall : update : " + updateValue);
//
//						updatePatientId(patientModelWithMobile.getId(), patient.getId());
//					}
//				}
//			}
//			else{
            contentValues = this.getContentValuesFromModel(patient);
            contentValues.put(UPDATED_AT, patient.getUpdatedAt());
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, syncStatus ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
            Logger.debug("insertOrUpdatePatientAfterApiCall : insert without mobile number check : " + insertValue);
//			}

        } else {

//            if ((long) patientModel.getUpdatedAt() == (long) patient.getUpdatedAt()) {
//
//            } else if ((long) patientModel.getUpdatedAt() > (long) patient.getUpdatedAt()) {
//
//            } else
            if (patientModel.getUpdatedAt() > patient.getUpdatedAt()
                    && patientModel.getRegistrationStatus().equals(AppConstants.YES)
                    && patient.getRegistrationStatus().equals(AppConstants.NO)){

            } else {

                if (patient.getRegistrationStatus() != null && patient.getRegistrationStatus().equals(AppConstants.NO)
                        && patientModel.getRegistrationStatus() != null && patientModel.getRegistrationStatus().equals(AppConstants.YES)){

                } else {
                    contentValues = this.getContentValuesFromModel(patient);
                    contentValues.put(UPDATED_AT, patient.getUpdatedAt());
                    contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
                    contentValues.put(SYNC_STATUS, syncStatus ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
                    int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {patient.getId()});
                    Logger.debug("insertOrUpdatePatientAfterApiCall : update : " + updateValue);
                }

            }
        }
    }

    public Patients getPatientWithId(String id) {

        Patients patients = null;
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                patients = getPatientModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patients;
    }

    public Patients getUnSyncedPatientWithId(String id) {

        Patients patients = null;
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "' AND " + SYNC_STATUS + " = '" + AppConstants.STATUS_NON_SYNC + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                patients = getPatientModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patients;
    }

    public ArrayList<Patients> getPatientListFromKey(String searchKey, String healthCareFirmId) {

        ArrayList<Patients> patientsArrayList = new ArrayList<Patients>();
        Patients patients = null;
        searchKey = searchKey.trim();
        String[] separated = searchKey.split(" ");
        String query = " ";
        query = "SELECT T1.*, T2." + PatientHealthCareFirmMapDao.REF_TAG + " || T2." + PatientHealthCareFirmMapDao.REF_NUMBER + " AS HIN "
                + ", T2." + PatientHealthCareFirmMapDao.OLD_HIN + " AS OLD_HIN, T2." + PatientHealthCareFirmMapDao.HEALTHCARE_FIRM_ID + " FROM "
                + TABLE_NAME + " T1 LEFT JOIN "+ PatientHealthCareFirmMapDao.TABLE_NAME + " T2 ON T1." + ID + " = " + "T2." + PatientHealthCareFirmMapDao.PATIENT_ID
                + " WHERE T1." + REGISTRATION_STATUS + " = '" + AppConstants.YES + "' AND (";

        if (separated.length == 1){
            String mobileNo = "";
            if (!separated[0].startsWith("+91")){
                mobileNo = "+91" + separated[0];
            } else {
                mobileNo = separated[0];
            }
            query = query + " T1." + FIRST_NAME + " LIKE '" + separated[0].trim() + "%' OR " + "T1." + MIDDLE_NAME + " LIKE '" + separated[0].trim() + "%' OR " + "T1." + LAST_NAME + " LIKE '" + separated[0].trim() + "%' OR " + "T1." + DHB_ID + " LIKE '" + separated[0].trim() + "' OR " + "T1." + MOBILE1 + " LIKE '" + mobileNo.trim() + "' OR " + "T1." + MOBILE2 + " LIKE '" + mobileNo.trim() + "' OR " + "T1." + MOBILE3 + " LIKE '" + mobileNo.trim() + "'";

            query = query + " OR ( HIN ='" + separated[0] + "' AND T2." + PatientHealthCareFirmMapDao.HEALTHCARE_FIRM_ID + "='" + healthCareFirmId + "') OR ( " + PatientHealthCareFirmMapDao.OLD_HIN + " ='" + separated[0] + "' AND T2." + PatientHealthCareFirmMapDao.HEALTHCARE_FIRM_ID + "='" + healthCareFirmId + "')";

        } else if (separated.length == 2){
            query = query + " T1." + FIRST_NAME + " LIKE '" + separated[0].trim() + "%' AND " + "T1." + LAST_NAME + " LIKE '" + separated[1].trim() + "%'";
        } else {
            query = query + " T1." + FIRST_NAME + " LIKE '" + separated[0].trim() + "%' AND " + "T1." + MIDDLE_NAME + " LIKE '" + separated[1].trim() + "%' AND " + "T1." + LAST_NAME + " LIKE '" + separated[2].trim() + "%'";
        }
        query = query + " OR (HIN LIKE '" + separated[0].trim() + "' AND T2." + PatientHealthCareFirmMapDao.HEALTHCARE_FIRM_ID + " = '" + healthCareFirmId + "') OR (OLD_HIN" + " LIKE '" + separated[0] + "' AND T2." + PatientHealthCareFirmMapDao.HEALTHCARE_FIRM_ID + " = '" + healthCareFirmId + "')" + ") ORDER BY " + FIRST_NAME + " ASC";

        Logger.debug("Search Query - " + query);
        Cursor cursor = this.db.rawQuery(query, null);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                patients = getPatientModelFromCursor(cursor);
                if (patients != null){
                    patientsArrayList.add(patients);
                }
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientsArrayList;
    }

    public void updateRegistrationStatus(String patientId, String userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(REGISTRATION_STATUS, AppConstants.YES);
        contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
        contentValues.put(SYNC_STATUS, AppConstants.STATUS_NON_SYNC);
        contentValues.put(UPDATED_AT, DateUtils.getCurrentTimeInDefault());
        contentValues.put(UPDATED_BY, userId);
        int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {patientId});
        Logger.debug("updateValue: " + updateValue);
		/*String query = "UPDATE " + TABLE_NAME + " SET " + REGISTRATION_STATUS + " = '" + AppConstants.YES + "', " + SYNC_STATUS + " = '" + AppConstants.STATUS_NON_SYNC + "', " + SYNC_ACTION + " = '" + AppConstants.UPDATE + "', "
		 + UPDATED_AT + " = " + DateUtils.getCurrentTimeInDefault() + ", " + UPDATED_BY + " = '" + userId + "' WHERE " + ID + " = '" + patientId + "'";
		                                                           Logger.debug("updateRegistrationStatus: " + query);
		                                                           String[] whereParams = new String[]{};
		                                                           this.db.rawQuery(query, whereParams);*/
    }

    public void deletePatient(String patientId) {
        db.delete(TABLE_NAME, ID + "=?", new String[] {patientId});
    }

    public Queue insertOrUpdatePatientAfterRegistration(Patients patient, String userId, AppPreferenceManager appPreferenceManager) {

        String queueId = "";
        Queue queue = null;
        ContentValues initialValues = new ContentValues();
        initialValues = this.getContentValuesFromModel(patient);
        initialValues.put(SYNC_ACTION, AppConstants.UPDATE);
        int id = db.update(TABLE_NAME, initialValues, ID + "=?", new String[] {String.valueOf(patient.getId())});

        if (id == 0){
            initialValues.put(SYNC_ACTION, AppConstants.INSERT);
            int newId = (int) db.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
            if (newId != 0){
                queue = new Queue();
                queueId = DeviceUtils.getRandomUUID();
                queue.setId(queueId);
                queue.setPatientId(patient.getId());
                queue.setSenderId("");
                queue.setReceiverID(userId);
                if (appPreferenceManager.getRoleName().equals(AppConstants.RECEPTION)){
                    queue.setStatus(Integer.parseInt(AppConstants.RECEPTION_PRESENT));
                }
                if (appPreferenceManager.getRoleName().equals(AppConstants.DOCTOR)){
                    queue.setStatus(Integer.parseInt(AppConstants.DOCTOR_PRESENT));
                }
                queue.setMissCallTime(DateUtils.getCurrentTimeInDefault());
                queue.setAppointmentStartTime(0);
                queue.setAppointmentEndTime(0);
                queue.setParentQueuId("");
                queue.setSenderComments("");
                queue.setReceiverComments("");
                queue.setCreatedAt(DateUtils.getCurrentTimeInDefault());
                queue.setCreatedBy(userId);
                queue.setUpdatedAt(DateUtils.getCurrentTimeInDefault());
                queue.setUpdatedBy(userId);
                queue.setSyncStatus(AppConstants.STATUS_NON_SYNC);
                queue.setSyncAction(AppConstants.INSERT);
                queue.setRecordStatus(AppConstants.ACTIVE_RECORD_STATUS);
                QueueDao queueDao = new QueueDao(db, appPreferenceManager);
                queueDao.insertQueue(queue);
                return queue;
            }
            return queue;
        }


        return queue;
    }

}
