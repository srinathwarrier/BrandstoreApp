package com.brandstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.brandstore.fragments.NavigationDrawerFragment;
import com.brandstore.views.SearchBox;
import com.brandstore.R;
import com.brandstore.adapters.CategoryGridViewAdapter;

public class MainActivity extends ActionBarActivity {

    GridView mCategoryGridView;

    int CategoryImages[] = {R.drawable.accessories, R.drawable.books, R.drawable.denims, R.drawable.electronics, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10,
            R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,
            R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10,R.drawable.p10};
    String ids[]={"3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"};
    String CategoryNames[]={"Accessories and Gifts","Books and Stationery","Denims","Electronics","Footwear","Hand Bags","Health / Saloon / Spa ","Home Decor & Art","Hypermarket",
            "Jewellery","Kid's Wear","Lingerie & Innerwear","Liquor Boutique","Luggage","Men's Ethnic Wear","Music Instruments & Professional Audio","Services","Sportswear","Toys",
    "Unisex Apparel","Watches and Eyewear","Western Men's Apparel","Western Unisex Apparel","Western Women's Apparel","Women's Ethnic Wear"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                bundle.putString("id",ids[position]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
