package edu.rose_hulman.weih.forsport;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

class Activities {

    public static List<String> initializeList(Context mContext) {
       // MainActivity m = (MainActivity) mContext;
       // String st = m.getCurrentType();
        List<String> tlist = new ArrayList<>();
        tlist.add(mContext.getString(R.string.Schedule));
        tlist.add(mContext.getString(R.string.JoinMatch));
        tlist.add(mContext.getString(R.string.gettrainning));

        return tlist;
    }

    public static List<String> initializeList(Context mContext, String mcurType) {
        List<String> tlist = new ArrayList<>();
     //   tlist.add(mcurType);
        tlist.add(mContext.getString(R.string.Schedule));
        tlist.add(mContext.getString(R.string.JoinMatch));
        tlist.add(mContext.getString(R.string.gettrainning));

        return tlist;
    }
}
