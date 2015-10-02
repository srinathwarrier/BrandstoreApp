package com.brandstore1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandstore1.R;

/**
 * Created by Ravi on 26-Mar-15.
 */
public class CategoryGridViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int categoryImages[];
    private String categoryNames[];

    public CategoryGridViewAdapter(Activity context, int images[], String categories[]) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        categoryImages = images;
        categoryNames = categories;
    }

    @Override
    public int getCount() {
        return categoryImages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView ivCategoryImage;
        TextView tvCategoryName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.category_grid_view_single_item, null);
            mHolder.ivCategoryImage = (ImageView) convertView.findViewById(R.id.category_image);
            mHolder.tvCategoryName = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.ivCategoryImage.setImageResource(categoryImages[position]);
        mHolder.tvCategoryName.setText(categoryNames[position]);
        return convertView;
    }
}
