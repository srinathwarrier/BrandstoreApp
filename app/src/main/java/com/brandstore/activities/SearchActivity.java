package com.brandstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.brandstore.fragments.NavigationDrawerFragment;
import com.brandstore.views.SearchBox;
import com.brandstore.entities.SearchResults;
import com.brandstore.SearchResultsAsyncTask;
import com.brandstore.R;
import com.brandstore.adapters.ResultsListViewAdapter;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    ResultsListViewAdapter mResultsAdapter;
    SearchBox mEdit;
    ListView mResultList;
    NavigationDrawerFragment drawerFragment;
    ArrayList<SearchResults> mSearchResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mEdit = (SearchBox) findViewById(R.id.search_box_results);
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

        mResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("category")||mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("hub")) {
                    Intent intent = new Intent(getApplicationContext(), OutletListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("name",mSearchResult.get(position).getName().toString());
                    bundle.putString("id",mSearchResult.get(position).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mSearchResult.get(position).getCategory().toString().equalsIgnoreCase("outlet")) {
                    Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                    intent.putExtra("id",mSearchResult.get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }

/*

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
*/
    @Override
    protected void onResume() {
        super.onResume();
        mResultsAdapter = new ResultsListViewAdapter(mSearchResult, this);
        mResultList.setAdapter(mResultsAdapter);
    }
}
