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

    public static List<Site> sitelist() {
        ArrayList<Site> result = new ArrayList<>();
        result.add(new Site("Earth"));
        result.add(new Site("Mars"));
        result.add(new Site("Moon"));
        result.add(new Site("Jupiter"));
        result.add(new Site("Mercury"));
        return result;
    }

    public static List<ForSportEvent> gamelist() {
        ArrayList<ForSportEvent> result = new ArrayList<>();
        result.add(new Game("comp 1"));
        //result.get(0).setCurrentEvent(new Competition("Comp1"));
        result.add(new Game("trainning 1"));
        //result.get(1).setCurrentEvent(new TrainingPlan("Plan A"));
        result.add(new Game("game c"));
        result.add(new Game("game d"));
        result.add(new Game("game e"));
        return result;
    }

    public static List<ForSportData> datalist() {
        ArrayList<ForSportData> result= new ArrayList<>();
        result.add(new Site("Site a"));
        result.add(new Site("Site b"));
        result.add(new TrainingPlan("Plan c"));
        result.add(new TrainingPlan("Plan d"));
        result.add(new User("White"));
        result.add(new User("Black"));
        result.add(new Competition("Competition4"));
        return result;
    }
}
