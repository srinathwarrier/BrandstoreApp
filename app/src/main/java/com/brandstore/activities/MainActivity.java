package com.brandstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.brandstore.AnalyticsSampleApp;
import com.brandstore.R;
import com.brandstore.adapters.CategoryGridViewAdapter;
import com.brandstore.fragments.NavigationDrawerFragment;
import com.brandstore.views.SearchBox;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends ActionBarActivity {

    GridView mCategoryGridView;

    int CategoryImages[] = {R.drawable.accessories, R.drawable.denims, R.drawable.footwear, R.drawable.handbags, R.drawable.kidswear,
            R.drawable.luggage,R.drawable.mensethnic,R.drawable.sportswear,R.drawable.unisex,
            R.drawable.watcheseyewear,R.drawable.westernmen,R.drawable.westernunisex,R.drawable.westernwomen,R.drawable.womensethnic};
    String ids[]={"3","5","7","8","13","16","17","20","22","23","24","25","26","27"};
    String CategoryNames[]={"Accessories and Gifts", "Denims","Footwear", "Hand Bags", "Kid's Wear", "Luggage", "Men's Ethnic Wear", "Sportswear","Unisex Apparel",
            "Watches and Eyewear", "Western Men's Apparel","Western Unisex Apparel", "Western Women's Apparel", "Women's Ethnic Wear" };


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get tracker.
        Tracker t = ((AnalyticsSampleApp) getApplication()).getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_main);


        //TODO Need to modify using Toolbar
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       /*Three Lines of code needed on every activity using the navigation drawer*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout));


        mCategoryGridView = (GridView) findViewById(R.id.category_grid_view);
        mCategoryGridView.setAdapter(new CategoryGridViewAdapter(this,CategoryImages,CategoryNames));
        SearchBox mEdit = (SearchBox) findViewById(R.id.search_box_edit_text);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        mCategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",CategoryNames[position]);
                bundle.putString("id",getCategoryIDs(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });





    }

    public String getCategoryIDs(int position)
    {
        String s = "1";
        switch (position){
            case 0:  s = "2";  break; // Accessories
            case 1:  s = "52,53,54,151";  break; // Denims -> Total = 27
            case 2:  s = "9";  break; // Footwear
            case 3:  s = "71";  break; // Hand bags
            case 4:  s = "151,153,155,156,177";  break; // Kids wear
            case 5:  s = "4";  break; // Luggage
            case 6:  s = "88";  break; // Men's ethnic wear
            case 7:  s = "8";  break; // Sportswear
            case 8:  s = "2,149,152,200,50,41";  break; // Unisex apparel
            case 9: s = "37,39,203";  break; // Watches & Eye wear
            case 10: s = "65";  break; // Western men's apparel
            case 11: s = "1";  break; // Western unisex apparel
            case 12: s = "89,102,93,94";  break; // Western women's apparel
            case 13: s = "88,90,80,81,115,116";  break; // Women's ethnic wear

            default:
                break;
        }
        return s;
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    }*/
}
