package com.bulgogi.flag.application;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class FlagApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                //.memoryCacheSizePercentage(20)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
