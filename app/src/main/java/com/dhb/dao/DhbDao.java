package com.dhb.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.dhb.dao.daomodels.PatientQueueCountDao;
import com.dhb.request_model.GeneralUserInfoModel;
import com.dhb.request_model.PatientUserInfoModel;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.Logger;
import com.dhb.dao.daomodels.QueueDao;
import com.dhb.request_model.UserDocumentModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DhbDao extends AbstractDao {

    private final String TABLE_PATIENT_INFO = "patient_info";
    private final String TABLE_PATIENT = "patient";
    private final String TABLE_FAMILY_MEMBER_INFO = "family_member_info";

    /*Patient Info Table*/
    private final String row_id = "ROWID";
    private final String id = "row_id";
    private final String patientId = "patient_id";
    private final String mobile_queue_id = "mobile_queue_id";
    private final String first_name = "first_name";
    private final String middle_name = "middle_name";
    private final String last_name = "last_name";
    private final String email = "email";
    private final String profile_image = "profile_image";
    private final String profile_image_thumbnail = "profile_image_thumb";
    //	private final String mobile = "mobile1";
    private final String mobile1 = "mobile1";
    private final String mobile2 = "mobile2";
    private final String mobile3 = "mobile3";
    private final String date_of_birth = "date_of_birth";
    private final String registration_status = "registration_status";
    //    private final String subscription_type = "subscription_type";
    private final String appointment_time = "appointment_time";
    private final String appointment_end_time = "appointment_end_time";
    private final String gender = "gender";
    private final String sync_status = "sync_status";
    private final String missed_call_time = "missed_call_time";
    private final String record_status = "record_status";
    private final String created_at = "created_at";
    private final String status = "status";
    private final String updated_at = "updated_at";
    private final String taluka = "taluka";
    private final String district = "district";
    private final String state = "state";
    private final String pin_code = "pin_code";
    private final String address_line_1 = "address_line_1";
    private final String address_line_2 = "address_line_2";
    private final String address_line_3 = "address_line_3";
    private final String address_line_4 = "address_line_4";
    private final String display_patient_id = "display_patient_id";

    private final String id_proof_type = "id_proof_type";
    private final String id_proof_no = "id_proof_no";
    private final String blood_group = "blood_group";
    private final String emergency_contact_name = "emergency_contact_name";
    private final String emergency_contact_no = "emergency_contact_no";
    private final String mr = "mr";
    private final String family_id = "family_id";
    private final String family_count = "family_count";
    private final String hin = "hin";
    private final String family_members_names = "family_members_names";

    /*User Documents Table*/
    private final String TABLE_USER_DOCUMENTS = "user_documents";
    private final String user_id = "user_id";
    private final String file_name = "file_name";
    private final String created_by = "created_by";
    private final String document_type_id = "document_type_id";
    private final String type_name = "type_name";
    private final String doc_url = "doc_url";
    private final String doc_thumb_url = "doc_thumb_url";
    private final String doc_title = "doc_title";
    private final String area = "area";


    private Context context;
    private PatientUserInfoModel patientUserInfoModel2, patientUserInfoModel3;
    private PatientUserInfoModel patientUserInfoModelFromFamily;
    AppPreferenceManager appPreferenceManager;


    public DhbDao(Context context) {
        this.mDbHelper = DbHelper.sharedDbHelper();
        openDatabase();
        this.context = context;
        appPreferenceManager = new AppPreferenceManager(context);
    }

    public boolean insertPatientInfo(PatientUserInfoModel patientUserInfoModel, boolean isNewPatient) {

        ContentValues values = getPatientInfoModelContentValues(patientUserInfoModel, TABLE_PATIENT_INFO);
        values.put(appointment_time, patientUserInfoModel.getAppointmentStartTime());
        values.put(appointment_end_time, patientUserInfoModel.getAppointmentEndTime());
        if (patientUserInfoModel.getGeneralInfo() != null
                && patientUserInfoModel.getGeneralInfo().getProfileImageThumbnail() != null){
            values.put(profile_image_thumbnail, patientUserInfoModel.getGeneralInfo().getProfileImageThumbnail());
        } else {
            values.put(profile_image_thumbnail, "");
        }
        if (patientUserInfoModel.getGeneralInfo() != null
                && patientUserInfoModel.getGeneralInfo().getProfileImage() != null){
            values.put(profile_image, patientUserInfoModel.getGeneralInfo().getProfileImage());
        } else {
            values.put(profile_image, "");
        }


        if (isNewPatient){
            PatientUserInfoModel patientUserInfoModelForMobileAndStatus = null;
            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                patientUserInfoModelForMobileAndStatus =
                        getPatientInfoOnMobileAndStatus(
//								patientUserInfoModel.getMobile(),
                                patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(),
                                patientUserInfoModel.getMobile3(), AppConstants.RECEPTION_PRESENT);
            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                patientUserInfoModelForMobileAndStatus = getPatientInfoOnMobileAndStatus(
//						patientUserInfoModel.getMobile(),
                        patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(),
                        patientUserInfoModel.getMobile3(), AppConstants.DOCTOR_PRESENT);
            }

            if (patientUserInfoModelForMobileAndStatus != null){
                return false;
            }
//			else {
//				PatientUserInfoModel patientUserInfoModelFromFamily = getFamilyMemberInfoByMobileNumber(
//						patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(),
//						patientUserInfoModel.getMobile3());
//				if (patientUserInfoModelFromFamily == null){
//					return false;
//				}
//			}

        }

        PatientUserInfoModel patientUserInfoModel1 = getPatientInfo(patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3(),
                patientUserInfoModel.getPatientId(), patientUserInfoModel.getStatus(), patientUserInfoModel.getMobileQueueId());
        if (patientUserInfoModel1 == null){
            patientUserInfoModel2 = getPatientInfoOnMobile(patientUserInfoModel.getMobile1(),
                    patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3(),
                    patientUserInfoModel.getStatus(), patientUserInfoModel.getMobileQueueId());
            if (patientUserInfoModel2 != null){
//				values.put(mobile_queue_id,patientUserInfoModel2.getMobileQueueId());
                long val = this.db.update(TABLE_PATIENT_INFO, values,
//						mobile + " =?"

                        mobile1 + " =?" + " AND " + mobile2 + " =?" + " AND " + mobile3 + " =?"
                                + " AND " + status + " =? AND " + mobile_queue_id + " =?",
                        new String[] {patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3(),
                                patientUserInfoModel.getStatus() + "", patientUserInfoModel.getMobileQueueId()});
            } else {
                patientUserInfoModel3 = getPatientInfoOnMobileQueueId(patientUserInfoModel.getMobileQueueId());
                if (patientUserInfoModel3 != null){
//					values.put(mobile_queue_id, patientUserInfoModel3.getMobileQueueId());
                    int update = this.db.update(TABLE_PATIENT_INFO, values, mobile_queue_id + " =?", new String[] {patientUserInfoModel.getMobileQueueId()});
                    Logger.debug(update + "");
                } else {
                    patientUserInfoModelFromFamily = getFamilyMemberInfoByMobileNumber(patientUserInfoModel.getMobile1(),
                            patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3());
                    if (patientUserInfoModelFromFamily != null){

                        ContentValues valuesFromFamily = getPatientInfoModelContentValues(patientUserInfoModelFromFamily, TABLE_FAMILY_MEMBER_INFO);
                        valuesFromFamily.put(mobile_queue_id, patientUserInfoModel.getMobileQueueId());
                        valuesFromFamily.put(family_members_names, patientUserInfoModel.getFamilyMembersNames());
//						if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
//							values.put(status, "" + patientUserInfoModel.getStatus());
//						} else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
//							values.put(status, "" + patientUserInfoModel.getStatus());
//						}
                        valuesFromFamily.put(status, "" + patientUserInfoModel.getStatus());

                        valuesFromFamily.put(registration_status, patientUserInfoModel.getRegistrationStatus());
                        if (patientUserInfoModel.getMissedCallTime() > 0){
                            valuesFromFamily.put(missed_call_time, patientUserInfoModel.getMissedCallTime());
                        }
                        valuesFromFamily.put(record_status, patientUserInfoModel.getRecordStatus());

                        this.db.insert(TABLE_PATIENT_INFO, null, valuesFromFamily);
                    }
                }
            }

        }
        if (patientUserInfoModel1 != null){
            //	values.put(mobile_queue_id,patientUserInfoModel1.getMobileQueueId());
            long val = this.db.update(TABLE_PATIENT_INFO, values,
//					mobile + " = ?"

                    mobile1 + " =?" + " AND " + mobile2 + " =?" + " AND " + mobile3 + " =?"
                            + " AND  " + patientId + " = ? AND " + status + " =? AND " + mobile_queue_id + " =?",
                    new String[] {patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(),
                            patientUserInfoModel.getMobile3(), patientUserInfoModel.getPatientId(),
                            patientUserInfoModel.getStatus() + "", patientUserInfoModel.getMobileQueueId()});
            Logger.debug(val + "");
        }
        if (patientUserInfoModel2 == null && patientUserInfoModel1 == null && patientUserInfoModel3 == null
                && patientUserInfoModelFromFamily == null){
            values.put(mobile_queue_id, patientUserInfoModel.getMobileQueueId());
//			if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
//				values.put(status, "" + patientUserInfoModel.getStatus());
//			} else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
//				values.put(status, "" + patientUserInfoModel.getStatus());
//			}
            values.put(status, "" + patientUserInfoModel.getStatus());
            this.db.insert(TABLE_PATIENT_INFO, null, values);
        }


        context.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);

        return true;

    }

    public long insertPatientInfoByID(PatientUserInfoModel patientUserInfoModel, long rowId, boolean isNewPatient) {
        long count = 0;
        ContentValues values = getPatientInfoModelContentValues(patientUserInfoModel, TABLE_PATIENT_INFO);
        if (!this.db.isOpen()){
            openDatabase();
        }
        if (isNewPatient){
            PatientUserInfoModel patientUserInfoModelForMobileAndStatus = null;
            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                patientUserInfoModelForMobileAndStatus = getPatientInfoOnMobileAndStatus(
//						patientUserInfoModel.getMobile(),
                        patientUserInfoModel.getMobile1(),
                        patientUserInfoModel.getMobile2(),
                        patientUserInfoModel.getMobile3(),
                        AppConstants.RECEPTION_PRESENT);
            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                patientUserInfoModelForMobileAndStatus = getPatientInfoOnMobileAndStatus(
//						patientUserInfoModel.getMobile(),

                        patientUserInfoModel.getMobile1(),
                        patientUserInfoModel.getMobile2(),
                        patientUserInfoModel.getMobile3(),
                        AppConstants.DOCTOR_PRESENT);
            }

            if (patientUserInfoModelForMobileAndStatus != null){
                return 0;
            }

        }

        if (patientUserInfoModel.getMobileQueueId() == null){
            patientUserInfoModel.setMobileQueueId("0");
        }
        if (patientUserInfoModel.getPatientId() == null){
            patientUserInfoModel.setPatientId("0");
        }
        PatientUserInfoModel patientUserInfoModel1 =
                getPatientInfo(patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(),
                        patientUserInfoModel.getMobile3(), patientUserInfoModel.getPatientId(),
                        patientUserInfoModel.getStatus(), patientUserInfoModel.getMobileQueueId());
        if (patientUserInfoModel1 == null){
            patientUserInfoModel2 = getPatientInfoOnMobile(patientUserInfoModel.getMobile1(),
                    patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3(),
                    patientUserInfoModel.getStatus(), patientUserInfoModel.getMobileQueueId());
            if (patientUserInfoModel2 != null){
//				count = this.db.update(TABLE_PATIENT_INFO, values,
//						mobile + " =? AND " + status + " =? AND " + mobile_queue_id + " =?",
//						new String[] {patientUserInfoModel.getMobile(), patientUserInfoModel.getStatus() + "", patientUserInfoModel.getMobileQueueId()});

                count = this.db.update(TABLE_PATIENT_INFO, values,
//						mobile + " =?"
                        mobile1 + " =?" + " AND " + mobile2 + " =?" + " AND " + mobile3 + " =?"
                                + " AND " + status + " =? AND " + mobile_queue_id + " =?",
                        new String[] {patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3(),
                                patientUserInfoModel.getStatus() + "", patientUserInfoModel.getMobileQueueId()});

                Logger.debug(count + "");
            } else {
                patientUserInfoModel3 = getPatientInfoOnMobileQueueId(patientUserInfoModel.getMobileQueueId());
                if (patientUserInfoModel3 != null){
                    count = this.db.update(TABLE_PATIENT_INFO, values, mobile_queue_id + " =?", new String[] {patientUserInfoModel.getMobileQueueId()});
                    Logger.debug(count + "");
                } else {
                    patientUserInfoModelFromFamily = getFamilyMemberInfoByMobileNumber(patientUserInfoModel.getMobile1(),
                            patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3());
                    if (patientUserInfoModelFromFamily != null){

                        ContentValues valuesFromFamily = getPatientInfoModelContentValues(patientUserInfoModelFromFamily, TABLE_FAMILY_MEMBER_INFO);
                        valuesFromFamily.put(mobile_queue_id, patientUserInfoModel.getMobileQueueId());
//						if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
//							values.put(status, "" + patientUserInfoModel.getStatus());
//						} else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
//							values.put(status, "" + patientUserInfoModel.getStatus());
//						}
                        valuesFromFamily.put(status, "" + patientUserInfoModel.getStatus());

                        valuesFromFamily.put(registration_status, patientUserInfoModel.getRegistrationStatus());
                        if (patientUserInfoModel.getMissedCallTime() > 0){
                            valuesFromFamily.put(missed_call_time, patientUserInfoModel.getMissedCallTime());
                        }
                        valuesFromFamily.put(record_status, patientUserInfoModel.getRecordStatus());

                        this.db.insert(TABLE_PATIENT_INFO, null, valuesFromFamily);
                    }
                }
            }

        }
        if (patientUserInfoModel1 != null){
            int update = this.db.update(TABLE_PATIENT_INFO, values,
//					mobile + " = ? "
                    mobile1 + " =?" + " AND " + mobile2 + " =?" + " AND " + mobile3 + " =?"
                            + " AND  " + patientId + " = ? AND " + status + " =? AND " + mobile_queue_id + " =?",
                    new String[] {patientUserInfoModel.getMobile1(), patientUserInfoModel.getMobile2(), patientUserInfoModel.getMobile3(),
                            patientUserInfoModel.getPatientId(), patientUserInfoModel.getStatus() + "", patientUserInfoModel.getMobileQueueId()});
            Logger.debug(update + "");
        }
        if (patientUserInfoModel2 == null && patientUserInfoModel1 == null && patientUserInfoModel3 == null
                && patientUserInfoModelFromFamily == null){
            values.put(mobile_queue_id, patientUserInfoModel.getMobileQueueId());
            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                values.put(status, "" + patientUserInfoModel.getStatus());
            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                values.put(status, "" + patientUserInfoModel.getStatus());
            }
            count = this.db.insert(TABLE_PATIENT_INFO, null, values);
        }

        context.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);
        return count;
    }

    @NonNull
    private ContentValues getPatientInfoModelContentValues(PatientUserInfoModel patientUserInfoModel, String forDbTable) {
        ContentValues values = new ContentValues();
        values.put(patientId, patientUserInfoModel.getPatientId());
        values.put(first_name, patientUserInfoModel.getFirstName());
        values.put(last_name, patientUserInfoModel.getLastName());
        values.put(middle_name, patientUserInfoModel.getMiddleName());
        values.put(mobile1, patientUserInfoModel.getMobile1());
        values.put(mobile2, patientUserInfoModel.getMobile2());
        values.put(mobile3, patientUserInfoModel.getMobile3());
        values.put(date_of_birth, patientUserInfoModel.getDateOfBirth());

        if (patientUserInfoModel != null && patientUserInfoModel.getGeneralInfo() != null){
            if (patientUserInfoModel.getGeneralInfo().getGender() != null){
                values.put(gender, patientUserInfoModel.getGeneralInfo().getGender());
            } else {
                values.put(gender, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getProfileImage() != null){
                values.put(profile_image, patientUserInfoModel.getGeneralInfo().getProfileImage());
            } else {
                values.put(profile_image, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getProfileImageThumbnail() != null){
                values.put(profile_image_thumbnail, patientUserInfoModel.getGeneralInfo().getProfileImageThumbnail());
            } else {
                values.put(profile_image_thumbnail, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getAddressLine1() != null){
                values.put(address_line_1, patientUserInfoModel.getGeneralInfo().getAddressLine1());
            } else {
                values.put(address_line_1, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getAddressLine2() != null){
                values.put(address_line_2, patientUserInfoModel.getGeneralInfo().getAddressLine2());
            } else {
                values.put(address_line_2, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getAddressLine3() != null){
                values.put(address_line_3, patientUserInfoModel.getGeneralInfo().getAddressLine3());
            } else {
                values.put(address_line_3, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getAddressLine4() != null){
                values.put(address_line_4, patientUserInfoModel.getGeneralInfo().getAddressLine4());
            } else {
                values.put(address_line_4, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getDistrict() != null){
                values.put(district, patientUserInfoModel.getGeneralInfo().getDistrict());
            } else {
                values.put(district, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getState() != null){
                values.put(state, patientUserInfoModel.getGeneralInfo().getState());
            } else {
                values.put(state, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getPincode() != null){
                values.put(pin_code, patientUserInfoModel.getGeneralInfo().getPincode());
            } else {
                values.put(pin_code, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getIdProofType() != null){
                values.put(id_proof_type, patientUserInfoModel.getGeneralInfo().getIdProofType());
            } else {
                values.put(id_proof_type, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getIdProofNo() != null){
                values.put(id_proof_no, patientUserInfoModel.getGeneralInfo().getIdProofNo());
            } else {
                values.put(id_proof_no, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getBloodGroup() != null){
                values.put(blood_group, patientUserInfoModel.getGeneralInfo().getBloodGroup());
            } else {
                values.put(blood_group, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getEmergencyContactName() != null){
                values.put(emergency_contact_name, patientUserInfoModel.getGeneralInfo().getEmergencyContactName());
            } else {
                values.put(emergency_contact_name, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getEmergencyContactNumber() != null){
                values.put(emergency_contact_no, patientUserInfoModel.getGeneralInfo().getEmergencyContactNumber());
            } else {
                values.put(emergency_contact_no, "");
            }

            if (patientUserInfoModel.getGeneralInfo().getTaluka() != null){
                values.put(taluka, patientUserInfoModel.getGeneralInfo().getTaluka());
            } else {
                values.put(taluka, "");
            }
            if (patientUserInfoModel.getGeneralInfo().getArea() != null){
                values.put(area, patientUserInfoModel.getGeneralInfo().getArea());
            } else {
                values.put(area, "");
            }

        }
        values.put(mr, patientUserInfoModel.getMr());
        values.put(family_id, patientUserInfoModel.getFamilyId());
        values.put(family_count, patientUserInfoModel.getFamilyCount());
        values.put(email, patientUserInfoModel.getEmail());

        values.put(sync_status, patientUserInfoModel.isSyncStatus());

//        values.put(subscription_type, patientUserInfoModel.getSubscriptionType());
        values.put(display_patient_id, patientUserInfoModel.getDisplayId());
        values.put(hin, patientUserInfoModel.getHin());


        if (forDbTable.equals(TABLE_PATIENT_INFO)){
            values.put(registration_status, patientUserInfoModel.getRegistrationStatus());
            if (patientUserInfoModel.getMissedCallTime() > 0){
                values.put(missed_call_time, patientUserInfoModel.getMissedCallTime());
            }
            values.put(status, patientUserInfoModel.getStatus());
            values.put(record_status, patientUserInfoModel.getRecordStatus());

            if (patientUserInfoModel.getFamilyMembersNames() != null){
                values.put(family_members_names, patientUserInfoModel.getFamilyMembersNames());
            } else {
                values.put(family_members_names, "");
            }
        }

        return values;
    }

    public List<PatientUserInfoModel> getAllPatientInfo(String pStatus) {
		/*List<PatientUserInfoModel> patientPresentQueueModelList = new ArrayList<>();
		           String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + status + "=?" + " ORDER BY " + missed_call_time + " ASC ";
		                   String[] whereParams = new String[] {pStatus + ""};
		                   Cursor cursor = this.db.rawQuery(query, whereParams);
		                   if (cursor != null && (cursor.moveToFirst())){

		                        do {
		                                PatientUserInfoModel patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
		                                patientPresentQueueModelList.add(patientUserInfoModel);

		                        } while ((cursor.moveToNext()));
		                   }

		                   if (cursor != null && !cursor.isClosed()){
		                        cursor.close();
		                   }
		                   return patientPresentQueueModelList;*/
        return null;

    }

    public List<PatientUserInfoModel> getAllPatientInfoForToday(String pStatus) {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endTime = calendar.getTimeInMillis();

        List<PatientUserInfoModel> patientUserInfoModels = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + status + "=?" +
                "AND " + missed_call_time + " BETWEEN " + startTime + " AND " + endTime +
                " ORDER BY " + missed_call_time + " ASC ";
        String[] whereParams = new String[] {pStatus + ""};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                PatientUserInfoModel patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
                patientUserInfoModels.add(patientUserInfoModel);

            } while ((cursor.moveToNext()));
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientUserInfoModels;

    }

    public List<PatientUserInfoModel> getPatientInfoForpatientID(ArrayList<QueueDao> queueDaos) {

        List<PatientUserInfoModel> patientUserInfoModels = new ArrayList<>();

//		String query = "SELECT * FROM " + TABLE_PATIENT + " WHERE id = '" + +
//				"AND " + missed_call_time + " BETWEEN " + startTime + " AND " + endTime +
//				" ORDER BY " + missed_call_time + " ASC ";
//		String[] whereParams = new String[] {pStatus + ""};
//		Cursor cursor = this.db.rawQuery(query, whereParams);
//		if (cursor != null && (cursor.moveToFirst())){
//
//			do {
//				PatientUserInfoModel patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
//				patientPresentQueueModelList.add(patientUserInfoModel);
//
//			} while ((cursor.moveToNext()));
//		}
//		if (cursor != null && !cursor.isClosed()){
//			cursor.close();
//		}
        return patientUserInfoModels;

    }

    @NonNull
    private PatientUserInfoModel getPatientUserInfoModelFromCursor(Cursor cursor, String forDBTable) {
        PatientUserInfoModel patientUserInfoModel = new PatientUserInfoModel();
        GeneralUserInfoModel generalInfo = new GeneralUserInfoModel();
        patientUserInfoModel.setGeneralInfo(generalInfo);

        patientUserInfoModel.setRowId(cursor.getLong(cursor.getColumnIndex(id)));
//		patientUserInfoModel.setMobile(cursor.getString(cursor.getColumnIndex(mobile)));
        patientUserInfoModel.setMobile1(cursor.getString(cursor.getColumnIndex(mobile1)));
        patientUserInfoModel.setMobile2(cursor.getString(cursor.getColumnIndex(mobile2)));
        patientUserInfoModel.setMobile3(cursor.getString(cursor.getColumnIndex(mobile3)));

        patientUserInfoModel.setDateOfBirth(cursor.getLong(cursor.getColumnIndex(date_of_birth)));
        patientUserInfoModel.setEmail(cursor.getString(cursor.getColumnIndex(email)));
        patientUserInfoModel.setFirstName(cursor.getString(cursor.getColumnIndex(first_name)));
        patientUserInfoModel.setLastName((cursor.getString(cursor.getColumnIndex(last_name))));
        patientUserInfoModel.setMiddleName((cursor.getString(cursor.getColumnIndex(middle_name))));
        patientUserInfoModel.getGeneralInfo().setGender((cursor.getString(cursor.getColumnIndex(gender))));
        patientUserInfoModel.setPatientId((cursor.getString(cursor.getColumnIndex(patientId))));
        patientUserInfoModel.getGeneralInfo().setProfileImageThumbnail((cursor.getString(cursor.getColumnIndex(profile_image_thumbnail))));
        patientUserInfoModel.getGeneralInfo().setProfileImage((cursor.getString(cursor.getColumnIndex(profile_image))));


        patientUserInfoModel.getGeneralInfo().setAddressLine1((cursor.getString(cursor.getColumnIndex(address_line_1))));
        patientUserInfoModel.getGeneralInfo().setAddressLine2((cursor.getString(cursor.getColumnIndex(address_line_2))));
        patientUserInfoModel.getGeneralInfo().setAddressLine3((cursor.getString(cursor.getColumnIndex(address_line_3))));
        patientUserInfoModel.getGeneralInfo().setAddressLine4((cursor.getString(cursor.getColumnIndex(address_line_4))));
        patientUserInfoModel.getGeneralInfo().setDistrict((cursor.getString(cursor.getColumnIndex(district))));
        patientUserInfoModel.getGeneralInfo().setState((cursor.getString(cursor.getColumnIndex(state))));
        patientUserInfoModel.getGeneralInfo().setPincode((cursor.getString(cursor.getColumnIndex(pin_code))));
//		patientUserInfoModel.setId("" + cursor.getLong(cursor.getColumnIndex(id)));


        patientUserInfoModel.setDisplayId(cursor.getString(cursor.getColumnIndex(display_patient_id)));
        patientUserInfoModel.getGeneralInfo().setIdProofType(cursor.getString(cursor.getColumnIndex(id_proof_type)));
        patientUserInfoModel.getGeneralInfo().setIdProofNo(cursor.getString(cursor.getColumnIndex(id_proof_no)));
        patientUserInfoModel.getGeneralInfo().setBloodGroup(cursor.getString(cursor.getColumnIndex(blood_group)));
        patientUserInfoModel.getGeneralInfo().setEmergencyContactName(cursor.getString(cursor.getColumnIndex(emergency_contact_name)));
        patientUserInfoModel.getGeneralInfo().setEmergencyContactNumber(cursor.getString(cursor.getColumnIndex(emergency_contact_no)));
        patientUserInfoModel.setMr(cursor.getString(cursor.getColumnIndex(mr)));
        patientUserInfoModel.setFamilyId(cursor.getString(cursor.getColumnIndex(family_id)));
        patientUserInfoModel.setFamilyCount(cursor.getInt(cursor.getColumnIndex(family_count)));
        patientUserInfoModel.getGeneralInfo().setTaluka(cursor.getString(cursor.getColumnIndex(taluka)));
        patientUserInfoModel.getGeneralInfo().setArea(cursor.getString(cursor.getColumnIndex(area)));
        patientUserInfoModel.getGeneralInfo().setProfileImage(cursor.getString(cursor.getColumnIndex(profile_image)));
        patientUserInfoModel.getGeneralInfo().setProfileImageThumbnail(cursor.getString(cursor.getColumnIndex(profile_image_thumbnail)));
        patientUserInfoModel.setHin(cursor.getString(cursor.getColumnIndex(hin)));

        if (forDBTable.equals(TABLE_PATIENT_INFO)){
            patientUserInfoModel.setAppointmentEndTime((cursor.getLong(cursor.getColumnIndex(appointment_end_time))));
            patientUserInfoModel.setAppointmentStartTime(cursor.getLong(cursor.getColumnIndex(appointment_time)));
            patientUserInfoModel.setMissedCallTime(cursor.getLong(cursor.getColumnIndex(missed_call_time)));
            patientUserInfoModel.setRegistrationStatus(cursor.getInt(cursor.getColumnIndex(registration_status)));
            patientUserInfoModel.setStatus(cursor.getInt(cursor.getColumnIndex(status)));
            patientUserInfoModel.setMobileQueueId(cursor.getString(cursor.getColumnIndex(mobile_queue_id)));
            patientUserInfoModel.setFamilyMembersNames(cursor.getString(cursor.getColumnIndex(family_members_names)));
        } else if (forDBTable.equals(TABLE_FAMILY_MEMBER_INFO)){
            if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.RECEPTION)){
                patientUserInfoModel.setStatus(Integer.parseInt(AppConstants.RECEPTION_PRESENT));
            } else if (appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){
                patientUserInfoModel.setStatus(Integer.parseInt(AppConstants.DOCTOR_PRESENT));
            }
        }

//		if(cursor != null && !cursor.isClosed()){
//			cursor.close();
//		}
        return patientUserInfoModel;
    }

    //=============== DONE
    public PatientUserInfoModel getPatientInfo(String mobileNo) {
        String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
//				+ mobile + "=?";
                + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?";
        String[] whereParams = new String[] {mobileNo, mobileNo, mobileNo};
        PatientUserInfoModel patientUserInfoModel = null;

        if (mobileNo != null && !mobileNo.trim().isEmpty()){
            Cursor cursor = this.db.rawQuery(query, whereParams);

            if (cursor != null && cursor.moveToFirst()){
                patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);

            }
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return patientUserInfoModel;
    }

    public PatientUserInfoModel getPatientInfo(String mobileNo1, String mobileNo2, String mobileNo3, String p_id) {

//		String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + mobile + "=?  AND  " + patientId + " = ?  ";
//		String[] whereParams = new String[] {mobileNo, p_id};

        String query = "";
        String[] whereParams = null;

        if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ?  ";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    p_id
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ?  ";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    p_id
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ?  ";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo3, mobileNo3, mobileNo3,
                    p_id
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ?  ";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    p_id
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ?  ";
            whereParams = new String[] {mobileNo1, mobileNo1, mobileNo1, p_id};
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ?  ";
            whereParams = new String[] {mobileNo2, mobileNo2, mobileNo2, p_id};
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ?  ";
            whereParams = new String[] {mobileNo3, mobileNo3, mobileNo3, p_id};
        } else {
            return null;
        }

        PatientUserInfoModel patientUserInfoModel = null;

        if (p_id != null){
            Cursor cursor = this.db.rawQuery(query, whereParams);
            if (cursor != null && cursor.moveToFirst()){
                patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
            }
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return patientUserInfoModel;
    }

    public PatientUserInfoModel getPatientInfo(String mobileNo1, String mobileNo2, String mobileNo3,
                                               String p_id, int pStatus, String mobileQueueId) {

//		String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + mobile + "=?  AND  " + patientId + " = ? AND " + status + "=?AND " + mobile_queue_id + " =?";
//		String[] whereParams = new String[] {mobileNo, p_id, pStatus + "", mobileQueueId};

        String query = "";
        String[] whereParams = null;

        if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){

            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    p_id, pStatus + "", mobileQueueId
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    p_id, pStatus + "", mobileQueueId
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo3, mobileNo3, mobileNo3,
                    p_id, pStatus + "", mobileQueueId
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    p_id, pStatus + "", mobileQueueId
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    p_id, pStatus + "", mobileQueueId
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    p_id, pStatus + "", mobileQueueId
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo3, mobileNo3, mobileNo3,
                    p_id, pStatus + "", mobileQueueId
            };
        } else {
            return null;
        }

        PatientUserInfoModel patientUserInfoModel = null;

        if (p_id != null || mobileQueueId != null){
            Cursor cursor = this.db.rawQuery(query, whereParams);

            if (cursor != null && cursor.moveToFirst()){

                patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);

            }
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return patientUserInfoModel;
    }

    public PatientUserInfoModel getPatientInfoOnMobile(String mobileNo1, String mobileNo2, String mobileNo3, int pStatus, String mobileQueueId) {

//		String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + mobile + "=? AND " + status + "=? AND " + mobile_queue_id + "=?";
//		String[] whereParams = new String[] {mobileNo, pStatus + "", mobileQueueId};

        String query = "";
        String[] whereParams = null;

        if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus + "", mobileQueueId
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    pStatus + "", mobileQueueId
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus + "", mobileQueueId
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus + "", mobileQueueId
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    pStatus + "", mobileQueueId
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    pStatus + "", mobileQueueId
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND  " + patientId + " = ? AND " + status + "=? AND " + mobile_queue_id + " =?";
            whereParams = new String[] {
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus + "", mobileQueueId
            };
        } else {
            return null;
        }

        PatientUserInfoModel patientUserInfoModel = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && cursor.moveToFirst()){
            patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);

        }

        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientUserInfoModel;
    }

    public PatientUserInfoModel getPatientInfoOnMobileAndStatus(String mobileNo1, String mobileNo2, String mobileNo3, String pStatus) {

//		String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + mobile + "=? AND " + status + "=?";
//		String[] whereParams = new String[] {mobileNo, pStatus};

        String query = "";
        String[] whereParams = null;

        if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND " + status + "=?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND " + status + "=?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    pStatus
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND " + status + "=?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND " + status + "=?";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND " + status + "=?";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    pStatus
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND " + status + "=?";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    pStatus
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")"
                    + " AND " + status + "=?";
            whereParams = new String[] {
                    mobileNo3, mobileNo3, mobileNo3,
                    pStatus
            };
        } else {
            return null;
        }

        PatientUserInfoModel patientUserInfoModel = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && cursor.moveToFirst()){
            patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
        }

        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientUserInfoModel;
    }

    public PatientUserInfoModel getPatientInfoOnMobile(String mobileNo) {
        String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?";
        String[] whereParams = new String[] {mobileNo, mobileNo, mobileNo};
        PatientUserInfoModel patientUserInfoModel = null;
        if (!mobileNo.trim().isEmpty()){
            Cursor cursor = this.db.rawQuery(query, whereParams);

            if (cursor != null && cursor.moveToFirst()){
                patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
            }
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return patientUserInfoModel;
    }

    public PatientUserInfoModel getPatientInfoOnMobileQueueId(String queue_id) {
        String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + mobile_queue_id + "=?";
        String[] whereParams = new String[] {queue_id};
        PatientUserInfoModel patientUserInfoModel = null;
        if (queue_id != null && !queue_id.trim().isEmpty()){
            Cursor cursor = this.db.rawQuery(query, whereParams);

            if (cursor != null && cursor.moveToFirst()){
                patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
            }

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return patientUserInfoModel;
    }

    public PatientUserInfoModel getPatientInfoByPatientID(String patientID) {
        String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + patientId + "=? ";
        String[] whereParams = new String[] {patientID};
        PatientUserInfoModel patientUserInfoModel = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && cursor.moveToFirst()){
            patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        if (patientUserInfoModel == null){
            String queryFamily = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE " + patientId + "=? ";
            String[] whereParamsFamily = new String[] {patientID};
            Cursor cursorFamily = this.db.rawQuery(queryFamily, whereParamsFamily);

            if (cursorFamily != null && cursorFamily.moveToFirst()){
                patientUserInfoModel = getPatientUserInfoModelFromCursor(cursorFamily, TABLE_FAMILY_MEMBER_INFO);
            }
            if (cursorFamily != null && !cursorFamily.isClosed()){
                cursorFamily.close();
            }
        }

        return patientUserInfoModel;
    }

    public int getPatientStatus(String pId) {
        String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + patientId + "=? ";
        String[] whereParams = new String[] {String.valueOf(pId)};
        PatientUserInfoModel patientUserInfoModel = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && cursor.moveToFirst()){
            patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientUserInfoModel.getStatus();
    }

    public PatientUserInfoModel getPatientInfoByRowID(long rowID) {
        String query = "SELECT * FROM " + TABLE_PATIENT_INFO + " WHERE " + id + "=? ";
        String[] whereParams = new String[] {String.valueOf(rowID)};
        PatientUserInfoModel patientUserInfoModel = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && cursor.moveToFirst()){
            patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_PATIENT_INFO);
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientUserInfoModel;
    }

    public int deleteOnePatientFromQueue(String mobileNo, String pId, String pStatus) {
        String[] whereParams1 = new String[] {pId, pStatus + ""};
        String[] whereParams2 = new String[] {mobileNo, mobileNo, mobileNo, pStatus + ""};
        int count = 0;
        if (mobileNo.trim().isEmpty()){
            count = this.db.delete(TABLE_PATIENT_INFO, patientId + " =? AND " + status + "=?", whereParams1);
        } else {
            count = this.db.delete(TABLE_PATIENT_INFO,
//					mobile + "=?"

                    mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                            + " AND " + status + "=?", whereParams2);
        }

        context.getContentResolver().

                notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);

        return count;
    }

    public List<PatientUserInfoModel> getAllFamilyMembers(String familyId) {

        List<PatientUserInfoModel> patientUserInfoModels = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE " + this.family_id + "=?";
        String[] whereParams = new String[] {familyId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                PatientUserInfoModel patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_FAMILY_MEMBER_INFO);
                patientUserInfoModels.add(patientUserInfoModel);

            } while ((cursor.moveToNext()));
        }
        return patientUserInfoModels;

    }

    public PatientUserInfoModel getFamilyMemberInfoByPatientID(String pId) {

        String query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE " + patientId + "=? ";
        String[] whereParams = new String[] {pId};
        PatientUserInfoModel patientUserInfoModel = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && cursor.moveToFirst()){
            patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_FAMILY_MEMBER_INFO);
        }

        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientUserInfoModel;
    }

    public PatientUserInfoModel getFamilyMemberInfoByMobileNumber(String mobileNo1, String mobileNo2, String mobileNo3) {

        String query = "";
        String[] whereParams = null;

        if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo2, mobileNo2, mobileNo2
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1,
                    mobileNo3, mobileNo3, mobileNo3
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?"
                    + " OR " + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2,
                    mobileNo3, mobileNo3, mobileNo3
            };
        } else if (mobileNo1 != null && !mobileNo1.isEmpty()
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo1, mobileNo1, mobileNo1
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && mobileNo2 != null && !mobileNo2.isEmpty()
                && (mobileNo3 == null || (mobileNo3 != null && mobileNo3.isEmpty()))){
            query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo2, mobileNo2, mobileNo2
            };
        } else if ((mobileNo1 == null || (mobileNo1 != null && mobileNo1.isEmpty()))
                && (mobileNo2 == null || (mobileNo2 != null && mobileNo2.isEmpty()))
                && mobileNo3 != null && !mobileNo3.isEmpty()){
            query = "SELECT * FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE "
                    + "(" + mobile1 + "=?" + " OR " + mobile2 + "=?" + " OR " + mobile3 + "=?" + ")";
            whereParams = new String[] {
                    mobileNo3, mobileNo3, mobileNo3
            };
        } else {
            return null;
        }

        PatientUserInfoModel patientUserInfoModel = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);

        if (cursor != null && cursor.moveToFirst()){
            patientUserInfoModel = getPatientUserInfoModelFromCursor(cursor, TABLE_FAMILY_MEMBER_INFO);
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return patientUserInfoModel;
    }

    public int getFamilyMemberCountForFamilyId(String familyId) {

        int count = 0;
        String[] whereParams = new String[] {familyId};
        String query = "SELECT " + family_count + " FROM " + TABLE_FAMILY_MEMBER_INFO + " WHERE " + family_id + "=?";
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && cursor.moveToFirst()){
            count = cursor.getInt(cursor.getColumnIndex(family_count));
        }

        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return count;
    }

    public int deleteFamilyMember(String pId, String familyId, int currentFamilyCount) {

        String[] whereParams = new String[] {pId};
        int count = this.db.delete(TABLE_FAMILY_MEMBER_INFO, patientId + " =?", whereParams);

        if (count > 0){

            int revisedFamilyMemberCount = currentFamilyCount - count;
            setFamilyMemberCountForFamilyId(familyId, revisedFamilyMemberCount);
            setFamilyMemberCountForPatientQueue(familyId, revisedFamilyMemberCount);
            context.getContentResolver().notifyChange(AppConstants.FAMILY_CONTENT_URI, null);
            context.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);
        }

        return count;
    }

    public void setFamilyMemberCountForFamilyId(String familyId, int familyCount) {

        ContentValues values = new ContentValues();
        values.put(family_count, familyCount);

        this.db.update(TABLE_FAMILY_MEMBER_INFO, values, family_id + " = ?", new String[] {familyId});
        context.getContentResolver().notifyChange(AppConstants.FAMILY_CONTENT_URI, null);
    }

    public void setFamilyMemberCountForPatientQueue(String familyId, int familyCount) {

        ContentValues values = new ContentValues();
        values.put(family_count, familyCount);

        this.db.update(TABLE_PATIENT_INFO, values, family_id + " = ?", new String[] {familyId});
        context.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);
    }

    public boolean insertFamilyMember(PatientUserInfoModel patientUserInfoModel) {

        ContentValues values = getPatientInfoModelContentValues(patientUserInfoModel, TABLE_FAMILY_MEMBER_INFO);
        if (patientUserInfoModel.getGeneralInfo() != null
                && patientUserInfoModel.getGeneralInfo().getProfileImageThumbnail() != null){
            values.put(profile_image_thumbnail, patientUserInfoModel.getGeneralInfo().getProfileImageThumbnail());
        } else {
            values.put(profile_image_thumbnail, "");
        }
        if (patientUserInfoModel.getGeneralInfo() != null
                && patientUserInfoModel.getGeneralInfo().getProfileImage() != null){
            values.put(profile_image, patientUserInfoModel.getGeneralInfo().getProfileImage());
        } else {
            values.put(profile_image, "");
        }

        PatientUserInfoModel patientUserInfoModelExisting = getFamilyMemberInfoByPatientID(patientUserInfoModel.getPatientId());
        if (patientUserInfoModelExisting != null){
            this.db.update(TABLE_FAMILY_MEMBER_INFO, values, patientId + " =?", new String[] {patientUserInfoModel.getPatientId()});
        } else {
            this.db.insert(TABLE_FAMILY_MEMBER_INFO, null, values);
        }

        setFamilyMemberCountForPatientQueue(patientUserInfoModel.getFamilyId(), patientUserInfoModel.getFamilyCount());

        context.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);
        context.getContentResolver().notifyChange(AppConstants.FAMILY_CONTENT_URI, null);

        return true;

    }

    public void insertDocumentInfo(UserDocumentModel userDocumentModel, String queueId) {
        ContentValues values = new ContentValues();
        values.put(id, userDocumentModel.getId());
        values.put(user_id, userDocumentModel.getUser_id());
        values.put(mobile_queue_id, queueId);
        values.put(created_by, userDocumentModel.getCreated_by());
        values.put(created_at, userDocumentModel.getCreated_at());
        values.put(record_status, userDocumentModel.getRecord_status());
        values.put(document_type_id, userDocumentModel.getDocument_type_id());
        values.put(type_name, userDocumentModel.getType_name());
        values.put(doc_url, userDocumentModel.getDoc_url());
        values.put(doc_title, userDocumentModel.getDoc_title());
        values.put(doc_thumb_url, userDocumentModel.getDoc_thumb_url());


        UserDocumentModel tempModel = getUserDocumentForDocumentUrl(userDocumentModel.getDoc_url());

        if (tempModel != null){
            values.put(mobile_queue_id, tempModel.getMobileQueueId());
            this.db.update(TABLE_USER_DOCUMENTS, values, doc_url + " = ?", new String[] {userDocumentModel.getDoc_url()});
        } else {

            this.db.insert(TABLE_USER_DOCUMENTS, null, values);
        }

        context.getContentResolver().notifyChange(AppConstants.PATIENT_DOCUMENT_URI, null);
    }

    public UserDocumentModel getUserDocumentForDocumentUrl(String documentUrl) {
        String query = "SELECT * FROM " + TABLE_USER_DOCUMENTS + " WHERE " + doc_url + "=? ORDER BY " + created_at + " DESC";
        String[] whereParams = new String[] {documentUrl};
        UserDocumentModel model = null;
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && cursor.moveToFirst()){
            model = new UserDocumentModel();
            model.setId(cursor.getString(cursor.getColumnIndex(id)));
            model.setUser_id(cursor.getString(cursor.getColumnIndex(user_id)));
            model.setCreated_by(cursor.getString(cursor.getColumnIndex(created_by)));
            model.setCreated_at(cursor.getLong(cursor.getColumnIndex(created_at)));
            model.setRecord_status(cursor.getString(cursor.getColumnIndex(record_status)));
            model.setDocument_type_id(cursor.getString(cursor.getColumnIndex(document_type_id)));
            model.setType_name(cursor.getString(cursor.getColumnIndex(type_name)));
            model.setDoc_url(cursor.getString(cursor.getColumnIndex(doc_url)));
            model.setDoc_title(cursor.getString(cursor.getColumnIndex(doc_title)));
            model.setDoc_thumb_url(cursor.getString(cursor.getColumnIndex(doc_thumb_url)));
        }

        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return model;

    }

    public List<UserDocumentModel> getUserDocumentModelsForUserId(String userId) {
        String query = "SELECT * FROM " + TABLE_USER_DOCUMENTS + " WHERE " + user_id + "=?";
        String[] whereParams = new String[] {userId};
        List<UserDocumentModel> models = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && cursor.moveToFirst()){
            do {
                UserDocumentModel model = new UserDocumentModel();
                model.setId(cursor.getString(cursor.getColumnIndex(id)));
                model.setUser_id(cursor.getString(cursor.getColumnIndex(user_id)));
                model.setCreated_by(cursor.getString(cursor.getColumnIndex(created_by)));
                model.setCreated_at(cursor.getLong(cursor.getColumnIndex(created_at)));
                model.setRecord_status(cursor.getString(cursor.getColumnIndex(record_status)));
                model.setDocument_type_id(cursor.getString(cursor.getColumnIndex(document_type_id)));
                model.setType_name(cursor.getString(cursor.getColumnIndex(type_name)));
                model.setDoc_url(cursor.getString(cursor.getColumnIndex(doc_url)));
                model.setDoc_title(cursor.getString(cursor.getColumnIndex(doc_title)));
                model.setDoc_thumb_url(cursor.getString(cursor.getColumnIndex(doc_thumb_url)));
                models.add(model);
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return models;
    }

    public ArrayList<UserDocumentModel> getUserDocumentModelsForUserIdAndType(String userId, String type) {
        String query = "SELECT * FROM " + TABLE_USER_DOCUMENTS + " WHERE " + user_id + "=? AND " + type_name + "=? ORDER BY " + created_at + " DESC";
        String[] whereParams = new String[] {userId, type};
        ArrayList<UserDocumentModel> models = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && cursor.moveToFirst()){
            do {
                UserDocumentModel model = new UserDocumentModel();
                model.setId(cursor.getString(cursor.getColumnIndex(id)));
                model.setUser_id(cursor.getString(cursor.getColumnIndex(user_id)));
                model.setCreated_by(cursor.getString(cursor.getColumnIndex(created_by)));
                model.setCreated_at(cursor.getLong(cursor.getColumnIndex(created_at)));
                model.setRecord_status(cursor.getString(cursor.getColumnIndex(record_status)));
                model.setDocument_type_id(cursor.getString(cursor.getColumnIndex(document_type_id)));
                model.setType_name(cursor.getString(cursor.getColumnIndex(type_name)));
                model.setDoc_url(cursor.getString(cursor.getColumnIndex(doc_url)));
                model.setDoc_title(cursor.getString(cursor.getColumnIndex(doc_title)));
                model.setDoc_thumb_url(cursor.getString(cursor.getColumnIndex(doc_thumb_url)));
                model.setMobileQueueId(cursor.getString(cursor.getColumnIndex(mobile_queue_id)));
                models.add(model);
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return models;
    }

    public int deleteDocument(String docId) {
        String[] whereParams = new String[] {docId};
        int count = this.db.delete(TABLE_USER_DOCUMENTS, id + "=? ", whereParams);
        return count;
    }

    public void deletePatient(String mobileQueueId) {
        String[] whereParams = new String[] {mobileQueueId};
        int count = this.db.delete(TABLE_PATIENT_INFO, mobile_queue_id + "=? ", whereParams);
        context.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);
    }

    public void deletePatient(String pId, String status) {
        String[] whereParams = new String[] {pId};
        int count = this.db.delete(TABLE_PATIENT_INFO, patientId + "=? ", whereParams);
        Logger.debug("PatientDeleted " + count);
        context.getContentResolver().notifyChange(AppConstants.PATIENT_QUEUE_CONTENT_URI, null);
    }

    public List<UserDocumentModel> getUserDocumentModelsUploadedBetweenDates(long from, long to, String userId, String type) {
        String query = "SELECT * FROM " + TABLE_USER_DOCUMENTS + " WHERE " + created_at + ">= ? OR " + created_at + "<=?  AND  user_id " + "=?  AND type_name" + " =? ORDER BY " + created_at + " ASC";

        Logger.debug("Filter query  " + query);
        String[] whereParams = new String[] {"" + from, "" + to, userId, type};
        List<UserDocumentModel> models = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(query, whereParams);
        Logger.debug("Query" + query);
        if (cursor != null && cursor.moveToFirst()){
            do {
                UserDocumentModel model = new UserDocumentModel();
                model.setId(cursor.getString(cursor.getColumnIndex(id)));
                model.setUser_id(cursor.getString(cursor.getColumnIndex(user_id)));
                model.setCreated_by(cursor.getString(cursor.getColumnIndex(created_by)));
                model.setCreated_at(cursor.getLong(cursor.getColumnIndex(created_at)));
                model.setRecord_status(cursor.getString(cursor.getColumnIndex(record_status)));
                model.setDocument_type_id(cursor.getString(cursor.getColumnIndex(document_type_id)));
                model.setType_name(cursor.getString(cursor.getColumnIndex(type_name)));
                model.setDoc_url(cursor.getString(cursor.getColumnIndex(doc_url)));
                model.setDoc_title(cursor.getString(cursor.getColumnIndex(doc_title)));
                model.setDoc_thumb_url(cursor.getString(cursor.getColumnIndex(doc_thumb_url)));
                model.setMobileQueueId(cursor.getString(cursor.getColumnIndex(mobile_queue_id)));
                models.add(model);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return models;
    }

    public void deleteDb() {

		/* String myPath = "dhb_db.db";
		   SQLiteDatabase.deleteDatabase(new File(context.getDatabasePath(DbHelper.DB_NAME).getAbsolutePath()));*/

//		db.execSQL("delete from " + TABLE_PATIENT_INFO);
//		db.execSQL("delete from " + TABLE_USER_DOCUMENTS);

        QueueDao queueDao = new QueueDao(getDb(), appPreferenceManager);
        queueDao.deleteAll();

        PatientQueueCountDao patientQueueCountDao = new PatientQueueCountDao(getDb());
        patientQueueCountDao.removeCountData();

       /* PatientHabitsDao patientHabitsDao = new PatientHabitsDao(getDb());
        patientHabitsDao.deleteAll();

        PatientAdviceDao patientAdviceDao = new PatientAdviceDao(getDb());
        patientAdviceDao.deleteAll();

        PatientSamplesDao patientSamplesDao = new PatientSamplesDao(getDb());
        patientSamplesDao.deleteAll();

        PatientChiefComplaintsDao patientChiefComplaintsDao = new PatientChiefComplaintsDao(getDb());
        patientChiefComplaintsDao.deleteAll();

        PatientAdviceGeneralInstructionDao patientAdviceGeneralInstructionDao = new PatientAdviceGeneralInstructionDao(getDb());
        patientAdviceGeneralInstructionDao.deleteAll();

        PatientAllergyDao patientAllergyDao = new PatientAllergyDao(getDb());
        patientAllergyDao.deleteAll();

        PatientPrescriptionDao patientPrescriptionDao = new PatientPrescriptionDao(getDb());
        patientPrescriptionDao.deleteAll();

        VitalsDao vitalsDao = new VitalsDao(getDb());
        vitalsDao.deleteAll();

        ObservationDao observationDao = new ObservationDao(getDb());
        observationDao.deleteAll();

        DiagnosisDao diagnosisDao = new DiagnosisDao(getDb());
        diagnosisDao.deleteAll();

        VisitDao visitDao = new VisitDao(getDb());
        visitDao.deleteAll();

        MedicineDao medicineDao = new MedicineDao(getDb());
        medicineDao.deleteAll();

        UserMedicineMapDao userMedicineMapDao = new UserMedicineMapDao(getDb());
        userMedicineMapDao.deleteAll();

        TemplateDao templateDao = new TemplateDao(getDb());
        templateDao.deleteAll();

        TemplateMedicineMappingDao templateMedicineMappingDao = new TemplateMedicineMappingDao(getDb());
        templateMedicineMappingDao.deleteAll();*/
    }

}
