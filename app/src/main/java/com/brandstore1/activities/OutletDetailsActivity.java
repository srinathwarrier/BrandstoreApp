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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brandstore1.asynctasks.AddFavOutletAsyncTask;
import com.brandstore1.adapters.RelatedBrandsListViewAdapter;
import com.brandstore1.entities.Outlet;
import com.brandstore1.entities.RelatedBrands;
import com.brandstore1.utils.HorizontalListView;

import com.brandstore1.asynctasks.OutletDetailsAsyncTask;
import com.brandstore1.R;
import com.brandstore1.adapters.TagPriceListViewAdapter;
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
    Outlet outlet;
    TextView emptyTagPriceView;
    TextView emptyRelatedBrandsView;

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

        // Commenting this, since checkbox is commented in XML file as well.
        CheckBox cb = (CheckBox) findViewById(R.id.favorites);




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
                cb,
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
        //Menu menu;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch(id){
            case android.R.id.home:
                //noinspection SimplifiableIfStatement
                onBackPressed();
                return true;
            case R.id.take_me_there:
                goToTakeMeThereActivityScreen();
                break;

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

    public void goToTakeMeThereActivityScreen(){
        Intent intent = new Intent(getApplicationContext(), TakeMeThereActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", (String) outletname.getText());
        intent.putExtra("type", TakeMeThereActivity.TMT_type.TO_KNOWN);
        startActivity(intent);

    }

}
