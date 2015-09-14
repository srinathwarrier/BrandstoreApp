package com.brandstore1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.brandstore1.activities.TakeMeThereActivity;
import com.brandstore1.adapters.TakeMeThereAdapter;
import com.brandstore1.entities.Outlet;
import com.brandstore1.utils.CircularProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sonika on 8/22/2015.
 */
public class TakeMeThereAsyncTask extends AsyncTask<Void, Void, Void> {
    ArrayList<String> pathArrayList = new ArrayList<String>();
    String to_id;
    Toolbar toolbar;
    TakeMeThereAdapter takeMeThereAdapter;
    Context mContext;
    CircularProgressDialog circularProgressDialog;
    TextView emptyView;

    public TakeMeThereAsyncTask(TakeMeThereAdapter adapter, String id, TextView theEmptyView, Toolbar toolbar, Context context) {
        this.to_id = id;
        takeMeThereAdapter = adapter;
        emptyView = theEmptyView;
        this.toolbar = toolbar;
        this.mContext = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        circularProgressDialog = new CircularProgressDialog(this.mContext);
        circularProgressDialog = CircularProgressDialog.show(this.mContext,"","");
    }

    @Override
    protected Void doInBackground(Void... params) {
        pathArrayList.clear();
        StringBuilder builder = null;
        try {

            URL url = new URL("http://ec2-52-26-206-185.us-west-2.compute.amazonaws.com/v2/getTakeMeThereCommands?fromoutletid=70&tooutletid=220");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream()
            );
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("sonika "+ builder.toString());
        String temp = builder.toString();
        //pathArrayList.add(temp);
        //ArrayList<String> temp = new ArrayList<String>();
        //temp.add(builder.toString());
        String delims = "[,\\[\\]]";
        try {
            String[] tokens = temp.split(delims);
            //String token = "";
            for(int i =1; i<= tokens.length ;i++)
              pathArrayList.add(i+". "+tokens[i]);
            //token = token+i+". "+tokens[i]+"\n";

            //pathArrayList.add(token);
        }catch (Exception e){
            pathArrayList.add(temp);
            System.out.println("Excptn"+e);
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        takeMeThereAdapter.resetList(pathArrayList);
        circularProgressDialog.dismiss();

    }
}
