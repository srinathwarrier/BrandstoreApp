package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.brandstore1.R;
import com.brandstore1.SearchResultsAsyncTask;
import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;
import com.brandstore1.adapters.ResultsListViewAdapter;
import com.brandstore1.entities.SearchResults;
import com.brandstore1.fragments.NavigationDrawerFragment;
//import com.brandstore1.views.SearchBox;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{
    ResultsListViewAdapter mResultsAdapter;
    //SearchBox mEdit;
    ListView mResultList;
    NavigationDrawerFragment drawerFragment;
    ArrayList<SearchResults> mSearchResult = new ArrayList<>();
    Toolbar toolbar;
    Context mContext;
    SearchView searchView;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sqLiteDatabase = openOrCreateDatabase("brandstoreDB",MODE_PRIVATE,null);
        UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
        updateSuggestionsAsyncTask.execute();

        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);
        mContext=this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        //drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout));


        //mEdit = (SearchBox) findViewById(R.id.search_box_results);
        mResultList = (ListView) findViewById(R.id.list_view_results);
        mResultList.setVisibility(View.INVISIBLE);


        mResultsAdapter = new ResultsListViewAdapter(mSearchResult, this);
        mResultList.setAdapter(mResultsAdapter);

/*
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEdit.getText().length() < 1) {
                   // mResultList.setVisibility(View.INVISIBLE);
                    mSearchResult.clear();
                } else {

                    SearchResultsAsyncTask mSearchAsyncTask = new SearchResultsAsyncTask(s.toString(), mResultsAdapter, mSearchResult);
                    mSearchAsyncTask.execute();
                    mResultList.setVisibility(View.VISIBLE);
                }
            }
        });
*/
        mResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        /*// Get tracker.
        Tracker t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("screen1");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        if(searchView!=null){
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
        mResultList.setAdapter(mResultsAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (searchView.getQuery().length() < 1) {
            mSearchResult.clear();
            SearchResults obj= new SearchResults();
            obj.setId("0");
            obj.setName("Start typing to view suggestions");
            obj.setCategory(" ");
            mSearchResult.add(obj);
            mResultsAdapter.notifyDataSetChanged();
        } else {
            String s="%" + newText+ "%";
            Cursor res= sqLiteDatabase.rawQuery("Select * from Suggestions where name like '" +s +"';", null);
            res.moveToFirst();
            if(res.getCount()==0)
            {
                mSearchResult.clear();
                SearchResults obj= new SearchResults();
                obj.setId("0");
                obj.setName("No results found");
                obj.setCategory(" ");
                mSearchResult.add(obj);
            }
            else {

                mSearchResult.clear();
                while (res.isAfterLast() == false) {

                    SearchResults obj = new SearchResults();
                    obj.setId(res.getString(0));
                    obj.setName(res.getString(1));
                    obj.setCategory(res.getString(2));
                    mSearchResult.add(obj);
                    res.moveToNext();

                }
            }

            //SearchResultsAsyncTask mSearchAsyncTask = new SearchResultsAsyncTask(newText, mResultsAdapter, mSearchResult, mContext);
            //mSearchAsyncTask.execute();
            mResultsAdapter.notifyDataSetChanged();
            mResultList.setVisibility(View.VISIBLE);
        }

        return false;
    }
}
