package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailGameFragment extends Fragment {

    private static final String ARG_GAME = "gamessdsafa";
    private Game mGame;
    private FragmentsEventListener mListener;

    public DetailGameFragment() {}

    public static DetailGameFragment newInstance(Game game) {
        DetailGameFragment fragment = new DetailGameFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GAME,game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGame = getArguments().getParcelable(ARG_GAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_game, container, false);
        TextView mTV = (TextView) view.findViewById(R.id.name_text_view);
        TextView EfTV = (TextView) view.findViewById(R.id.Eventfront_text_view);
        TextView eTV = (TextView) view.findViewById(R.id.Event_text_view);
        TextView IfTV = (TextView) view.findViewById(R.id.Initfront_text_view);
        RecyclerView Icv = (RecyclerView) view.findViewById(R.id.init_list_recycler_view);
        RecyclerView Pcv = (RecyclerView) view.findViewById(R.id.part_list_recycler_view);

        Icv.setHasFixedSize(true);
        Icv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        Pcv.setHasFixedSize(true);
        Pcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mTV.setText(mGame.getName());
        if(mGame.getCurrentEvent() == null){
            EfTV.setVisibility(View.INVISIBLE);
            eTV.setVisibility(View.INVISIBLE);
        }else {
            eTV.setText(mGame.getCurrentEvent().getName());
        }
        if(mGame.getHolders() == null){
            IfTV.setVisibility(View.INVISIBLE);
            Icv.setVisibility(View.INVISIBLE);
        }else {
            Icv.setAdapter(new mAdapter(mListener,getContext(),mGame.getHolders()));
        }
        Pcv.setAdapter(new mAdapter(mListener,getContext(),mGame.getPlayers()));


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
        actionBar.setTitle(mGame.getName());
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>{

        private List<User> mData;
        private FragmentsEventListener mListener;
        private Context mContext;

        public mAdapter(FragmentsEventListener listener, Context context, ArrayList<User> users) {
            mListener = listener;
            mContext = context;
            mData = users;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
            private TextView mTV;
            private ImageView iV;
            public ViewHolder(View itemView) {
                super(itemView);
                mTV = (TextView) itemView.findViewById(R.id.only_text_view);
                iV = (ImageView) itemView.findViewById(R.id.only_image_view);
                itemView.setOnLongClickListener(this);
            }
            @Override
            public boolean onLongClick(View view) {
                mListener.onUserSelect(mData.get(getAdapterPosition()));
                return true;
            }

        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_name_view, parent, false);
            return new mAdapter.ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onBindViewHolder(mAdapter.ViewHolder holder, int position) {
            ForSportData mt = mData.get(position);
            holder.mTV.setText(mt.getName());
        }
    }

}
