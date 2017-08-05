package edu.rose_hulman.weih.forsport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FragmentsEventListener, GoogleApiClient.OnConnectionFailedListener {
    private String currentType;
    private User mUser;
    private NavigationView mNavView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthLis;
    OnCompleteListener mOnCompleteListener;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_GOOGLE_SIGN_IN = 1;
    private static final int RC_ROSEFIRE_LOGIN = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();



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

        mNavView = (NavigationView) findViewById(R.id.nav_view); //TODO
        mNavView.setNavigationItemSelectedListener(this);

        initializeListeners();
        initializeGoogle();


        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new ListTypeFragment();
            ft.add(R.id.fragment_container, fragment);
            ft.commit();
        }
    }


    private void initializeListeners() {
        mAuthLis = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                Log.e("TTT","Users: "+ user);
                if(user != null){
                    switchToUserNav(user.getUid());
                }else {
                    switchToLoginNav();
                }
            }
        };
        mOnCompleteListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task task) {
                Log.e("TTT","onComplete");
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.remove(getSupportFragmentManager().findFragmentByTag(getString(R.string.TagLogin))).commit();
                if(getSupportFragmentManager().findFragmentByTag(getString(R.string.TagLogin))!= null){
                    MainActivity.super.onBackPressed();}
                if(!task.isSuccessful()){
                    Log.e("TTT","finished");
                    showLoginError("Login failed");
                }
            }
        };
    }

    private void initializeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
    }


    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.TagLogin));
        loginFragment.onLoginError(message);
    }

    private void switchToLoginNav() {
        TextView ntv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navUsername);
        TextView ttv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navAccounttype);
        ntv.setText(R.string.app_name);
        ttv.setText(R.string.forsportdes);
        mNavView.getMenu().clear();
        mNavView.inflateMenu(R.menu.activity_login_drawer);

    }

    private void switchToUserNav(String uid) {
        TextView ntv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navUsername);
        TextView ttv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navAccounttype);
        ntv.setText(uid);
        ttv.setText(R.string.forsportdes);
        mNavView.getMenu().clear();
        mNavView.inflateMenu(R.menu.activity_main_drawer);
        mUser = new User(uid);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthLis);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mAuthLis!=null){
            mAuth.removeAuthStateListener(mAuthLis);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.nav_email:
                StartLogin();
                return true;
            case R.id.nav_google:
                onGoogleLogin();
                return true;
            case R.id.nav_events:
                Fragment efragment = ListEventFragment.newInstance(mUser);
                ft.replace(R.id.fragment_container, efragment);
                ft.addToBackStack("detail");
                ft.commit();
                return true;
            case R.id.nav_marks:
                Fragment mfragment = ListMarksFragment.newInstance(mUser);
                ft.replace(R.id.fragment_container, mfragment);
                ft.addToBackStack("detail");
                ft.commit();
                return true;
            case R.id.nav_tracks:
                Fragment tfragment = ListTrackFragment.newInstance(mUser);
                ft.replace(R.id.fragment_container, tfragment);
                ft.addToBackStack("detail");
                ft.commit();
                return true;
            case R.id.nav_setting:
                Fragment sfragment = SettingFragment.newInstance(mUser);
                ft.replace(R.id.fragment_container, sfragment);
                ft.addToBackStack("detail");
                ft.commit();
                return true;
            case R.id.nav_logout:
                mAuth.signOut();
                return true;

        }




        return true;
    }

    private void StartLogin() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new LoginFragment();
        ft.replace(R.id.fragment_container, fragment,getString(R.string.TagLogin));
        ft.addToBackStack("detail");
        ft.commit();
    }

    @Override
    public void onTypeSelected(String st) {
        currentType = st;
        Log.e("RTT",st);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ListActivityFragment.newInstance(st);
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
            Fragment fragment = ListCompetetionFragment.newInstance(currentType,currentType);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack("detail");
            ft.commit();
        }else if(at.equals(getResources().getString(R.string.gettrainning))){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = ListTrainingFragment.newInstance(currentType);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack("detail");
            ft.commit();
        }
    }


    @Override
    public void onCompselect(Competition competition) {
        Log.e("RTT",competition.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = DetailCompetetionFragment.newInstance(competition);
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
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(mOnCompleteListener);
    }

    @Override
    public void onGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void onDataSelect(ForSportData mdata) {
        if(mdata.getClass() == Site.class){
            onSiteSelect((Site) mdata);
        }else if(mdata.getClass() == User.class){
            onUserSelect((User) mdata);
        }else if(mdata.getClass() == Competition.class){
            onCompselect((Competition) mdata);
        }else if(mdata.getClass() == TrainingPlan.class){
            onPlanSelect((TrainingPlan) mdata);
        }
    }

    public void onSiteSelect(Site site) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = DetailSiteFragment.newInstance(site);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }

    @Override
    public void onEventSelect(ForSportEvent mevent) {
        if(mevent.getClass() == Game.class){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = DetailGameFragment.newInstance((Game) mevent);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack("detail");
            ft.commit();
        }
    }

    @Override
    public void UserDataChanged(User user) {
        mUser = user;
        TextView ntv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navUsername);
        TextView ttv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navAccounttype);
        ImageView iv = (ImageView) mNavView.getHeaderView(0).findViewById(R.id.navimageView);
        ntv.setText(this.mUser.getName());
        ttv.setText(this.mUser.getEmail());
        iv.setImageURI(this.mUser.getImage());
        super.onBackPressed();
        Log.e("TTT", this.mUser.toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showLoginError("Google sign-in failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TTT", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,mOnCompleteListener);
    }

    @Override
    public void Register(Competition competition) {
        Log.e("RTT","REG!!"+ competition.getName());
    }

    @Override
    public void onUserSelect(User user) {
        Log.e("RTT","REG!!"+ user.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = DetailOtheruserFragment.newInstance(user);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }

    @Override
    public void onPlanSelect(TrainingPlan trainingPlan) {
        Log.e("RTT","REG!!"+ trainingPlan.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = DetailTrainingFragment.newInstance(trainingPlan);
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
        Fragment fragment = ListTrainingPlanFragment.newInstance(currentType,user);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLoginError("Google connection failed");
    }
}
