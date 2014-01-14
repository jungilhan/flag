package com.bulgogi.flag.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class PlaceholderFragment extends Fragment {
    private StickyGridHeadersGridView gvFlags;
    private MobileAdView adPost;

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_main, container, false);

        gvFlags = (StickyGridHeadersGridView) rootView.findViewById(R.id.gv_flags);

        List<String> countries = Arrays.asList(getResources().getStringArray(R.array.countries));
        List<String> uris = Arrays.asList(getResources().getStringArray(R.array.thumb_uris));
        if (countries.size() != uris.size()) {
            throw new IllegalStateException();
        }

        ArrayList<Flag> flags = new ArrayList<Flag>();
        for (int i = 0; i < countries.size(); i++) {
            flags.add(Flag.valueOf(countries.get(i), uris.get(i)));
        }

        FlagArrayAdapter<Flag> adapter = new FlagArrayAdapter<Flag>(
                getActivity().getApplicationContext(), flags, R.layout.ll_section_header, R.layout.tv_grid_item);
        gvFlags.setAdapter(adapter);

        gvFlags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FlagActivity.class);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });

        if (!Constants.Config.DEBUG) {
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
