package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListMarksFragment extends Fragment {
    private static final String ARG_USER = "currentUserscsc";
    private User mUser;
    private mAdapter mApt;
    private FragmentsEventListener mListener;

    public ListMarksFragment() {
    }

    public static ListMarksFragment newInstance(User user) {
        ListMarksFragment fragment = new ListMarksFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
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
        View view = inflater.inflate(R.layout.fragment_marks, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.type_list_recycler_viewM);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mApt);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.my_marks);
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

        private List<ForSportData> mData;
        private FragmentsEventListener mListener;
        private DatabaseReference mRef;
        private Context mContext;

        public mAdapter(FragmentsEventListener listener, Context context, User user) {
            mListener = listener;
            mContext = context;
            mData = new ArrayList<>();
            mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getID()).child("mMark");
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.e("SSS1", String.valueOf(mData.size()));
                    if(dataSnapshot.getKey().contains("S")){
                        FirebaseDatabase.getInstance().getReference().child("sites").child(dataSnapshot.getKey().substring(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Site mt = dataSnapshot.getValue(Site.class);
                                mt.setID(dataSnapshot.getKey());
                                mData.add(mt);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else if(dataSnapshot.getKey().contains("U")){
                        FirebaseDatabase.getInstance().getReference().child("users").child(dataSnapshot.getKey().substring(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User mt = dataSnapshot.getValue(User.class);
                                mt.setID(dataSnapshot.getKey());
                                mData.add(mt);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else if(dataSnapshot.getKey().contains("T")){
                        FirebaseDatabase.getInstance().getReference().child("trans").child(dataSnapshot.getKey().substring(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                TrainingPlan mt = dataSnapshot.getValue(TrainingPlan.class);
                                mt.setID(dataSnapshot.getKey());
                                mData.add(mt);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else if(dataSnapshot.getKey().contains("C")){
                        FirebaseDatabase.getInstance().getReference().child("comps").child(dataSnapshot.getKey().substring(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Competition mt = dataSnapshot.getValue(Competition.class);
                                mt.setID(dataSnapshot.getKey());
                                mData.add(mt);
                                notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

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
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private TextView mTV;
            public ViewHolder(View itemView) {
                super(itemView);
                mTV = (TextView) itemView.findViewById(R.id.type_text_view);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);

            }
            @Override
            public void onClick(View view) {
                mListener.onDataSelect(mData.get(getAdapterPosition()));
            }
            @Override
            public boolean onLongClick(View v) {
                ForSportData removed = mData.remove(getAdapterPosition());
                if(removed.getClass() == User.class){
                    mRef.child("U"+removed.getID()).removeValue();
                }else if(removed.getClass() == Site.class){
                    mRef.child("S"+removed.getID()).removeValue();
                }
                notifyDataSetChanged();

                return true;
            }
        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_row_view, parent, false);
            return new mAdapter.ViewHolder(view);
        }
        @Override
        public int getItemCount() {
            return mData.size();
        }
        @Override
        public void onBindViewHolder(mAdapter.ViewHolder holder, int position) {
            ForSportData mt = mData.get(position);
            holder.mTV.setText(mt.getName());
        }
    }
}
