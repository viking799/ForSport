package edu.rose_hulman.weih.forsport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/22.
 */

public class User implements Parcelable {
    private String name;
    private int age = 99;
    private String ID = "0000099999";
    private String password = "0000099999";
    private String phonenum = "(+1)8882228888";
    private String email = "0000099999@gmail.com";
    private boolean gender = true;
    private String location = "1111, XX street, TerreHaute, IN, USA ";
    private String des = "brabrabra";
    private double rate = 0;
    private boolean isCoach = true;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, int age, String ID, String password, String phonenum, String email, boolean gender, String photo) {
        this.name = name;
        this.age = age;
        this.ID = ID;
        this.password = password;
        this.phonenum = phonenum;
        this.email = email;
        this.gender = gender;
        this.photo = photo;
    }

    protected User(Parcel in) {
        name = in.readString();
        age = in.readInt();
        ID = in.readString();
        password = in.readString();
        phonenum = in.readString();
        email = in.readString();
        gender = in.readByte() != 0;
        location = in.readString();
        des = in.readString();
        rate = in.readDouble();
        photo = in.readString();
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

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getLocation() {
        return location;
    }

    public String getDes() {
        return des;
    }

    public boolean isCoach() {
        return isCoach;
    }

    public String getEmail() {
        return email;
    }

    public boolean isGender() {
        return gender;
    }

    public String getPhoto() {
        return photo;
    }

    private String photo;

    public double getRate() {
        return rate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(ID);
        dest.writeString(password);
        dest.writeString(phonenum);
        dest.writeString(email);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(location);
        dest.writeString(des);
        dest.writeDouble(rate);
        dest.writeString(photo);
    }
}
