package edu.rose_hulman.weih.forsport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/29.
 */

public class Site implements ForSportData,Parcelable{
    private long ID = -1;
    private String name ;
    private String location = "XX city XX street XX state XX country";
    private String image;


    public Site(String name) {
        this.name = name;
    }

    protected Site(Parcel in) {
        ID = in.readLong();
        name = in.readString();
        location = in.readString();
        image = in.readString();
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

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(image);
    }
}
