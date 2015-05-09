package com.brandstore;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandstore.entities.OutletDetails;
import com.brandstore.entities.TagPrice;
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

    // Entity Object
    OutletDetails obj;
    //JSONObject jsonobject;
    Context context;
    //TagPriceListViewAdapter mTagPrice;
    //ListView tagpriceListView;
    //ArrayList<TagPrice> mTagPriceArrayList;
    LinearLayout tagPriceLinearLayout;
public OutletDetailsAsyncTask(
                              //TagPriceListViewAdapter TagPrice,
                              ImageView outletimage,
                              TextView outletname,
                              TextView floor,
                              TextView hubname,
                              String ids,
                              TextView description,
                              TextView website,
                              //ArrayList<TagPrice> tagPriceArrayList,
                              //ListView newtagpriceListView,
                              LinearLayout theTagPriceLinearLayout,
                              Context context) {

    this.outletimage=outletimage;
    this.outletname=outletname;
    this.floor=floor;
    this.hubname=hubname;
    this.description=description;
    this.website=website;
    id=ids;
    this.context=context;
    //mTagPrice=TagPrice;
    //this.mTagPriceArrayList = tagPriceArrayList;
    //this.tagpriceListView = newtagpriceListView;
    this.tagPriceLinearLayout = theTagPriceLinearLayout;

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
            //jsonobject= json.getJSONObject(0);
            obj.setOutletName(jsonobject.getString("brandName"));
            obj.setOutletImage(jsonobject.getString("imageUrl"));
            obj.setFloor(jsonobject.getString("floorNumber").concat(", "));
            obj.setHubName(jsonobject.getString("hubName"));
            obj.setDescription(jsonobject.getString("description"));
            //obj.setWebsite(jsonobject.getString("website"));

            JSONArray tagsArray = jsonobject.getJSONArray("tagsArray");
            JSONObject tagsObject;
            ArrayList<TagPrice> tagPriceArrayList = new ArrayList<TagPrice>();

            for (int i=0;i<tagsArray.length();i++)
            {
                tagsObject = tagsArray.getJSONObject(i);

                TagPrice tagPriceObject = new TagPrice();
                tagPriceObject.setTagString(  tagsObject.getString("tagName") );
                tagPriceObject.setPriceString(tagsObject.getString("avgPrice"));

                tagPriceArrayList.add(tagPriceObject);
            }
            obj.setTagPriceArrayList(tagPriceArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }


    @Override
    protected void onPostExecute(OutletDetails outletDetails) {
        super.onPostExecute(outletDetails);

        if(outletDetails != null)
        {

            // 1. Add Default views to the LinearLayout
            outletname.setText(outletDetails.getOutletName());
            floor.setText(outletDetails.getFloor());
            description.setText(outletDetails.getDescription());
            website.setText(outletDetails.getWebsite());
            hubname.setText(outletDetails.getHubName());
            ImageLoader.getInstance().displayImage(outletDetails.getOutletImage(), outletimage);

            // 2. Add TagPrice views to the LinearLayout
            for(int i=0;i<obj.getTagPriceArrayList().size();i++)
            {
                TextView tagText = new TextView(context),
                        priceText = new TextView(context);
                tagText.setText("Avg price of "+obj.getTagPriceArrayList().get(i).getTagString());
                priceText.setText(" -  Rs."+obj.getTagPriceArrayList().get(i).getPriceString());
                LinearLayout newRow = new LinearLayout(context);
                newRow.setOrientation(LinearLayout.HORIZONTAL);
                newRow.addView(tagText);
                newRow.addView(priceText);
                tagPriceLinearLayout.addView(newRow);
            }

        }



    }




}
