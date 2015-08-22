package com.brandstore1.activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
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
    MenuItem favoriteMenuItem;
    MenuItem saleMenuItem;
    OutletListAdapter mOutletListAdapter;

    public enum OutletListType{
        CLICKED_ON_TAG , CLICKED_ON_CATEGORY ,SEARCHED_QUERY , ALL_FAVORITE , ALL_ON_SALE ,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_list);

        /*
            Toolbar setup
         */
        toolbar = (Toolbar) findViewById(R.id.outletlisttoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        /*
            Bundle setup >
            Get title for toolbar
            Get parameter to decide call in AsyncTask
         */
        Bundle bundle = getIntent().getExtras();
        String query = bundle.getString("name");
        String id = bundle.getString("id");
        OutletListType outletListType = (OutletListType)bundle.get("type");


        /*
            UniversalImageLoader setup
         */
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

        /*
            ListView setup
         */
        TextView emptyView = (TextView) findViewById(R.id.outlet_list_empty_textView);
        outletListView = (ListView) findViewById(R.id.outlet_list_list_view);
        outletListView.setEmptyView(emptyView);

        /*
            Adapter setup
         */
        mOutletListAdapter = new OutletListAdapter(outletArrayList, this, toolbar, emptyView);
        outletListView.setAdapter(mOutletListAdapter);

        /*
            AsyncTask setup
         */
        // Basic (ArrayList , Adapter , this )
        // UI Elements : ( toolbar , emptyView )
        // Parameters : query , id , outletListType

        OutletListAsyncTask mOutletListAsyncTask = new OutletListAsyncTask(outletArrayList, query, mOutletListAdapter, id, emptyView, toolbar,this,outletListType );
        mOutletListAsyncTask.execute();


        outletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                intent.putExtra("id", outletArrayList.get(position).getId());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outlet_list, menu);

        // Initialize the menuItem variables
        favoriteMenuItem = menu.findItem(R.id.display_favorites);
        saleMenuItem = menu.findItem(R.id.on_sale);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                //noinspection SimplifiableIfStatement
                onBackPressed();
                return true;
            case R.id.display_favorites:
                onFavoriteMenuItemClicked(!item.isChecked());
                break;
            case R.id.on_sale:
                onSaleMenuItemClicked(!item.isChecked());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFavoriteMenuItemClicked(boolean toBeSelected){
        mOutletListAdapter.resetData();
        if (toBeSelected) {
            //filter favorites
            favoriteMenuItem.setChecked(true);
            favoriteMenuItem.setIcon(android.R.drawable.btn_star_big_on);
            mOutletListAdapter.getFilter().filter("X");
        } else {
            favoriteMenuItem.setChecked(false);
            favoriteMenuItem.setIcon(android.R.drawable.btn_star_big_off);
            mOutletListAdapter.getFilter().filter("Y");
        }
    }

    public void onSaleMenuItemClicked(boolean toBeSelected){
        mOutletListAdapter.resetData();
        if (toBeSelected) {
            //filter OnSale Outlets
            saleMenuItem.setChecked(true);
            saleMenuItem.setIcon(R.drawable.sale_on);
            mOutletListAdapter.getSaleFilter().filter("X");

        } else {
            saleMenuItem.setChecked(false);
            saleMenuItem.setIcon(R.drawable.sale);
            mOutletListAdapter.getSaleFilter().filter("Y");

        }
    }

}
