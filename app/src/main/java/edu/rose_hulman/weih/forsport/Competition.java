package edu.rose_hulman.weih.forsport;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/22.
 */

public class Competition implements Parcelable,ForSportData {
    private String name;
    private String site;
    private String ID;
    private String phonenum;
    private String startdate;
    private String enddate;
    private String des;
    private String holder;
    private Bitmap image = null;

    public Competition() {
    }

    public Competition(String name) {
        this.name = name;
    }

    protected Competition(Parcel in) {
        name = in.readString();
        site = in.readString();
        ID = in.readString();
        phonenum = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        des = in.readString();
        holder = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(ID);
        dest.writeString(phonenum);
        dest.writeString(startdate);
        dest.writeString(enddate);
        dest.writeString(des);
        dest.writeString(holder);
        dest.writeParcelable(image,flags);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
