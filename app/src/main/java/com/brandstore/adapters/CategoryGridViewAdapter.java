package com.brandstore.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandstore.R;

/**
 * Created by Ravi on 26-Mar-15.
 */
public class CategoryGridViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int CategoryImages[];
private String CategoryNames[];

    public CategoryGridViewAdapter(Activity context,int images[],String categories[]) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CategoryImages=images;
        CategoryNames=categories;

    }

    @Override
    public int getCount() {
        return CategoryImages.length;
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

        ImageView CategoryImage;
        TextView CategoryName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.category_grid_view_single_item, null);
            mHolder.CategoryImage = (ImageView) convertView.findViewById(R.id.category_image);
mHolder.CategoryName=(TextView)convertView.findViewById(R.id.category_name);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }


        mHolder.CategoryImage.setImageResource(CategoryImages[position]);
        mHolder.CategoryName.setText(CategoryNames[position]);
        return convertView;
    }
}
