package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class ListScheduleFragment extends Fragment {
    private static final String ARG_SCHEDULE = "scheduleageamegas";
    private static final String ARG_OBJ = "scheduleageamegasobj";
    private User mUser;
    private HeadimageSiteAdapter HSAP;
    private HeadimageUserAdapter HUAP;
    private TextView DTV;
    private int UserSelectPosition = -1;
    private int SiteSelectPosition = -1;
    private FragmentsEventListener mListener;

    private ArrayList<String> SiteSelectresult = new ArrayList<>();
    private ArrayList<String> UserSelectresult = new ArrayList<>();
    private Site SiteSelected;
    private User UserSelected;


    public ListScheduleFragment() {
    }

    public static ListScheduleFragment newInstance(User user) {
        ListScheduleFragment fragment = new ListScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SCHEDULE,user);
        args.putParcelable(ARG_OBJ, null);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_SCHEDULE);
            UserSelected = getArguments().getParcelable(ARG_OBJ);
            if(UserSelected!=null){
                selectAUser();
            }
        }
        String cID = "-1";
        if(mUser != null){
            cID = mUser.getID();
        }
        HSAP = new HeadimageSiteAdapter(mListener,getContext(),cID);
        HUAP = new HeadimageUserAdapter(mListener,getContext(),cID);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        RecyclerView recyclerViewU = (RecyclerView) view.findViewById(R.id.type_list_recycler_viewUser);
        recyclerViewU.setHasFixedSize(true);
        recyclerViewU.setLayoutManager(new LinearLayoutManager(recyclerViewU.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewU.setAdapter(HUAP);
        RecyclerView recyclerViewS = (RecyclerView) view.findViewById(R.id.type_list_recycler_viewSite);
        recyclerViewS.setHasFixedSize(true);
        recyclerViewS.setLayoutManager(new LinearLayoutManager(recyclerViewS.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewS.setAdapter(HSAP);
        DTV = (TextView) view.findViewById(R.id.date_text_view);
        Calendar calendar = Calendar.getInstance();
        DTV.setText(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        DTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view  = inflater.inflate(R.layout.changedate,container,false);
                final CalendarView mCV = (CalendarView) view.findViewById(R.id.calendarView);
                final GregorianCalendar calendar = new GregorianCalendar();
                mCV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                    }
                });

                builder.setView(view);
                builder.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DTV.setText(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }).setNegativeButton(android.R.string.cancel,null);
                builder.show();
            }
        });
        Button bt  = (Button) view.findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SiteSelected == null || UserSelected == null ){
                    Toast.makeText(getContext(),"Please select a user and site.",Toast.LENGTH_LONG).show();
                }else {
                    mListener.scheduleAGame((String) DTV.getText(),UserSelected,SiteSelected);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Need to wait for the activity to be created to have an action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.Schedule);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentsEventListener) {
            mListener = (FragmentsEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static Fragment newInstance(User mUser, User obj) {
        ListScheduleFragment fragment = new ListScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SCHEDULE,mUser);
        args.putParcelable(ARG_OBJ,obj);
        fragment.setArguments(args);
        return fragment;
    }

    public class HeadimageSiteAdapter extends RecyclerView.Adapter<HeadimageSiteAdapter.ViewHolder>{

        private List<Site> mSite;
        private FragmentsEventListener mListener;
        private Context mContext;
        private String mtype;

        public HeadimageSiteAdapter(FragmentsEventListener listener, Context context, final String Sporttype) {
            mListener = listener;
            mContext = context;
            mSite = new ArrayList<>();
            mtype = Sporttype;

            FirebaseDatabase.getInstance().getReference().child("sites").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final Site temp = dataSnapshot.getValue(Site.class);
                    temp.setID(dataSnapshot.getKey());
                    mSite.add(temp);
                    StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("sites").child(String.valueOf(temp.getID()) + ".png");
                    final long ONE_MEGABYTE = 1024 * 1024;
                    imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            temp.setImage(bitmap);
                            notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("SSS", exception.toString()+ "?");
                        }
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        @Override
        public HeadimageSiteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_name_view, parent, false);
            return new HeadimageSiteAdapter.ViewHolder(view);

        }

        @Override
        public int getItemCount() {
            return mSite.size();
        }


        @Override
        public void onBindViewHolder(HeadimageSiteAdapter.ViewHolder holder, int position) {
            Site mt = mSite.get(position);
            holder.nTV.setText(mt.getName());
            holder.iV.setImageBitmap(mt.getImage());
            if(UserSelected != null){
                if(!UserSelectresult.contains(mt.getID())){
                    holder.itemView.setBackgroundColor(Color.GRAY);
                }else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            if(SiteSelected!=null) {
                if (SiteSelected.getID().equals(mt.getID())) {
                    holder.itemView.setBackgroundColor(Color.YELLOW);
                }
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView iV;
            private TextView nTV;

            public ViewHolder(View itemView) {
                super(itemView);
                iV = (ImageView) itemView.findViewById(R.id.only_image_view);
                nTV = (TextView) itemView.findViewById(R.id.only_text_view);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(final View siteview) {

                if(UserSelectPosition!= -1){
                    if (UserSelectresult.contains(mSite.get(getAdapterPosition()).getID())){
                        SiteSelectPosition = getAdapterPosition();
                        SiteSelected = mSite.get(getAdapterPosition());
                        selectASite();
                    }else{
                        new AlertDialog.Builder(getContext()).setTitle(R.string.PlaySiteNotMatch)
                                .setPositiveButton(R.string.SelectThisPlayerAnyway, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserSelectPosition = -1;
                                        UserSelected = null;
                                        SiteSelectPosition = getAdapterPosition();
                                        SiteSelected = mSite.get(getAdapterPosition());
                                        selectASite();
                                    }
                                }).setNegativeButton(android.R.string.cancel, null).show();
                    }
                }else {
                    SiteSelectPosition = getAdapterPosition();
                    SiteSelected = mSite.get(getAdapterPosition());
                    selectASite();
                }
            }
        }
    }



    public class HeadimageUserAdapter extends RecyclerView.Adapter<HeadimageUserAdapter.ViewHolder>{

        private List<User> mUsers;
        private FragmentsEventListener mListener;
        private Context mContext;
        private String mtype;

        public HeadimageUserAdapter(FragmentsEventListener listener, Context context, String Sporttype) {
            mListener = listener;
            mContext = context;
            mUsers = new ArrayList<>();
            mtype = Sporttype;

            FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (!dataSnapshot.getKey().equals(mUser.getID())) {
                        final User temp = dataSnapshot.getValue(User.class);
                        temp.setID(dataSnapshot.getKey());
                        mUsers.add(temp);
                        StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("users").child(String.valueOf(temp.getID()) + ".png");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                temp.setImage(bitmap);
                                notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("SSS", exception.toString() + "?");
                            }
                        });
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        @Override
        public HeadimageUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_name_view, parent, false);
            return new HeadimageUserAdapter.ViewHolder(view);

        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }


        @Override
        public void onBindViewHolder(HeadimageUserAdapter.ViewHolder holder, int position) {
            User mt = mUsers.get(position);
            holder.nTV.setText(mt.getName());
            holder.iV.setImageBitmap(mt.getImage());

            if(SiteSelected != null){
                if(!SiteSelectresult.contains(mt.getID())){
                    holder.itemView.setBackgroundColor(Color.GRAY);
                }else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            if(UserSelected!= null) {
                if (UserSelected.getID().equals(mt.getID())) {
                    holder.itemView.setBackgroundColor(Color.YELLOW);
                }
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView iV;
            private TextView nTV;

            public ViewHolder(View itemView) {
                super(itemView);
                iV = (ImageView) itemView.findViewById(R.id.only_image_view);
                nTV = (TextView) itemView.findViewById(R.id.only_text_view);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(final View userview) {
                if(SiteSelectPosition!= -1){
                    if (SiteSelectresult.contains(mUsers.get(getAdapterPosition()).getID())){
                        UserSelectPosition = getAdapterPosition();
                        UserSelected = mUsers.get(getAdapterPosition());
                        selectAUser();
                    }else{
                        new AlertDialog.Builder(getContext()).setTitle(R.string.PlaySiteNotMatch)
                                .setPositiveButton(R.string.SelectThisPlayerAnyway, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SiteSelectPosition = -1;
                                        SiteSelected = null;
                                        UserSelectPosition = getAdapterPosition();
                                        UserSelected = mUsers.get(getAdapterPosition());
                                        selectAUser();
                                    }
                                }).setNegativeButton(android.R.string.cancel, null).show();
                    }
                }else {
                    UserSelectPosition = getAdapterPosition();
                    UserSelected = mUsers.get(getAdapterPosition());
                    selectAUser();
                }
            }
        }
    }


    private void selectASite() {
        SiteSelectresult  = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("sites").child(SiteSelected.getID()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> mm = (HashMap<String, String>) dataSnapshot.getValue();
                for(String m :mm.keySet()){
                    SiteSelectresult.add(m);
                }
                HUAP.notifyDataSetChanged();
                HSAP.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void selectAUser() {
        UserSelectresult  = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("users").child(UserSelected.getID()).child("mTrack").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> mm = (HashMap<String, String>) dataSnapshot.getValue();
                for(String m :mm.keySet()){
                    UserSelectresult.add(m);
                }
                HUAP.notifyDataSetChanged();
                HSAP.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
