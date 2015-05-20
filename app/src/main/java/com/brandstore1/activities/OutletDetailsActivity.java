package com.brandstore1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brandstore1.adapters.RelatedBrandsListViewAdapter;
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

import java.io.File;
import java.util.ArrayList;

public class OutletDetailsActivity extends ActionBarActivity {
    ImageView outletimage;
    TextView outletname;
    TextView floor;
    TextView website;
    TextView description;
    TextView hubname;
    ListView tagprice;
    LinearLayout tagPriceLinearLayout;
    Button readmore;
Toolbar toolbar;
    TagPriceListViewAdapter mTagPriceListViewAdapter;
    ArrayList<String> tag = new ArrayList();
    ArrayList<String> price= new ArrayList();
    HorizontalListView relatedBrands;
    ArrayList<RelatedBrands> brandsarray= new ArrayList();
    ImageView first,second,third;

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
        String id = getIntent().getStringExtra("id");

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


        tagprice=(ListView)findViewById(R.id.tag_and_price);
        mTagPriceListViewAdapter=new TagPriceListViewAdapter(tag,price,this);
        tagprice.setAdapter(mTagPriceListViewAdapter);


        RelatedBrandsListViewAdapter relatedBrandsListViewAdapter=new RelatedBrandsListViewAdapter(brandsarray,this);
        relatedBrands=(HorizontalListView)findViewById(R.id.relatedbrands);
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
                scroll,
                readmore,
                toolbar,
                relatedBrandsListViewAdapter,
                brandsarray,
                first,second,third,
                this);
        mOutletDetailsAsyncTask.execute();
        scroll.scrollTo(0,0);


        relatedBrands.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                intent.putExtra("id", brandsarray.get(position).getId());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outlet_details, menu);
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

}
