package edu.rose_hulman.weih.forsport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DetailSiteFragment extends Fragment {
    private static final String ARG_SITE = "CUrrentSite";
    private Site mSite;
    private FragmentsEventListener mListener;
    public DetailSiteFragment() {}

    public static DetailSiteFragment newInstance(Site site) {
        DetailSiteFragment fragment = new DetailSiteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SITE,site);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSite = getArguments().getParcelable(ARG_SITE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_site, container, false);
        TextView mTV = (TextView) view.findViewById(R.id.name_text_view);
        ImageView iV = (ImageView) view.findViewById(R.id.site_image_view);
        TextView lTV = (TextView) view.findViewById(R.id.loc_text_view);
        Button mbt = (Button) view.findViewById(R.id.mark_button);

        mTV.setText(mSite.getName());
        lTV.setText(mSite.getLocation());

        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),R.string.Mark,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mSite.getName());
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
