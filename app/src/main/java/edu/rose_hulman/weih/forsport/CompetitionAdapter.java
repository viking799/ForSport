package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Administrator on 2017/7/22.
 */

public class CompetitionAdapter extends RecyclerView.Adapter<edu.rose_hulman.weih.forsport.CompetitionAdapter.ViewHolder>{
    private List<Competition> mCom;
    private CompetetionFragment.OnCompetetionSelectListener mListener;
    private Context mContext;
    private String type;

    public CompetitionAdapter(CompetetionFragment.OnCompetetionSelectListener listener, Context context,String type) {
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





