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

public class DetailCompetetionFragment extends Fragment {

    private static final String ARG_CURCOMPETION = "currentCompetion";
    private Competition mCom;
    private Site mSite;
    private User mUser;
    private FragmentsEventListener mListener;

    public DetailCompetetionFragment() {

    }

    public static DetailCompetetionFragment newInstance(Competition competition) {
        DetailCompetetionFragment fragment = new DetailCompetetionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CURCOMPETION,competition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCom = getArguments().getParcelable(ARG_CURCOMPETION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competetion_detail, container, false);
        TextView mTV = (TextView) view.findViewById(R.id.name_text_view);
        final ImageView iV = (ImageView) view.findViewById(R.id.com_image_view);
        final TextView lTV = (TextView) view.findViewById(R.id.loc_text_view);
        final TextView pTV = (TextView) view.findViewById(R.id.phone_text_view);
        TextView dTV = (TextView) view.findViewById(R.id.date_text_view);
        TextView sTV = (TextView) view.findViewById(R.id.des_text_view);
        final TextView hTV = (TextView) view.findViewById(R.id.holder_text_view);
        Button mbt = (Button) view.findViewById(R.id.mark_button);
        Button rbt = (Button) view.findViewById(R.id.reg_button);



        mTV.setText(mCom.getName());

        FirebaseDatabase.getInstance().getReference().child("users").child(mCom.getHolder()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("SSSD",dataSnapshot.toString());
                mUser = dataSnapshot.getValue(User.class);
                mUser.setID(dataSnapshot.getKey());
                hTV.setText(getString(R.string.holderfront)+mUser.getName());
                pTV.setText(mUser.getPhonenum());
                hTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onUserSelect(mUser);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("sites").child(mCom.getSite()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("SSSD",dataSnapshot.toString());
                mSite = dataSnapshot.getValue(Site.class);
                mSite.setID(dataSnapshot.getKey());
                lTV.setText(getString(R.string.sitefront)+mSite.getName());
                lTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSiteSelect(mSite);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //lTV.setText(mCom.getLocation());




        dTV.setText(mCom.getStartdate()+" - "+mCom.getEnddate());
        sTV.setText(mCom.getDes());

        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.mark(mCom);
            }
        });

        rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.Register,
                        Toast.LENGTH_SHORT).show();
                mListener.JoinComp(mCom);
            }
        });


        StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("comps").child(String.valueOf(mCom.getID())+".png");
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
        actionBar.setTitle(mCom.getName());
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
