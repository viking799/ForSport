package edu.rose_hulman.weih.forsport;

/**
 * Created by Administrator on 2017/8/5.
 */

public interface ForSportEvent {
    String name = null;
    ForSportData CurrentEvent = null;

    void setCurrentEvent(String data);

    String getName();
    String getCurrentEvent();
}
