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
public class OutletListAsyncTask extends AsyncTask<Void, Void, Void> {
    ArrayList<Outlet> mOutletArrayList;
    String query;
    OutletListAdapter mOutletListAdapter;
    Outlet obj;
    String id;

    public OutletListAsyncTask(ArrayList<Outlet> outletArrayList, String text, OutletListAdapter adapter, String id) {
        this.id = id;
        mOutletArrayList = outletArrayList;
        query = text;
        mOutletListAdapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mOutletArrayList.clear();
        StringBuilder builder = null;
        try {
            URL url = new URL("http://awsm-awsmproject.rhcloud.com/getOutlets?userid=6&type=category&id=" + id);


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

        try {

            JSONArray json = new JSONArray(builder.toString());
            for (int i = 0; i < json.length(); i++) {
                // Log.i("Brandstore - Outletlist","Start ");
                obj = new Outlet();
                JSONObject object = json.getJSONObject(i);
                obj.setBrandOutletName(object.get("brandName").toString());
                obj.setImageUrl(object.get("imageUrl").toString());
                obj.setContactNumber(object.get("phoneNumber").toString());
                obj.setFloorNumber(object.get("floorNumber").toString());
                obj.setId(object.get("outletID").toString());
                obj.setRelevantTag(object.get("tagName").toString());
                obj.setPrice(object.get("avgPrice").toString());
                obj.setGenderCodeString(object.get("genderCodeString").toString());
                obj.setMallName(object.get("hubName").toString());

                // Log.i("Brandstore - Outletlist","object:"+obj.toString());
                mOutletArrayList.add(obj);
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