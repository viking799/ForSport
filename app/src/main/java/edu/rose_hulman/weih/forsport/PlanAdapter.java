package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<edu.rose_hulman.weih.forsport.PlanAdapter.ViewHolder>{
    private List<TrainingPlan> mPlans;
    private FragmentsEventListener mListener;
    private Context mContext;
    private String mtype;
    private User mCoach ;

    public PlanAdapter(FragmentsEventListener listener, Context context, String Sporttype, User coach) {
        mListener = listener;
        mContext = context;
        mPlans = Hardcodefortest.matchPlanlist();
        mtype = Sporttype;
        mCoach = coach;
    }

    public PlanAdapter(FragmentsEventListener listener, Context context, String Sporttype) {
        mListener = listener;
        mContext = context;
        mPlans = Hardcodefortest.matchPlanlist();
        mtype = Sporttype;
        mCoach = null;
    }

    @Override
    public edu.rose_hulman.weih.forsport.PlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tplan_row_view, parent, false);
        return new edu.rose_hulman.weih.forsport.PlanAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return mPlans.size();
    }

    @Override
    public void onBindViewHolder(edu.rose_hulman.weih.forsport.PlanAdapter.ViewHolder holder, int position) {
        TrainingPlan mt = mPlans.get(position);
        holder.PmTV.setText(mt.getName());
        holder.lTV.setText(mt.getLocation());
        holder.dTV.setText(mt.getStartdate()+"-"+mt.getEnddate());
        holder.tTV.setText(mt.getStarttime()+"-"+mt.getEndtime());
        //holder.iTV.setText(mt.getName());
        holder.CnTV.setText(mt.getCoach().getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView PmTV;
        private TextView lTV;
        private ImageView iV;
        private RatingBar rB;
        private TextView dTV;
        private TextView tTV;
        private TextView CnTV;

        public ViewHolder(View itemView) {
            super(itemView);

            PmTV = (TextView) itemView.findViewById(R.id.Pname_text_view);
            lTV = (TextView) itemView.findViewById(R.id.loc_text_view);
            dTV = (TextView) itemView.findViewById(R.id.date_text_view);
            tTV = (TextView) itemView.findViewById(R.id.time_text_view);
            rB = (RatingBar) itemView.findViewById(R.id.ratingBar);
            iV = (ImageView) itemView.findViewById(R.id.coa_image_view);
            CnTV = (TextView) itemView.findViewById(R.id.cname_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onPlanSelect(mPlans.get(getAdapterPosition()));
        }


    }
}





