package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ScheduleFragment extends Fragment {
    private static final String ARG_SCHEDULE = "scheduleageamegas";
    private String curtype;
    private HeadimageSiteAdapter HSAP;
    private HeadimageUserAdapter HUAP;
    private FragmentsEventListener mListener;

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(String currenttype) {
        ScheduleFragment fragment = new ScheduleFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        }

        @Override
        public HeadimageSiteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_view, parent, false);
            return new HeadimageSiteAdapter.ViewHolder(view);

        }

        @Override
        public int getItemCount() {
            return mSite.size();
        }


        @Override
        public void onBindViewHolder(HeadimageSiteAdapter.ViewHolder holder, int position) {
            Site mt = mSite.get(position);
            //
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView iV;

            public ViewHolder(View itemView) {
                super(itemView);
                iV = (ImageView) itemView.findViewById(R.id.only_image_view);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                mListener.onListSiteSelect(mSite.get(getAdapterPosition()));
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
            mtype = Sporttype;
        }

        @Override
        public HeadimageUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_view, parent, false);
            return new HeadimageUserAdapter.ViewHolder(view);

        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }


        @Override
        public void onBindViewHolder(HeadimageUserAdapter.ViewHolder holder, int position) {
            User mt = mUsers.get(position);
            //
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView iV;

            public ViewHolder(View itemView) {
                super(itemView);
                iV = (ImageView) itemView.findViewById(R.id.only_image_view);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                mListener.onListUserSelect(mUsers.get(getAdapterPosition()));
            }


        }
    }

}
