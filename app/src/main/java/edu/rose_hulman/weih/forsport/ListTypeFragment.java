package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
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

import edu.rose_hulman.weih.forsport.R;

public class ListTypeFragment extends Fragment {


    private TypeAdapter mTP;
    private FragmentsEventListener mListener;

    public ListTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTP = new TypeAdapter(mListener,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type_select, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.type_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mTP);
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
        // Need to wait for the activity to be created to have an action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

        private List<String> mType;
        private FragmentsEventListener mListener;
        private Context mContext;

        public TypeAdapter(FragmentsEventListener listener, Context context) {
            mListener = listener;
            mContext = context;
            mType = Type.initializeList(mContext);
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
                mListener.onTypeSelected(mType.get(getAdapterPosition()));
            }



        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_row_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mType.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String mt = mType.get(position);
            holder.mTV.setText(mt);
        }

    }


}
