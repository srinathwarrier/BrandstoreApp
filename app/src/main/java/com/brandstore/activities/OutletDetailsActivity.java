package com.brandstore.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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


    TagPriceListViewAdapter mTagPriceListViewAdapter;
    ArrayList<TagPrice> tagPriceArrayList = new ArrayList();

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_details);
        String id=getIntent().getStringExtra("id");

        // get the list of UI Elements' Objects

        outletimage=(ImageView)findViewById(R.id.outlet_image);
        outletname=(TextView)findViewById(R.id.outletDetails_brandName);
        floor=(TextView)findViewById(R.id.outletDetails_floorName);
        hubname= (TextView) findViewById(R.id.outletDetails_hubName);
        description= (TextView) findViewById(R.id.outletDetails_description);
        website= (TextView) findViewById(R.id.outletDetails_website);
        tagPriceLinearLayout = (LinearLayout) findViewById(R.id.outletDetails_tagAndPrice_linearLayout);



        //tagprice=(ListView)findViewById(R.id.tag_and_price);
        //mTagPriceListViewAdapter=new TagPriceListViewAdapter(tagPriceArrayList,this);
        //tagprice.setAdapter(mTagPriceListViewAdapter);


        OutletDetailsAsyncTask mOutletDetailsAsyncTask=new OutletDetailsAsyncTask(
                outletimage,
                outletname,
                floor,
                hubname,
                id,
                description,
                website,
                tagPriceLinearLayout,
                this);
        mOutletDetailsAsyncTask.execute();

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
