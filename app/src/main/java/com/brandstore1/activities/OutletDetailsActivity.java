package com.brandstore1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandstore1.AddFavOutletAsyncTask;
import com.brandstore1.CheckFavoritesAsyncTask;
import com.brandstore1.adapters.RelatedBrandsListViewAdapter;
import com.brandstore1.entities.Outlet;
import com.brandstore1.entities.OutletDetails;
import com.brandstore1.entities.RelatedBrands;
import com.brandstore1.utils.HorizontalListView;

import com.brandstore1.OutletDetailsAsyncTask;
import com.brandstore1.R;
import com.brandstore1.adapters.TagPriceListViewAdapter;
import com.brandstore1.entities.TagPrice;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.util.concurrent.ExecutionException;

public class OutletDetailsActivity extends ActionBarActivity {
    ImageView outletimage;
    TextView outletname;
    TextView floor;
    TextView website;
    TextView description;
    TextView hubname;
    ListView tagprice;
    Button readmore;
    TextView offerContentTextView;
    Toolbar toolbar;
    TagPriceListViewAdapter mTagPriceListViewAdapter;
    ArrayList<String> tag = new ArrayList();
    ArrayList<String> price= new ArrayList();
    ArrayList<String> offersArrayList= new ArrayList();
    HorizontalListView relatedBrands;
    ArrayList<RelatedBrands> brandsarray= new ArrayList();
    ImageView first,second,third;
    String id;
    TextView emptyTagPriceView;
    TextView emptyRelatedBrandsView;
    MenuItem tmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_details);
        toolbar = (Toolbar) findViewById(R.id.outletdetailstoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));

        File cacheDir = StorageUtils.getCacheDirectory(this);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.blank_screen) // resource or drawable
                .showImageForEmptyUri(R.drawable.blank_screen) // resource or drawable
                .showImageOnFail(R.drawable.blank_screen) // resource or drawable
                .resetViewBeforeLoading(true)  // default

                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)

                .diskCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        id = getIntent().getStringExtra("id");

        // get the list of UI Elements' Objects
        ScrollView scroll=(ScrollView)findViewById(R.id.outletDetails_ScrollView);
        outletimage = (ImageView) findViewById(R.id.outlet_image);
        outletname = (TextView) findViewById(R.id.outletDetails_brandName);
        floor = (TextView) findViewById(R.id.outletDetails_floorName);
        hubname = (TextView) findViewById(R.id.outletDetails_hubName);
        description = (TextView) findViewById(R.id.outletDetails_description);
        website = (TextView) findViewById(R.id.outletDetails_website);
        first= (ImageView) findViewById(R.id.first);
        second= (ImageView) findViewById(R.id.second);
        third= (ImageView) findViewById(R.id.third);
        readmore = (Button) findViewById(R.id.readmore);
        readmore.setVisibility(View.INVISIBLE);
        offerContentTextView = (TextView) findViewById(R.id.outletDetails_offer_content);
        emptyTagPriceView = (TextView) findViewById(R.id.outlet_details_empty_tagPrice_textView);
        emptyRelatedBrandsView = (TextView) findViewById(R.id.outlet_details_empty_relatedBrands_textView);

        tagprice=(ListView)findViewById(R.id.tag_and_price);
        tagprice.setEmptyView(emptyTagPriceView);
        mTagPriceListViewAdapter=new TagPriceListViewAdapter(tag,price,this);
        tagprice.setAdapter(mTagPriceListViewAdapter);


        RelatedBrandsListViewAdapter relatedBrandsListViewAdapter=new RelatedBrandsListViewAdapter(brandsarray,this);
        relatedBrands=(HorizontalListView)findViewById(R.id.relatedbrands);
        //relatedBrands.setEmptyView(emptyRelatedBrandsView);
        relatedBrands.setAdapter(relatedBrandsListViewAdapter);

        OutletDetailsAsyncTask mOutletDetailsAsyncTask = new OutletDetailsAsyncTask(
                mTagPriceListViewAdapter,
                outletimage,
                outletname,
                floor,
                hubname,
                id,
                description,
                website,
                tag,
                price,
                tagprice,
                offersArrayList,
                offerContentTextView,
                scroll,
                readmore,
                toolbar,
                relatedBrandsListViewAdapter,
                brandsarray,
                emptyRelatedBrandsView,
                first,second,third,
                this);
        mOutletDetailsAsyncTask.execute();
        scroll.scrollTo(0, 0);


        relatedBrands.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                intent.putExtra("id", brandsarray.get(position).getId());
                startActivity(intent);
            }
        });

        // Commenting this, since checkbox is commented in XML file as well.
        //CheckBox cb = (CheckBox) findViewById(R.id.favorites);
        //CheckFavoritesAsyncTask mCheckFavoritesAsyncTask = new CheckFavoritesAsyncTask(id, cb);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outlet_details, menu);
        takeMeThere(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addFavorites(View view){
        boolean checked = ((CheckBox) view).isChecked();
        boolean operation;
        //Toast.makeText(OutletDetailsActivity.this,id, Toast.LENGTH_LONG).show();
        operation = checked;
        AddFavOutletAsyncTask mAddFavOutletAsyncTask = new AddFavOutletAsyncTask(id,operation);
        mAddFavOutletAsyncTask.execute();

    }

    public void takeMeThere(Menu menu){
        tmt = menu.findItem(R.id.take_me_there);
        tmt.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                try {
                    Intent intent = new Intent(getApplicationContext(), TakeMeThereActivity.class);
                    intent.getStringExtra("id");
                    intent.putExtra("name", (String) outletname.getText());
                    intent.putExtra("type", TakeMeThereActivity.TMT_type.TO_KNOWN);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Exception ss" + e);
                }
                return true;
            }
        });

    }

}
