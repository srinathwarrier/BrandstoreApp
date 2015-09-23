package com.brandstore1.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.brandstore1.activities.OutletListActivity;
import com.brandstore1.adapters.OutletListAdapter;
import com.brandstore1.entities.Outlet;
import com.brandstore1.utils.CircularProgressDialog;
import com.brandstore1.utils.Connections;

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
public class OutletListAsyncTask extends AsyncTask<Void, Void, String> {
    ArrayList<Outlet> mOutletArrayList;
    String query;
    OutletListAdapter mOutletListAdapter;
    Outlet obj;
    String tagId;
    TextView emptyView;
    Toolbar toolbar;

    CircularProgressDialog circularProgressDialog;
    Context mContext;

    String urlString;

    public OutletListAsyncTask(ArrayList<Outlet> outletArrayList,
                               String text,
                               OutletListAdapter adapter,
                               String id,
                               TextView theEmptyView,
                               Toolbar toolbar,
                               Context context,
                               OutletListActivity.OutletListType outletListType) {
        // Basic
        this.mOutletArrayList = outletArrayList;
        this.mOutletListAdapter = adapter;
        this.mContext = context;

        // UI Elements
        this.emptyView = theEmptyView;
        this.toolbar = toolbar;

        // Parameters
        String userId = "6";
        this.tagId = id;
        this.query = text;
        Connections connections = new Connections();
        this.urlString = connections.getOutletListURL(this.tagId);

        switch(outletListType){
            case ALL_FAVORITE:
                this.urlString = connections.getAllFavoriteOutletsURL();
                break;
            case ALL_ON_SALE:
                this.urlString = connections.getAllOnSaleOutletsURL();
                break;
            case CLICKED_ON_CATEGORY: // same as single tagId
            case CLICKED_ON_TAG:
                this.urlString = connections.getOutletListURL(this.tagId);
                break;
            case SEARCHED_QUERY:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        circularProgressDialog = new CircularProgressDialog(this.mContext);
        circularProgressDialog = CircularProgressDialog.show(this.mContext,"","");
    }

    @Override
    protected String doInBackground(Void... params) {
        mOutletArrayList.clear();
        StringBuilder builder = null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream()
            );
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);

            return (builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String resultString) {
        super.onPostExecute(resultString);
        try {

            JSONArray json = new JSONArray(resultString);

            for (int i = 0; i < json.length(); i++) {
                Log.i("Brandstore - Outletlist", "Start ");
                obj = new Outlet();
                JSONObject object = json.getJSONObject(i);
                obj.setBrandOutletName(object.get("outletName").toString());
                obj.setImageUrl(object.get("imageUrl").toString());
                obj.setContactNumber(object.get("phoneNumber").toString());
                obj.setFloorNumber(object.get("floorNumber").toString());
                obj.setId(object.get("outletID").toString());
                obj.setRelevantTag(object.get("tagName").toString());
                obj.setPrice(object.get("avgPrice").toString());
                obj.setGenderCodeString(object.get("genderCodeString").toString());
                obj.setMallName(object.get("hubName").toString());
                obj.setIsFavorite(object.get("isFavorite").toString());
                obj.setIsOnSale(object.get("isOnSale").toString());

                Log.i("Brandstore - Outletlist", "object:" + obj.toString());
                mOutletArrayList.add(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (mOutletArrayList.size() == 0) {
                emptyView.setText("No outlets found");
            }
            else{
                //toolbar.setTitle(mOutletArrayList.get(0).getRelevantTag().toString());
                toolbar.setSubtitle(mOutletArrayList.size() + " " + "Outlets");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        mOutletListAdapter.notifyDataSetChanged();
         circularProgressDialog.dismiss();
        //OutletList

    }
}