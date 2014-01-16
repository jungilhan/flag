package com.bulgogi.flag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bulgogi.flag.R;
import com.bulgogi.flag.model.CategoryItem;
import com.hb.views.PinnedSectionListView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private List<CategoryItem> items;
    private int resId;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, int resId, List<CategoryItem> items) {
        this.items = items;
        this.resId = resId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resId, parent, false);
            holder = new ViewHolder();
            holder.sectionWrapper = convertView.findViewById(R.id.section_wrapper);
            holder.section = (TextView) holder.sectionWrapper.findViewById(R.id.section);
            holder.item = (TextView) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryItem item = items.get(position);
        if (item.type == CategoryItem.SECTION) {
            holder.sectionWrapper.setVisibility(View.VISIBLE);
            holder.section.setText(item.name);
            holder.item.setVisibility(View.GONE);
        } else {
            holder.sectionWrapper.setVisibility(View.GONE);
            holder.item.setVisibility(View.VISIBLE);
            holder.item.setText(item.name);
        }
        return convertView;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == CategoryItem.SECTION;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    static class ViewHolder {
        View sectionWrapper;
        TextView section;
        TextView item;
    }
}
