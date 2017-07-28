package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/29.
 */




public class HeadimageSiteAdapter extends RecyclerView.Adapter<edu.rose_hulman.weih.forsport.HeadimageSiteAdapter.ViewHolder>{

    private List<Site> mSite;
    private FragmentsEventListener mListener;
    private Context mContext;
    private String mtype;

    public HeadimageSiteAdapter(FragmentsEventListener listener, Context context, String Sporttype) {
        mListener = listener;
        mContext = context;
        mSite = Hardcodefortest.sitelist();
        mtype = Sporttype;
    }

    @Override
    public HeadimageSiteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_view, parent, false);
        return new HeadimageSiteAdapter.ViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return mSite.size();
    }


    @Override
    public void onBindViewHolder(HeadimageSiteAdapter.ViewHolder holder, int position) {
        Site mt = mSite.get(position);
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
            mListener.onListSiteSelect(mSite.get(getAdapterPosition()));
        }


    }
}





