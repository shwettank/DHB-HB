package com.dhb.request_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhb.models.Patients;
import com.dhb.models.Visit;

import java.util.ArrayList;

public class VisitModel implements Parcelable {

    Visit visit;

    ArrayList<Visit> followUpVisits;

    public VisitModel() {
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public ArrayList<Visit> getFollowUpVisits() {
        return followUpVisits;
    }

    public void setFollowUpVisits(ArrayList<Visit> followUpVisits) {
        this.followUpVisits = followUpVisits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(visit, flags);
        dest.writeList(followUpVisits);
    }

    protected VisitModel(Parcel in) {
        visit = in.readParcelable(Patients.class.getClassLoader());
        followUpVisits = in.readArrayList(Visit.class.getClassLoader());

    }

    public static final Creator<VisitModel> CREATOR = new Creator<VisitModel>() {
        @Override
        public VisitModel createFromParcel(Parcel in) {
            return new VisitModel(in);
        }

        @Override
        public VisitModel[] newArray(int size) {
            return new VisitModel[size];
        }

    };


}
