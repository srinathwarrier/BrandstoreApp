package com.brandstore1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.brandstore1.adapters.OutletListAdapter;
import com.brandstore1.entities.Outlet;
import com.brandstore1.utils.CircularProgressDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.List;

/**
 * Created by Sonika on 8/9/2015.
 */

public class AddFavOutletAsyncTask extends AsyncTask<Void, Void, Void> {

    String id;
    boolean operation;


    public AddFavOutletAsyncTask(String id, boolean operation) {
        this.id = id;
        this.operation = operation;
       }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://ec2-52-26-206-185.us-west-2.compute.amazonaws.com/v2/setFavoriteOutlet?userid=1&outletid="+this.id+"&set="+this.operation);
            HttpResponse response = httpClient.execute(httpPost);


        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}