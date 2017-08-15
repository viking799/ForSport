package edu.rose_hulman.weih.forsport;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/5.
 */

public class Game implements ForSportEvent,Parcelable {
    private String name;
    private String currentEvent;
    private ArrayList<User> Holders = new ArrayList<>();
    private ArrayList<User> players = new ArrayList<>();
    private String site;
    private String startdate = "19999999";
    private String enddate = "19999999";
    private String remarks = "";
    private String ID;


    public Game(String name) {
        this.name = name;
    }

    protected Game(Parcel in) {
        name = in.readString();
        Holders = in.createTypedArrayList(User.CREATOR);
        players = in.createTypedArrayList(User.CREATOR);
        site = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        remarks = in.readString();
        ID = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public Game() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(String currentEvent) {
        this.currentEvent = currentEvent;
    }

    public ArrayList<User> getHolders() {
        return Holders;
    }

    public void setHolders(ArrayList<User> holders) {
        Holders = holders;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getID() {
        return ID;
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
        dest.writeString(name);
        dest.writeTypedList(Holders);
        dest.writeTypedList(players);
        dest.writeString(site);
        dest.writeString(startdate);
        dest.writeString(enddate);
        dest.writeString(remarks);
        dest.writeString(ID);
    }
}
