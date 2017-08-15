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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DetailOtheruserFragment extends Fragment {
    private static final String ARG_USER = "currentLookingUser";
    private User mUser;
    private FragmentsEventListener mListener;

    public DetailOtheruserFragment() {}

    public static DetailOtheruserFragment newInstance(User user) {
        DetailOtheruserFragment fragment = new DetailOtheruserFragment();
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mUser.getName());
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otheruserdetail, container, false);
        TextView mTV = (TextView) view.findViewById(R.id.name_text_view);
        final ImageView iV = (ImageView) view.findViewById(R.id.com_image_view);
        TextView lTV = (TextView) view.findViewById(R.id.loc_text_view);
        TextView pTV = (TextView) view.findViewById(R.id.phone_text_view);
        TextView eTV = (TextView) view.findViewById(R.id.email_text_view);
        TextView dTV = (TextView) view.findViewById(R.id.des_text_view);
        //TextView hTV = (TextView) view.findViewById(R.id.honour_text_view);
        Button mbt = (Button) view.findViewById(R.id.mark_button);
        Button sbt = (Button) view.findViewById(R.id.seg_button);
        Button gbt= (Button) view.findViewById(R.id.get_button);

        mTV.setText(mUser.getName());
        lTV.setText(mUser.getLocation());
        pTV.setText(mUser.getPhonenum());
        eTV.setText(mUser.getEmail());
        dTV.setText(mUser.getDes());
        if(mUser.getImage()!= null){
            iV.setImageBitmap(mUser.getImage());
        }else {
            StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("users").child(String.valueOf(mUser.getID()) + ".png");
            final long ONE_MEGABYTE = 1024 * 1024;
            imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    iV.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("SSS", exception.toString()+ "?"+ mUser.getID());
                }
            });
        }
        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.mark(mUser);
            }
        });

        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SchedulingAGame(mUser);
            }
        });

        gbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SelectTrainingFromUser(mUser);

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
