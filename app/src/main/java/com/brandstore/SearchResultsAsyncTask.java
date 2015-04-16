package com.brandstore;

import android.os.AsyncTask;
import android.util.Log;

import com.brandstore.adapters.ResultsListViewAdapter;
import com.brandstore.entities.SearchResults;

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
public class SearchResultsAsyncTask extends AsyncTask<String, Void, Void> {
    String query;
    SearchResults obj;
    ResultsListViewAdapter mResultsAdapter;

    ArrayList<SearchResults> mSearchResults;

    public SearchResultsAsyncTask(String text, ResultsListViewAdapter adapter, ArrayList<SearchResults> searchResults) {
        query = text;
        mResultsAdapter = adapter;
        mSearchResults = searchResults;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        //url = "http://brandstore.co.in/database/scripts/getSuggestions.php?query=" + params;

        mSearchResults.clear();
        StringBuilder builder = null;
        try {
            URL url = new URL("http://awsm-awsmproject.rhcloud.com/getSuggestions?q=" + query+"&userid="+6);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);
            Log.d("response", ""+urlConnection.getResponseCode());
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
            if(json.length()==0)
            {
                obj=new SearchResults();
                obj.setCategory(" ");
                obj.setName("No results found");
                obj.setId("0");
                mSearchResults.add(obj);
            }
            else {
                for (int i = 0; i < json.length(); i++) {
                    obj = new SearchResults();
                    JSONObject object = json.getJSONObject(i);
                    obj.setCategory(object.get("type").toString());
                    obj.setName(object.get("name").toString());
                    obj.setId(object.get("Id").toString());
                    mSearchResults.add(obj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        mResultsAdapter.notifyDataSetChanged();

    }
}
