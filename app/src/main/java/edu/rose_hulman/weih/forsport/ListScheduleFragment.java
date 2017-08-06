package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ListScheduleFragment extends Fragment {
    private static final String ARG_SCHEDULE = "scheduleageamegas";
    private String curtype;
    private HeadimageSiteAdapter HSAP;
    private HeadimageUserAdapter HUAP;
    private TextView DTV;
    private int UserSelectPosition = -1;
    private int SiteSelectPosition = -1;
    private FragmentsEventListener mListener;

    private ArrayList<Long> SiteSelectresult;
    private ArrayList<Long> UserSelectresult;


    public ListScheduleFragment() {
    }

    public static ListScheduleFragment newInstance(String currenttype) {
        ListScheduleFragment fragment = new ListScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SCHEDULE,currenttype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curtype = getArguments().getString(ARG_SCHEDULE);
        }
        HSAP = new HeadimageSiteAdapter(mListener,getContext(),curtype);
        HUAP = new HeadimageUserAdapter(mListener,getContext(),curtype);


        //TODO
        String sa = "A000001";
        String sb = "B000001";

        Log.e("TTTT",String.valueOf(Long.valueOf(sa.substring(1))==Long.valueOf(sb.substring(1))));

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
        DTV.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR));
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
                        DTV.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR));
                    }
                }).setNegativeButton(android.R.string.cancel,null);
                builder.show();
            }
        });
        Button bt  = (Button) view.findViewById(R.id.button);
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

    public class HeadimageSiteAdapter extends RecyclerView.Adapter<HeadimageSiteAdapter.ViewHolder>{

        private List<Site> mSite;
        private FragmentsEventListener mListener;
        private Context mContext;
        private String mtype;

        public HeadimageSiteAdapter(FragmentsEventListener listener, Context context, String Sporttype) {
            mListener = listener;
            mContext = context;
            mSite = Hardcodefortest.sitelist();
            mtype = Sporttype;


            mSite.get(0).setID("00001");
            mSite.get(1).setID("00002");
            mSite.get(2).setID("00003");
            mSite.get(3).setID("00004");
            mSite.get(4).setID("00005");
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
            if(UserSelectPosition != -1){
                if(!UserSelectresult.contains(Long.valueOf(mt.getID()))){
                    holder.itemView.setBackgroundColor(Color.GRAY);
                }else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            if(SiteSelectPosition == position){
                holder.itemView.setBackgroundColor(Color.YELLOW);
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
                    if (UserSelectresult.contains(Long.valueOf(mSite.get(getAdapterPosition()).getID()))){
                        SiteSelectPosition = getAdapterPosition();
                        selectASite();
                    }else{
                        new AlertDialog.Builder(getContext()).setTitle(R.string.PlaySiteNotMatch)
                                .setPositiveButton(R.string.SelectThisPlayerAnyway, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserSelectPosition = -1;
                                        SiteSelectPosition = getAdapterPosition();
                                        selectASite();
                                    }
                                }).setNegativeButton(android.R.string.cancel, null).show();
                    }
                }else {
                    SiteSelectPosition = getAdapterPosition();
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
            mUsers = Hardcodefortest.matchuserlist();
            mUsers.get(0).setID("00001");
            mUsers.get(1).setID("00002");
            mUsers.get(2).setID("00003");
            mUsers.get(3).setID("00004");
            mUsers.get(4).setID("00005");
            mtype = Sporttype;
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
            if(SiteSelectPosition != -1){
                if(!SiteSelectresult.contains(Long.valueOf(mt.getID()))){
                    holder.itemView.setBackgroundColor(Color.GRAY);
                }else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            if(UserSelectPosition == position){
                holder.itemView.setBackgroundColor(Color.YELLOW);
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
                    if (SiteSelectresult.contains(Long.valueOf(mUsers.get(getAdapterPosition()).getID()))){
                        UserSelectPosition = getAdapterPosition();
                        selectAUser();
                    }else{
                        new AlertDialog.Builder(getContext()).setTitle(R.string.PlaySiteNotMatch)
                                .setPositiveButton(R.string.SelectThisPlayerAnyway, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SiteSelectPosition = -1;
                                        UserSelectPosition = getAdapterPosition();
                                        selectAUser();
                                    }
                                }).setNegativeButton(android.R.string.cancel, null).show();
                    }
                }else {
                    UserSelectPosition = getAdapterPosition();
                    selectAUser();
                }
            }
        }
    }


    private void selectASite() {
        SiteSelectresult  = new ArrayList<>();
        switch (SiteSelectPosition){
            case 0:
                SiteSelectresult.add(Long.valueOf("00001"));
                SiteSelectresult.add(Long.valueOf("00002"));
                break;
            case 1:
                SiteSelectresult.add(Long.valueOf("00003"));
                SiteSelectresult.add(Long.valueOf("00002"));
                break;
            case 2:
                SiteSelectresult.add(Long.valueOf("00003"));
                SiteSelectresult.add(Long.valueOf("00004"));
                break;
            case 3:
                SiteSelectresult.add(Long.valueOf("00004"));
                SiteSelectresult.add(Long.valueOf("00005"));
                break;
            case 4:
                SiteSelectresult.add(Long.valueOf("00001"));
                SiteSelectresult.add(Long.valueOf("00005"));
                break;
            case -1:
                break;
        }
        HUAP.notifyDataSetChanged();
        HSAP.notifyDataSetChanged();


    }

    private void selectAUser() {
        UserSelectresult  = new ArrayList<>();
        switch (UserSelectPosition){
            case 0:
                UserSelectresult.add(Long.valueOf("00001"));
                UserSelectresult.add(Long.valueOf("00005"));
                break;
            case 1:
                UserSelectresult.add(Long.valueOf("00001"));
                UserSelectresult.add(Long.valueOf("00002"));
                break;
            case 2:
                UserSelectresult.add(Long.valueOf("00003"));
                UserSelectresult.add(Long.valueOf("00002"));
                break;
            case 3:
                UserSelectresult.add(Long.valueOf("00004"));
                UserSelectresult.add(Long.valueOf("00003"));
                break;
            case 4:
                UserSelectresult.add(Long.valueOf("00004"));
                UserSelectresult.add(Long.valueOf("00005"));
                break;
            case -1:
                break;
        }
        HSAP.notifyDataSetChanged();
        HUAP.notifyDataSetChanged();
    }

}
