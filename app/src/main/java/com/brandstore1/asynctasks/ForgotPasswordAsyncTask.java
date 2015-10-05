package com.brandstore1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.brandstore1.utils.Connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sonika on 8/9/2015.
 */

public class ForgotPasswordAsyncTask extends AsyncTask<Void, Void, String> {

    String emailId;
    Context mContext;

    public ForgotPasswordAsyncTask(String emailId,Context mContext) {
        this.emailId= emailId;
        this.mContext = mContext;
   }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {
        //String urlString = new Connections().getSetFavoriteOutletURL(this.id, this.operation);

        StringBuilder builder = null;
        try {
            String urlString = new Connections().getForgotPasswordURL(this.emailId);
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
        Log.i("Brandstore", resultString);
        Toast.makeText(mContext, "Password reset link has been sent via mail to the Email address.", Toast.LENGTH_LONG).show();
    }
}