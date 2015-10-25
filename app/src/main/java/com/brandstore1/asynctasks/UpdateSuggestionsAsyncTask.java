package com.brandstore1.asynctasks;

/**
 * Created by I076324 on 7/22/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.brandstore1.interfaces.UpdateSuggestionsAsyncResponse;
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

public class UpdateSuggestionsAsyncTask extends AsyncTask<Void,Void,Void> {
    int flag=0;
    SQLiteDatabase sqLiteDatabase;
    public UpdateSuggestionsAsyncResponse delegate=null;

    public UpdateSuggestionsAsyncTask(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase=sqLiteDatabase;
    }


    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder builder = null;
        try {
            String urlString = new Connections().getSuggestionsURL("");
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
            flag=1;



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // If connection to DB is unsuccessful at this point, then we need to update the DB again  next when the connection is available,
        // probably at the time when the user is searching for the item. At the time when the user is searching for the item, if the localDB is empty or
        // if the local DB is not uptodate(very rare case) , the local DB needs to updated and the search provided.
        // Right now , we are only updating the local DB once when the app is first started. If this time , an internet connection was not available for some reason,
        // the update would not have taken place and we need to update it again.

        try {
            //sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Suggestions(id VARCHAR, name VARCHAR, category VARCHAR , floorName VARCHAR );");
            JSONArray json = new JSONArray(builder.toString());
            if (json.length() == 0) {
                // DO something .
            } else {
                if(flag==1)
                    MySQLiteDatabase.emptySuggestionTable(sqLiteDatabase);
                for (int i = 0; i < json.length(); i++) {

                    JSONObject object = json.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id",object.get("Id").toString() );
                    contentValues.put("name", object.get("name").toString());
                    contentValues.put("category", object.get("type").toString());
                    if( ( object.get("type").toString().equals("outlet") ||object.get("type").toString().equals("others"))
                            && object.get("floorNumber")!=null){
                        contentValues.put("floor", object.get("floorNumber").toString());
                    }
                    sqLiteDatabase.insert(MySqliteDatabaseContract.TableSuggestion.TABLE_NAME, null, contentValues);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


        /*if(delegate!=null){
            delegate.closeAndGoToMainActivityScreen();
        }*/


    }
}
