package com.bulgogi.flag.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bulgogi.flag.R;
import com.bulgogi.flag.activity.FlagActivity;
import com.bulgogi.flag.adapter.FlagArrayAdapter;
import com.bulgogi.flag.config.Constants;
import com.bulgogi.flag.model.Flag;
import com.bulgogi.flag.util.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nbpcorp.mobilead.sdk.MobileAdListener;
import com.nbpcorp.mobilead.sdk.MobileAdView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlagFragment extends Fragment {
    private StickyGridHeadersGridView gvFlags;
    private MobileAdView adPost;

    public static FlagFragment newInstance(String title, int nameArrayId, int thumbArrayId, int flagArrayId, boolean centerInside) {
        FlagFragment fragment = new FlagFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_TITLE, title);
        args.putInt(Constants.ARG_ARRAY_ID_NAME, nameArrayId);
        args.putInt(Constants.ARG_ARRAY_ID_THUMB, thumbArrayId);
        args.putInt(Constants.ARG_ARRAY_ID_FLAG, flagArrayId);
        args.putBoolean(Constants.ARG_THUMB_CENTER_INSIDE, centerInside);
        fragment.setArguments(args);
        return fragment;
    }

    public FlagFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_main, container, false);
        gvFlags = (StickyGridHeadersGridView) rootView.findViewById(R.id.gv_flags);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getArguments().getString(Constants.ARG_TITLE));
        int nameArrayId = getArguments().getInt(Constants.ARG_ARRAY_ID_NAME);
        int thumbArrayId = getArguments().getInt(Constants.ARG_ARRAY_ID_THUMB);
        int flagArrayId = getArguments().getInt(Constants.ARG_ARRAY_ID_FLAG);
        final List<String> countries = Arrays.asList(getResources().getStringArray(nameArrayId));
        final List<String> thumbUris = Arrays.asList(getResources().getStringArray(thumbArrayId));
        final List<String> flagUris = Arrays.asList(getResources().getStringArray(flagArrayId));
        if (countries.size() != thumbUris.size() || countries.size() != flagUris.size()) {
            throw new IllegalStateException();
        }

        ArrayList<Flag> flags = new ArrayList<Flag>();
        for (int i = 0; i < countries.size(); i++) {
            flags.add(Flag.valueOf(countries.get(i), thumbUris.get(i), flagUris.get(i)));
        }

        boolean isCenterInside = getArguments().getBoolean(Constants.ARG_THUMB_CENTER_INSIDE);
        FlagArrayAdapter<Flag> adapter = new FlagArrayAdapter<Flag>(getActivity().getApplicationContext(), flags, R.layout.ll_section_header, R.layout.tv_grid_item, isCenterInside);
        gvFlags.setAdapter(adapter);
        gvFlags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FlagActivity.class);
                intent.putExtra(Constants.EXTRA_FLAG_URI, flagUris.get(i));
                startActivity(intent);
            }
        });

        if (Constants.Config.AD_BANNER) {
            if (Utils.isKorea(getActivity())) {
                adPost = (MobileAdView) rootView.findViewById(R.id.adpost);
                adPost.setListener(new MobileAdListener() {
                    @Override
                    public void onReceive(int error) {
                        if (error == -1 || error == 3 || error == 4 || error == 5 || error == 101
                                || error == 102 || error == 103 || error == 105 || error == 106) {
                            adPost.setVisibility(View.GONE);
                        } else {
                            adPost.setVisibility(View.VISIBLE);
                        }
                    }
                });
                adPost.start();
            } else {
                AdView admob = (AdView) rootView.findViewById(R.id.admob);
                admob.setVisibility(View.VISIBLE);
                AdRequest adRequest = new AdRequest.Builder().build();
                admob.loadAd(adRequest);
            }
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (adPost != null) {
            adPost.destroy();
            adPost = null;
        }
    }
}
