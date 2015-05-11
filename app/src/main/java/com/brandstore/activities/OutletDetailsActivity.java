package com.brandstore.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brandstore.OutletDetailsAsyncTask;
import com.brandstore.R;
import com.brandstore.adapters.TagPriceListViewAdapter;
import com.brandstore.entities.TagPrice;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_details);
        toolbar = (Toolbar) findViewById(R.id.outletdetailstoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String id = getIntent().getStringExtra("id");

        // get the list of UI Elements' Objects
ScrollView scroll=(ScrollView)findViewById(R.id.outletDetails_ScrollView);
        outletimage = (ImageView) findViewById(R.id.outlet_image);
        outletname = (TextView) findViewById(R.id.outletDetails_brandName);
        floor = (TextView) findViewById(R.id.outletDetails_floorName);
        hubname = (TextView) findViewById(R.id.outletDetails_hubName);
        description = (TextView) findViewById(R.id.outletDetails_description);
        website = (TextView) findViewById(R.id.outletDetails_website);

        readmore = (Button) findViewById(R.id.readmore);
        readmore.setVisibility(View.INVISIBLE);


        tagprice=(ListView)findViewById(R.id.tag_and_price);
        mTagPriceListViewAdapter=new TagPriceListViewAdapter(tag,price,this);
        tagprice.setAdapter(mTagPriceListViewAdapter);


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
                this);
        mOutletDetailsAsyncTask.execute();
        scroll.scrollTo(0,0);
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
