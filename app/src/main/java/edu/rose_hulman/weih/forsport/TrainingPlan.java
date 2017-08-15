package edu.rose_hulman.weih.forsport;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/22.
 */

public class TrainingPlan implements Parcelable,ForSportData {
    private String name;
    private String fee = "200";
    private String des = "";
    private String coach = "0000000001";
    private String startdate = "19991212";
    private String enddate = "19991212";
    private String ID = "";
    private String site = null;

    protected TrainingPlan(Parcel in) {
        name = in.readString();
        fee = in.readString();
        des = in.readString();
        coach = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        site = in.readString();
        ID = in.readString();
    }

    public static final Creator<TrainingPlan> CREATOR = new Creator<TrainingPlan>() {
        @Override
        public TrainingPlan createFromParcel(Parcel in) {
            return new TrainingPlan(in);
        }

        @Override
        public TrainingPlan[] newArray(int size) {
            return new TrainingPlan[size];
        }
    };



    public TrainingPlan() {
    }

    public TrainingPlan(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public Bitmap getImage() {
        return null;
    }


    public String getDes() {
        return des;
    }

    public String getCoach() {
        return coach;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(des);
        dest.writeString(coach);
        dest.writeString(startdate);
        dest.writeString(enddate);
        dest.writeString(site);
        dest.writeString(ID);
        dest.writeString(fee);
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
