package com.brandstore1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.entities.RelatedBrands;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Ravi on 17-May-15.
 */
public class RelatedBrandsListViewAdapter extends BaseAdapter {

    ArrayList<RelatedBrands> brandsArrayList;
    LayoutInflater inflater;

    public RelatedBrandsListViewAdapter(ArrayList<RelatedBrands> brandsArray, Context context) {
        this.brandsArrayList = brandsArray;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return brandsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder1();
            convertView = inflater.inflate(R.layout.relatedbrands_list_view_item, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder1) convertView.getTag();

        }
        viewHolder.relatedBrandsImage = (ImageView) convertView.findViewById(R.id.relatedbrandsimage);
        viewHolder.relatedBrandsName = (TextView) convertView.findViewById(R.id.relatedbrandsname);

        viewHolder.relatedBrandsName.setText(brandsArrayList.get(position).getName());
        ImageLoader.getInstance().displayImage(brandsArrayList.get(position).getImage(), viewHolder.relatedBrandsImage);
        return convertView;
    }

    static class ViewHolder1 {

        TextView relatedBrandsName;
        ImageView relatedBrandsImage;


    }
}
