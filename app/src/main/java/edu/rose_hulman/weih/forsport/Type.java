package edu.rose_hulman.weih.forsport;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class Type {
    private String mType;

    public static List<String> initializeList(Context mContext) {
        List<String> tlist = new ArrayList<>();
        tlist.add(mContext.getResources().getString(R.string.Tennis));
        tlist.add(mContext.getResources().getString(R.string.Basketball));
        tlist.add(mContext.getResources().getString(R.string.Soccer));
        tlist.add(mContext.getResources().getString(R.string.TableTennis));

        return tlist;
    }
}
