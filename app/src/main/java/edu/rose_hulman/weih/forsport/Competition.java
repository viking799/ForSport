package edu.rose_hulman.weih.forsport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/22.
 */

public class Competition implements Parcelable,ForSportData {
    private String name;
    private String location;
    private String phonenum;
    private long startdate;
    private long enddate;
    private long deadline;
    private String holder;
    private long bonus;
    private char bonustype;
    private long fee;
    private char feetype;

    public Competition() {
        this.name = "Competition！";
        this.location = "Location is over there";
        this.phonenum = "2222222333";
        this.startdate = 19000101;
        this.enddate = 19000101;
        this.deadline = 19000101;
        this.holder = "Holder is me!";
        this.bonus = 99999;
        this.bonustype = '$';
        this.fee = 5;
        this.feetype = '$';
    }

    public Competition(String name) {
        this.name = name;
        this.location = "over there";
        this.phonenum = "2222222333";
        this.startdate = 19000101;
        this.enddate = 19000101;
        this.deadline = 19000101;
        this.holder = "is me!";
        this.bonus = 99999;
        this.bonustype = '$';
        this.fee = 5;
        this.feetype = '$';
    }

    public Competition(String name, String location, String phonenum, long startdate, long enddate, long deadline, String holder, long bonus, char bonustype, long fee, char feetype) {
        this.name = name;
        this.location = location;
        this.phonenum = phonenum;
        this.startdate = startdate;
        this.enddate = enddate;
        this.deadline = deadline;
        this.holder = holder;
        this.bonus = bonus;
        this.bonustype = bonustype;
        this.fee = fee;
        this.feetype = feetype;
    }

    protected Competition(Parcel in) {
        name = in.readString();
        location = in.readString();
        phonenum = in.readString();
        startdate = in.readLong();
        enddate = in.readLong();
        deadline = in.readLong();
        holder = in.readString();
        bonus = in.readLong();
        fee = in.readLong();
    }

    public static final Creator<Competition> CREATOR = new Creator<Competition>() {
        @Override
        public Competition createFromParcel(Parcel in) {
            return new Competition(in);
        }

        @Override
        public Competition[] newArray(int size) {
            return new Competition[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public long getStartdate() {
        return startdate;
    }

    public long getEnddate() {
        return enddate;
    }

    public long getDeadline() {
        return deadline;
    }

    public String getHolder() {
        return holder;
    }

    public long getBonus() {
        return bonus;
    }

    public char getBonustype() {
        return bonustype;
    }

    public long getFee() {
        return fee;
    }

    public char getFeetype() {
        return feetype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(phonenum);
        dest.writeLong(startdate);
        dest.writeLong(enddate);
        dest.writeLong(deadline);
        dest.writeString(holder);
        dest.writeLong(bonus);
        dest.writeLong(fee);
    }
}
