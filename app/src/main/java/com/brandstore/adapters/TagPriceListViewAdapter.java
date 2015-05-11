package com.brandstore.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brandstore.R;
import com.brandstore.entities.TagPrice;

import java.util.ArrayList;

/**
 * Created by Ravi on 07-Apr-15.
 */
public class TagPriceListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
   ArrayList<String> tag;
    ArrayList<String>price;

    public TagPriceListViewAdapter(ArrayList<String>tag,ArrayList<String> price, Activity context) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tag=tag;
        this.price=price;
    }

    @Override
    public int getCount() {
        return tag.size();
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
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tag_price_list_view_item, null);
            mHolder.tagTextView = (TextView) convertView.findViewById(R.id.Tag);
            mHolder.priceTextView = (TextView) convertView.findViewById(R.id.Price);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }


        mHolder.tagTextView.setText(tag.get(position).toString());
        mHolder.priceTextView.setText(price.get(position).toString());
        return convertView;
    }

    class ViewHolder {
        TextView tagTextView;
        TextView priceTextView;
    }
}
