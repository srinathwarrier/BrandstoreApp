package com.brandstore1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brandstore1.R;
import com.brandstore1.TakeMeThereAsyncTask;
import com.brandstore1.adapters.TakeMeThereAdapter;

import java.util.ArrayList;

/**
 * Created by Sonika on 8/16/2015.
 */
public class TakeMeThereActivity extends ActionBarActivity{
    ListView pathListView;
    ListView outletListView;
    ArrayList<String> pathArrayList = new ArrayList();
    ArrayList<String> outletArrayList = new ArrayList();
    Toolbar toolbar;
    Menu menu;
    EditText to;
    TakeMeThereAdapter takeMeThereAdapter;
    private EditText filterFrom;
    private ArrayAdapter<String> listAdapter;
    String to_id;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_me_there);

        toolbar = (Toolbar) findViewById(R.id.takemetheretoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setTitle("Take Me There");

        emptyView = (TextView) findViewById(R.id.tmt_list_empty_textView);

        pathListView = (ListView) findViewById(R.id.tmt_list_view);
        pathListView.setEmptyView(emptyView);
        Bundle bundle = getIntent().getExtras();
        to_id = bundle.getString("id");
        String name = bundle.getString("name");

        to = (EditText) findViewById(R.id.tmt_to);
        to.setText(name);

        filterFrom = (EditText)findViewById(R.id.tmt_from);
        //outletListView = (ListView) findViewById(R.id.outlet_list);
        takeMeThereAdapter = new TakeMeThereAdapter(pathArrayList, this, toolbar, emptyView);

        pathListView.setAdapter(takeMeThereAdapter);
        //outletListView.setAdapter(takeMeThereAdapter);

        //TakeMeThereAsyncTask pathListAsyncTask = new TakeMeThereAsyncTask(takeMeThereAdapter, to_id,  emptyView, toolbar, this);
        //pathListAsyncTask.execute();


        filterFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Always use a TextKeyListener when clearing a TextView to prevent android
                    // warnings in the log
                    //TextKeyListener.clear((filterFrom).getText());
                    filterFrom.clearFocus();
                    Intent intent = new Intent(getApplicationContext(), SearchTMTActivity.class);
                    startActivityForResult(intent, 2);

                }
            }
        });



        filterFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterFrom.clearFocus();
                Intent intent = new Intent(getApplicationContext(), SearchTMTActivity.class);
                try {
                    startActivityForResult(intent, 2);
                }catch (Exception e){

                }
            }
        });
      /*
        final String [] listViewAdapterContent = {"School", "House", "Building", "Food", "Sports", "Dress", "Ring"};

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listViewAdapterContent);
        outletListView.setAdapter(listAdapter);
        outletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                filterFrom.setText(listViewAdapterContent[position]);
                //outletListView.setEmptyView(findViewById(R.id.tmt_list_empty_editText));


                pathListAsyncTask.execute();
                TakeMeThereActivity.this.listAdapter.getFilter().filter("w");
            }
        });

        filterFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TakeMeThereActivity.this.listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
      */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            String message=data.getStringExtra("MESSAGE");
            filterFrom.setText(message);
            TakeMeThereAsyncTask pathListAsyncTask = new TakeMeThereAsyncTask(takeMeThereAdapter, to_id,  emptyView, toolbar, this);
            pathListAsyncTask.execute();

        }
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

        if(id==android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
