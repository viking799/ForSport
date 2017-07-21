package edu.rose_hulman.weih.forsport;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/21.
 */

public class Hardcodefortest {
    public static ArrayList<Competition> matchlist(){
        ArrayList<Competition> result = new ArrayList<>();
        result.add(new Competition("Competition1"));
        result.add(new Competition("Competition2"));
        result.add(new Competition("Competition3"));
        result.add(new Competition("Competition4"));
        result.add(new Competition("Competition5"));
        return result;
    }
}
