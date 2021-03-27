package com.dhb.dao.daomodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhb.models.Document;
import com.dhb.utils.AppConstants;
import com.dhb.utils.DateUtils;
import com.dhb.utils.Logger;

import java.util.ArrayList;

public class DocumentDao {

    private SQLiteDatabase db;


    // DB TABLE NAME

    public static String TABLE_NAME = "documents";


    // DB TABLE COLUMN INFO

    public static final String ID = "id";
    public static final String PATIENT_ID = "user_id";
    public static final String UPLOADED_BY = "uploaded_by";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_THUMB = "file_thumb";
    public static final String MIME_TYPE = "mime_type";
    public static final String FILE_SIZE = "file_size";
    public static final String UPLOAD_DATE = "upload_date";
    public static final String FILE_PATH = "file_path";
    public static final String FILE_LOCAL_PATH = "file_local_path";
    public static final String VISIT_ID = "visit_id";
    public static final String DOCUMENT_TYPE_ID = "document_type_id";
    public static final String RECORD_STATUS = "record_status";
    public static final String CREATED_AT = "created_at";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_AT = "updated_at";
    public static final String UPDATED_BY = "updated_by";
    public static final String SYNC_STATUS = "sync_status";
    public static final String SYNC_ACTION = "sync_action";

    // Model info


    // Constructors

    public DocumentDao() {
    }

    public DocumentDao(SQLiteDatabase db) {
        this.db = db;
    }

    // get model from cursor

    private Document getDocumentFromCursor(Cursor cursor) {
        Document document = new Document();
        document.setId(cursor.getString(cursor.getColumnIndex(ID)));
        document.setPatientId(cursor.getString(cursor.getColumnIndex(PATIENT_ID)));
        document.setUploadedBy(cursor.getString(cursor.getColumnIndex(UPLOADED_BY)));
        document.setFileName(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
        document.setFileThumb(cursor.getString(cursor.getColumnIndex(FILE_THUMB)));
        document.setMimeType(cursor.getString(cursor.getColumnIndex(MIME_TYPE)));
        document.setFileSize(cursor.getString(cursor.getColumnIndex(FILE_SIZE)));
        document.setUploadDate(cursor.getLong(cursor.getColumnIndex(UPLOAD_DATE)));
        document.setFilePath(cursor.getString(cursor.getColumnIndex(FILE_PATH)));
        document.setFileLocalPath(cursor.getString(cursor.getColumnIndex(FILE_LOCAL_PATH)));
        document.setVisitId(cursor.getString(cursor.getColumnIndex(VISIT_ID)));
        document.setDocumentTypeId(cursor.getInt(cursor.getColumnIndex(DOCUMENT_TYPE_ID)));
        document.setRecordStatus(cursor.getString(cursor.getColumnIndex(RECORD_STATUS)));
        document.setCreatedAt(cursor.getLong(cursor.getColumnIndex(CREATED_AT)));
        document.setCreatedBy(cursor.getString(cursor.getColumnIndex(CREATED_BY)));
        document.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT)));
        document.setUpdatedBy(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
        document.setSyncStatus(cursor.getString(cursor.getColumnIndex(SYNC_STATUS)));
        document.setSyncAction(cursor.getString(cursor.getColumnIndex(SYNC_ACTION)));

        return document;
    }

    // get all user mappings data from db


    public int insertDocument(Document document) {

        ContentValues initialValues;
        initialValues = getContentValuesFromModel(document);

        int id = db.update(TABLE_NAME, initialValues, ID + "=?", new String[] {String.valueOf(document.getId())});

        if (id == 0){
            id = (int) db.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        }

        return id;
    }

    public ContentValues getContentValuesFromModel(Document document) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, document.getId());
        initialValues.put(PATIENT_ID, document.getPatientId());
        initialValues.put(UPLOADED_BY, document.getUploadedBy());
        initialValues.put(FILE_NAME, document.getFileName());
        initialValues.put(FILE_THUMB, document.getFileThumb());
        initialValues.put(MIME_TYPE, document.getMimeType());
        initialValues.put(FILE_SIZE, document.getFileSize());
        initialValues.put(UPLOAD_DATE, document.getUploadDate());
        initialValues.put(FILE_PATH, document.getFilePath());
        initialValues.put(FILE_LOCAL_PATH, document.getFileLocalPath());
        initialValues.put(VISIT_ID, document.getVisitId());
        initialValues.put(DOCUMENT_TYPE_ID, document.getDocumentTypeId());
        initialValues.put(CREATED_AT, document.getCreatedAt());
        initialValues.put(CREATED_BY, document.getCreatedBy());
        initialValues.put(UPDATED_AT, document.getUpdatedAt());
        initialValues.put(UPDATED_BY, document.getUpdatedBy());
        initialValues.put(RECORD_STATUS, document.getRecordStatus());
        initialValues.put(SYNC_STATUS, document.getSyncStatus());
        initialValues.put(SYNC_ACTION, document.getSyncAction());
        return initialValues;
    }

//	public ArrayList<String> getNonSyncedDocumentsIds(String userId) {
//		String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=" + AppConstants.STATUS_NON_SYNC
//		               + " AND " + UPDATED_BY + "=\"" + userId + "\"";
//		String[] whereParams = new String[] {};
//		Cursor cursor = this.db.rawQuery(query, whereParams);
//		ArrayList<String> ids = new ArrayList<>();
//		if (cursor != null && (cursor.moveToFirst())){
//			do {
//				String id = cursor.getString(cursor.getColumnIndex(ID));
//				if (id != null && !id.isEmpty()){
//					ids.add(id);
//				}
//			} while ((cursor.moveToNext()));
//			if (cursor != null && !cursor.isClosed()){
//				cursor.close();
//			}
//		}
//		return ids;
//	}

    public ArrayList<String> getNonSyncedDocumentIdsWithImageUrl(String userId) {
        int count = 0;
        String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=" + AppConstants.STATUS_NON_SYNC
                + " AND " + DocumentDao.UPDATED_BY + "=\"" + userId + "\""
                + " AND " + DocumentDao.FILE_PATH + " NOT NULL AND " + DocumentDao.FILE_PATH + " != \"\""
                + " AND " + DocumentDao.FILE_THUMB + " NOT NULL AND " + DocumentDao.FILE_THUMB + " != \"\"";
        String[] whereParams = new String[] {};
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

    public ArrayList<String> getNonSyncedDocumentIdsWithoutImageUrl(String userId) {
        int count = 0;
        String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=" + AppConstants.STATUS_NON_SYNC
                + " AND " + UPDATED_BY + "=\"" + userId + "\""
                + " AND (" + DocumentDao.FILE_PATH + " IS NULL OR " + DocumentDao.FILE_PATH + " = \"\")"
                + " AND (" + DocumentDao.FILE_THUMB + " IS NULL OR " + DocumentDao.FILE_THUMB + " = \"\")";
        String[] whereParams = new String[] {};
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

//	public int getNonSyncedDocumentsCountWithImageUrl(String userId) {
//		int count = 0;
//		String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=" + AppConstants.STATUS_NON_SYNC
//		               + " AND " + DocumentDao.UPDATED_BY + "=\"" + userId + "\""
//		               + " AND " + DocumentDao.FILE_PATH + " NOT NULL AND " + DocumentDao.FILE_PATH + " != \"\""
//		               + " AND " + DocumentDao.FILE_THUMB + " NOT NULL AND " + DocumentDao.FILE_THUMB + " != \"\"";
//		String[] whereParams = new String[] {};
//		Cursor cursor = this.db.rawQuery(query, whereParams);
//		if (cursor != null){
//			count = cursor.getCount();
//			cursor.close();
//		}
//
//		return count;
//	}
//
//	public int getNonSyncedDocumentsCountWithoutImageUrl(String userId) {
//		int count = 0;
//		String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=" + AppConstants.STATUS_NON_SYNC
//		               + " AND " + UPDATED_BY + "=\"" + userId + "\""
//		               + " AND (" + DocumentDao.FILE_PATH + " IS NULL OR " + DocumentDao.FILE_PATH + " = \"\")"
//		               + " AND (" + DocumentDao.FILE_THUMB + " IS NULL OR " + DocumentDao.FILE_THUMB + " = \"\")";
//		String[] whereParams = new String[] {};
//		Cursor cursor = this.db.rawQuery(query, whereParams);
//		if (cursor != null){
//			count = cursor.getCount();
//			cursor.close();
//		}
//
//		return count;
//	}

    public Document getLocalDocumentEntryBatch(String userId) {

        String query = "SELECT T1.* FROM " + TABLE_NAME + " T1"
                + " JOIN " + VisitDao.TABLE_NAME + " T2"
                + " ON " + "T1." + DocumentDao.VISIT_ID + "=" + "T2." + VisitDao.ID
                + " WHERE T1." + DocumentDao.SYNC_STATUS + "='" + AppConstants.STATUS_NON_SYNC + "'"
                + " AND T2." + VisitDao.SYNC_STATUS + "='" + AppConstants.STATUS_SYNCED + "'"
                + " AND T1." + DocumentDao.UPDATED_BY + "='" + userId + "'"
                + " AND T1." + DocumentDao.FILE_PATH + " NOT NULL AND T1." + DocumentDao.FILE_PATH + " != \"\""
                + " AND T1." + DocumentDao.FILE_THUMB + " NOT NULL AND T1." + DocumentDao.FILE_THUMB + " != \"\""
                + " LIMIT 1";

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            Document document = getDocumentFromCursor(cursor);
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            return document;
        } else {
            String queryNonVisit = "SELECT * FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=" + AppConstants.STATUS_NON_SYNC
                    + " AND " + UPDATED_BY + "=\"" + userId + "\""
                    + " AND (" + DocumentDao.VISIT_ID + " IS NULL OR " + DocumentDao.VISIT_ID + " = \"\")"
                    + " AND " + DocumentDao.FILE_PATH + " NOT NULL AND " + DocumentDao.FILE_PATH + " != \"\""
                    + " AND " + DocumentDao.FILE_THUMB + " NOT NULL AND " + DocumentDao.FILE_THUMB + " != \"\""
                    + " LIMIT 1";
            String[] whereParamsNonVisit = new String[] {};
            Cursor cursorNonVisit = this.db.rawQuery(queryNonVisit, whereParamsNonVisit);
            if (cursorNonVisit != null && (cursorNonVisit.moveToFirst())){
                Document document = getDocumentFromCursor(cursorNonVisit);
                if (cursorNonVisit != null && !cursorNonVisit.isClosed()){
                    cursorNonVisit.close();
                }
                return document;
            }
        }
        return null;
    }

    public Document getLocalDocumentUploadBatch(String userId) {

        String query = "SELECT T1.* FROM " + TABLE_NAME + " T1"
                + " JOIN " + VisitDao.TABLE_NAME + " T2"
                + " ON " + "T1." + DocumentDao.VISIT_ID + "=" + "T2." + VisitDao.ID
                + " WHERE T1." + DocumentDao.SYNC_STATUS + "='" + AppConstants.STATUS_NON_SYNC + "'"
                + " AND T2." + VisitDao.SYNC_STATUS + "='" + AppConstants.STATUS_SYNCED + "'"
                + " AND T1." + DocumentDao.UPDATED_BY + "='" + userId + "'"
                + " AND (T1." + DocumentDao.FILE_PATH + " IS NULL OR T1." + DocumentDao.FILE_PATH + " = \"\")"
                + " AND (T1." + DocumentDao.FILE_THUMB + " IS NULL OR T1." + DocumentDao.FILE_THUMB + " = \"\")"
                + " LIMIT 1";

        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            Document document = getDocumentFromCursor(cursor);
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            return document;
        } else {
            String queryNonVisit = "SELECT * FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + "=" + AppConstants.STATUS_NON_SYNC
                    + " AND " + UPDATED_BY + "=\"" + userId + "\""
                    + " AND (" + DocumentDao.VISIT_ID + " IS NULL OR " + DocumentDao.VISIT_ID + " = \"\")"
                    + " AND (" + DocumentDao.FILE_PATH + " IS NULL OR " + DocumentDao.FILE_PATH + " = \"\")"
                    + " AND (" + DocumentDao.FILE_THUMB + " IS NULL OR " + DocumentDao.FILE_THUMB + " = \"\")"
                    + " LIMIT 1";
            String[] whereParamsNonVisit = new String[] {};
            Cursor cursorNonVisit = this.db.rawQuery(queryNonVisit, whereParamsNonVisit);
            if (cursorNonVisit != null && (cursorNonVisit.moveToFirst())){
                Document document = getDocumentFromCursor(cursorNonVisit);
                if (cursorNonVisit != null && !cursorNonVisit.isClosed()){
                    cursorNonVisit.close();
                }
                return document;
            }
        }
        return null;
    }

    public void insertOrUpdateDocumentAfterApiCall(Document document, boolean isSynced) {
        ContentValues contentValues = new ContentValues();
        Document documentModel;
        documentModel = this.getDocumentWithId(document.getId());
        if (documentModel == null){
            contentValues = this.getContentValuesFromModel(document);
            contentValues.put(SYNC_ACTION, AppConstants.INSERT);
            contentValues.put(SYNC_STATUS, isSynced ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
            int insertValue = (int) db.insert(TABLE_NAME, null, contentValues);
            Logger.debug("insertOrUpdateDocumentAfterApiCall : insert : " + insertValue);
        } else {
            contentValues = this.getContentValuesFromModel(document);
            contentValues.put(SYNC_ACTION, AppConstants.UPDATE);
            contentValues.put(SYNC_STATUS, isSynced ? AppConstants.STATUS_SYNCED : AppConstants.STATUS_NON_SYNC);
            int updateValue = (int) db.update(TABLE_NAME, contentValues, ID + "=?", new String[] {document.getId()});
            Logger.debug("insertOrUpdateDocumentAfterApiCall : update : " + updateValue);
        }
    }

    public Document getDocumentWithId(String id) {

        Document document = null;
        if (id != null){
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
            Logger.debug("Query - " + query);
            String[] whereParams = new String[] {};
            Cursor cursor = this.db.rawQuery(query, whereParams);
            if (cursor != null && (cursor.moveToFirst())){

                do {
                    document = getDocumentFromCursor(cursor);
                } while ((cursor.moveToNext()));

            }
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return document;
    }

    public String[] getDocumentUrlsWithId(String id) {

        String[] arrVal = {"", "", ""};
        if (id != null){
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
            String query = "SELECT " + FILE_LOCAL_PATH + "," + FILE_PATH + "," + FILE_THUMB + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
            Logger.debug("Query - " + query);
            String[] whereParams = new String[] {};
            Cursor cursor = this.db.rawQuery(query, whereParams);
            if (cursor != null && (cursor.moveToFirst())){

                do {
                    if(cursor.getString(cursor.getColumnIndex(FILE_LOCAL_PATH)) == null){
                        arrVal[0] = "";
                    }
                    else {
                        arrVal[0] = cursor.getString(cursor.getColumnIndex(FILE_LOCAL_PATH));
                    }
                    if(cursor.getString(cursor.getColumnIndex(FILE_PATH)) == null){
                        arrVal[1] = "";
                    }
                    else {
                        arrVal[1] = cursor.getString(cursor.getColumnIndex(FILE_PATH));
                    }
                    if(cursor.getString(cursor.getColumnIndex(FILE_THUMB)) == null){
                        arrVal[2] = "";
                    }
                    else {
                        arrVal[2] = cursor.getString(cursor.getColumnIndex(FILE_THUMB));
                    }

                } while ((cursor.moveToNext()));

            }
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return arrVal;
    }

    public void updatePatientId(String id, String changedId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PATIENT_ID, changedId);
        db.update(TABLE_NAME, contentValues, PATIENT_ID + "=?", new String[] {id});
    }

    public ArrayList<Document> getImageListFromVisitId(String visitId, int documentType) {
        ArrayList<Document> documentsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + VISIT_ID + "=?" + " AND "
                + DOCUMENT_TYPE_ID + "=" + documentType + " AND " + RECORD_STATUS + "='A' ORDER BY " + CREATED_AT + " DESC LIMIT 5";
        String[] whereParams = new String[] {visitId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    documentsList.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return documentsList;
    }

    public ArrayList<Document> getAllImageListFromVisitId(String visitId, int documentType) {
        ArrayList<Document> documentsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + VISIT_ID + "=?" + " AND "
                + DOCUMENT_TYPE_ID + "=" + documentType + " AND " + RECORD_STATUS + "='A' ORDER BY " + CREATED_AT + " DESC";
        String[] whereParams = new String[] {visitId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    documentsList.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return documentsList;
    }

    public ArrayList<Document> getImageListFromStartAndEndDate(long startDate, long endDate, int documentType, String patientId) {
        ArrayList<Document> documentsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + "= '" + patientId + "' AND " + DOCUMENT_TYPE_ID + "=" + documentType + " AND "
                + RECORD_STATUS + "='A' AND " + UPDATED_AT + " BETWEEN " + startDate + " AND " + endDate + " ORDER BY " + CREATED_AT + " DESC";
        Logger.debug(query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    documentsList.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return documentsList;
    }

    public ArrayList<Document> getRegistrationFromPatientId(int documentType, String patientId) {
        ArrayList<Document> documentsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + "= '" + patientId + "' AND " + DOCUMENT_TYPE_ID + "=" + documentType + " AND "
                + RECORD_STATUS + "='A' AND " + UPDATED_AT + " ORDER BY " + CREATED_AT + " DESC LIMIT 1";
        Logger.debug(query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    documentsList.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return documentsList;
    }

    public ArrayList<Document> getDocumentListFromPatientId(int documentType, String patientId) {
        ArrayList<Document> documentsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + "= '" + patientId + "' AND " + DOCUMENT_TYPE_ID + "=" + documentType + " AND "
                + RECORD_STATUS + "='A' AND " + UPDATED_AT + " ORDER BY " + CREATED_AT + " DESC LIMIT 5";
        Logger.debug(query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    documentsList.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return documentsList;
    }

    public void updateDocumentStatus(int documentType, String patientId, String userId) {
        ArrayList<Document> documentsList = new ArrayList<>();
        String query = "UPDATE " + TABLE_NAME + " SET " + RECORD_STATUS + " = 'D', " + SYNC_STATUS + " = '" + AppConstants.STATUS_NON_SYNC + "', " + SYNC_ACTION + " = '" + AppConstants.UPDATE + "', "
                + UPDATED_AT + " = " + DateUtils.getCurrentTimeInDefault() + ", " + UPDATED_BY + " = '" + userId + "' WHERE " + PATIENT_ID + " = '" + patientId + "' AND " + DOCUMENT_TYPE_ID + " = " + documentType;
        Logger.debug(query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    documentsList.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }


    }

    public ArrayList<Document> getAllEntriesAsPerDocumentType(String documentTypeId) {
        ArrayList<Document> exercises = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + DOCUMENT_TYPE_ID + "=? AND " + RECORD_STATUS + "='A' ";
        Logger.debug(query);
        String[] whereParams = new String[] {documentTypeId};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    exercises.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return exercises;
    }

    public void deleteScannedPrescriptionEntryForVisit(String visitId, String prescriptionDocTypeId) {
        String[] bindArgs = new String[] {visitId, prescriptionDocTypeId};
        String whereClause = VISIT_ID + "=? AND " + DOCUMENT_TYPE_ID + " =?";

        db.delete(TABLE_NAME, whereClause, bindArgs);
    }

    public ArrayList<Document> getPreviousScannedPrescriptions(String patientId, int documentTypeId, String startDate, String endDate) {
        ArrayList<Document> previousScannedPrescriptions = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + VISIT_ID + " IN (SELECT " + ID + " FROM visit WHERE patient_id=\"" + patientId + "\" AND " + RECORD_STATUS + "=\"A\" AND " + CREATED_AT + " BETWEEN " + startDate + " AND " + endDate + " ) AND " + DOCUMENT_TYPE_ID + " = " + documentTypeId + "";
        Logger.debug("query  " + query);

        // @formatter:on

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && (cursor.moveToFirst())){

            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    previousScannedPrescriptions.add(document);
                }

            } while ((cursor.moveToNext()));

            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return previousScannedPrescriptions;
    }

    public Document getPreviousScannedPrescriptionDocumentForVisit(String visitId) {

        Document document = null;
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + VISIT_ID + " = '" + visitId + "' AND " + DOCUMENT_TYPE_ID + " = " + 2 + "";
        Logger.debug("Query - " + query);
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                document = getDocumentFromCursor(cursor);
            } while ((cursor.moveToNext()));

        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return document;
    }

    public ArrayList<Document> getPreviousScannedPrescriptionDocumentsForPatient(String patientId, String startDate, String endDate, boolean isFromFilter) {

        ArrayList<Document> documents = null;
        String query;
//		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + userId + "' AND " + RECORD_STATUS + " ='A' AND " + STATUS + " = 0 AND " + MISSCALL_TIME + " BETWEEN " + DateUtils.getStartOfDayInDefaultTimeZone() + " AND " + DateUtils.getEndOfDayInDefaultTimeZone();
        if (isFromFilter){
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + patientId + "' AND " + DOCUMENT_TYPE_ID + " = " + AppConstants.DOCUMENT_TYPE_PRESCRIPTION_ID + " AND (" + UPDATED_AT + " BETWEEN " + startDate + " AND " + endDate + ") AND " + RECORD_STATUS + "='" + AppConstants.ACTIVE_RECORD_STATUS + "' ORDER BY " + UPDATED_AT + " DESC";
            Logger.debug("Query - " + query);
        } else {
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PATIENT_ID + " = '" + patientId + "' AND " + DOCUMENT_TYPE_ID + " = " + AppConstants.DOCUMENT_TYPE_PRESCRIPTION_ID + " AND (" + UPDATED_AT + " BETWEEN " + startDate + " AND " + endDate + ") AND " + RECORD_STATUS + "='" + AppConstants.ACTIVE_RECORD_STATUS + "' ORDER BY " + UPDATED_AT + " DESC LIMIT 5";
            Logger.debug("Query - " + query);
        }
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){
            documents = new ArrayList<>();
            do {
                Document document = getDocumentFromCursor(cursor);
                if (document != null){
                    documents.add(document);
                }
            } while ((cursor.moveToNext()));

        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return documents;
    }

    public long getLastUpdatedDate() {

        long updatedDate = 0;
        String query = "SELECT " + UPDATED_AT + " FROM " + TABLE_NAME
                + " WHERE	UPDATED_AT = (SELECT max( "+ UPDATED_AT + ") FROM " + TABLE_NAME + ")"
                + " LIMIT 1";
        String[] whereParams = new String[] {};
        Cursor cursor = this.db.rawQuery(query, whereParams);
        if (cursor != null && (cursor.moveToFirst())){

            do {
                updatedDate = cursor.getLong(cursor.getColumnIndex(UPDATED_AT));
            } while ((cursor.moveToNext()));

            cursor.close();
        }

        return updatedDate;
    }

}
