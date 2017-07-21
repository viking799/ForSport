package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 */
public class ActivityListFragment extends Fragment {

    private ActivityAdapter mAP;
    private String mcurType;
    private OnActivitySelectedListener mListener;
    private static final String ARG_TYPE = "typeypersafa";
    private GestureDetectorCompat mGestureDetector;

    public ActivityListFragment() {
        // Required empty public constructor
    }

    public static ActivityListFragment newInstance(String st) {
        ActivityListFragment fragment = new ActivityListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, st);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = new GestureDetectorCompat(getContext(),new MyGestureDetector());

        if (getArguments() != null) {
            mcurType = getArguments().getString(ARG_TYPE);
        }


        mAP = new ActivityAdapter(mListener,getContext(),mcurType);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.type_list_recycler_viewA);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAP);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySelectedListener) {
            mListener = (OnActivitySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Need to wait for the activity to be created to have an action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mcurType);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnActivitySelectedListener {
        void onActSelected(String Activity);
    }

    private class MyGestureDetector implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
