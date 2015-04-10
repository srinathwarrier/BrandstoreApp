package com.brandstore.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.brandstore.R;

import java.util.ArrayList;

/**
 * Created by Ravi on 07-Apr-15.
 */
public class TagPriceListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<String>Tag;
    ArrayList<String>Price;
    public TagPriceListViewAdapter(ArrayList<String> Tag, ArrayList<String>Price,Activity context)
    {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Tag=Tag;
        this.Price=Price;
    }
    @Override
    public int getCount() {
        return 0;
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
            mHolder.textView = (TextView) convertView.findViewById(R.id.Tag);
            mHolder.textView2=(TextView)convertView.findViewById(R.id.Price);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }


        mHolder.textView.setText(Tag.get(position));
        mHolder.textView2.setText(Price.get(position));
        return convertView;
    }

    class ViewHolder
    {
        TextView textView;
        TextView textView2;
    }
}
