package com.example.ravi.myapplication;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    ResultsListViewAdapter mResultsAdapter;
    SearchBoxEditText mEdit;
    ListView mResultList;
    NavigationDrawerFragment drawerFragment;
    ArrayList<SearchResultHelper> mSearchResult = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout));


        mEdit = (SearchBoxEditText) findViewById(R.id.search_box_results);
        mResultList = (ListView) findViewById(R.id.list_view_results);
        mResultList.setVisibility(View.INVISIBLE);


        mResultsAdapter = new ResultsListViewAdapter(mSearchResult, this);
        mResultList.setAdapter(mResultsAdapter);


        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEdit.getText().length() < 2) {
                    mResultList.setVisibility(View.INVISIBLE);
                } else {

                    SearchResultsAsyncTask mSearchAsyncTask = new SearchResultsAsyncTask(s.toString(), mResultsAdapter, mSearchResult);
                    mSearchAsyncTask.execute();
                    mResultList.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
