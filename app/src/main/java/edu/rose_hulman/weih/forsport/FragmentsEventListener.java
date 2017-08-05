package edu.rose_hulman.weih.forsport;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by Administrator on 2017/7/22.
 */

public interface FragmentsEventListener {
    void onUserSelect(User user);

    void Register(Competition competition);

    void onSelectActionToUser(User user);

    void SelectTrainingFromUser(User user);

    void onActSelected(String Activity);

    void onTypeSelected(String painting);

    void onPlanSelect(TrainingPlan trainingPlan);

    void onCompselect(Competition competition);

    void onListUserSelect(User user);

    void onListSiteSelect(Site site);

    void onLogin(String account, String password);

    void onGoogleLogin();

    void onDataSelect(ForSportData data);

    void onSiteSelect(Site site);

    void onEventSelect(ForSportEvent forSportEvent);

    void UserDataChanged(User mUser);
}
