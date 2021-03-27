package com.dhb.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.dhb.R;
import com.dhb.dao.DhbDao;
import com.dhb.dao.daomodels.DocumentDao;
import com.dhb.dao.daomodels.HealthCareFirmDao;
import com.dhb.dao.daomodels.PatientHealthCareFirmMapDao;
import com.dhb.models.Queue;
import com.dhb.request_model.AgeModel;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.DateUtils;
import com.dhb.utils.UiUtils;
import com.dhb.viewholder.RecycleViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PatientQueueRecycleAdapter extends RecyclerView.Adapter<RecycleViewHolder> {

    private LayoutInflater layoutInflater;
    private Activity activity;
    private List<Queue> patientQueueModelArrayList;
    private List<String> patientQueueHinArrayList;
    private List<String> patientQueueProfileImageArrayList;
    private String roleName;
    int sdk;
    private DhbDao dhbDao;
    private boolean isOnlyViewable;
    private AppPreferenceManager appPreferenceManager;

    /* public PatientQueueRecycleAdapter(Context context, ArrayList<PatientQueueModel>patientQueueModelArrayList)
       {
         this.context=context;
         this.patientQueueModelArrayList=patientQueueModelArrayList;
         layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       }
     */
    public PatientQueueRecycleAdapter(Activity activity, List<Queue> patientQueueModelArrayList, String roleName, boolean isOnlyViewable) {
        this.activity = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
        sdk = android.os.Build.VERSION.SDK_INT;
        dhbDao = new DhbDao(activity);
        this.patientQueueModelArrayList = patientQueueModelArrayList;
        setPatientInformationArrays(patientQueueModelArrayList);
        this.roleName = roleName;
        this.isOnlyViewable = isOnlyViewable;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPatientInformationArrays(List<Queue> patientQueueModelArrayList){

        patientQueueHinArrayList = new ArrayList<>();
        patientQueueProfileImageArrayList = new ArrayList<>();

        PatientHealthCareFirmMapDao patientHealthcareFirmMapDao = new PatientHealthCareFirmMapDao(dhbDao.getDb());
        HealthCareFirmDao healthCareFirmDao = new HealthCareFirmDao(dhbDao.getDb());
        DocumentDao documentDao = new DocumentDao(dhbDao.getDb());

        for (int position = 0; position < patientQueueModelArrayList.size(); position++){
            String[] refNoAndOldHinAndRefTag = patientHealthcareFirmMapDao.getRefNoAndOldHinAndRefTagWithUserId(patientQueueModelArrayList.get(position).getPatientId(), appPreferenceManager.getHospitalId());
            if (refNoAndOldHinAndRefTag != null){
                String hin = "";
                if(refNoAndOldHinAndRefTag[2] != null && !refNoAndOldHinAndRefTag[2].isEmpty() && refNoAndOldHinAndRefTag[0] != null && !refNoAndOldHinAndRefTag[0].isEmpty()){
                    hin = refNoAndOldHinAndRefTag[2] + refNoAndOldHinAndRefTag[0];
                }
                else{
                    if(refNoAndOldHinAndRefTag[0] != null && !refNoAndOldHinAndRefTag[0].isEmpty()){
                        String healthCareFirmRefTag = healthCareFirmDao.getHealthCareFirmReferenceTagFromId(appPreferenceManager.getHospitalId());
                        if (healthCareFirmRefTag != null && !healthCareFirmRefTag.isEmpty()){
                            hin = healthCareFirmRefTag + refNoAndOldHinAndRefTag[0];
                        }
                    }
                }

                if (refNoAndOldHinAndRefTag[1] != null && !refNoAndOldHinAndRefTag[1].equalsIgnoreCase("")){
                    patientQueueHinArrayList.add(position, "HIN: " + refNoAndOldHinAndRefTag[1] + (hin.isEmpty() ? "" : (", " + hin)));
                } else {
                    if (hin != null){
                        patientQueueHinArrayList.add(position, "HIN: " + hin);
                    } else {
                        patientQueueHinArrayList.add(position, "HIN: ");
                    }
                }

            }
            else{
                patientQueueHinArrayList.add(position, "HIN: ");
            }

            String imageUrl;
            String[] docUrls = documentDao.getDocumentUrlsWithId(patientQueueModelArrayList.get(position).getPatient().getInfo().getProfileImage());
            if (docUrls != null && docUrls[0] != null && !docUrls[0].isEmpty()){
                imageUrl = docUrls[0];
            } else if (docUrls != null && docUrls[2] != null && !docUrls[2].isEmpty()){
                imageUrl = docUrls[2];
            } else if (docUrls != null && docUrls[1] != null && !docUrls[1].isEmpty()){
                imageUrl = docUrls[1];
            } else {
                imageUrl = "";
            }
            patientQueueProfileImageArrayList.add(position, imageUrl);
        }

    }

    public void removePatientInformationArrayElements(int position){
        if(patientQueueHinArrayList != null && patientQueueHinArrayList.size() != 0
                && patientQueueHinArrayList.size() > position){
            patientQueueHinArrayList.remove(position);
        }

        if(patientQueueProfileImageArrayList != null && patientQueueProfileImageArrayList.size() != 0
                && patientQueueProfileImageArrayList.size() > position){
            patientQueueProfileImageArrayList.remove(position);
        }
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.item_patient_queue, parent, false);

        RecycleViewHolder patientViewHolder = new RecycleViewHolder((activity), v);

        return patientViewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {

        Typeface fontMed = UiUtils.getInstance().createTypeFace(activity, "roboto-medium.ttf");
        Typeface fontReg = UiUtils.getInstance().createTypeFace(activity, "roboto-regular.ttf");

        if (isOnlyViewable){
            holder.getIbtnPlus().setVisibility(View.INVISIBLE);
        } else {
            holder.getIbtnPlus().setVisibility(View.VISIBLE);
        }

        holder.getRltLayoutPatientQueueFamily().setVisibility(View.INVISIBLE);

        if (patientQueueModelArrayList != null && patientQueueModelArrayList.get(position).getPatient() != null){
            if (patientQueueModelArrayList.get(position).getPatient().getRegistrationStatus().equalsIgnoreCase(AppConstants.NO)){

                holder.getRltLayoutPatientQueue().setVisibility(View.INVISIBLE);
                holder.getRltLayoutPatientQueueUnRegister().setVisibility(View.VISIBLE);
                holder.getRltLayoutPatientQueueMR().setVisibility(View.INVISIBLE);

                holder.getTxtPatientMobileUnRegister().setText(patientQueueModelArrayList.get(position).getPatient().getMobile1());
                holder.getTxtPatientMobileUnRegister().setTypeface(fontReg);
                holder.getTxtPatientTimingUnRegister().setText(DateUtils.getTimeInDefaultTimeZone(patientQueueModelArrayList.get(position).getMissCallTime()));
                holder.getTxtPatientTimingUnRegister().setTypeface(fontReg);
                holder.getImgPatientProfilePic().setBackgroundResource(R.drawable.profile_pic_icon_pink);
                holder.getImgPatientProfilePicUnRegister().setImageResource(R.drawable.profile_pic_icon_pink);
                holder.getTxtPatientSerialNoUnRegister().setText(position + 1 + "");
                holder.getTxtPatientSerialNoUnRegister().setTypeface(fontReg);
                if ((position + 1) < 10)
                    holder.getTxtPatientSerialNoUnRegister().setText("0" + (position + 1));
                holder.getTxtPatientAppointmentTimeUnRegister().setText("");

                if (roleName.equalsIgnoreCase(AppConstants.DOCTOR)){
                    if (patientQueueModelArrayList.get(position).getAppointmentStartTime() == 0
                            && patientQueueModelArrayList.get(position).getAppointmentEndTime() == 0){
                        holder.getTxtPatientAppointmentTimeUnRegister().setText(" " + "\n    \n" + " ");
                    } else {
                        String t1 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentStartTime());
                        String t2 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentEndTime());
                        holder.getTxtPatientAppointmentTimeUnRegister().setText(t1 + "\n ---\n" + t2);
                    }
                }
                if (roleName.equalsIgnoreCase(AppConstants.RECEPTION) && isOnlyViewable){
                    if (patientQueueModelArrayList.get(position).getAppointmentStartTime() == 0
                            && patientQueueModelArrayList.get(position).getAppointmentEndTime() == 0){
                        holder.getTxtPatientAppointmentTimeUnRegister().setText(" " + "\n    \n" + " ");
                    } else {
                        String t1 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentStartTime());
                        String t2 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentEndTime());
                        holder.getTxtPatientAppointmentTimeUnRegister().setText(t1 + "\n ---\n" + t2);
                    }
                }
            } else {

                if (patientQueueModelArrayList.get(position).getPatient().getIsMr().equalsIgnoreCase(AppConstants.YES)){
                    holder.getRltLayoutPatientQueueMR().setVisibility(View.VISIBLE);
                    holder.getRltLayoutPatientQueue().setVisibility(View.INVISIBLE);
                } else {
                    holder.getRltLayoutPatientQueueMR().setVisibility(View.INVISIBLE);
                    holder.getRltLayoutPatientQueue().setVisibility(View.VISIBLE);
                }
                holder.getRltLayoutPatientQueueUnRegister().setVisibility(View.INVISIBLE);

                if (patientQueueModelArrayList.get(position).getPatient().getIsMr().equalsIgnoreCase(AppConstants.YES)){

                    if (isOnlyViewable){
                        holder.getIbtnMenuMR().setVisibility(View.INVISIBLE);
                    } else {
                        holder.getIbtnMenuMR().setVisibility(View.VISIBLE);
                    }

                    holder.getTxtPatientNameMR().setText(patientQueueModelArrayList.get(position).getPatient().getFirstName()
                            + " " + patientQueueModelArrayList.get(position).getPatient().getLastName());
                    holder.getTxtPatientNameMR().setTypeface(fontMed);

                    holder.getTxtPatientGenderMR().setText(patientQueueModelArrayList.get(position).getPatient().getInfo().getGender());
                    holder.getTxtPatientGenderMR().setTypeface(fontReg);
                    AgeModel ageModel = (DateUtils.calculateAge((patientQueueModelArrayList.get(position).getPatient().getDob())));
                    holder.getTxtPatientVisitReason().setVisibility(View.INVISIBLE);

                    holder.getTxtPatientAgeMR().setText(ageModel.toString());
                    holder.getTxtBloodGroupMR().setText(patientQueueModelArrayList.get(position).getPatient().getInfo().getBloodGroup());
                    if (patientQueueModelArrayList.get(position).getSenderComments() != null && !patientQueueModelArrayList.get(position).getSenderComments().equalsIgnoreCase("")){
                        holder.getTxtChiefComplaintMR().setText("Complaint: " + patientQueueModelArrayList.get(position).getSenderComments());
                    } else {
                        holder.getTxtChiefComplaintMR().setText("Complaint: ");
                    }

                    if(patientQueueHinArrayList.size() > position && patientQueueHinArrayList.get(position) != null && !patientQueueHinArrayList.get(position).isEmpty()){
                        holder.getTxtHINMR().setText(patientQueueHinArrayList.get(position));
                    }
                    else{
                        holder.getTxtHINMR().setText("HIN: ");
                    }


                    if (ageModel.getYears() == 0 && ageModel.getMonths() == 0 && ageModel.getDays() == 0){
                        holder.getTxtPatientAgeMR().setText(R.string.not_available);
                    }
                    holder.getTxtPatientAgeMR().setTypeface(fontReg);


                    if (patientQueueModelArrayList.get(position).getPatient().getMobile1() != null && !patientQueueModelArrayList.get(position).getPatient().getMobile1().trim().equalsIgnoreCase("")){
                        holder.getTxtPatientMobileMR().setText(patientQueueModelArrayList.get(position).getPatient().getMobile1());
                    } else if (patientQueueModelArrayList.get(position).getPatient().getMobile2() != null && !patientQueueModelArrayList.get(position).getPatient().getMobile2().trim().equalsIgnoreCase("")){
                        holder.getTxtPatientMobileMR().setText(patientQueueModelArrayList.get(position).getPatient().getMobile2());
                    } else if (patientQueueModelArrayList.get(position).getPatient().getMobile3() != null && !patientQueueModelArrayList.get(position).getPatient().getMobile3().trim().equalsIgnoreCase("")){
                        holder.getTxtPatientMobileMR().setText(patientQueueModelArrayList.get(position).getPatient().getMobile3());
                    } else {
                        holder.getTxtPatientMobileMR().setText("");
                    }
                    holder.getTxtPatientMobileMR().setTypeface(fontReg);
                    holder.getTxtPatientTimingMR().setText(DateUtils.getTimeInDefaultTimeZone(patientQueueModelArrayList.get(position).getMissCallTime()));
                    holder.getTxtPatientTimingMR().setTypeface(fontReg);

                    holder.getImgPatientProfilePicMR().setBackgroundResource(R.drawable.profile_pic_icon_yellow);
                    holder.getImgPatientProfilePicMR().setImageResource(R.drawable.profile_pic_icon_yellow);

                    if (patientQueueProfileImageArrayList.size() > position && patientQueueProfileImageArrayList.get(position) != null && !TextUtils.isEmpty(patientQueueProfileImageArrayList.get(position))){
                        if (patientQueueProfileImageArrayList.get(position).startsWith("http")){
                            ImageLoader.getInstance().displayImage(patientQueueProfileImageArrayList.get(position), holder.getImgPatientProfilePicMR());
                        } else {
                            ImageLoader.getInstance().displayImage("file://" + patientQueueProfileImageArrayList.get(position), holder.getImgPatientProfilePicMR());
                        }
                    } else {
                        holder.getImgPatientProfilePicMR().setImageResource(R.drawable.profile_pic_icon_yellow);
                    }

                    holder.getTxtPatientSerialNoMR().setTypeface(fontReg);
                    if ((position + 1) < 10){
                        holder.getTxtPatientSerialNoMR().setText("0" + (position + 1));
                    }
                    else {
                        holder.getTxtPatientSerialNoMR().setText("" + (position + 1));
                    }

                    if (roleName.equalsIgnoreCase(AppConstants.DOCTOR)){
                        if (patientQueueModelArrayList.get(position).getAppointmentStartTime() == 0
                                && patientQueueModelArrayList.get(position).getAppointmentEndTime() == 0){
                            holder.getTxtPatientAppointmentTimeMR().setVisibility(View.GONE);
                            holder.getTxtPatientAppointmentTimeMR().setText(" " + "\n    \n" + " ");
                        } else {
                            String t1 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentStartTime());
                            String t2 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentEndTime());
                            holder.getTxtPatientAppointmentTimeMR().setVisibility(View.VISIBLE);
                            holder.getTxtPatientAppointmentTimeMR().setText(t1 + "\n ---\n" + t2);
                        }
                    }

                    if (roleName.equalsIgnoreCase(AppConstants.RECEPTION) && isOnlyViewable){
                        if (patientQueueModelArrayList.get(position).getAppointmentStartTime() == 0
                                && patientQueueModelArrayList.get(position).getAppointmentEndTime() == 0){
                            holder.getTxtPatientAppointmentTimeMR().setVisibility(View.GONE);
                            holder.getTxtPatientAppointmentTimeMR().setText(" " + "\n    \n" + " ");
                        } else {
                            String t1 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentStartTime());
                            String t2 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentEndTime());
                            holder.getTxtPatientAppointmentTimeMR().setVisibility(View.VISIBLE);
                            holder.getTxtPatientAppointmentTimeMR().setText(t1 + "\n ---\n" + t2);
                        }
                    }
                } else {

                    if (isOnlyViewable){
                        holder.getIbtnMenu().setVisibility(View.INVISIBLE);
                    } else {
                        holder.getIbtnMenu().setVisibility(View.VISIBLE);
                    }
                    holder.getTxtPatientName().setText(patientQueueModelArrayList.get(position).getPatient().getFirstName()
                            + " " + patientQueueModelArrayList.get(position).getPatient().getLastName());
                    holder.getTxtPatientName().setTypeface(fontMed);
                    holder.getTxtPatientGender().setText(patientQueueModelArrayList.get(position).getPatient().getInfo().getGender());
                    holder.getTxtPatientGender().setTypeface(fontReg);
                    AgeModel ageModel = (DateUtils.calculateAge((patientQueueModelArrayList.get(position).getPatient().getDob())));
                    holder.getTxtPatientVisitReason().setVisibility(View.INVISIBLE);
                    holder.getTxtPatientAge().setText(ageModel.toString());
                    holder.getTxtBloodGroup().setText(patientQueueModelArrayList.get(position).getPatient().getInfo().getBloodGroup());
                    if (patientQueueModelArrayList.get(position).getSenderComments() != null && !patientQueueModelArrayList.get(position).getSenderComments().equalsIgnoreCase("")){
                        holder.getTxtChiefComplaint().setText("Complaint: " + patientQueueModelArrayList.get(position).getSenderComments());
                    } else {
                        holder.getTxtChiefComplaint().setText("Complaint: ");
                    }

                    if(patientQueueHinArrayList.size() > position && patientQueueHinArrayList.get(position) != null && !patientQueueHinArrayList.get(position).isEmpty()){
                        holder.getTxtHIN().setText(patientQueueHinArrayList.get(position));
                    }
                    else{
                        holder.getTxtHIN().setText("HIN: ");
                    }

                    if (ageModel.getYears() == 0 && ageModel.getMonths() == 0 && ageModel.getDays() == 0){
                        holder.getTxtPatientAge().setText(R.string.not_available);
                    }
                    holder.getTxtPatientAge().setTypeface(fontReg);

                    if (patientQueueModelArrayList.get(position).getPatient().getMobile1() != null && !patientQueueModelArrayList.get(position).getPatient().getMobile1().trim().equalsIgnoreCase("")){
                        holder.getTxtPatientMobile().setText(patientQueueModelArrayList.get(position).getPatient().getMobile1());
                    } else if (patientQueueModelArrayList.get(position).getPatient().getMobile2() != null && !patientQueueModelArrayList.get(position).getPatient().getMobile2().trim().equalsIgnoreCase("")){
                        holder.getTxtPatientMobile().setText(patientQueueModelArrayList.get(position).getPatient().getMobile2());
                    } else if (patientQueueModelArrayList.get(position).getPatient().getMobile3() != null && !patientQueueModelArrayList.get(position).getPatient().getMobile3().trim().equalsIgnoreCase("")){
                        holder.getTxtPatientMobile().setText(patientQueueModelArrayList.get(position).getPatient().getMobile3());
                    } else {
                        holder.getTxtPatientMobile().setText("");
                    }
                    holder.getTxtPatientMobile().setTypeface(fontReg);
                    holder.getTxtPatientTiming().setText(DateUtils.getTimeInDefaultTimeZone(patientQueueModelArrayList.get(position).getMissCallTime()));
                    holder.getTxtPatientTiming().setTypeface(fontReg);

                    holder.getImgPatientProfilePic().setBackgroundResource(R.drawable.profile_pic_icon_blue);
                    holder.getImgPatientProfilePic().setImageResource(R.drawable.profile_pic_icon_blue);

                    if (patientQueueProfileImageArrayList.size() > position && patientQueueProfileImageArrayList.get(position) != null && !TextUtils.isEmpty(patientQueueProfileImageArrayList.get(position))){
                        if (patientQueueProfileImageArrayList.get(position).startsWith("http")){
                            ImageLoader.getInstance().displayImage(patientQueueProfileImageArrayList.get(position), holder.getImgPatientProfilePic());
                        } else {
                            ImageLoader.getInstance().displayImage("file://" + patientQueueProfileImageArrayList.get(position), holder.getImgPatientProfilePic());
                        }
                    } else {
                        holder.getImgPatientProfilePic().setImageResource(R.drawable.profile_pic_icon_blue);
                    }

                    holder.getTxtPatientSerialNo().setTypeface(fontReg);
                    if ((position + 1) < 10){
                        holder.getTxtPatientSerialNo().setText("0" + (position + 1));
                    }
                    else {
                        holder.getTxtPatientSerialNo().setText("" + (position + 1));
                    }

                    if (roleName.equalsIgnoreCase(AppConstants.DOCTOR)){
                        if (patientQueueModelArrayList.get(position).getAppointmentStartTime() == 0
                                && patientQueueModelArrayList.get(position).getAppointmentEndTime() == 0){
                            holder.getTxtPatientAppointmentTime().setVisibility(View.GONE);
                            holder.getTxtPatientAppointmentTime().setText(" " + "\n    \n" + " ");
                        } else {
                            String t1 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentStartTime());
                            String t2 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentEndTime());
                            holder.getTxtPatientAppointmentTime().setVisibility(View.VISIBLE);
                            holder.getTxtPatientAppointmentTime().setText(t1 + "\n ---\n" + t2);
                        }
                    }

                    if (roleName.equalsIgnoreCase(AppConstants.RECEPTION) && isOnlyViewable){
                        if (patientQueueModelArrayList.get(position).getAppointmentStartTime() == 0
                                && patientQueueModelArrayList.get(position).getAppointmentEndTime() == 0){
                            holder.getTxtPatientAppointmentTime().setVisibility(View.GONE);
                            holder.getTxtPatientAppointmentTime().setText(" " + "\n    \n" + " ");
                        } else {
                            String t1 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentStartTime());
                            String t2 = DateUtils.getTime(patientQueueModelArrayList.get(position).getAppointmentEndTime());
                            holder.getTxtPatientAppointmentTime().setVisibility(View.GONE);
                            holder.getTxtPatientAppointmentTime().setText(t1 + "\n ---\n" + t2);
                        }
                    }
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return patientQueueModelArrayList.size();
    }

    public void remove(int i) {
        patientQueueModelArrayList.remove(i);
        notifyDataSetChanged();
    }

}
