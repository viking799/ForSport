package edu.rose_hulman.weih.forsport;

/**
 * Created by Administrator on 2017/7/29.
 */

public class Site {
    private long ID = -1;
    private String name ;
    private String location = "XX city XX street XX state XX country";
    private String image;


    public Site(String name) {
        this.name = name;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
