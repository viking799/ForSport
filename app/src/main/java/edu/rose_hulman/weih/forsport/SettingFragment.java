package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment {
    private static final String ARG_USERFORSETTING = "myusersetting";
    private static final int SELECTED_PICTURE = 101;
    private User mUser;
    private Uri uri = null;
    private Bitmap bitmap = null;
    private FragmentsEventListener mListener;
    private ImageView iv;
    private TextView nTV;
    private TextView pTV;
    private TextView eTV;
    public SettingFragment() {}

    public static SettingFragment newInstance(User user, String userIDinFB) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USERFORSETTING,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_USERFORSETTING);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        iv = (ImageView) view.findViewById(R.id.HeadimageView);
        iv.setImageBitmap(mUser.getImage());
        nTV = (TextView) view.findViewById(R.id.name_text_view);
        nTV.setText(mUser.getName());
        pTV = (TextView) view.findViewById(R.id.phone_text_view);
        pTV.setText(mUser.getPhonenum());
        eTV = (TextView) view.findViewById(R.id.email_text_view);
        eTV.setText(mUser.getEmail());
        ImageButton bt1 = (ImageButton) view.findViewById(R.id.HeadimageButton);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImage();
            }
        });
        ImageButton bt2 = (ImageButton) view.findViewById(R.id.NameButton);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit1();
            }
        });
        ImageButton bt3 = (ImageButton) view.findViewById(R.id.PhoneButton);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit2();
            }
        });
        ImageButton bt4 = (ImageButton) view.findViewById(R.id.EmailButton);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit3();
            }
        });
        Button cBt = (Button) view.findViewById(R.id.ComButton);
        cBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comfirm();
            }
        });
        return view;
    }

    private void Comfirm() {
        if(bitmap!=null){
            mUser.setImage(bitmap);
        }
        mUser.setName(nTV.getText().toString());
        mUser.setPhonenum(pTV.getText().toString());
        mUser.setEmail(eTV.getText().toString());
        Log.e("TTT",mUser.toString());
        mListener.UserDataChanged(mUser,(bitmap!=null));
    }

    private void onEdit1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit your name:");
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_change,null,false);
        builder.setView(view);
        final EditText myText = (EditText) view.findViewById(R.id.dialog_add_quote_text);
        myText.setHint(R.string.EnternameHint);
        myText.setText(mUser.getName());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nTV.setText(myText.getText());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
    private void onEdit2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit your Phone Number:");
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_change,null,false);
        builder.setView(view);
        final EditText myText = (EditText) view.findViewById(R.id.dialog_add_quote_text);
        myText.setInputType(InputType.TYPE_CLASS_NUMBER);
        myText.setHint(R.string.EnterPhonehint);
        myText.setText(mUser.getPhonenum());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pTV.setText(myText.getText());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
    private void onEdit3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit your Email Addresss:");
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_change,null,false);
        builder.setView(view);
        final EditText myText = (EditText) view.findViewById(R.id.dialog_add_quote_text);
        myText.setHint(R.string.EnterEmailHint);
        myText.setText(mUser.getEmail());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newemail = myText.getText().toString();
                if(newemail.contains("@")&&newemail.contains(".")){
                    eTV.setText(myText.getText());
                }else {
                    new AlertDialog.Builder(getContext()).setTitle("Not a Valid Email Address").setNegativeButton(android.R.string.cancel, null).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void ChangeImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,SELECTED_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case SELECTED_PICTURE:
                if(resultCode == RESULT_OK){
                    uri = data.getData(); // You get the uri for the Image
                    iv.setImageURI(uri);  // Using the image
                    bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap(); // Get the bitmap for the image
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.setting);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
