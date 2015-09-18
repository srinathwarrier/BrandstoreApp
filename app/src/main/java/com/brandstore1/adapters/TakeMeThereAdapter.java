package com.brandstore1.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brandstore1.R;

import java.util.ArrayList;

/**
 * Created by Sonika on 8/22/2015.
 */
public class TakeMeThereAdapter extends BaseAdapter {
    ArrayList<String> pathArrayList;
    //ArrayList<String> outletArrayList;
    private LayoutInflater inflater;
    Toolbar toolbar;
    TextView emptyView;

    public TakeMeThereAdapter(ArrayList pathArrayList, Activity context, Toolbar toolbar, TextView emptyView) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.toolbar = toolbar;
        this.pathArrayList = pathArrayList;
        this.emptyView =  emptyView;
        //this.outletArrayList = outletArrayList;
}

    @Override
    public int getCount() {
        //System.out.println("size" + pathArrayList.size());

        return pathArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 mHolder;


        if (convertView == null) {
            mHolder = new ViewHolder1();
            convertView = inflater.inflate(R.layout.tmt_list_view, null);
            mHolder.pathTextView = (TextView) convertView.findViewById(R.id.tmt_path);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder1) convertView.getTag();
        }
        System.out.println(position);
        mHolder.pathTextView.setText(pathArrayList.get(position));
        return convertView;
    }


    static class ViewHolder1 {

        TextView pathTextView;

    }

    public void resetList(ArrayList<String> newList) {
        this.pathArrayList = newList;
        this.notifyDataSetChanged();
    }
}


