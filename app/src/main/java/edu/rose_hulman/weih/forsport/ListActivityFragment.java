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
import android.widget.TextView;

import java.util.List;

public class ListActivityFragment extends Fragment {

    private ActivityAdapter mAP;
    private String mcurType;
    private FragmentsEventListener mListener;
    private static final String ARG_TYPE = "typeypersafa";
    public ListActivityFragment() {}

    public static ListActivityFragment newInstance(String st) {
        ListActivityFragment fragment = new ListActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, st);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        actionBar.setTitle(mcurType);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>{
        private List<String> mActivity;
        private FragmentsEventListener mListener;
        private Context mContext;

        public ActivityAdapter(FragmentsEventListener listener, Context context) {
            mListener = listener;
            mContext = context;
            mActivity = Activities.initializeList(mContext);
        }
        public ActivityAdapter(FragmentsEventListener listener , Context context, String mcurType) {
            mListener = listener;
            mContext = context;
            mActivity = Activities.initializeList(mContext,mcurType);
        }
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTV;
            public ViewHolder(View itemView) {
                super(itemView);
                mTV = (TextView) itemView.findViewById(R.id.type_text_view);
                itemView.setOnClickListener(this);

            }
            @Override
            public void onClick(View view) {
                mListener.onActSelected(mActivity.get(getAdapterPosition()));
            }
        }
        @Override
        public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_row_view, parent, false);
            return new ActivityAdapter.ViewHolder(view);
        }
        @Override
        public int getItemCount() {
            return mActivity.size();
        }

        @Override
        public void onBindViewHolder(ActivityAdapter.ViewHolder holder, int position) {
            String mt = mActivity.get(position);
            holder.mTV.setText(mt);
        }
    }

}
