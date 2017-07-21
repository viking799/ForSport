package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import edu.rose_hulman.weih.forsport.TypeSelect;
/**
 * Created by Administrator on 2017/7/11.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

    private List<String> mType;
    private TypeSelect.OnTypeSelectedListener mListener;
    private Context mContext;

    public TypeAdapter(TypeSelect.OnTypeSelectedListener listener, Context context) {
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
