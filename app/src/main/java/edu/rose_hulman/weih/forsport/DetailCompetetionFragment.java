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

public class DetailCompetetionFragment extends Fragment {

    private static final String ARG_CURCOMPETION = "currentCompetion";
    private Competition mCom;
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
        ImageView iV = (ImageView) view.findViewById(R.id.com_image_view);
        TextView lTV = (TextView) view.findViewById(R.id.loc_text_view);
        TextView pTV = (TextView) view.findViewById(R.id.phone_text_view);
        TextView dTV = (TextView) view.findViewById(R.id.date_text_view);
        TextView hTV = (TextView) view.findViewById(R.id.holder_text_view);
        Button mbt = (Button) view.findViewById(R.id.mark_button);
        Button rbt = (Button) view.findViewById(R.id.reg_button);

        mTV.setText(mCom.getName());
        lTV.setText(mCom.getLocation());
        pTV.setText(mCom.getPhonenum());
        dTV.setText(mCom.getStartdate()+"-"+mCom.getEnddate());
        hTV.setText(mCom.getHolder());

        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),R.string.Mark,
                        Toast.LENGTH_SHORT).show();
            }
        });

        rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.Register,
                        Toast.LENGTH_SHORT).show();
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
