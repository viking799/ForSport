package edu.rose_hulman.weih.forsport;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FragmentsEventListener, GoogleApiClient.OnConnectionFailedListener {
    private String currentType;
    private User mUser;
    private NavigationView mNavView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthLis;
    private FirebaseDatabase mFB;
    private FirebaseStorage mFS;
    private String userIDinFB;
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
        mFB = FirebaseDatabase.getInstance();
        mFS = FirebaseStorage.getInstance();


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

        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        initializeListeners();
        initializeGoogle();


        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = ListActivityFragment.newInstance("Tennis"); //HardCode here
            currentType = "Tennis";  //Hardcode Here
            ft.add(R.id.fragment_container, fragment);
            ft.commit();
        }
    }


    private void initializeListeners() {
        mAuthLis = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if(user != null){
                    Log.e("TTT","Users: "+ user.getEmail());
                    switchToUserNav(user);
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
        if(loginFragment!= null){
            loginFragment.onLoginError(message);
        }else{
            new AlertDialog.Builder(this).setTitle(R.string.googleloginfailed)
                    .setNegativeButton(android.R.string.cancel, null).show();
        }

    }

    private void switchToLoginNav() {
        TextView ntv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navUsername);
        TextView ttv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navAccounttype);
        ntv.setText(R.string.app_name);
        ttv.setText(R.string.forsportdes);
        mNavView.getMenu().clear();
        mNavView.inflateMenu(R.menu.activity_login_drawer);

    }

    private void switchToUserNav(final FirebaseUser user) {
        TextView ntv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navUsername);
        TextView ttv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navAccounttype);
        userIDinFB = "";
        mFB.getReference().child("reg").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> regmap = (HashMap<String, String>) dataSnapshot.getValue();
                if(regmap.keySet().contains(user.getUid())){
                    userIDinFB = (String) regmap.get(user.getUid());
                    CompleteLogin(user);
                }else {
                    int size = regmap.size()+1;
                    StringBuilder sb = new StringBuilder();
                    int numof0 = 9-(size/10);
                    for(int i = 0; i < numof0 ; i++){
                        sb.append(0);
                    }
                    sb.append(size);
                    userIDinFB = sb.toString();
                    mFB.getReference().child("reg").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null){
                                mFB.getReference().child("reg").child(user.getUid()).setValue(userIDinFB, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        CompleteLogin(user);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void CompleteLogin(final FirebaseUser user) {
        final TextView ntv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navUsername);
        final TextView ttv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navAccounttype);
        final ImageView niv = (ImageView) mNavView.getHeaderView(0).findViewById(R.id.navimageView);
        final DatabaseReference curref = mFB.getReference().child("users").child(userIDinFB);
        curref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //HashMap<String,Objects> mUserData = (HashMap<String,Objects>) dataSnapshot.getValue();
                if(dataSnapshot.getValue()!= null){
                    mUser = new User();
                    HashMap<String,String> mUserData = (HashMap<String,String>) dataSnapshot.getValue();
                    mUser.setName(mUserData.get("name"));
                    mUser.setPhonenum(mUserData.get("phonenum"));
                    mUser.setEmail(mUserData.get("email"));
                    mUser.setID(userIDinFB);
                    ntv.setText(mUser.getName());
                    ttv.setText(mUser.getEmail());
                    StorageReference imagerf = mFS.getReference().child("users").child(String.valueOf(userIDinFB)+".png");
                    final long ONE_MEGABYTE = 1024 * 1024;
                    imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            niv.setImageBitmap(bitmap);
                            mUser.setImage(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }else {
                    mUser = new User(user);
                    mUser.setID(userIDinFB);
                    ntv.setText(mUser.getName());
                    ttv.setText(mUser.getEmail());
                    curref.child("name").setValue(mUser.getName());
                    curref.child("gender").setValue(String.valueOf(mUser.getGender()));
                    curref.child("email").setValue(mUser.getEmail());
                    curref.child("des").setValue(mUser.getDes());
                    curref.child("phonenum").setValue(mUser.getPhonenum());
                    SettingAccount();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mNavView.getMenu().clear();
        mNavView.inflateMenu(R.menu.activity_main_drawer);
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
                SettingAccount();
                return true;
            case R.id.nav_logout:
                mAuth.signOut();
                mUser = null;
                userIDinFB = "";
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment nfragment = ListActivityFragment.newInstance("Tennis");
                ft.replace(R.id.fragment_container,nfragment);
                ft.commit();
                return true;
        }




        return true;
    }

    private void SettingAccount() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment sfragment = SettingFragment.newInstance(mUser,userIDinFB);
        ft.replace(R.id.fragment_container, sfragment);
        ft.addToBackStack("detail");
        ft.commit();
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
            Fragment fragment = ListScheduleFragment.newInstance(mUser);
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

    public void SchedulingAGame(User obj) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ListScheduleFragment.newInstance(this.mUser, obj);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("detail");
        ft.commit();
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
    public void UserDataChanged(User user, boolean change) {
        mUser.setEmail(user.getEmail());
        mUser.setPhonenum(user.getPhonenum());
        mUser.setName(user.getName());
        if(change){
            mUser.setImage(user.getImage());
        }
        TextView ntv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navUsername);
        TextView ttv = (TextView) mNavView.getHeaderView(0).findViewById(R.id.navAccounttype);
        ImageView iv = (ImageView) mNavView.getHeaderView(0).findViewById(R.id.navimageView);
        ntv.setText(this.mUser.getName());
        ttv.setText(this.mUser.getEmail());
        iv.setImageBitmap(this.mUser.getImage());



        super.onBackPressed();
        mFB.getReference().child("users").child(userIDinFB).child("name").setValue(mUser.getName());
        mFB.getReference().child("users").child(userIDinFB).child("email").setValue(mUser.getEmail());
        mFB.getReference().child("users").child(userIDinFB).child("phonenum").setValue(mUser.getPhonenum());

        if(change) {
            StorageReference mf = mFS.getReference().child("users").child(String.valueOf(userIDinFB) + ".png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mUser.getImage().compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mf.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("TTTAS", exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("TTTP!A", "done");
                }
            });
        }
        //TODO check if image exist;

        //TODO put image to firebase



        Log.e("TTT", this.mUser.toString());
    }

    @Override
    public void mark(ForSportData mdata) {
        Toast.makeText(this, R.string.Mark,Toast.LENGTH_SHORT).show();
        DatabaseReference curref = FirebaseDatabase.getInstance().getReference().child("users").child(userIDinFB).child("mMark");
        if(mdata.getClass() == Site.class){
            curref.child("S"+mdata.getID()).setValue("");
        }else if(mdata.getClass() == User.class){
            curref.child("U"+mdata.getID()).setValue("");
        }else if(mdata.getClass() == Competition.class){
            curref.child("C"+mdata.getID()).setValue("");
        }else if(mdata.getClass() == TrainingPlan.class){
            curref.child("T"+mdata.getID()).setValue("");
        }
    }

    @Override
    public void GetTraining(TrainingPlan mTp, Site mSt, User mCoach) {
        String tempID = mTp.getID()+mUser.getID();
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("events").child(tempID);
        tempRef.child("name").setValue(mTp.getName() + " for " + mUser.getName());
        tempRef.child("site").setValue(mSt.getID());
        tempRef.child("startdate").setValue(mTp.getStartdate());
        tempRef.child("enddate").setValue(mTp.getEnddate());
        tempRef.child("currentEvent").setValue(mTp.getID());
        tempRef.child("mholders").child(mCoach.getID()).setValue("");
        tempRef.child("mplayers").child(mUser.getID()).setValue("");

        FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getID()).child("mEvent").child(tempID).setValue("");
        FirebaseDatabase.getInstance().getReference().child("users").child(mCoach.getID()).child("mEvent").child(tempID).setValue("");
    }

    @Override
    public void JoinComp(Competition mCom) {
        String tempID = mCom.getID();
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("events").child(tempID);
        tempRef.child("name").setValue(mCom.getName());
        tempRef.child("site").setValue(mCom.getSite());
        tempRef.child("startdate").setValue(mCom.getStartdate());
        tempRef.child("enddate").setValue(mCom.getEnddate());
        tempRef.child("currentEvent").setValue(mCom.getID());
        tempRef.child("mholders").child(mCom.getHolder()).setValue("");
        tempRef.child("mplayers").child(mUser.getID()).setValue("");
        FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getID()).child("mEvent").child(tempID).setValue("");
        FirebaseDatabase.getInstance().getReference().child("users").child(mCom.getHolder()).child("mEvent").child(tempID).setValue("");
    }

    @Override
    public void scheduleAGame(String text, User user, Site site) {
        String tempID = mUser.getID()+user.getID()+text;
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("events").child(tempID);
        tempRef.child("name").setValue(user.getName()+ " Vs "+ mUser.getName() + " On " + text);
        tempRef.child("site").setValue(site.getID());
        tempRef.child("startdate").setValue(text);
        tempRef.child("enddate").setValue(text);
        tempRef.child("mplayers").child(mUser.getID()).setValue("");
        tempRef.child("mplayers").child(user.getID()).setValue("");
        FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getID()).child("mEvent").child(tempID).setValue("");
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getID()).child("mEvent").child(tempID).setValue("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
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
