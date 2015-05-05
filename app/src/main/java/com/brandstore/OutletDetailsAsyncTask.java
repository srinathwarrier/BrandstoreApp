package com.brandstore;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandstore.adapters.TagPriceListViewAdapter;
import com.brandstore.entities.OutletDetails;
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

    int i;
    ImageView outletimage;
    TextView outletname;
    TextView floor;
    String id;
    TextView hubname;
    TextView website;
    TextView description;
    OutletDetails obj;
    JSONObject jsonobject;
    Context context;
    ArrayList<String> Tag;
    ArrayList<String> Price;
    TagPriceListViewAdapter mTagPrice;

    public OutletDetailsAsyncTask(TagPriceListViewAdapter TagPrice, ImageView outletimage, TextView outletname, TextView floor, TextView hubname, String ids, TextView description, TextView website, Context context, ArrayList<String> Tag, ArrayList<String> Price) {
        this.Tag = Tag;
        this.Price = Price;
        this.outletimage = outletimage;
        this.outletname = outletname;
        this.floor = floor;
        this.hubname = hubname;
        this.description = description;
        this.website = website;
        id = ids;
        this.context = context;
        mTagPrice = TagPrice;
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
            Tag.clear();
            Price.clear();
            // JSONArray json = new JSONArray(builder.toString());
            JSONObject jsonobject = new JSONObject(builder.toString());
            obj = new OutletDetails();
            //jsonobject= json.getJSONObject(0);
            //Log.d("outledeatisljsonobject",""+jsonobject);
            obj.setFloor(jsonobject.getString("floorNumber").concat(", "));
            obj.setOutletName(jsonobject.getString("brandName"));
            obj.setOutletImage(jsonobject.getString("imageUrl"));
            obj.setHubName(jsonobject.getString("hubName"));
            obj.setDescription(jsonobject.getString("description"));

            JSONArray json2 = jsonobject.getJSONArray("tagsArray");
            for (i = 0; i < json2.length(); i++) {
                JSONObject object = json2.getJSONObject(i);
                Tag.add(object.getString("tagName"));
                Price.add(object.getString("avgPrice"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }


    @Override
    protected void onPostExecute(OutletDetails outletDetails) {
        super.onPostExecute(outletDetails);

        outletname.setText(outletDetails.getOutletName());
        floor.setText(outletDetails.getFloor());
        description.setText(outletDetails.getDescription());
        //website.setText(outletDetails.getWebsite());
        hubname.setText(outletDetails.getHubName());
        mTagPrice.notifyDataSetChanged();
        ImageLoader.getInstance().displayImage(outletDetails.getOutletImage(), outletimage);
    }
}
