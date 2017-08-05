package edu.rose_hulman.weih.forsport;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/5.
 */

public class Game implements ForSportEvent,Parcelable {
    private String name;
    private ForSportData currentEvent;
    private ArrayList<User> Holders = (ArrayList<User>) Hardcodefortest.matchuserlist();
    private ArrayList<User> players = (ArrayList<User>) Hardcodefortest.matchuserlist();
    private Site site = new Site("Site1");
    private long startdate = 19999999;
    private long enddate = 19999999;
    private String remarks = "";


    public Game(String name) {
        this.name = name;
    }

    protected Game(Parcel in) {
        name = in.readString();
        Holders = in.createTypedArrayList(User.CREATOR);
        players = in.createTypedArrayList(User.CREATOR);
        site = in.readParcelable(Site.class.getClassLoader());
        startdate = in.readLong();
        enddate = in.readLong();
        remarks = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForSportData getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(ForSportData currentEvent) {
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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public long getEnddate() {
        return enddate;
    }

    public void setEnddate(long enddate) {
        this.enddate = enddate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        dest.writeParcelable(site, flags);
        dest.writeLong(startdate);
        dest.writeLong(enddate);
        dest.writeString(remarks);
    }
}
