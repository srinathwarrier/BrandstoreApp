package com.brandstore1.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.brandstore1.adapters.RecentPopularSuggestionsAdapter;
import com.brandstore1.adapters.ResultsListViewAdapter;
import com.brandstore1.entities.SearchResults;
import com.brandstore1.interfaces.RecentPopularSuggestionsAsyncResponse;
import com.brandstore1.utils.Connections;
import com.brandstore1.utils.MySQLiteDatabase;
import com.brandstore1.utils.MySqliteDatabaseContract;

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
 * Created by Ravi on 27-Mar-15.
 */
public class RecentPopularSuggestionsAsyncTask extends AsyncTask<String, Void, String> {
    Context context;
    int currentTime ;
    static int lastCallTime;
    public RecentPopularSuggestionsAsyncResponse delegate;

    public RecentPopularSuggestionsAsyncTask(Context context) {
        this.context = context;
        this.currentTime= (int)System.currentTimeMillis();
        lastCallTime = currentTime;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        //url = "http://brandstore.co.in/database/scripts/getSuggestions.php?query=" + params;

        StringBuilder builder = null;
        /*if(lastCallTime>currentTime){
            //Log.i("SearchResults doInBg","Query:"+query+" lastCallTime>currentTime : "+lastCallTime+" > "+currentTime);
        }
        else if(lastCallTime<currentTime){
            //Log.i("SearchResults doInBg","Query:"+query+" lastCallTime<currentTime : "+lastCallTime+" < "+currentTime);
        }
        else{
            //Log.i("SearchResults doInBg","Query:"+query+" lastCallTime=currentTime : "+lastCallTime+" = "+currentTime);
        }*/


        try {
            String urlString = new Connections().getRecentAndPopularSuggestionsURL();
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);
            Log.d("response", "" + urlConnection.getResponseCode());
            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
            return (builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String resultString) {
        super.onPostExecute(resultString);
        SQLiteDatabase sqLiteDatabase = new MySQLiteDatabase(context).getWritableDatabase();
        try {
            JSONObject jsonObject = new JSONObject(resultString);

            JSONArray recentArray = jsonObject.getJSONArray("recent");

            if (recentArray.length() == 0) {
                // DO something .
            } else {
                MySQLiteDatabase.emptyRecentSuggestionTable(sqLiteDatabase);
                for (int i = 0; i < recentArray.length(); i++) {

                    JSONObject object = recentArray.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id",object.get("Id").toString() );
                    contentValues.put("name", object.get("name").toString());
                    contentValues.put("category", object.get("type").toString());
                    /*if( ( object.get("type").toString().equals("outlet") ||object.get("type").toString().equals("others"))
                            && object.get("floorNumber")!=null){
                        contentValues.put("floor", object.get("floorNumber").toString());
                    }*/
                    sqLiteDatabase.insert(MySqliteDatabaseContract.TableRecentSuggestion.TABLE_NAME, null, contentValues);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(delegate!=null){
            delegate.updateRecentListView();
        }


    }
}
