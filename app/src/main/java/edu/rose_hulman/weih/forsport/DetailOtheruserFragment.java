package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
        ImageView iV = (ImageView) view.findViewById(R.id.com_image_view);
        TextView lTV = (TextView) view.findViewById(R.id.loc_text_view);
        TextView pTV = (TextView) view.findViewById(R.id.phone_text_view);
        TextView aTV = (TextView) view.findViewById(R.id.age_text_view);
        TextView gTV = (TextView) view.findViewById(R.id.gen_text_view);
        TextView hTV = (TextView) view.findViewById(R.id.honour_text_view);
        Button mbt = (Button) view.findViewById(R.id.mark_button);
        Button sbt = (Button) view.findViewById(R.id.seg_button);
        Button gbt= (Button) view.findViewById(R.id.get_button);

        mTV.setText(mUser.getName());
        lTV.setText(mUser.getLocation());
        pTV.setText(mUser.getPhonenum());
        aTV.setText(String.valueOf(mUser.getAge()));
        if(mUser.isGender()){
            gTV.setText(R.string.Male);
        }else{
            gTV.setText(R.string.Female);
        }
        //hTV.setText(mCom.getHolder());

        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),R.string.Mark,
                        Toast.LENGTH_SHORT).show();
            }
        });

        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.Register,
                        Toast.LENGTH_SHORT).show();
            }
        });

        gbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), R.string.get_his_her_trainning,
//                        Toast.LENGTH_SHORT).show();
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
