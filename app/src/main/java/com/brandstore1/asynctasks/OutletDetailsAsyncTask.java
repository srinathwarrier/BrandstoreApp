package com.brandstore1.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.adapters.RelatedBrandsListViewAdapter;
import com.brandstore1.adapters.TagPriceListViewAdapter;
import com.brandstore1.entities.OutletDetails;
import com.brandstore1.entities.RelatedBrands;
import com.brandstore1.entities.TagPrice;
import com.brandstore1.utils.CircularProgressDialog;
import com.brandstore1.utils.Connections;
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
public class OutletDetailsAsyncTask extends AsyncTask<Void, Void, String> {

    String id;  // Input parameter

    // UI Element Objects
    ImageView outletimage;
    TextView outletname;
    TextView floor;
    TextView hubname;
    TextView offerDetails;
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
    //ArrayList<String> offersArrayList;
    TextView offerContentTextView;
    ScrollView scrollView;
    Toolbar toolbar;
    ArrayList<RelatedBrands> brandsArray;
    RelatedBrandsListViewAdapter relatedBrandsListViewAdapter;
    ImageView first,second,third;
    CircularProgressDialog circularProgressDialog;
    TextView emptyRelatedBrandsView;
    CheckBox cb ;


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
            ArrayList<String> offersArrayList,
            TextView offerContentTextView,
            ScrollView scrollView,
            Button readmore,
            Toolbar toolbar,
            RelatedBrandsListViewAdapter relatedBrandsListViewAdapter,
            ArrayList<RelatedBrands>brandsArray,
            TextView theEmptyRelatedBrandsView,
            ImageView first,
            ImageView second,
            ImageView third,
            CheckBox cb,
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
        //this.offersArrayList = offersArrayList;
        this.offerContentTextView = offerContentTextView;
        this.scrollView=scrollView;
        this.toolbar=toolbar;
        this.relatedBrandsListViewAdapter=relatedBrandsListViewAdapter;
        this.emptyRelatedBrandsView = theEmptyRelatedBrandsView;
        this.tagpriceListView = tagpriceListView;
        this.first=first;
        this.second=second;
        this.third=third;
        this.cb=cb;

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

        circularProgressDialog = new CircularProgressDialog(this.context);
        circularProgressDialog = CircularProgressDialog.show(this.context,"","");
        tag.clear();
        price.clear();
    }


    @Override
    protected String doInBackground(Void... params) {
        StringBuilder builder = null;
        try {
            //URL url = new URL("http://awsm-awsmproject.rhcloud.com/getOutletDetails?id=" + id);
            String urlString = new Connections().getOutletDetailsURL(id);
            URL url = new URL(urlString);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);

            return (builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String resultString) {
        super.onPostExecute(resultString);

        try {

            JSONObject jsonobject = new JSONObject(resultString);
            obj = new OutletDetails();

            /*
                Extract : Basic data
             */
            obj.setOutletName(jsonobject.getString("outletName"));
            obj.setOutletImage(jsonobject.getString("imageUrl"));
            obj.setFloor(jsonobject.getString("floorNumber").concat(", "));
            obj.setHubName(jsonobject.getString("hubName"));
            obj.setLongDescription(jsonobject.getString("description"));
            if(obj.getLongDescription().length()<100){
                obj.setShortDescription(obj.getLongDescription());
            }
            else{
                obj.setShortDescription(obj.getLongDescription().substring(0, 100).concat("..."));
            }
            obj.setGenderCodeString(jsonobject.get("genderCodeString").toString());
            obj.setIsFavorite(Boolean.valueOf(jsonobject.get("isFavorite").toString()));

            /*
                Extract : TagsArray
             */

            JSONArray tagsArray = jsonobject.getJSONArray("tagsArray");
            JSONObject tagsObject;
            for (int i = 0; i < tagsArray.length(); i++) {
                tagsObject = tagsArray.getJSONObject(i);
                tag.add(tagsObject.getString("tagName"));
                price.add("₹ "+tagsObject.getString("avgPrice"));
            }

            /*
                Extract : Related Brands
             */

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
            if(brandsArray.size()==0){
                emptyRelatedBrandsView.setVisibility(View.VISIBLE);
            }else{
                emptyRelatedBrandsView.setVisibility(View.GONE);
            }

            /*
                Extract : Offers
             */

            JSONArray offersArray = jsonobject.getJSONArray("offersArray");
            JSONObject offersObject;
            String offerString="";
            for (int i = 0; i < offersArray.length(); i++) {
                offersObject = offersArray.getJSONObject(i);
                offerString+=offersObject.getString("offerDesc")+"\n";
                //offersArrayList.add(offersObject.getString("offerDesc"));
            }
            if(!offerString.equals("")){
                offerContentTextView.setText(offerString);
            }

            /*
                Set UI : Basic data
             */

            final OutletDetails outletDetails = obj;
            if (outletDetails != null) {

                // 1. Add Default views to the LinearLayout

                outletname.setText(outletDetails.getOutletName());
                floor.setText(outletDetails.getFloor());
                description.setText(outletDetails.getShortDescription());
                website.setText(outletDetails.getWebsite());
                hubname.setText(outletDetails.getHubName());
                ImageLoader.getInstance().displayImage(outletDetails.getOutletImage(), outletimage);
            }

            /*
                Set UI : Add readmore button
             */
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

            /*
                Set UI : Add gender tag elements
            */
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

            /*
                Set UI : Set toolbar name
             */
            toolbar.setTitle(outletDetails.getOutletName());

            /*
                Set UI : Favorite checkbox
             */
            cb.setChecked(outletDetails.isFavorite());


            /*
                Set UI : Calculate height to decide tagPrice dimensions
             */
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(tagpriceListView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < mTagPrice.getCount(); i++) {
                View listItem = mTagPrice.getView(i, null, tagpriceListView);
                listItem.setLayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = tagpriceListView.getLayoutParams();
            params.height = totalHeight + (tagpriceListView.getDividerHeight() * (mTagPrice.getCount() - 1));
            tagpriceListView.setLayoutParams(params);
            //tagpriceListView.requestLayout();

        }catch (JSONException e) {
                e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        scrollView.smoothScrollTo(0, 0);
        scrollView.setVisibility(View.VISIBLE);

        circularProgressDialog.dismiss();
        mTagPrice.notifyDataSetChanged();
        relatedBrandsListViewAdapter.notifyDataSetChanged();





    }

    public String getStringFromOfferArrayList(ArrayList offersArrayList){
        if(offersArrayList==null || offersArrayList.size()==0){
            return ("No Ongoing Offers !!!");
        }
        else{
            String returnValue="";
        }

        return "";
    }


}
