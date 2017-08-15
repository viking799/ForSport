package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
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
    private DatabaseReference mRef;

    public UserAdapter(FragmentsEventListener listener, Context context, String Sporttype, int usertype) {
        mListener = listener;
        mContext = context;
        mUsers = new ArrayList<>();
        mtype = Sporttype;
        musertype = usertype;
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User temp = dataSnapshot.getValue(User.class);
                temp.setID(dataSnapshot.getKey());
                mUsers.add(temp);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_row_view, parent, false);
        return new UserAdapter.ViewHolder(view, musertype);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    @Override
    public void onBindViewHolder(final UserAdapter.ViewHolder holder, int position) {
        final User mt = mUsers.get(position);
        holder.mTV.setText(mt.getName());
        holder.eTV.setText(mt.getEmail());
        holder.pTV.setText(mt.getPhonenum());
        if(mt.getGender().equals("M")){
            holder.gTV.setText(R.string.Male);
        }else{
            holder.gTV.setText(R.string.Female);
        }
        if(mt.getImage()!= null){
            holder.iV.setImageBitmap(mt.getImage());
        }else {
            StorageReference imagerf = FirebaseStorage.getInstance().getReference().child("users").child(String.valueOf(mt.getID()) + ".png");
            final long ONE_MEGABYTE = 1024 * 1024;
            imagerf.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.iV.setImageBitmap(bitmap);
                    mt.setImage(bitmap);
                    notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("SSS", exception.toString()+ "?"+ mt.getID());
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTV;
        private TextView eTV;
        private ImageView iV;
        private TextView pTV;
        private TextView gTV;

        public ViewHolder(View itemView, int musertype) {
            super(itemView);

            mTV = (TextView) itemView.findViewById(R.id.name_text_view);
            eTV = (TextView) itemView.findViewById(R.id.email_text_view);
            iV = (ImageView) itemView.findViewById(R.id.coa_image_view);
            pTV = (TextView) itemView.findViewById(R.id.phone_text_view);
            gTV = (TextView) itemView.findViewById(R.id.gen_text_view);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onUserSelect(mUsers.get(getAdapterPosition()));
        }


    }
}





