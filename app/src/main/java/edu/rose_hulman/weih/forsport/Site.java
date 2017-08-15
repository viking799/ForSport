package edu.rose_hulman.weih.forsport;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/29.
 */

public class Site implements ForSportData,Parcelable{
    private String ID = "-1";
    private String name ;
    private String loc = "XX city XX street XX state XX country";
    private Bitmap image;
    private String des;

    public Site() {
    }

    public Site(String name) {
        this.name = name;
    }

    protected Site(Parcel in) {
        ID = in.readString();
        name = in.readString();
        loc = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        des = in.readString();
    }

    public static final Creator<Site> CREATOR = new Creator<Site>() {
        @Override
        public Site createFromParcel(Parcel in) {
            return new Site(in);
        }

        @Override
        public Site[] newArray(int size) {
            return new Site[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getLocation() {
        return loc;
    }

    public String getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(name);
        dest.writeString(loc);
        dest.writeString(des);
        dest.writeParcelable(image,flags);
    }
}
