package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DetailGameFragment extends Fragment {

    private static final String ARG_GAME = "gamessdsafa";
    private Game mGame;
    private FragmentsEventListener mListener;

    public DetailGameFragment() {}

    public static DetailGameFragment newInstance(Game game) {
        DetailGameFragment fragment = new DetailGameFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GAME,game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGame = getArguments().getParcelable(ARG_GAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_game, container, false);
        TextView mTV = (TextView) view.findViewById(R.id.name_text_view);
        final TextView sTV = (TextView) view.findViewById(R.id.Site_text_view);
        final TextView eTV = (TextView) view.findViewById(R.id.Event_text_view);
        TextView IfTV = (TextView) view.findViewById(R.id.Initfront_text_view);
        TextView PfTV = (TextView) view.findViewById(R.id.Partfront_text_view);
        RecyclerView Icv = (RecyclerView) view.findViewById(R.id.init_list_recycler_view);
        RecyclerView Pcv = (RecyclerView) view.findViewById(R.id.part_list_recycler_view);
        TextView tTV = (TextView) view.findViewById(R.id.date);

        Icv.setHasFixedSize(true);
        Icv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        Pcv.setHasFixedSize(true);
        Pcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mTV.setText(mGame.getName());
        tTV.setText("Date : " + mGame.getStartdate()+ " - "+ mGame.getEnddate());

        if(mGame.getID().length() == 19){
            IfTV.setText("Coach : ");
            PfTV.setText("Student : ");
            FirebaseDatabase.getInstance().getReference().child("trans").child(mGame.getCurrentEvent()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final TrainingPlan tp = dataSnapshot.getValue(TrainingPlan.class);
                    tp.setID(dataSnapshot.getKey());
                    eTV.setText("Trainning : " + tp.getName());
                    eTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onPlanSelect(tp);
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }else if(mGame.getID().length() == 9 ){
            IfTV.setText("Holder : ");
            PfTV.setText("Competitor : ");
            FirebaseDatabase.getInstance().getReference().child("comps").child(mGame.getCurrentEvent()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Competition tp = dataSnapshot.getValue(Competition.class);
                    tp.setID(dataSnapshot.getKey());
                    eTV.setText("Compitition : " + tp.getName());
                    eTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onCompselect(tp);
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }else {
            IfTV.setText("");
            eTV.setText("");
        }

        FirebaseDatabase.getInstance().getReference().child("sites").child(mGame.getSite()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Site st = dataSnapshot.getValue(Site.class);
                st.setID(dataSnapshot.getKey());
                sTV.setText("Site : " + st.getName());
                sTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSiteSelect(st);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Icv.setAdapter(new mAdapter(mListener,getContext(),mGame.getHolders(),"mholders"));
        Pcv.setAdapter(new mAdapter(mListener,getContext(),mGame.getPlayers(),"mplayers"));


        return view;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mGame.getName());
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>{

        private List<User> mData;
        private FragmentsEventListener mListener;
        private String mPath;
        private Context mContext;
        private DatabaseReference mRef;

        public mAdapter(FragmentsEventListener listener, Context context, ArrayList<User> users,String path) {
            mListener = listener;
            mContext = context;
            mData = users;
            mData.clear();
            mPath = path;
            FirebaseDatabase.getInstance().getReference().child("events").child(mGame.getID()).child(mPath).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FirebaseDatabase.getInstance().getReference().child("users").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final User us = dataSnapshot.getValue(User.class);
                            us.setID(dataSnapshot.getKey());
                            mData.add(us);
                            StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("users").child(String.valueOf(us.getID()) + ".png");
                            final long ONE_MEGABYTE = 1024 * 1024;
                            imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    us.setImage(bitmap);
                                    notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e("SSS", exception.toString() + "?");
                                }
                            });
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

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

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
            private TextView mTV;
            private ImageView iV;
            public ViewHolder(View itemView) {
                super(itemView);
                mTV = (TextView) itemView.findViewById(R.id.only_text_view);
                iV = (ImageView) itemView.findViewById(R.id.only_image_view);
                itemView.setOnLongClickListener(this);
            }
            @Override
            public boolean onLongClick(View view) {
                mListener.onUserSelect(mData.get(getAdapterPosition()));
                return true;
            }

        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_name_view, parent, false);
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
            holder.iV.setImageBitmap(mt.getImage());

        }
    }

}
