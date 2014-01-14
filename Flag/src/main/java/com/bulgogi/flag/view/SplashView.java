package com.bulgogi.flag.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bulgogi.flag.R;
import com.bulgogi.flag.util.SvgHelper;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import static com.bulgogi.flag.util.SvgHelper.SvgPath;

public class SplashView extends View {
    private static final String TAG = "SplashView";

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final SvgHelper svg = new SvgHelper(paint);

    private final Object svgLock = new Object();
    private List<SvgHelper.SvgPath> paths = new ArrayList<SvgHelper.SvgPath>(0);
    private Thread loader;

    private int svgRes;
    private float phase;
    private float fadeFactor;
    private int duration;

    private ObjectAnimator svgAnimator;

    public SplashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            ViewCompat.setLayerType(this, LAYER_TYPE_SOFTWARE, null);
        }

        paint.setStyle(Paint.Style.STROKE);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SplashView, defStyle, 0);
        try {
            if (a != null) {
                paint.setStrokeWidth(a.getFloat(R.styleable.SplashView_strokeWidth, 1.0f));
                paint.setColor(a.getColor(R.styleable.SplashView_strokeColor, 0xff000000));
                phase = a.getFloat(R.styleable.SplashView_phase, 0.0f);
                duration = a.getInt(R.styleable.SplashView_duration, 4000);
                fadeFactor = a.getFloat(R.styleable.SplashView_fadeFactor, 10.0f);
            }
        } finally {
            if (a != null) a.recycle();
        }

        svgAnimator = ObjectAnimator.ofFloat(this, "phase", phase, 0.0f);
        svgAnimator.setDuration(duration);
        svgAnimator.start();
    }

    public int getSvgResource() {
        return svgRes;
    }

    public void setSvgResource(int svgRes) {
        this.svgRes = svgRes;
    }

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
        synchronized (svgLock) {
            updatePathsPhaseLocked();
        }
        invalidate();
    }

    private void updatePathsPhaseLocked() {
        final int count = paths.size();
        for (int i = 0; i < count; i++) {
            SvgPath svgPath = paths.get(i);
            svgPath.paint.setPathEffect(createPathEffect(svgPath.length, phase, 0.0f));
        }
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[] { pathLength, pathLength },
                Math.max(phase * pathLength, offset));
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (loader != null) {
            try {
                loader.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Unexpected error", e);
            }
        }

        loader = new Thread(new Runnable() {
            @Override
            public void run() {
                svg.load(getContext(), svgRes);
                synchronized (svgLock) {
                    paths = svg.getPathsForViewport(
                            w - getPaddingLeft() - getPaddingRight(),
                            h - getPaddingTop() - getPaddingBottom());
                    updatePathsPhaseLocked();
                }
            }
        }, "SVG Loader");
        loader.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        synchronized (svgLock) {
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop() - getPaddingBottom());
            final int count = paths.size();
            for (int i = 0; i < count; i++) {
                SvgHelper.SvgPath svgPath = paths.get(i);

                // We use the fade factor to speed up the alpha animation
                int alpha = (int) (Math.min((1.0f - phase) * fadeFactor, 1.0f) * 255.0f);
                svgPath.paint.setAlpha((int) (alpha));

                canvas.drawPath(svgPath.path, svgPath.paint);
            }
            canvas.restore();
        }
    }
}
