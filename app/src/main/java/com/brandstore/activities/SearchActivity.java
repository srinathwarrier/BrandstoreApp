package com.brandstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.brandstore.R;
import com.brandstore.SearchResultsAsyncTask;
import com.brandstore.adapters.ResultsListViewAdapter;
import com.brandstore.entities.SearchResults;
import com.brandstore.fragments.NavigationDrawerFragment;
import com.brandstore.views.SearchBox;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    ResultsListViewAdapter mResultsAdapter;
    SearchBox mEdit;
    ListView mResultList;
    NavigationDrawerFragment drawerFragment;
    ArrayList<SearchResults> mSearchResult = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);

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
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();
        searchView.setQueryHint("Type brand store or product category");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (searchView.getQuery().length() < 1) {
                    mSearchResult.clear();
                    mResultsAdapter.notifyDataSetChanged();
                } else {
                    SearchResultsAsyncTask mSearchAsyncTask = new SearchResultsAsyncTask(s.toString(), mResultsAdapter, mSearchResult);
                    mSearchAsyncTask.execute();
                    mResultList.setVisibility(View.VISIBLE);
                }

                return false;

            }
        });
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
}
