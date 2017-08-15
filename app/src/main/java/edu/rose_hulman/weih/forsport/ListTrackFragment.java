package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListTrackFragment extends Fragment {
    private static final String ARG_USER = "currentUsersdfasf";
    private User mUser;
    private mAdapter mApt;
    private FragmentsEventListener mListener;


    public ListTrackFragment() {}

    public static ListTrackFragment newInstance(User user) {
        ListTrackFragment fragment = new ListTrackFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_USER);
        }
        mApt = new mAdapter(mListener,getContext(),mUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_track, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.type_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mApt);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.my_tracks);
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

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>  {

        private List<Site> mSite;
        private FragmentsEventListener mListener;
        private Context mContext;
        private DatabaseReference mRef;
        private DatabaseReference mRefID;
        private List<String> IDs;

        public mAdapter(FragmentsEventListener listener, Context context, User user) {
            mListener = listener;
            mContext = context;
            mSite =  new ArrayList<>();
            IDs = new ArrayList<>();
            mRefID = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getID()).child("mTrack");
            mRefID.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    IDs.add(dataSnapshot.getKey());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    if(IDs.contains(dataSnapshot.getKey())){
                      IDs.remove(dataSnapshot.getKey());
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mRef = FirebaseDatabase.getInstance().getReference().child("sites");
            mRef.addChildEventListener(new SiteListener());
        }
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView mTV;
            private String mID;
            private ToggleButton mBT;
            public ViewHolder(View itemView) {
                super(itemView);
                mTV = (TextView) itemView.findViewById(R.id.type_text_view);
                mBT = (ToggleButton) itemView.findViewById(R.id.check_button);
                mBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            mRefID.child(mID).setValue("");
                            mRef.child(mID).child("users").child(mUser.getID()).setValue("");
                        }else {
                            mRefID.child(mID).removeValue();
                            mRef.child(mID).child("users").child(mUser.getID()).removeValue();
                        }
                    }
                });
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                mListener.onSiteSelect(mSite.get(getAdapterPosition()));
            }
        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_row_view, parent, false);
            return new mAdapter.ViewHolder(view);
        }
        @Override
        public int getItemCount() {
            return mSite.size();
        }
        @Override
        public void onBindViewHolder(mAdapter.ViewHolder holder, int position) {
            Site mt = mSite.get(position);
            holder.mTV.setText(mt.getName());
            holder.mID = mt.getID();
            if(IDs.contains(mt.getID())){
                holder.mBT.setChecked(true);
            }
        }

        private class SiteListener implements ChildEventListener {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Site site = dataSnapshot.getValue(Site.class);
                site.setID(dataSnapshot.getKey());
                mSite.add(site);
                notifyDataSetChanged();
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
        }
    }
}
