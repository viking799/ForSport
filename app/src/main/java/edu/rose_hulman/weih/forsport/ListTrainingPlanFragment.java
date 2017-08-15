package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;

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


public class ListTrainingPlanFragment extends Fragment {
    private static final String ARG_TRAINTYPE = "trainningtype1";
    private static final String ARG_CURRENTCOACH = "currentcoach";
    private String mtype;
    private User mCoach;
    private FragmentsEventListener mListener;
    private PlanAdapter mPAP;

    public ListTrainingPlanFragment() {}

  public static ListTrainingPlanFragment newInstance(String type, User user) {
        ListTrainingPlanFragment fragment = new ListTrainingPlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRAINTYPE,type);
        args.putParcelable(ARG_CURRENTCOACH,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mtype = getArguments().getString(ARG_TRAINTYPE);
            mCoach = getArguments().getParcelable(ARG_CURRENTCOACH);
        }
        mPAP= new PlanAdapter(mListener,getContext(),mtype,mCoach);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.trainningplanlist);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_list,container,false);
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.type_list_recycler_viewTL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mPAP);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder>{
        private List<TrainingPlan> mPlans;
        private FragmentsEventListener mListener;
        private Context mContext;
        private String mtype;
        private User mCoach = null;
        private DatabaseReference mRef;

        public PlanAdapter(FragmentsEventListener listener, Context context, String Sporttype, User coach) {
            mListener = listener;
            mContext = context;
            mCoach = coach;
            mPlans = new ArrayList<>();
            mtype = Sporttype;
            mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCoach.getID()).child("teach");
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String cur = dataSnapshot.getKey();
                    FirebaseDatabase.getInstance().getReference().child("trans").child(cur).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TrainingPlan temp = dataSnapshot.getValue(TrainingPlan.class);
                            temp.setID(dataSnapshot.getKey());
                            mPlans.add(temp);
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


        @Override
        public PlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tplan_row_view, parent, false);
            return new PlanAdapter.ViewHolder(view);
        }


        @Override
        public int getItemCount() {
            return mPlans.size();
        }

        @Override
        public void onBindViewHolder(final PlanAdapter.ViewHolder holder, int position) {
            TrainingPlan mt = mPlans.get(position);
            holder.PmTV.setText(mt.getName());
            holder.dTV.setText(mt.getStartdate()+" - "+mt.getEnddate());
            if(holder.site == null){
                FirebaseDatabase.getInstance().getReference().child("sites").child(mt.getSite()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.site = dataSnapshot.getValue(Site.class).getName();
                        holder.lTV.setText(holder.site);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else {
                holder.lTV.setText(holder.site);
            }
            holder.CnTV.setText(mCoach.getName());
            if(mCoach.getImage() == null){
                StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("users").child(String.valueOf(mCoach.getID()) + ".png");
                final long ONE_MEGABYTE = 1024 * 1024;
                imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.iV.setImageBitmap(bitmap);
                        mCoach.setImage(bitmap);
                        notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("SSS", exception.toString()+ "?");
                    }
                });
            }else {
                holder.iV.setImageBitmap(mCoach.getImage());
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView PmTV;
            private TextView lTV;
            private ImageView iV;
            private TextView dTV;
            private TextView CnTV;
            private String site = null;
            //private User PlanCoach = null;

            public ViewHolder(View itemView) {
                super(itemView);

                PmTV = (TextView) itemView.findViewById(R.id.Pname_text_view);
                lTV = (TextView) itemView.findViewById(R.id.loc_text_view);
                dTV = (TextView) itemView.findViewById(R.id.date_text_view);
                iV = (ImageView) itemView.findViewById(R.id.coa_image_view);
                CnTV = (TextView) itemView.findViewById(R.id.cname_text_view);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                mListener.onPlanSelect(mPlans.get(getAdapterPosition()));
            }


        }
    }

}
