package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.brandstore1.R;
import com.brandstore1.adapters.ResultsListViewAdapter;
import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;
import com.brandstore1.entities.SearchResults;
import com.brandstore1.fragments.NavigationDrawerFragment;

import java.util.ArrayList;

/**
 * Created by Sonika on 8/30/2015.
 */
public class SearchTMTActivity extends ActionBarActivity {
    ResultsListViewAdapter mResultsAdapter;
    //SearchBox mEdit;
    ListView mResultList;
    NavigationDrawerFragment drawerFragment;
    ArrayList<SearchResults> mSearchResult = new ArrayList<>();
    Toolbar toolbar;
    Context mContext;
    SearchView searchView;
    SQLiteDatabase sqLiteDatabase;
    EditText from_outlet;
    String to_from;
    String hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tmt);

        sqLiteDatabase = openOrCreateDatabase("brandstoreDB",MODE_PRIVATE,null);
        //UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
        //updateSuggestionsAsyncTask.execute();
        toolbar = (Toolbar) findViewById(R.id.takemetheretoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setTitle("Take Me There");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        to_from = bundle.getString("search");
        hint = bundle.getString("hint");
        from_outlet =(EditText) findViewById(R.id.tmt_outlet);
        from_outlet.setHint(hint);

        mResultList = (ListView) findViewById(R.id.list_view_results);
        mResultList.setVisibility(View.INVISIBLE);


        mResultsAdapter = new ResultsListViewAdapter(mSearchResult, this);
        mResultList.setAdapter(mResultsAdapter);
        mResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryString = mSearchResult.get(position).getCategory();
                if (categoryString.equalsIgnoreCase("outlet") || categoryString.equalsIgnoreCase("others")) {
                    Intent intent=new Intent();
                    intent.putExtra("outlet_name",mSearchResult.get(position).getName());
                    intent.putExtra("outlet_id",mSearchResult.get(position).getId());
                    intent.putExtra("TO_FROM",to_from);
                    setResult(2, intent);
                    finish();//finishing activity

                }
            }
        });


        from_outlet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (from_outlet.getText().length() < 1) {
                    // mResultList.setVisibility(View.INVISIBLE);
                    mSearchResult.clear();
                    SearchResults obj = new SearchResults();
                    obj.setId("0");
                    obj.setName("Start typing to view suggestions");
                    obj.setCategory(" ");
                    mSearchResult.add(obj);
                    mResultsAdapter.notifyDataSetChanged();
                } else {

                    String s1 = "%" + s + "%";
                    Cursor res = sqLiteDatabase.rawQuery("Select * from Suggestions where name like '" + s1 + "'AND category IN ('outlet','others');", null);
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
                            mSearchResult.add(obj);
                            res.moveToNext();

                        }
                    }

                    mResultsAdapter.notifyDataSetChanged();
                    mResultList.setVisibility(View.VISIBLE);
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_take_me_there, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement

        if(id==android.R.id.home){
           onBackPressed();
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
