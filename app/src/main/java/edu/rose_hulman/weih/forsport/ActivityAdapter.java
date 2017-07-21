package edu.rose_hulman.weih.forsport;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ActivityAdapter extends RecyclerView.Adapter<edu.rose_hulman.weih.forsport.ActivityAdapter.ViewHolder>{


    private List<String> mActivity;
    private ActivityListFragment.OnActivitySelectedListener mListener;
    private Context mContext;

    public ActivityAdapter(ActivityListFragment.OnActivitySelectedListener listener, Context context) {
            mListener = listener;
            mContext = context;
            mActivity = Activities.initializeList(mContext);
    }

    public ActivityAdapter(ActivityListFragment.OnActivitySelectedListener listener , Context context, String mcurType) {
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

