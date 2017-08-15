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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DetailTrainingFragment extends Fragment {
    private static final String ARG_CURPLAN = "currenttrainnningplannn";
    private TrainingPlan mTp;
    private Site mSt;
    private User mCoach;
    private FragmentsEventListener mListener;

    public DetailTrainingFragment() {}

    public static DetailTrainingFragment newInstance(TrainingPlan tp) {
        DetailTrainingFragment fragment = new DetailTrainingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CURPLAN, tp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTp = getArguments().getParcelable(ARG_CURPLAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_detail, container, false);
        TextView mTV = (TextView) view.findViewById(R.id.name_text_view);
        final ImageView iV = (ImageView) view.findViewById(R.id.com_image_view);
        final TextView lTV = (TextView) view.findViewById(R.id.loc_text_view);
        TextView dTV = (TextView) view.findViewById(R.id.date_text_view);
        TextView sTV = (TextView) view.findViewById(R.id.des_text_view);
        Button mbt = (Button) view.findViewById(R.id.mark_button);
        Button rbt = (Button) view.findViewById(R.id.reg_button);
        Button cbt = (Button) view.findViewById(R.id.check_button);

        mTV.setText(mTp.getName());
        if(mTp.getSite()!= null){
            FirebaseDatabase.getInstance().getReference().child("sites").child(mTp.getSite()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mSt = dataSnapshot.getValue(Site.class);
                    mSt.setID(dataSnapshot.getKey());
                    lTV.setText(mSt.getName());
                    lTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onSiteSelect(mSt);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(mTp.getCoach()!= null){
            FirebaseDatabase.getInstance().getReference().child("users").child(mTp.getCoach()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCoach = dataSnapshot.getValue(User.class);
                    mCoach.setID(dataSnapshot.getKey());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        dTV.setText(mTp.getStartdate()+" - "+mTp.getEnddate());
        sTV.setText(mTp.getDes());

        StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("trans").child(String.valueOf(mTp.getID())+".png");
        final long ONE_MEGABYTE = 1024 * 1024;
        imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                iV.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("SSS",exception.toString());
            }
        });



        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.mark(mTp);
            }
        });

        rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.Register,
                        Toast.LENGTH_SHORT).show();
                mListener.GetTraining(mTp,mSt,mCoach);
            }
        });

        cbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserSelect(mCoach);
            }
        });


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
        actionBar.setTitle(mTp.getName());
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
