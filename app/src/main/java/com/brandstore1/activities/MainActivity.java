package com.brandstore1.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.brandstore1.AnalyticsSampleApp;
import com.brandstore1.R;
import com.brandstore1.adapters.CategoryGridViewAdapter;
import com.brandstore1.fragments.NavigationDrawerFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.nearby.connection.Connections;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    GridView mCategoryGridView;
    int CategoryImages[] = {R.drawable.accessories, R.drawable.denims, R.drawable.footwear, R.drawable.handbags, R.drawable.kidswear,
            R.drawable.luggage, R.drawable.mensethnic, R.drawable.sportswear, R.drawable.unisex,
            R.drawable.watcheseyewear, R.drawable.westernmen, R.drawable.westernunisex, R.drawable.westernwomen, R.drawable.womensethnic};

    //region test
    String CategoryNames[] = {"ACCESSORIES AND GIFTS", "DENIMS", "FOOTWEAR", "HAND BAGS", "KID'S WEAR", "LUGGAGE", "MEN'S ETHNIC WEAR", "SPORTSWEAR", "UNISEX APPAREL",
            "WATCHES AND EYEWEAR", "WESTERN MEN'S APPAREL", "WESTERN UNISEX APPAREL", "WESTERN WOMEN'S APPAREL", "WOMEN'S ETHNIC WEAR"};
    //endregion


    // Navigation drawer :
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;


    // SearchView
    SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        NavigationDrawerFragment drawerFragment;

        // Toolbar methods
        setUpToolbar();


        //NavigationDrawer methods:
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                boolean returnValue = true;
                switch (menuItem.getItemId()) {
                    case R.id.drawer_item_categories:
                        mCurrentSelectedPosition = 0;
                        returnValue = true;
                        break;
                    case R.id.drawer_item_favorite:
                        mCurrentSelectedPosition = 1;
                        returnValue = true;
                        break;
                    case R.id.drawer_item_sale:
                        mCurrentSelectedPosition = 2;
                        returnValue = true;
                        break;
                    default:
                        returnValue = true;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return returnValue;

            }
        });



        // Get tracker.
        Tracker t = ((AnalyticsSampleApp) getApplication()).getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());


        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mCategoryGridView = (GridView) findViewById(R.id.category_grid_view);
        mCategoryGridView.setAdapter(new CategoryGridViewAdapter(this, CategoryImages, CategoryNames));


        mCategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", CategoryNames[position]);
                bundle.putString("id", getCategoryIDs(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    public String getCategoryIDs(int position) {
        String s = "1";
        switch (position) {
            case 0:
                s = "2";
                break; // Accessories
            case 1:
                s = "52,53,54,151";
                break; // Denims -> Total = 27
            case 2:
                s = "9";
                break; // Footwear
            case 3:
                s = "71";
                break; // Hand bags
            case 4:
                s = "151,153,155,156,177";
                break; // Kids wear
            case 5:
                s = "4";
                break; // Luggage
            case 6:
                s = "88";
                break; // Men's ethnic wear
            case 7:
                s = "8";
                break; // Sportswear
            case 8:
                s = "2,149,152,200,50,41";
                break; // Unisex apparel
            case 9:
                s = "37,39,203";
                break; // Watches & Eye wear
            case 10:
                s = "65";
                break; // Western men's apparel
            case 11:
                s = "1";
                break; // Western unisex apparel
            case 12:
                s = "89,102,93,94";
                break; // Western women's apparel
            case 13:
                s = "88,90,80,81,115,116";
                break; // Women's ethnic wear
            default:
                break;
        }
        return s;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search1);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        if(searchView!=null){
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("Let's go shopping!");
            //searchView.setOnQueryTextListener(this);
            /*searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.clearFocus();
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                }
            });*/
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        searchView.clearFocus();
                        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intent);
                    }
                }
            });
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.clearFocus();
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                }
            });
        }
        // Inflate the menu; this adds items to the action bar if it is present.

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

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }


    /*
        Navigation Drawer methods
     */
    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mToolbar.setNavigationIcon(null);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }
    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchView.clearFocus();
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
        return false;
    }
}
