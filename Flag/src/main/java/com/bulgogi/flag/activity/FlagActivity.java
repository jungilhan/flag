package com.bulgogi.flag.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bulgogi.flag.R;
import com.bulgogi.flag.view.renderer.WavingFlagRenderer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Arrays;
import java.util.List;

public class FlagActivity extends TrackerActionBarActivity {
    private boolean DEBUG = false;
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_flag);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setFullScreen(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.accent1)));
        getSupportActionBar().setIcon(R.drawable.ic_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupViews();
    }

    private void setupViews() {
        int index = getIntent().getIntExtra("index", -1);
        if (DEBUG) {
            Toast.makeText(this, "Index: " + index, Toast.LENGTH_SHORT).show();
        }

        if (index == -1) {
            throw new IllegalArgumentException("Index should be not -1");
        }

        List<String> thumbUris = Arrays.asList(getResources().getStringArray(R.array.thumb_uris));
        List<String> flagUris = Arrays.asList(getResources().getStringArray(R.array.large_uris));
        if (thumbUris.size() != flagUris.size()) {
            throw new IllegalStateException();
        }

        final FrameLayout container = (FrameLayout) findViewById(R.id.container);
        final View loading = findViewById(R.id.loading);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFullScreen(!isFullScreen());
            }
        });

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();

        ImageLoader.getInstance().loadImage(flagUris.get(index), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                loading.setVisibility(View.GONE);

                Bitmap powerOfTwoBitmap = Bitmap.createScaledBitmap(bitmap, (int) 1024, (int) 512, true);
                glSurfaceView = new GLSurfaceView(getApplicationContext());
                glSurfaceView.setRenderer(new WavingFlagRenderer(powerOfTwoBitmap));
                glSurfaceView.getHolder().setKeepScreenOn(true);
                container.addView(glSurfaceView);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    private void setFullScreen(boolean enable) {
        if (enable == isFullScreen()) {
            return;
        }

        Window window = getWindow();
        if ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (enable) {
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }
    }

    private boolean isFullScreen() {
        return !getSupportActionBar().isShowing();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
