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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ListCompetetionFragment extends Fragment {
    private static final String ARG_COMPETITION = "compcompcomp";
    private String curtype;
    private CompetitionAdapter mCAP;
    private FragmentsEventListener mListener;
    public ListCompetetionFragment() {}
    public static ListCompetetionFragment newInstance(String currenttype, String param2) {
        ListCompetetionFragment fragment = new ListCompetetionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COMPETITION,currenttype);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curtype = getArguments().getString(ARG_COMPETITION);
        }
        mCAP = new CompetitionAdapter(mListener,getContext(),curtype);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competetion, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.type_list_recycler_viewC);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mCAP);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.JoinMatch);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
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

    public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.ViewHolder>{
        private List<Competition> mCom;
        private FragmentsEventListener mListener;
        private Context mContext;
        private String type;

        public CompetitionAdapter(FragmentsEventListener listener, Context context,String type) {
            mListener = listener;
            mContext = context;
            mCom = Hardcodefortest.matchlist();
            //Activities.initializeList(mContext);
        }

        @Override
        public CompetitionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_row_view, parent, false);
            return new CompetitionAdapter.ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mCom.size();
        }

        @Override
        public void onBindViewHolder(CompetitionAdapter.ViewHolder holder, int position) {
            Competition mt = mCom.get(position);
            holder.mTV.setText(mt.getName());
            holder.lTV.setText(mt.getLocation());
            //holder.iTV.setText(mt.getName());
            holder.pTV.setText(mt.getPhonenum());
            holder.dTV.setText(mt.getStartdate()+"-"+mt.getEnddate());
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTV;
            private TextView lTV;
            private ImageView iV;
            private TextView pTV;
            private TextView dTV;

            public ViewHolder(View itemView) {
                super(itemView);

                mTV = (TextView) itemView.findViewById(R.id.name_text_view);
                lTV = (TextView) itemView.findViewById(R.id.loc_text_view);
                iV = (ImageView) itemView.findViewById(R.id.com_image_view);
                pTV = (TextView) itemView.findViewById(R.id.phone_text_view);
                dTV = (TextView) itemView.findViewById(R.id.date_text_view);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                mListener.onCompselect(mCom.get(getAdapterPosition()));
            }


        }
    }
}
