package edu.rose_hulman.weih.forsport;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Administrator on 2017/7/22.
 */

public class User implements Parcelable, ForSportData {
    private String name = "Your Name";
    private Bitmap image;
    private int age = 99;
    private String ID = "XXXXXXXXX";
    private String password = "0000099999";
    private String phonenum = "";
    private String email = "";
    private String gender = "M";
    private String location = "";
    private String des = "";
    private double rate = 0;
    private boolean isCoach = true;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }


    protected User(Parcel in) {
        name = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        age = in.readInt();
        ID = in.readString();
        password = in.readString();
        phonenum = in.readString();
        email = in.readString();
        gender = in.readString();
        location = in.readString();
        des = in.readString();
        rate = in.readDouble();
        isCoach = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(FirebaseUser user) {
        name = user.getDisplayName();
        email = user.getEmail();
        //image = user.getPhotoUrl();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(image, flags);
        dest.writeInt(age);
        dest.writeString(ID);
        dest.writeString(password);
        dest.writeString(phonenum);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(location);
        dest.writeString(des);
        dest.writeDouble(rate);
        dest.writeByte((byte) (isCoach ? 1 : 0));
    }

    @Override
    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getAge() {
        return age;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getEmail() {
        return email;
    }

    public String setGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getDes() {
        return des;
    }

    public double getRate() {
        return rate;
    }

    public boolean isCoach() {
        return isCoach;
    }

    public String getGender() {
        return gender;
    }
}
