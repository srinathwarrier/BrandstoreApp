package com.brandstore1;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brandstore1.adapters.RelatedBrandsListViewAdapter;
import com.brandstore1.adapters.TagPriceListViewAdapter;
import com.brandstore1.entities.OutletDetails;
import com.brandstore1.entities.RelatedBrands;
import com.brandstore1.entities.TagPrice;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ravi on 30-Mar-15.
 */
public class OutletDetailsAsyncTask extends AsyncTask<Void, Void, OutletDetails> {

    String id;  // Input parameter

    // UI Element Objects
    ImageView outletimage;
    TextView outletname;
    TextView floor;
    TextView hubname;
    TextView website;
    TextView description;
    Button readmore;

    // Entity Object
    OutletDetails obj;
    //JSONObject jsonobject;
    Context context;
    TagPriceListViewAdapter mTagPrice;
    ListView tagpriceListView;
    ArrayList<String> tag;
    ArrayList<String> price;
ScrollView scrollView;
    Toolbar toolbar;
    ArrayList<RelatedBrands> brandsArray;
    RelatedBrandsListViewAdapter relatedBrandsListViewAdapter;
    ImageView first,second,third;

    public OutletDetailsAsyncTask(
            TagPriceListViewAdapter TagPrice,
            ImageView outletimage,
            TextView outletname,
            TextView floor,
            TextView hubname,
            String ids,
            TextView description,
            TextView website,
            ArrayList<String>tag,
            ArrayList<String>price,
           ListView tagpriceListView,
            ScrollView scrollView,
            Button readmore,
            Toolbar toolbar,
            RelatedBrandsListViewAdapter relatedBrandsListViewAdapter,
            ArrayList<RelatedBrands>brandsArray,
            ImageView first,
            ImageView second,
            ImageView third,
            Context context) {

        this.outletimage = outletimage;
        this.outletname = outletname;
        this.floor = floor;
        this.hubname = hubname;
        this.description = description;
        this.website = website;
        id = ids;
        this.brandsArray=brandsArray;
        this.context = context;
        this.readmore = readmore;
        mTagPrice=TagPrice;
        this.tag=tag;
        this.price=price;
        this.scrollView=scrollView;
        this.toolbar=toolbar;
        this.relatedBrandsListViewAdapter=relatedBrandsListViewAdapter;
        this.tagpriceListView = tagpriceListView;
this.first=first;
        this.second=second;
        this.third=third;

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
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected OutletDetails doInBackground(Void... params) {
        tag.clear();
        price.clear();
        StringBuilder builder = null;
        try {
            URL url = new URL("http://awsm-awsmproject.rhcloud.com/getOutletDetails?id=" + id);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            JSONObject jsonobject = new JSONObject(builder.toString());

            obj = new OutletDetails();

            obj.setOutletName(jsonobject.getString("brandName"));
            obj.setOutletImage(jsonobject.getString("imageUrl"));
            obj.setFloor(jsonobject.getString("floorNumber").concat(", "));
            obj.setHubName(jsonobject.getString("hubName"));
            obj.setLongDescription(jsonobject.getString("description"));
            obj.setShortDescription(obj.getLongDescription().substring(0, 100).concat("..."));
            obj.setGenderCodeString(jsonobject.get("genderCodeString").toString());


            JSONArray tagsArray = jsonobject.getJSONArray("tagsArray");
            JSONObject tagsObject;


            for (int i = 0; i < tagsArray.length(); i++) {
                tagsObject = tagsArray.getJSONObject(i);
                tag.add(tagsObject.getString("tagName"));
                price.add("₹ "+tagsObject.getString("avgPrice"));
            }

            RelatedBrands obj1;
            JSONArray relatedBrandsArray=jsonobject.getJSONArray("relatedBrandsArray");
            JSONObject relatedBrandsObject;

            for(int i=0;i<relatedBrandsArray.length();i++)
            {
                obj1=new RelatedBrands();
                relatedBrandsObject=relatedBrandsArray.getJSONObject(i);
                obj1.setId(relatedBrandsObject.getString("outletID"));
                obj1.setImage(relatedBrandsObject.getString("imageUrl"));
                obj1.setName(relatedBrandsObject.getString("brandName"));
                brandsArray.add(obj1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }


    @Override
    protected void onPostExecute(final OutletDetails outletDetails) {
        super.onPostExecute(outletDetails);

        if (outletDetails != null) {

            // 1. Add Default views to the LinearLayout
            outletname.setText(outletDetails.getOutletName());
            floor.setText(outletDetails.getFloor());
            description.setText(outletDetails.getShortDescription());
            website.setText(outletDetails.getWebsite());
            hubname.setText(outletDetails.getHubName());
            ImageLoader.getInstance().displayImage(outletDetails.getOutletImage(), outletimage);
/*
            // 2. Add TagPrice views to the LinearLayout
            for (int i = 0; i < obj.getTagPriceArrayList().size(); i++) {
                TextView tagText = new TextView(context),
                        priceText = new TextView(context);
                tagText.setText("Avg price of " + obj.getTagPriceArrayList().get(i).getTagString());
                priceText.setText(" -  Rs." + obj.getTagPriceArrayList().get(i).getPriceString());
                LinearLayout newRow = new LinearLayout(context);
                newRow.setOrientation(LinearLayout.HORIZONTAL);
                newRow.addView(tagText);
                newRow.addView(priceText);
                tagPriceLinearLayout.addView(newRow);
            }*/

        }
        readmore.setVisibility(View.VISIBLE);
        readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (readmore.getText() == "READ LESS") {
                    readmore.setText("READ MORE");
                    description.setText(outletDetails.getShortDescription());
                } else {
                    Log.d("entered", "Entered");
                    description.setText(outletDetails.getLongDescription());
                    readmore.setText("READ LESS");
                }

            }

        });

        if(obj.getGenderCodeString().contains("M"))
        {
            first.setVisibility(View.VISIBLE);
        }
        if(obj.getGenderCodeString().contains("F"))
        {
            second.setVisibility(View.VISIBLE);
        }
        if(obj.getGenderCodeString().contains("K"))
        {
            third.setVisibility(View.VISIBLE);
        }
    relatedBrandsListViewAdapter.notifyDataSetChanged();
    mTagPrice.notifyDataSetChanged();
        toolbar.setTitle(outletDetails.getOutletName());

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(tagpriceListView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < mTagPrice.getCount(); i++) {
            View listItem = mTagPrice.getView(i, null, tagpriceListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = tagpriceListView.getLayoutParams();
        params.height = totalHeight + (tagpriceListView.getDividerHeight() * (mTagPrice.getCount() - 1));
        tagpriceListView.setLayoutParams(params);
        //tagpriceListView.requestLayout();
        scrollView.smoothScrollTo(0,0);
    }


}
