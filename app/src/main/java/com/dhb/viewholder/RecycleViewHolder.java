package com.dhb.viewholder;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhb.R;
import com.dhb.customview.RoundedCornerRectangleImageView;
import com.dhb.utils.UiUtils;

public class RecycleViewHolder extends RecyclerView.ViewHolder {


    private TextView txtChiefComplaint;
    private Activity abstractActivity;

    /*Register USer*/
    private TextView txtPatientName;
    private TextView txtPatientAge;
    private TextView txtPatientMobile;
    private TextView txtPatientGender;
    private TextView txtPatientTiming;
    private TextView txtPatientVisitReason;
    private TextView txtPatientSerialNo;
    private RoundedCornerRectangleImageView imgPatientProfilePic;
    private RelativeLayout rltLayoutPatientQueue;
    private ImageButton ibtnMenu;
    private TextView txtPatientAppointmentTime;
    private TextView txtBloodGroup;
    private TextView txtHIN;
    private TextView txtHospitalId;

    /*Unregister user*/

    private TextView txtPatientNameUnRegister;
    private TextView txtPatientMobileUnRegister;
    private TextView txtPatientTimingUnRegister;
    private TextView txtPatientSerialNoUnRegister;
    private RoundedCornerRectangleImageView imgPatientProfilePicUnRegister;
    private RelativeLayout rltLayoutPatientQueueUnRegister;
    private ImageButton ibtnPlus;
    private TextView txtPatientAppointmentTimeUnRegister;

    /*Family*/

    private TextView txtFamilyName;
    private TextView txtFamilyMobile;
    private TextView txtFamilyTiming;
    private TextView txtFamilySerialNo;
    private TextView txtFamilyMembersName;
    private RoundedCornerRectangleImageView imgFamilyProfilePic;
    private RelativeLayout rltLayoutPatientQueueFamily;
    private ImageButton ibtnFamilyMenu;
    private TextView txtPatientAppointmentTimeFamily;

    /*MR user*/

    private TextView txtPatientNameMR;
    private TextView txtPatientMobileMR;
    private TextView txtPatientTimingMR;
    private TextView txtPatientSerialNoMR;
    private TextView txtFamilyMembersMR;
    private RoundedCornerRectangleImageView imgPatientProfilePicMR;
    private RelativeLayout rltLayoutPatientQueueMR;
    private TextView txtPatientAppointmentTimeMR;
    private ImageButton ibtnMenuMR;


    private TextView txtPatientAgeMR;
    private TextView txtPatientGenderMR;
    private TextView txtPatientVisitReasonMR;
    private TextView txtBloodGroupMR;
    private TextView txtHINMR;
    private TextView txtHospitalIdMR;
    private TextView txtChiefComplaintMR;

    Typeface fontOpenRobotoRegular;
    Typeface fontOpenRobotoMedium;

    public RecycleViewHolder(Activity abstractActivity, View v) {

        super(v);

        this.abstractActivity = abstractActivity;

        fontOpenRobotoRegular = UiUtils.getInstance().createTypeFace(abstractActivity, "roboto-regular.ttf");
        fontOpenRobotoMedium = UiUtils.getInstance().createTypeFace(abstractActivity, "roboto-regular.ttf");

        this.txtPatientName = (TextView) v.findViewById(R.id.txt_name_register_user);
        this.txtPatientName.setTypeface(fontOpenRobotoMedium);
        this.txtPatientAge = (TextView) v.findViewById(R.id.txt_age_register_user);
        this.txtPatientAge.setTypeface(fontOpenRobotoRegular);
        this.txtPatientGender = (TextView) v.findViewById(R.id.txt_gender_register_user);
        this.txtPatientGender.setTypeface(fontOpenRobotoRegular);
        this.txtPatientMobile = (TextView) v.findViewById(R.id.txt_mobile_register_user);
        this.txtPatientMobile.setTypeface(fontOpenRobotoRegular);
        this.txtPatientTiming = (TextView) v.findViewById(R.id.txt_time_register_user);
        this.txtPatientTiming.setTypeface(fontOpenRobotoRegular);

        this.txtPatientVisitReason = (TextView) v.findViewById(R.id.txt_visit_reason_register_user);
        this.txtPatientVisitReason.setTypeface(fontOpenRobotoRegular);

        this.txtPatientSerialNo = (TextView) v.findViewById(R.id.txt_serial_no_register_user);
        this.txtPatientSerialNo.setTypeface(fontOpenRobotoRegular);

        this.txtPatientAppointmentTime = (TextView) v.findViewById(R.id.txt_appointment_time_register_user);
        this.txtPatientAppointmentTime.setTypeface(fontOpenRobotoRegular);

        this.imgPatientProfilePic = (RoundedCornerRectangleImageView) v.findViewById(R.id.img_profile_pic_register_user);
        this.ibtnMenu = (ImageButton) v.findViewById(R.id.ibtn_view_details_register_user);
        this.rltLayoutPatientQueue = (RelativeLayout) v.findViewById(R.id.rlt_layout_patient_queue_register_user);

        this.txtBloodGroup = (TextView) v.findViewById(R.id.txt_blood_group_register_user);

        this.txtChiefComplaint = (TextView) v.findViewById(R.id.txt_chief_complaint_register_user);
        this.txtHIN = (TextView) v.findViewById(R.id.txt_HIN_register_user);
        this.txtHospitalId = (TextView) v.findViewById(R.id.txt_hospital_id_register_user);

        this.txtBloodGroup.setTypeface(fontOpenRobotoRegular);
        this.txtHIN.setTypeface(fontOpenRobotoRegular);
        this.txtChiefComplaint.setTypeface(fontOpenRobotoRegular);
        this.txtHospitalId.setTypeface(fontOpenRobotoRegular);


        this.txtPatientNameUnRegister = (TextView) v.findViewById(R.id.txt_name_unregister_user);
        this.txtPatientNameUnRegister.setTypeface(fontOpenRobotoMedium);
        this.txtPatientMobileUnRegister = (TextView) v.findViewById(R.id.txt_mobile_unregister_user);
        this.txtPatientMobileUnRegister.setTypeface(fontOpenRobotoRegular);
        this.txtPatientTimingUnRegister = (TextView) v.findViewById(R.id.txt_time_unregister_user);
        this.txtPatientTimingUnRegister.setTypeface(fontOpenRobotoRegular);
        this.txtPatientSerialNoUnRegister = (TextView) v.findViewById(R.id.txt_serial_no_unregister_user);
        this.txtPatientSerialNoUnRegister.setTypeface(fontOpenRobotoRegular);
        this.imgPatientProfilePicUnRegister = (RoundedCornerRectangleImageView) v.findViewById(R.id.img_profile_pic_unregister_user);

        this.txtPatientAppointmentTimeUnRegister = (TextView) v.findViewById(R.id.txt_appointment_time_unregister_user);
        this.txtPatientAppointmentTimeUnRegister.setTypeface(fontOpenRobotoRegular);
        this.ibtnPlus = (ImageButton) v.findViewById(R.id.ibtn_view_details_unregister_user);
        this.rltLayoutPatientQueueUnRegister = (RelativeLayout) v.findViewById(R.id.rlt_layout_patient_queue_unregister_user);

        //Family

        this.txtFamilyName = (TextView) v.findViewById(R.id.txt_name_family);
        this.txtFamilyMobile = (TextView) v.findViewById(R.id.txt_mobile_family);
        this.txtFamilyTiming = (TextView) v.findViewById(R.id.txt_time_family);
        this.txtFamilyMembersName = (TextView) v.findViewById(R.id.txt_family_members_names);
        this.txtFamilySerialNo = (TextView) v.findViewById(R.id.txt_serial_no_family);
        this.txtPatientAppointmentTimeFamily = (TextView) v.findViewById(R.id.txt_appointment_time_family);
        this.imgFamilyProfilePic = (RoundedCornerRectangleImageView) v.findViewById(R.id.img_profile_pic_family);
        this.rltLayoutPatientQueueFamily = (RelativeLayout) v.findViewById(R.id.rlt_layout_patient_queue_family);
        this.ibtnFamilyMenu = (ImageButton) v.findViewById(R.id.ibtn_view_details_family);

        this.txtFamilyName.setTypeface(fontOpenRobotoMedium);
        this.txtFamilyMobile.setTypeface(fontOpenRobotoRegular);
        this.txtFamilyTiming.setTypeface(fontOpenRobotoRegular);
        this.txtFamilyMembersName.setTypeface(fontOpenRobotoRegular);
        this.txtFamilySerialNo.setTypeface(fontOpenRobotoRegular);
        this.txtPatientAppointmentTimeFamily.setTypeface(fontOpenRobotoRegular);


        /*MR*/

        this.txtPatientNameMR = (TextView) v.findViewById(R.id.txt_name_mr);
        this.txtPatientMobileMR = (TextView) v.findViewById(R.id.txt_mobile_mr);
        this.txtPatientTimingMR = (TextView) v.findViewById(R.id.txt_time_mr);
//        this.txtFamilyMembersMR = (TextView) v.findViewById(R.id.txt_family_members_mr_names);
        this.txtPatientSerialNoMR = (TextView) v.findViewById(R.id.txt_serial_no_mr);
        this.txtPatientAppointmentTimeMR = (TextView) v.findViewById(R.id.txt_appointment_time_mr);
        this.imgPatientProfilePicMR = (RoundedCornerRectangleImageView) v.findViewById(R.id.img_profile_pic_mr);
        this.rltLayoutPatientQueueMR = (RelativeLayout) v.findViewById(R.id.rlt_layout_patient_queue_mr);
        this.ibtnMenuMR = (ImageButton) v.findViewById(R.id.ibtn_view_details_mr);

        this.txtPatientAgeMR = (TextView) v.findViewById(R.id.txt_age_mr);
        this.txtPatientGenderMR = (TextView) v.findViewById(R.id.txt_gender_mr);
//        this.txtPatientVisitReasonMR = (TextView) v.findViewById(R.id.txt_age_mr);
        this.txtBloodGroupMR = (TextView) v.findViewById(R.id.txt_blood_group_mr);
        this.txtHINMR = (TextView) v.findViewById(R.id.txt_HIN_mr);
//        this.txtHospitalIdMR = (TextView) v.findViewById(R.id.txt_age_mr);
        this.txtChiefComplaintMR = (TextView) v.findViewById(R.id.txt_chief_complaint_mr);
        this.txtPatientNameMR.setTypeface(fontOpenRobotoMedium);
        this.txtPatientMobileMR.setTypeface(fontOpenRobotoRegular);
        this.txtPatientTimingMR.setTypeface(fontOpenRobotoRegular);
//        this.txtFamilyMembersMR.setTypeface(fontOpenRobotoRegular);
        this.txtPatientSerialNoMR.setTypeface(fontOpenRobotoRegular);
        this.txtPatientAppointmentTimeMR.setTypeface(fontOpenRobotoRegular);

    }

    public Activity getAbstractActivity() {
        return abstractActivity;
    }

    public TextView getTxtPatientName() {
        return txtPatientName;
    }

    public TextView getTxtPatientAge() {
        return txtPatientAge;
    }

    public TextView getTxtPatientMobile() {
        return txtPatientMobile;
    }

    public RelativeLayout getRltLayoutPatientQueue() {
        return rltLayoutPatientQueue;
    }

    public TextView getTxtPatientGender() {
        return txtPatientGender;
    }

    public TextView getTxtPatientTiming() {
        return txtPatientTiming;
    }

    public TextView getTxtPatientVisitReason() {
        return txtPatientVisitReason;
    }

    public TextView getTxtPatientSerialNo() {
        return txtPatientSerialNo;
    }

    public RoundedCornerRectangleImageView getImgPatientProfilePic() {
        return imgPatientProfilePic;
    }

    public ImageButton getIbtnMenu() {
        return ibtnMenu;
    }

    public TextView getTxtPatientNameUnRegister() {
        return txtPatientNameUnRegister;
    }

    public TextView getTxtPatientMobileUnRegister() {
        return txtPatientMobileUnRegister;
    }

    public TextView getTxtPatientTimingUnRegister() {
        return txtPatientTimingUnRegister;
    }

    public TextView getTxtPatientSerialNoUnRegister() {
        return txtPatientSerialNoUnRegister;
    }

    public RoundedCornerRectangleImageView getImgPatientProfilePicUnRegister() {
        return imgPatientProfilePicUnRegister;
    }

    public RelativeLayout getRltLayoutPatientQueueUnRegister() {
        return rltLayoutPatientQueueUnRegister;
    }

    public ImageButton getIbtnPlus() {
        return ibtnPlus;
    }

    public TextView getTxtPatientAppointmentTime() {
        return txtPatientAppointmentTime;
    }

    public TextView getTxtPatientAppointmentTimeUnRegister() {
        return txtPatientAppointmentTimeUnRegister;
    }

    public TextView getTxtFamilyName() {
        return txtFamilyName;
    }

    public void setTxtFamilyName(TextView txtFamilyName) {
        this.txtFamilyName = txtFamilyName;
    }

    public TextView getTxtFamilyMobile() {
        return txtFamilyMobile;
    }

    public void setTxtFamilyMobile(TextView txtFamilyMobile) {
        this.txtFamilyMobile = txtFamilyMobile;
    }

    public TextView getTxtFamilyTiming() {
        return txtFamilyTiming;
    }

    public void setTxtFamilyTiming(TextView txtFamilyTiming) {
        this.txtFamilyTiming = txtFamilyTiming;
    }

    public TextView getTxtFamilySerialNo() {
        return txtFamilySerialNo;
    }

    public void setTxtFamilySerialNo(TextView txtFamilySerialNo) {
        this.txtFamilySerialNo = txtFamilySerialNo;
    }

    public TextView getTxtFamilyMembersName() {
        return txtFamilyMembersName;
    }

    public void setTxtFamilyMembersName(TextView txtFamilyMembersName) {
        this.txtFamilyMembersName = txtFamilyMembersName;
    }

    public RoundedCornerRectangleImageView getImgFamilyProfilePic() {
        return imgFamilyProfilePic;
    }

    public void setImgFamilyProfilePic(RoundedCornerRectangleImageView imgFamilyProfilePic) {
        this.imgFamilyProfilePic = imgFamilyProfilePic;
    }

    public RelativeLayout getRltLayoutPatientQueueFamily() {
        return rltLayoutPatientQueueFamily;
    }

    public void setRltLayoutPatientQueueFamily(RelativeLayout rltLayoutPatientQueueFamily) {
        this.rltLayoutPatientQueueFamily = rltLayoutPatientQueueFamily;
    }

    public TextView getTxtPatientAppointmentTimeFamily() {
        return txtPatientAppointmentTimeFamily;
    }

    public void setTxtPatientAppointmentTimeFamily(TextView txtPatientAppointmentTimeFamily) {
        this.txtPatientAppointmentTimeFamily = txtPatientAppointmentTimeFamily;
    }

    /*Newly added fileds for register user*/

    public TextView getTxtBloodGroup() {
        return txtBloodGroup;
    }

    public void setTxtBloodGroup(TextView txtBloodGroup) {
        this.txtBloodGroup = txtBloodGroup;
    }

    public TextView getTxtHIN() {
        return txtHIN;
    }

    public void setTxtHIN(TextView txtHIN) {
        this.txtHIN = txtHIN;
    }

    public TextView getTxtChiefComplaint() {
        return txtChiefComplaint;
    }

    public void setTxtChiefComplaint(TextView txtChiefComplaint) {
        this.txtChiefComplaint = txtChiefComplaint;
    }

    public TextView getTxtHospitalId() {
        return txtHospitalId;
    }

    public void setTxtHospitalId(TextView txtHospitalId) {
        this.txtHospitalId = txtHospitalId;
    }

    /*MR fields*/

    public TextView getTxtPatientNameMR() {
        return txtPatientNameMR;
    }

    public void setTxtPatientNameMR(TextView txtPatientNameMR) {
        this.txtPatientNameMR = txtPatientNameMR;
    }

    public TextView getTxtPatientMobileMR() {
        return txtPatientMobileMR;
    }

    public void setTxtPatientMobileMR(TextView txtPatientMobileMR) {
        this.txtPatientMobileMR = txtPatientMobileMR;
    }

    public TextView getTxtPatientTimingMR() {
        return txtPatientTimingMR;
    }

    public void setTxtPatientTimingMR(TextView txtPatientTimingMR) {
        this.txtPatientTimingMR = txtPatientTimingMR;
    }

    public TextView getTxtPatientSerialNoMR() {
        return txtPatientSerialNoMR;
    }

    public void setTxtPatientSerialNoMR(TextView txtPatientSerialNoMR) {
        this.txtPatientSerialNoMR = txtPatientSerialNoMR;
    }

//    public TextView getTxtFamilyMembersMR() {
//        return txtFamilyMembersMR;
//    }
//
//    public void setTxtFamilyMembersMR(TextView txtFamilyMembersMR) {
//        this.txtFamilyMembersMR = txtFamilyMembersMR;
//    }

    public RoundedCornerRectangleImageView getImgPatientProfilePicMR() {
        return imgPatientProfilePicMR;
    }

    public void setImgPatientProfilePicMR(RoundedCornerRectangleImageView imgPatientProfilePicMR) {
        this.imgPatientProfilePicMR = imgPatientProfilePicMR;
    }

    public RelativeLayout getRltLayoutPatientQueueMR() {
        return rltLayoutPatientQueueMR;
    }

    public void setRltLayoutPatientQueueMR(RelativeLayout rltLayoutPatientQueueMR) {
        this.rltLayoutPatientQueueMR = rltLayoutPatientQueueMR;
    }

    public TextView getTxtPatientAppointmentTimeMR() {
        return txtPatientAppointmentTimeMR;
    }

    public void setTxtPatientAppointmentTimeMR(TextView txtPatientAppointmentTimeMR) {
        this.txtPatientAppointmentTimeMR = txtPatientAppointmentTimeMR;
    }

    public ImageButton getIbtnMenuMR() {
        return ibtnMenuMR;
    }

    public void setIbtnMenuMR(ImageButton ibtnMenuMR) {
        this.ibtnMenuMR = ibtnMenuMR;
    }

    public TextView getTxtPatientAgeMR() {
        return txtPatientAgeMR;
    }

    public void setTxtPatientAgeMR(TextView txtPatientAgeMR) {
        this.txtPatientAgeMR = txtPatientAgeMR;
    }

    public TextView getTxtPatientGenderMR() {
        return txtPatientGenderMR;
    }

    public void setTxtPatientGenderMR(TextView txtPatientGenderMR) {
        this.txtPatientGenderMR = txtPatientGenderMR;
    }

    public TextView getTxtBloodGroupMR() {
        return txtBloodGroupMR;
    }

    public void setTxtBloodGroupMR(TextView txtBloodGroupMR) {
        this.txtBloodGroupMR = txtBloodGroupMR;
    }

    public TextView getTxtHINMR() {
        return txtHINMR;
    }

    public void setTxtHINMR(TextView txtHINMR) {
        this.txtHINMR = txtHINMR;
    }

    public TextView getTxtChiefComplaintMR() {
        return txtChiefComplaintMR;
    }

    public void setTxtChiefComplaintMR(TextView txtChiefComplaintMR) {
        this.txtChiefComplaintMR = txtChiefComplaintMR;
    }

}
