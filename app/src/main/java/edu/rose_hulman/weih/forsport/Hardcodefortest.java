package edu.rose_hulman.weih.forsport;

import java.util.ArrayList;
import java.util.List;

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

    public static List<User> matchuserlist() {
        ArrayList<User> result = new ArrayList<>();
        result.add(new User("Tony"));
        result.add(new User("Green"));
        result.add(new User("White"));
        result.add(new User("Black"));
        result.add(new User("Brown"));
        return result;
    }

    public static List<TrainingPlan> matchPlanlist() {
        ArrayList<TrainingPlan> result = new ArrayList<>();
        result.add(new TrainingPlan("Plan a"));
        result.add(new TrainingPlan("Plan b"));
        result.add(new TrainingPlan("Plan c"));
        result.add(new TrainingPlan("Plan d"));
        result.add(new TrainingPlan("Plan e"));
        return result;
    }
}
