package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Administrator on 2017/7/22.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private List<User> mUsers;
    private FragmentsEventListener mListener;
    private Context mContext;
    private String mtype;
    private int musertype;

    public UserAdapter(FragmentsEventListener listener, Context context, String Sporttype, int usertype) {
        mListener = listener;
        mContext = context;
        mUsers = Hardcodefortest.matchuserlist();
        mtype = Sporttype;
        musertype = usertype;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(musertype == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_row_view, parent, false);
            return new UserAdapter.ViewHolder(view, musertype);
        }
        return  null;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        User mt = mUsers.get(position);
        holder.mTV.setText(mt.getName());
        holder.lTV.setText(mt.getLocation());
        //holder.iTV.setText(mt.getName());
        holder.pTV.setText(mt.getPhonenum());
        holder.aTV.setText(String.valueOf(mt.getAge()));
        if(mt.isGender()){
            holder.gTV.setText(R.string.Male);
        }else{
            holder.gTV.setText(R.string.Female);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTV;
        private TextView lTV;
        private ImageView iV;
        private TextView pTV;
        private TextView aTV;
        private TextView gTV;
        private RatingBar rB;
      //  private TextView dTV;

        public ViewHolder(View itemView, int musertype) {
            super(itemView);
            if(musertype == 1) {
                mTV = (TextView) itemView.findViewById(R.id.name_text_view);
                lTV = (TextView) itemView.findViewById(R.id.loc_text_view);
                iV = (ImageView) itemView.findViewById(R.id.coa_image_view);
                pTV = (TextView) itemView.findViewById(R.id.phone_text_view);
                aTV = (TextView) itemView.findViewById(R.id.age_text_view);
                gTV = (TextView) itemView.findViewById(R.id.gen_text_view);
                rB = (RatingBar) itemView.findViewById(R.id.ratingBar);
            }else if(musertype == 0){

            }
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onUserSelect(mUsers.get(getAdapterPosition()));
        }


    }
}





