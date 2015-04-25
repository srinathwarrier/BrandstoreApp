package com.brandstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.brandstore.OutletListAsyncTask;
import com.brandstore.R;
import com.brandstore.adapters.OutletListAdapter;
import com.brandstore.entities.Outlet;

import java.util.ArrayList;


public class OutletListActivity extends ActionBarActivity {
    TextView category;
    ListView outletListView;
    ArrayList<Outlet> outletArrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_list);
        Bundle bundle = getIntent().getExtras();
        String query = bundle.getString("name");
        String id=bundle.getString("id");

        outletListView = (ListView) findViewById(R.id.outlet_list_list_view);
        OutletListAdapter mOutletListAdapter = new OutletListAdapter(outletArrayList, this);
        outletListView.setAdapter(mOutletListAdapter);
        OutletListAsyncTask mOutletListAsyncTask = new OutletListAsyncTask(outletArrayList, query, mOutletListAdapter,id);
        mOutletListAsyncTask.execute();

        outletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OutletDetailsActivity.class);
                intent.putExtra("id", outletArrayList.get(position).getId());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outlet_list, menu);
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
