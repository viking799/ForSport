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

/**
 * Created by Administrator on 2017/7/29.
 */

public class HeadimageUserAdapter extends RecyclerView.Adapter<edu.rose_hulman.weih.forsport.HeadimageUserAdapter.ViewHolder>{

    private List<User> mUsers;
    private FragmentsEventListener mListener;
    private Context mContext;
    private String mtype;

    public HeadimageUserAdapter(FragmentsEventListener listener, Context context, String Sporttype) {
        mListener = listener;
        mContext = context;
        mUsers = Hardcodefortest.matchuserlist();
        mtype = Sporttype;
    }

    @Override
    public HeadimageUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_view, parent, false);
            return new HeadimageUserAdapter.ViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    @Override
    public void onBindViewHolder(HeadimageUserAdapter.ViewHolder holder, int position) {
        User mt = mUsers.get(position);
       //
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iV;

        public ViewHolder(View itemView) {
            super(itemView);
            iV = (ImageView) itemView.findViewById(R.id.only_image_view);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onListUserSelect(mUsers.get(getAdapterPosition()));
        }


    }
}





