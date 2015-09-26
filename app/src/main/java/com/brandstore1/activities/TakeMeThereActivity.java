package com.brandstore1.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.asynctasks.TakeMeThereAsyncTask;
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
    private EditText from;
    private ArrayAdapter<String> listAdapter;
    String to_id;
    String from_id;
    TextView emptyView;

    public enum TMT_type{
        TO_KNOWN , TO_UNKNOWN ,
    };

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

        takeMeThereAdapter = new TakeMeThereAdapter(pathArrayList, this, toolbar, emptyView);
        pathListView.setAdapter(takeMeThereAdapter);

        Bundle bundle = getIntent().getExtras();
        TMT_type tmtType = (TMT_type)bundle.get("type");
        to = (EditText) findViewById(R.id.tmt_to);
        from = (EditText)findViewById(R.id.tmt_from);

        from.getBackground().setColorFilter(getResources().getColor(R.color.tmt_bg), PorterDuff.Mode.SRC_ATOP);
        to.getBackground().setColorFilter(getResources().getColor(R.color.tmt_bg), PorterDuff.Mode.SRC_ATOP);

        switch(tmtType){
            case TO_KNOWN:
                to_id = bundle.getString("id");
                to.setText(bundle.getString("name"));
                break;
            case TO_UNKNOWN:
                to.setEnabled(true);
                to.setText("To");
                to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoSearchTMTActivityScreen(to);
                    }
                });

                break;
            default:
                break;
        }


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchTMTActivityScreen(from);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(data != null){
           if(requestCode==2) {
               String outlet_name = data.getStringExtra("outlet_name");
               String outlet_id = data.getStringExtra("outlet_id");
               String to_from = data.getStringExtra("TO_FROM");

               if(to_from.compareToIgnoreCase("tmt_from")== 0) {
                   from.setText(outlet_name);
                   from_id = outlet_id;
               }
               else if (to_from.compareToIgnoreCase("tmt_to")== 0) {
                   to.setText(outlet_name);
                   to_id = outlet_id;
               }
               if(from_id!=null && to_id!=null){
                   TakeMeThereAsyncTask pathListAsyncTask = new TakeMeThereAsyncTask(takeMeThereAdapter, to_id,from_id, emptyView, toolbar, this);
                   pathListAsyncTask.execute();
               }
           }

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


    public void gotoSearchTMTActivityScreen(EditText search){

        search.clearFocus();
        Intent intent = new Intent(getApplicationContext(), SearchTMTActivity.class);
        intent.putExtra("search", search.getTag().toString());
        startActivityForResult(intent, 2);


    }
}
