package com.brandstore1.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.entities.SearchResults;

import java.util.ArrayList;

/**
 * Created by Ravi on 27-Mar-15.
 */
public class ResultsListViewAdapter extends BaseAdapter {
    ArrayList<SearchResults> mSearchResult;
    private LayoutInflater inflater;

    public ResultsListViewAdapter(ArrayList<SearchResults> searchResult, Activity context) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSearchResult = searchResult;

    }

    @Override
    public int getCount() {
        return mSearchResult.size();
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
        ViewHolder1 mHolder;
        Log.d("SearchResult size", "" + mSearchResult.size() );
        if (mSearchResult.size() != 0) {

            if (convertView == null) {
                mHolder = new ViewHolder1();
                convertView = inflater.inflate(R.layout.search_result_list_view_item, null);
                mHolder.textView = (TextView) convertView.findViewById(R.id.textView);

                mHolder.textView2 = (TextView) convertView.findViewById(R.id.textView2);

                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder1) convertView.getTag();
            }

            try{
                mHolder.textView2.setText(mSearchResult.get(position).getCategory());
                mHolder.textView.setText(mSearchResult.get(position).getName());
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        return convertView;
    }

    static class ViewHolder1 {

        TextView textView;
        TextView textView2;
    }

}
