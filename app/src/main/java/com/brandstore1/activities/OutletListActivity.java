package com.brandstore1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandstore1.OutletListAsyncTask;
import com.brandstore1.R;
import com.brandstore1.adapters.OutletListAdapter;
import com.brandstore1.entities.Outlet;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;


public class OutletListActivity extends ActionBarActivity {
    TextView category;
    ListView outletListView;
    ArrayList<Outlet> outletArrayList = new ArrayList();
    Toolbar toolbar;
    CheckBox disFav;
    OutletListAdapter mOutletListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_outlet_list);
        toolbar = (Toolbar) findViewById(R.id.outletlisttoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Bundle bundle = getIntent().getExtras();
        String query = bundle.getString("name");
        String id = bundle.getString("id");
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
        TextView emptyView = (TextView) findViewById(R.id.outlet_list_empty_textView);

        outletListView = (ListView) findViewById(R.id.outlet_list_list_view);
        outletListView.setEmptyView(emptyView);


        mOutletListAdapter = new OutletListAdapter(outletArrayList, this, toolbar);

        outletListView.setAdapter(mOutletListAdapter);

        OutletListAsyncTask mOutletListAsyncTask = new OutletListAsyncTask(outletArrayList, query, mOutletListAdapter, id, emptyView, toolbar,this);

        mOutletListAsyncTask.execute();


        outletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                intent.putExtra("id", outletArrayList.get(position).getId());
                startActivity(intent);
            }
        });


        displayFavorites();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outlet_list, menu);
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

    public void displayFavorites(){

        disFav = (CheckBox)findViewById(R.id.display_favorites);
        disFav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mOutletListAdapter.resetData();
                if (((CheckBox)view).isChecked()) {

                    //filter favorites

                    mOutletListAdapter.getFilter().filter("X");


                }
                else {
                    mOutletListAdapter.getFilter().filter("Y");
                }


            }
        });

    }




}
