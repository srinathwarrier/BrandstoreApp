package com.brandstore1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.brandstore1.BrandstoreApplication;
import com.brandstore1.R;
import com.brandstore1.adapters.CategoryGridViewAdapter;
import com.brandstore1.entities.User;
import com.brandstore1.fragments.NavigationDrawerFragment;
import com.brandstore1.utils.AnalyticsConstants;
import com.brandstore1.utils.GAUtils;
import com.brandstore1.utils.MySharedPreferences;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    // Use this as tag in Log . Helps to easily identify which class the log is being shown from.
    // Example: Log.d(DEBUG_TAG, "In onResume Method");
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CATEGORY_ITEM = "category_item_home_page";

    GridView mCategoryGridView;

    // SearchView
    SearchView searchView;

    // Navigation drawer :
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;

    int categoryImages[] = {R.drawable.category_accessories, R.drawable.category_denims, R.drawable.category_footwear, R.drawable.category_handbags, R.drawable.category_kidswear,
            R.drawable.category_luggage, R.drawable.category_mensethnic, R.drawable.category_sportswear,
            R.drawable.category_watcheseyewear, R.drawable.category_westernmen, R.drawable.category_westernunisex, R.drawable.category_westernwomen, R.drawable.category_womensethnic};

    //region test
    String categoryNames[] = {"ACCESSORIES AND GIFTS", "DENIMS", "FOOTWEAR", "HAND BAGS", "KID'S WEAR", "LUGGAGE", "MEN'S ETHNIC WEAR", "SPORTSWEAR",
            "WATCHES AND EYEWEAR", "WESTERN MEN'S APPAREL", "WESTERN UNISEX APPAREL", "WESTERN WOMEN'S APPAREL", "WOMEN'S ETHNIC WEAR"};
    String categoryNamesCamelCase[] = {"Accessories and Gifts", "Denims", "Footwear", "Hand bags", "Kid's wear", "Luggage", "Men's ethnic wear", "Sportswear",
            "Watches and Eyewear", "Westwern men's apparel", "Western unisex apparel", "Western women's apparel", "Women's ethnic wear"};
    //endregion

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
        mUserLearnedDrawer = Boolean.valueOf(MySharedPreferences.getUserLearnedDrawer(this));
        //readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false")

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        setUpNavDrawer();
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //menuItem.setChecked(true);
                Menu menu = mNavigationView.getMenu();
                menu.getItem(0).setChecked(true); //TODO: Later, remove this.

                boolean returnValue = true;
                switch (menuItem.getItemId()) {
                    case R.id.drawer_item_categories:
                        mCurrentSelectedPosition = 0;
                        returnValue = true;
                        break;
                    case R.id.drawer_item_favorite:
                        goToOutletListScreenAllFavorites();
                        mCurrentSelectedPosition = 1;
                        returnValue = true;
                        break;
                    case R.id.drawer_item_sale:
                        goToOutletListScreenAllOnSale();
                        mCurrentSelectedPosition = 2;
                        returnValue = true;
                        break;
                    case R.id.drawer_item_takeMeThere:
                        goToTakeMeThereScreen();
                        mCurrentSelectedPosition = 3;
                        returnValue = true;
                        break;
                    default:
                        returnValue = true;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return returnValue;
            }
        });

        String userJsonObjectString = MySharedPreferences.getUserJsonObjectString(this);
        Gson gson = new Gson();
        User user = gson.fromJson(userJsonObjectString, User.class);
        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(user.getName());

        // Get tracker.
        Tracker t = ((BrandstoreApplication) getApplication()).getTracker(BrandstoreApplication.TrackerName.APP_TRACKER);
        t.setScreenName(TAG);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mCategoryGridView = (GridView) findViewById(R.id.category_grid_view);
        mCategoryGridView.setAdapter(new CategoryGridViewAdapter(this, categoryImages, categoryNames));
        mCategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToOutletListScreen(position);
            }
        });
    }

    public void goToOutletListScreen(int position) {
        Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", categoryNamesCamelCase[position]);
        bundle.putString("id", getCollectionIDs(position));
        bundle.putSerializable("type", OutletListActivity.OutletListType.CLICKED_ON_COLLECTION);
        intent.putExtras(bundle);
        GAUtils.sendEvent(this, AnalyticsConstants.UI_CATEGORY,
                CATEGORY_ITEM, position);
        startActivity(intent);
    }

    public void goToOutletListScreenAllFavorites() {
        Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", getString(R.string.nav_item_favorite));
        bundle.putSerializable("type", OutletListActivity.OutletListType.ALL_FAVORITE);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToOutletListScreenAllOnSale() {
        Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", getString(R.string.nav_item_sale));
        bundle.putSerializable("type", OutletListActivity.OutletListType.ALL_ON_SALE);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToTakeMeThereScreen() {
        Intent intent = new Intent(getApplicationContext(), TakeMeThereActivity.class);
        intent.putExtra("type", TakeMeThereActivity.TMT_type.TO_UNKNOWN);
        startActivity(intent);
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
                s = "37,39,203";
                break; // Watches & Eye wear
            case 9:
                s = "65";
                break; // Western men's apparel
            case 10:
                s = "1";
                break; // Western unisex apparel
            case 11:
                s = "89,102,93,94";
                break; // Western women's apparel
            case 12:
                s = "88,90,80,81,115,116";
                break; // Women's ethnic wear
            default:
                break;
        }
        return s;
    }

    public String getCollectionIDs(int position) {
        String s = "1";
        switch (position) {
            case 0:
                s = "1";
                break; // Accessories
            case 1:
                s = "2";
                break; // Denims -> Total = 27
            case 2:
                s = "3";
                break; // Footwear
            case 3:
                s = "4";
                break; // Hand bags
            case 4:
                s = "5";
                break; // Kids wear
            case 5:
                s = "6";
                break; // Luggage
            case 6:
                s = "7";
                break; // Men's ethnic wear
            case 7:
                s = "8";
                break; // Sportswear
            case 8:
                s = "9";
                break; // Watches & Eye wear
            case 9:
                s = "10";
                break; // Western men's apparel
            case 10:
                s = "11";
                break; // Western unisex apparel
            case 11:
                s = "12";
                break; // Western women's apparel
            case 12:
                s = "13";
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
        if (searchView != null) {
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint(getResources().getString(R.string.search_box_hint));
            searchView.setFocusable(false);
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
                    if (hasFocus) {
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
        } else if (id == android.R.id.home) {
            Log.d(TAG, "action bar clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    // Navigation Drawer methods
    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
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
            //saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
            MySharedPreferences.setUserLearnedDrawer(this, "true");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, 0);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(0).setChecked(true); //TODO: Later, change to getItem(mCurrentSelectedPosition)
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
