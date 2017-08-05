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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class ListTrainingFragment extends Fragment {
    private static final String ARG_TRAINTYPE = "trainningtype";
    private String mtype;
    private FragmentsEventListener mListener;
    private UserAdapter mUAP;
    private PlanAdapter mPAP;

    public ListTrainingFragment() {}

    public static ListTrainingFragment newInstance(String type) {
        ListTrainingFragment fragment = new ListTrainingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRAINTYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mtype = getArguments().getString(ARG_TRAINTYPE);
        }
        mUAP = new UserAdapter(mListener,getContext(),mtype,1);
        mPAP = new PlanAdapter(mListener,getContext(),mtype);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training,container,false);
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.type_list_recycler_viewT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mUAP);
        ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        toggleButton.setChecked(true);
        toggleButton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if(isChecked){
                    recyclerView.setAdapter(mUAP);
                }else {
                    recyclerView.setAdapter(mPAP);
                }
            }
        }) ;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.gettrainning);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

}
