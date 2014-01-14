package com.bulgogi.flag.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;

import com.bulgogi.flag.util.FontUtils;
import com.google.analytics.tracking.android.EasyTracker;

public class TrackerActionBarActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
            FontUtils.setRobotoFont(this, decorView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
