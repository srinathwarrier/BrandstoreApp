package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.brandstore1.BrandstoreApplication;
import com.brandstore1.R;
import com.brandstore1.adapters.RecentPopularSuggestionsAdapter;
import com.brandstore1.adapters.ResultsListViewAdapter;
import com.brandstore1.asynctasks.RecentPopularSuggestionsAsyncTask;
import com.brandstore1.entities.SearchResults;
import com.brandstore1.fragments.NavigationDrawerFragment;
import com.brandstore1.interfaces.RecentPopularSuggestionsAsyncResponse;
import com.brandstore1.utils.MySQLiteDatabase;
import com.brandstore1.utils.MySqliteDatabaseContract;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity
        implements  SearchView.OnQueryTextListener, RecentPopularSuggestionsAsyncResponse{

    private static final String TAG = SearchActivity.class.getSimpleName();

    // Suggestion search results
    ResultsListViewAdapter mResultsAdapter;
    ListView lvResultList;
    ArrayList<SearchResults> mSearchResult = new ArrayList<>();

    // Recent Suggestion
    RecentPopularSuggestionsAdapter mRecentPopularSuggestionsAdapter;
    ListView lvRecentPopularList;
    ArrayList<SearchResults> mRecentPopularSearchResult = new ArrayList<>();

    Toolbar toolbar;
    Context mContext;
    SearchView searchView;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        MySQLiteDatabase mySQLiteDatabase = new MySQLiteDatabase(mContext);
        sqLiteDatabase = mySQLiteDatabase.getReadableDatabase();
        //UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
        //updateSuggestionsAsyncTask.execute();

        // Get tracker.
        Tracker t = ((BrandstoreApplication) getApplication()).getTracker(BrandstoreApplication.TrackerName.APP_TRACKER);
        // Send a screen view.
        t.setScreenName(TAG);
        t.send(new HitBuilders.ScreenViewBuilder().build());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvRecentPopularList = (ListView) findViewById(R.id.recent_popular_list_view);
        lvRecentPopularList.setVisibility(View.VISIBLE);
        mRecentPopularSuggestionsAdapter = new RecentPopularSuggestionsAdapter(mRecentPopularSearchResult,this);
        lvRecentPopularList.setAdapter(mRecentPopularSuggestionsAdapter);
        lvRecentPopularList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRecentPopularSearchResult.get(position).getCategory().toString().equalsIgnoreCase("category") || mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("hub")) {
                    Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mSearchResult.get(position).getName().toString());
                    bundle.putString("id", mSearchResult.get(position).getId());
                    bundle.putSerializable("type", OutletListActivity.OutletListType.CLICKED_ON_TAG);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("outlet")) {
                    Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                    intent.putExtra("id", mSearchResult.get(position).getId());
                    startActivity(intent);
                }
            }
        });

        View view = View.inflate(mContext, R.layout.recent_suggestion_header, null);
        lvRecentPopularList.addHeaderView(view);

        RecentPopularSuggestionsAsyncTask recentPopularSuggestionsAsyncTask = new RecentPopularSuggestionsAsyncTask(mContext);
        recentPopularSuggestionsAsyncTask.delegate = this;
        recentPopularSuggestionsAsyncTask.execute();

        lvResultList = (ListView) findViewById(R.id.list_view_results);
        lvResultList.setVisibility(View.INVISIBLE);
        mResultsAdapter = new ResultsListViewAdapter(mSearchResult, this);
        lvResultList.setAdapter(mResultsAdapter);
        lvResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("category") || mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("hub")) {
                    Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mSearchResult.get(position).getName().toString());
                    bundle.putString("id", mSearchResult.get(position).getId());
                    bundle.putSerializable("type", OutletListActivity.OutletListType.CLICKED_ON_TAG);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("outlet")) {
                    Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                    intent.putExtra("id", mSearchResult.get(position).getId());
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        if (searchView != null) {
            searchView.setIconifiedByDefault(false);
            searchView.requestFocusFromTouch();
            searchView.setQueryHint("Type brand or product");
            searchView.setOnQueryTextListener(this);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        mResultsAdapter = new ResultsListViewAdapter(mSearchResult, this);
        lvResultList.setAdapter(mResultsAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Hide the Recent and Popular listviews
        lvRecentPopularList.setVisibility(View.GONE);


        // Get tracker.
        Tracker t = ((BrandstoreApplication) getApplication()).getTracker(BrandstoreApplication.TrackerName.APP_TRACKER);
        // Build and Send an event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.ga_category_search_activity))
                .setAction(getString(R.string.ga_action_search_suggestion))
                .setLabel(newText)
                .build());

        if (searchView.getQuery().length() < 1) {
            mSearchResult.clear();
            SearchResults obj = new SearchResults();
            obj.setId("0");
            obj.setName("Start typing to view suggestions");
            obj.setCategory(" ");
            mSearchResult.add(obj);
            mResultsAdapter.notifyDataSetChanged();
        } else {
            String s = "%" + newText + "%";
            String[] tableColumns = new String[] {
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_ID,
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_TITLE,
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_CATEGORY,
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_FLOOR_NAME
            };
            String whereClause = MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_TITLE+
                    " like ? "+
                    " AND "+
                    MySqliteDatabaseContract.TableSuggestion.COLUMN_NAME_CATEGORY+
                    " NOT IN ('others')";
            String[] whereArgs = { s };
            Cursor res = sqLiteDatabase.query(MySqliteDatabaseContract.TableSuggestion.TABLE_NAME,
                    tableColumns,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null);

            res.moveToFirst();
            if (res.getCount() == 0) {
                mSearchResult.clear();
                SearchResults obj = new SearchResults();
                obj.setId("0");
                obj.setName("No results found");
                obj.setCategory(" ");
                mSearchResult.add(obj);
            } else {

                mSearchResult.clear();
                while (res.isAfterLast() == false) {

                    SearchResults obj = new SearchResults();
                    obj.setId(res.getString(0));
                    obj.setName(res.getString(1));
                    obj.setCategory(res.getString(2));
                    obj.setShowFloorName(false);
                    mSearchResult.add(obj);
                    res.moveToNext();

                }
            }

            //SearchResultsAsyncTask mSearchAsyncTask = new SearchResultsAsyncTask(newText, mResultsAdapter, mSearchResult, mContext);
            //mSearchAsyncTask.execute();
            mResultsAdapter.notifyDataSetChanged();
            lvResultList.setVisibility(View.VISIBLE);
        }

        return false;
    }

    @Override
    public void updateRecentListView() {
        // fetch Recent table into mRecentPopularSearchResult
        String[] tableColumns = new String[] {
                MySqliteDatabaseContract.TableRecentSuggestion.COLUMN_NAME_ID,
                MySqliteDatabaseContract.TableRecentSuggestion.COLUMN_NAME_TITLE,
                MySqliteDatabaseContract.TableRecentSuggestion.COLUMN_NAME_CATEGORY,
                MySqliteDatabaseContract.TableRecentSuggestion.COLUMN_NAME_FLOOR_NAME
        };

        Cursor res = sqLiteDatabase.query(MySqliteDatabaseContract.TableRecentSuggestion.TABLE_NAME,
                tableColumns,
                null,
                null,
                null,
                null,
                null);

        res.moveToFirst();
        if (res.getCount() == 0) {
            mRecentPopularSearchResult.clear();
            SearchResults obj = new SearchResults();
            obj.setId("0");
            obj.setName("No recent results found");
            obj.setCategory(" ");
            mRecentPopularSearchResult.add(obj);
        } else {
            mRecentPopularSearchResult.clear();
            while (res.isAfterLast() == false) {
                SearchResults obj = new SearchResults();
                obj.setId(res.getString(0));
                obj.setName(res.getString(1));
                obj.setCategory(res.getString(2));
                obj.setShowFloorName(false);
                mRecentPopularSearchResult.add(obj);
                res.moveToNext();

            }
        }


        mRecentPopularSuggestionsAdapter.notifyDataSetChanged();
    }
}
