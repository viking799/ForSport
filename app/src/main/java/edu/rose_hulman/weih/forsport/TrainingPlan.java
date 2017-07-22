package edu.rose_hulman.weih.forsport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/22.
 */

public class TrainingPlan implements Parcelable {
    private String name;
    private int fee = 200;
    private char feetype = '$';
    private String des = "brabrabrabrabra";
    private User coach = new User("coach");
    private long startdate = 19991212;
    private long enddate = 19991212;
    private int starttime = 600;
    private int endtime = 800;
    private double rate = 0;

    protected TrainingPlan(Parcel in) {
        name = in.readString();
        fee = in.readInt();
        des = in.readString();
        coach = in.readParcelable(User.class.getClassLoader());
        startdate = in.readLong();
        enddate = in.readLong();
        starttime = in.readInt();
        endtime = in.readInt();
        rate = in.readDouble();
        location = in.readString();
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

    public double getRate() {
        return rate;
    }

    public String getLocation() {
        return location;
    }

    private String location = "trainninglocation";

    public TrainingPlan() {
    }

    public TrainingPlan(String name) {
        this.name = name;
    }

    public TrainingPlan(String name, int fee, char feetype, String des, User coach, long startdate, long enddate, int starttime, int endtime) {
        this.name = name;
        this.fee = fee;
        this.feetype = feetype;
        this.des = des;
        this.coach = coach;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getName() {
        return name;
    }

    public int getFee() {
        return fee;
    }

    public char getFeetype() {
        return feetype;
    }

    public String getDes() {
        return des;
    }

    public User getCoach() {
        return coach;
    }

    public long getStartdate() {
        return startdate;
    }

    public long getEnddate() {
        return enddate;
    }

    public int getStarttime() {
        return starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(fee);
        dest.writeString(des);
        dest.writeParcelable(coach, flags);
        dest.writeLong(startdate);
        dest.writeLong(enddate);
        dest.writeInt(starttime);
        dest.writeInt(endtime);
        dest.writeDouble(rate);
        dest.writeString(location);
    }
}
