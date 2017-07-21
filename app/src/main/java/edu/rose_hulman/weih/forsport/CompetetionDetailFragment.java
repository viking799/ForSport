package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CompetetionDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompetetionDetailFragment extends Fragment {

    private static final String ARG_CURCOMPETION = "currentCompetion";
    private Competition mCom;
    private OnRegisterListener mListener;

    public CompetetionDetailFragment() {

    }

    public static CompetetionDetailFragment newInstance(Competition competition) {
        CompetetionDetailFragment fragment = new CompetetionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CURCOMPETION,competition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCom = getArguments().getParcelable(ARG_CURCOMPETION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competetion_detail, container, false);
        TextView mTV = (TextView) view.findViewById(R.id.name_text_view);
        ImageView iV = (ImageView) view.findViewById(R.id.com_image_view);
        TextView lTV = (TextView) view.findViewById(R.id.loc_text_view);
        TextView pTV = (TextView) view.findViewById(R.id.phone_text_view);
        TextView dTV = (TextView) view.findViewById(R.id.date_text_view);
        TextView hTV = (TextView) view.findViewById(R.id.holder_text_view);
        Button mbt = (Button) view.findViewById(R.id.mark_button);
        Button rbt = (Button) view.findViewById(R.id.reg_button);

        mTV.setText(mCom.getName());
        lTV.setText(mCom.getLocation());
        pTV.setText(mCom.getPhonenum());
        dTV.setText(mCom.getStartdate()+"-"+mCom.getEnddate());
        hTV.setText(mCom.getHolder());

        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),R.string.Mark,
                        Toast.LENGTH_SHORT).show();
            }
        });

        rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.Register,
                        Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterListener) {
            mListener = (OnRegisterListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRegisterListener {
        // TODO: Update argument type and name
        void Register(Competition competition);
    }
}
