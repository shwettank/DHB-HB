package com.dhb.dao.daomodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.HealthCareFirm;
import com.dhb.utils.AppConstants;
import com.dhb.utils.Logger;

import java.util.ArrayList;

public class HealthCareFirmDao {

    private SQLiteDatabase db;

    // DB TABLE NAME
    public static String TABLE_NAME = "healthcare_firms";
    // DB TABLE COLUMN INFO

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COUNTRY_ID = "country_id";
    private static final String STATE_ID = "state_id";
    private static final String DISTRICT_ID = "district_id";
    private static final String TALUKA_ID = "taluka_id";
    private static final String VILLAGE_ID = "village_id";
    private static final String PLACE = "place";
    private static final String ADDRESS1 = "address1";
    private static final String ADDRESS2 = "address2";
    private static final String PINCODE = "pincode";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String EMAIL = "email";
    private static final String CONTACT_PERSON_NAME = "contact_person_name";
    private static final String CONTACT_PERSON_EMAIL = "contact_person_email";
    private static final String CONTACT_PERSON_MOBILE = "contact_person_mobile";
    private static final String LOGO = "logo";
    private static final String LOGO_THUMB = "logo_thumb";
    private static final String REFERENCE_TAG = "reference_tag";
    private static final String PARENT_HEALTHCARE_FIRM_ID = "parent_healthcare_firm_id";
    private static final String HEALTHCARE_FIRM_TYPE = "healthcare_firm_type";
    private static final String CREATED_AT = "created_at";
    private static final String CREATED_BY = "created_by";
    private static final String UPDATED_AT = "updated_at";
    private static final String UPDATED_BY = "updated_by";
    private static final String RECORD_STATUS = "record_status";
    private static final String SYNC_STATUS = "sync_status";
    private static final String SYNC_ACTION = "sync_action";

    private static final String TALUKA_MASTER_TABLE = "taluka";
    private static final String TALUKA_NAME = "taluka_name";

    // Constructors
    public HealthCareFirmDao(SQLiteDatabase db) {
        this.db = db;
    }

    // get model from cursor
    private HealthCareFirm getHealthCareFirmModelFromCursor(Cursor cursor) {
        HealthCareFirm healthCareFirm = new HealthCareFirm();
        healthCareFirm.setId(cursor.getString(cursor.getColumnIndex(ID)));
        healthCareFirm.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        healthCareFirm.setCountryId(cursor.getLong(cursor.getColumnIndex(COUNTRY_ID)));
        healthCareFirm.setStateId(cursor.getLong(cursor.getColumnIndex(STATE_ID)));
        healthCareFirm.setDistrictId(cursor.getLong(cursor.getColumnIndex(DISTRICT_ID)));
        healthCareFirm.setTalukaId(cursor.getLong(cursor.getColumnIndex(TALUKA_ID)));
        healthCareFirm.setVillageId(cursor.getLong(cursor.getColumnIndex(VILLAGE_ID)));
        healthCareFirm.setPlace(cursor.getString(cursor.getColumnIndex(PLACE)));
        healthCareFirm.setAddress1(cursor.getString(cursor.getColumnIndex(ADDRESS1)));
        healthCareFirm.setAddress2(cursor.getString(cursor.getColumnIndex(ADDRESS2)));
        healthCareFirm.setPincode(cursor.getString(cursor.getColumnIndex(PINCODE)));
        healthCareFirm.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
        healthCareFirm.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
        healthCareFirm.setContactPersonName(cursor.getString(cursor.getColumnIndex(CONTACT_PERSON_NAME)));
        healthCareFirm.setContactPersonMobile(cursor.getString(cursor.getColumnIndex(CONTACT_PERSON_MOBILE)));
        healthCareFirm.setContactPersonEmail(cursor.getString(cursor.getColumnIndex(CONTACT_PERSON_EMAIL)));
        healthCareFirm.setLogo(cursor.getString(cursor.getColumnIndex(LOGO)));
        healthCareFirm.setLogoThumb(cursor.getString(cursor.getColumnIndex(LOGO_THUMB)));
        healthCareFirm.setReferenceTag(cursor.getString(cursor.getColumnIndex(REFERENCE_TAG)));
        healthCareFirm.setParentHealthcareFirmId(cursor.getString(cursor.getColumnIndex(PARENT_HEALTHCARE_FIRM_ID)));
        healthCareFirm.setHealthcareFirmType(cursor.getString(cursor.getColumnIndex(HEALTHCARE_FIRM_TYPE)));

        healthCareFirm.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
        healthCareFirm.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
        healthCareFirm.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
        healthCareFirm.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
        healthCareFirm.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
        healthCareFirm.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
        healthCareFirm.setSyncAction(cursor.getString(cursor.getColumnIndex(SYNC_ACTION)));

        return healthCareFirm;
    }

    public ContentValues getContentValuesFromModel(HealthCareFirm healthCareFirm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, healthCareFirm.getId());
        contentValues.put(NAME, healthCareFirm.getName());
        contentValues.put(COUNTRY_ID, healthCareFirm.getCountryId());
        contentValues.put(STATE_ID, healthCareFirm.getStateId());
        contentValues.put(DISTRICT_ID, healthCareFirm.getDistrictId());
        contentValues.put(TALUKA_ID, healthCareFirm.getTalukaId());
        contentValues.put(VILLAGE_ID, healthCareFirm.getVillageId());
        contentValues.put(PLACE, healthCareFirm.getPlace());
        contentValues.put(ADDRESS1, healthCareFirm.getAddress1());
        contentValues.put(ADDRESS2, healthCareFirm.getAddress2());
        contentValues.put(PINCODE, healthCareFirm.getPincode());
        contentValues.put(PHONE_NUMBER, healthCareFirm.getPhoneNumber());
        contentValues.put(EMAIL, healthCareFirm.getEmail());
        contentValues.put(CONTACT_PERSON_NAME, healthCareFirm.getContactPersonName());
        contentValues.put(CONTACT_PERSON_MOBILE, healthCareFirm.getContactPersonMobile());
        contentValues.put(CONTACT_PERSON_EMAIL, healthCareFirm.getContactPersonEmail());
        contentValues.put(LOGO, healthCareFirm.getLogo());
        contentValues.put(LOGO_THUMB, healthCareFirm.getLogoThumb());
        contentValues.put(REFERENCE_TAG, healthCareFirm.getReferenceTag());
        contentValues.put(PARENT_HEALTHCARE_FIRM_ID, healthCareFirm.getParentHealthcareFirmId());
        contentValues.put(HEALTHCARE_FIRM_TYPE, healthCareFirm.getHealthcareFirmType());
        contentValues.put(CREATED_AT, healthCareFirm.getCreatedAt());
        contentValues.put(CREATED_BY, healthCareFirm.getCreatedBy());
        contentValues.put(UPDATED_AT, healthCareFirm.getUpdatedAt());
        contentValues.put(UPDATED_BY, healthCareFirm.getCreatedBy());
        contentValues.put(RECORD_STATUS, healthCareFirm.getRecordStatus());
        contentValues.put(SYNC_STATUS, healthCareFirm.getSyncStatus());
        contentValues.put(SYNC_ACTION, healthCareFirm.getSyncAction());


        return contentValues;
    }

    // get all observation from db
    public ArrayList<HealthCareFirm> getAllHealthCareFirms() {

        ArrayList<HealthCareFirm> healthCareFirms = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=?";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                HealthCareFirm healthCareFirm = getHealthCareFirmModelFromCursor(cursor);
                if (healthCareFirm != null){
                    healthCareFirms.add(healthCareFirm);
                }

            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return healthCareFirms;
    }

    public void insertOrUpdateHealthCareFirmAfterApiCall(HealthCareFirm healthCareFirm) {
        ContentValues contentValues = new ContentValues();
        HealthCareFirm healthCareFirmModel;
        healthCareFirmModel = this.getHealthCareFirmWithId(healthCareFirm.getId());
        if (healthCareFirmModel == null){
            contentValues = this.getContentValuesFromModel(healthCareFirm);
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
            Logger.debug("insertOrUpdateHealthCareFirmAfterApiCall : insert : " + insertValue);
        } else {
            contentValues = this.getContentValuesFromModel(healthCareFirm);
            contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
            contentValues.put(SYNC_STATUS, AppConstants.STATUS_SYNCED);
            int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {healthCareFirm.getId()});
            Logger.debug("insertOrUpdateHealthCareFirmAfterApiCall : update : " + updateValue);
        }
    }

    public HealthCareFirm getHealthCareFirmWithId(String id) {

        HealthCareFirm healthCareFirm = null;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                healthCareFirm = getHealthCareFirmModelFromCursor(cursor);
            } while ((cursor.moveToNext()));


        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return healthCareFirm;
    }

    public int getNonSyncedHealthCareFirmsCount() {
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

    // update if exists or insert Observation to db

    public void insertHealthCareFirm(HealthCareFirm healthCareFirm) {

        ContentValues initialValues = new ContentValues();
        initialValues = this.getContentValuesFromModel(healthCareFirm);
        initialValues.put(SYNC_ACTION, AppConstants.UPDATE);
        int id = db.update(TABLE_NAME, initialValues, ID + "=?", new String[] {String.valueOf(healthCareFirm.getId())});

        if (id == 0){
//			initialValues.put(ID, DeviceUtils.getRandomUUID());
//			initialValues.put(CREATED_AT, Calendar.getInstance().getTimeInMillis());
//			initialValues.put(CREATED_BY, healthCareFirm.getCreatedBy());
            initialValues.put(SYNC_ACTION, AppConstants.INSERT);
            int newId = (int) db.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        }

    }

    public ArrayList<HealthCareFirm> getAllHealthCareFirmsFromUserId(String userId) {

        ArrayList<HealthCareFirm> healthCareFirms = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=? AND " + ID + " IN (SELECT DISTINCT healthcare_firm_id FROM user_healthcare_firm_map WHERE user_id LIKE '" + userId + "' AND record_status = 'A')";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                HealthCareFirm healthCareFirm = getHealthCareFirmModelFromCursor(cursor);
                if (healthCareFirm != null){
                    healthCareFirms.add(healthCareFirm);
                }

            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return healthCareFirms;
    }

    public HealthCareFirm getHealthCareFirmsFromId(String healthCareFirmId) {

        HealthCareFirm healthCareFirm = new HealthCareFirm();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=? AND " + ID + "=?";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS, healthCareFirmId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                healthCareFirm = getHealthCareFirmModelFromCursor(cursor);
            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return healthCareFirm;
    }

    public String getHealthCareFirmReferenceTagFromId(String healthCareFirmId) {

        String healthCareFirmRefTag = "";
        String query = "SELECT " + REFERENCE_TAG + " FROM " + TABLE_NAME + " WHERE " + RECORD_STATUS + "=? AND " + ID + "=?";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS, healthCareFirmId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                healthCareFirmRefTag = cursor.getString(cursor.getColumnIndex(REFERENCE_TAG));
            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return healthCareFirmRefTag;
    }

    public String getTalukaOfHospitalByHospitalId(String healthCareFirmId) {
        String taluka = null;
        //String sql= "SELECT taluka_name FROM taluka as t INNER JOIN healthcare_firms as hf on t.taluka_id= hf.taluka_id WHERE hf.id=\"a35653\"";
        String query = "SELECT " + TALUKA_NAME + " FROM " + TALUKA_MASTER_TABLE + " as t INNER JOIN " + TABLE_NAME + " as hf on t.taluka_id= hf.taluka_id " + " WHERE " + RECORD_STATUS + "=? AND hf.id" + "=?";
        String[] whereParams = new String[] {AppConstants.ACTIVE_RECORD_STATUS, healthCareFirmId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                taluka = cursor.getString(cursor.getColumnIndex(TALUKA_NAME));
            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return taluka;
    }

}
