package com.bulgogi.flag.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulgogi.flag.R;
import com.bulgogi.flag.model.Flag;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

public class FlagArrayAdapter<T> extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private LayoutInflater inflater;
    private List<T> items;
    private int headerResId;
    private int itemResId;
    private Animation fadeIn;
    private ImageLoader imgLoader;
    private DisplayImageOptions dpOptions;
    private boolean iscenterInside = false;

    public FlagArrayAdapter(Context context, List<T> items, int headerResId, int itemResId, boolean iscenterInside) {
        this.items = items;
        this.headerResId = headerResId;
        this.itemResId = itemResId;
        this.iscenterInside = iscenterInside;
        inflater = LayoutInflater.from(context);
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        imgLoader = ImageLoader.getInstance();
        dpOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Config.RGB_565)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public long getHeaderId(int i) {
        T item = getItem(i);
        CharSequence cs = "";
        if (item instanceof Flag) {
            cs = ((Flag) item).getCountry();
        }
        return cs.subSequence(0, 1).charAt(0);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(headerResId, parent, false);
            holder = new HeaderViewHolder();
            holder.title = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        T item = getItem(position);
        CharSequence cs = ((Flag) item).getCountry();

        // set header text as first char in string
        holder.title.setText(cs.subSequence(0, 1));

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(itemResId, parent, false);
            holder = new ViewHolder();
            holder.container = convertView.findViewById(R.id.container);
            holder.flag = (ImageView) convertView.findViewById(R.id.flag);
            holder.country = (TextView) convertView.findViewById(R.id.country);
            holder.loading = convertView.findViewById(R.id.loading);
            if (iscenterInside) holder.flag.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        T item = getItem(position);
        Flag flag = (Flag) item;
        imgLoader.displayImage(flag.getThumbUri(), holder.flag, dpOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.loading.setVisibility(View.GONE);
                holder.country.setVisibility(View.VISIBLE);
            }
        });
        holder.country.setText(flag.getCountry());

        return convertView;
    }

    private static class HeaderViewHolder {
        public TextView title;
    }

    private static class ViewHolder {
        public View container;
        public ImageView flag;
        public TextView country;
        public View loading;
    }
}
