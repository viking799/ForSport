package edu.rose_hulman.weih.forsport;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FragmentsEventListener
{
private String currentType;

                public String getCurrentType() {
                    return currentType;
                }

                public void setCurrentType(String currentType) {
                    this.currentType = currentType;
                }

                @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new TypeSelect();
            ft.add(R.id.fragment_container, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTypeSelected(String st) {
        currentType = st;
        Log.e("RTT",st);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ActivityListFragment.newInstance(st);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }
    @Override
    public void onActSelected(String at) {
        Log.e("RTT",at);
        if(at.equals(getResources().getString(R.string.Schedule))){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = ScheduleFragment.newInstance(currentType);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack("detail");
            ft.commit();
        }else if(at.equals(getResources().getString(R.string.JoinMatch))){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = CompetetionFragment.newInstance(currentType,currentType);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack("detail");
            ft.commit();
        }else if(at.equals(getResources().getString(R.string.gettrainning))){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = TrainingFragment.newInstance(currentType);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack("detail");
            ft.commit();
        }
    }


    @Override
    public void onCompselect(Competition competition) {
        Log.e("RTT",competition.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = CompetetionDetailFragment.newInstance(competition);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }

    @Override
    public void onListUserSelect(User user) {
        Log.e("TTT",user.getName());
    }

    @Override
    public void onListSiteSelect(Site site) {
        Log.e("TTT",site.getName());
    }

    @Override
    public void Register(Competition competition) {
        Log.e("RTT","REG!!"+ competition.getName());
    }

    @Override
    public void onUserSelect(User user) {
        Log.e("RTT","REG!!"+ user.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = OtheruserdetailFragment.newInstance(user);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }

    @Override
    public void onPlanSelect(TrainingPlan trainingPlan) {
        Log.e("RTT","REG!!"+ trainingPlan.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = TrainingDetailFragment.newInstance(trainingPlan);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }

    @Override
    public void onSelectActionToUser(User user) {
        Log.e("RTT","REG!!"+ user.getName());
    }

    @Override
    public void SelectTrainingFromUser(User user) {
        Log.e("RTT","REG!!"+ user.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = TrainingListFragment.newInstance(currentType,user);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }
}
