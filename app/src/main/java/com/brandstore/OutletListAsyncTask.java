package com.brandstore;

import android.os.AsyncTask;

import com.brandstore.adapters.OutletListAdapter;
import com.brandstore.entities.Outlet;

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

/**
 * Created by Ravi on 29-Mar-15.
 */
public class OutletListAsyncTask extends AsyncTask<Void,Void,Void>
{
ArrayList<Outlet> mOutlet;
    String query;
    OutletListAdapter mOutletListAdapter;
    Outlet obj;
    String id;
    public OutletListAsyncTask(ArrayList<Outlet> outlet,String text,OutletListAdapter adapter,String id)
    {
        this.id=id;
        mOutlet=outlet;
        query=text;
        mOutletListAdapter=adapter;
    }
    @Override
    protected Void doInBackground(Void... params) {
        mOutlet.clear();
        StringBuilder builder = null;
        try {
            URL url = new URL("http://awsm-awsmproject.rhcloud.com/getOutlets?id=" +id+"&type=category" );


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            JSONArray json = new JSONArray(builder.toString());
            for (int i = 0; i < json.length(); i++) {
                obj = new Outlet();
                JSONObject object = json.getJSONObject(i);
                obj.setBrandOutletName(object.get("brandOutletName").toString());
                obj.setGenderCodeString(object.get("genderCodeString").toString());
                obj.setImageUrl(object.get("imageUrl").toString());
               // obj.setId(object.get("brandOutletId").toString());
                obj.setContactNumber(object.get("phoneNumber").toString());
                obj.setFloorNumber("Lower Ground Floor");
                obj.setPrice("500");

                mOutlet.add(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mOutletListAdapter.notifyDataSetChanged();

    }
}