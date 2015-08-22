package com.brandstore1;

import android.os.AsyncTask;
import android.widget.CheckBox;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sonika on 8/12/2015.
 */
public class CheckFavoritesAsyncTask extends AsyncTask<Void, Void, Integer> {
    String id;
    CheckBox FavCheckBox;

    public CheckFavoritesAsyncTask(String id, CheckBox cb) {
        this.id = id;
        FavCheckBox = cb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(Void... params) {

        StringBuilder builder = null;
        Integer operation = 0;
        try {
            //URL url = new URL("http://awsm-awsmproject.rhcloud.com/getOutlets?userid=6&type=category&id=" + id);
            URL url = new URL("http://ec2-52-26-206-185.us-west-2.compute.amazonaws.com/v2/getAllFavoriteOutlets?userid=6");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String line;

            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream()
            );

            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Exception occurred");
        }

        try {

            JSONArray json = new JSONArray(builder.toString());


            for (int i = 0; i < json.length(); i++) {

                //obj = new Outlet();
                JSONObject object = json.getJSONObject(i);
                System.out.println(object.get("outletID").toString());

                if((object.get("outletID").toString()).compareToIgnoreCase(this.id)==0){
                    System.out.println(this.id);
                    operation = 1;
                    break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return operation;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if(result == 1)
            FavCheckBox.setChecked(true);

    }
}
