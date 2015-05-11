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
import com.brandstore.entities.Outlet;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ravi on 29-Mar-15.
 */
public class OutletListAdapter extends BaseAdapter {
    ArrayList<Outlet> mOutletList;
    private LayoutInflater inflater;

    public OutletListAdapter(ArrayList<Outlet> outlet, Activity context) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOutletList = outlet;
        File cacheDir = StorageUtils.getCacheDirectory(context);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.blank_screen) // resource or drawable
                .showImageForEmptyUri(R.drawable.blank_screen) // resource or drawable
                .showImageOnFail(R.drawable.blank_screen) // resource or drawable
                .resetViewBeforeLoading(true)  // default

                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)

                .diskCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public int getCount() {
        return mOutletList.size();
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
        if (convertView == null) {
            mHolder = new ViewHolder1();
            convertView = inflater.inflate(R.layout.outlet_list_list_view_item, null);
            mHolder.brandNameTextView = (TextView) convertView.findViewById(R.id.outletname);
            mHolder.floorAndHubNameTextView = (TextView) convertView.findViewById(R.id.floorAndHubName);
            mHolder.tagAndPriceTextView = (TextView) convertView.findViewById(R.id.tag_and_price_label);
            mHolder.image = (ImageView) convertView.findViewById(R.id.outlet_image);
            mHolder.male = (ImageView) convertView.findViewById(R.id.first);
            mHolder.female = (ImageView) convertView.findViewById(R.id.second);
            mHolder.kids = (ImageView) convertView.findViewById(R.id.third);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder1) convertView.getTag();
        }

        mHolder.male.setVisibility(View.INVISIBLE);
        mHolder.female.setVisibility(View.INVISIBLE);
        mHolder.kids.setVisibility(View.INVISIBLE);
        if (mOutletList.get(position).getGenderCodeString().contains("M"))
            mHolder.male.setVisibility(View.VISIBLE);

        if (mOutletList.get(position).getGenderCodeString().contains("F"))
            mHolder.female.setVisibility(View.VISIBLE);

        if (mOutletList.get(position).getGenderCodeString().contains("K"))
            mHolder.kids.setVisibility(View.VISIBLE);


        mHolder.brandNameTextView.setText(mOutletList.get(position).getBrandOutletName());
        mHolder.floorAndHubNameTextView.setText(mOutletList.get(position).getFloorNumber() + ", " + mOutletList.get(position).getMallName());
        mHolder.tagAndPriceTextView.setText(mOutletList.get(position).getRelevantTag() + " : ₹ " + mOutletList.get(position).getPrice());

        ImageLoader.getInstance().displayImage(mOutletList.get(position).getImageUrl(), mHolder.image);

        return convertView;
    }


    static class ViewHolder1 {

        TextView brandNameTextView;
        TextView floorAndHubNameTextView;

        TextView tagAndPriceTextView;
        //TextView tagNameTextView;
        //TextView avgPriceTextView;

        TextView textView2;

        ImageView image;
        //ImageView logoOnTop;
        ImageView male;
        ImageView female;
        ImageView kids;

    }
}
