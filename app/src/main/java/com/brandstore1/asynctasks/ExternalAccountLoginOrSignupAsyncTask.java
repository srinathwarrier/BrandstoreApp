package com.brandstore1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.brandstore1.entities.User;
import com.brandstore1.interfaces.SignupAsyncResponse;
import com.brandstore1.model.Connection;
import com.brandstore1.utils.CircularProgressDialog;
import com.brandstore1.utils.Connections;
import com.brandstore1.utils.MySharedPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by I076630 on 06-May-15.
 */
public class ExternalAccountLoginOrSignupAsyncTask extends AsyncTask<Void,Void,String> {


    String name;
    String accountId;
    String emailId;
    String genderCode;
    String dobString;
    Connections.AccountType accountType;
    Context mContext;
    CircularProgressDialog circularProgressDialog;

    public ExternalAccountLoginOrSignupAsyncTask(String name,
                                                 String accountId,
                                                 String emailId,
                                                 String genderCode,
                                                 String dobString,
                                                 Connections.AccountType accountType,
                                                 Context mContext)
    {
        this.name = name;
        this.accountId=accountId;
        this.emailId=emailId;
        this.genderCode = genderCode;
        this.dobString = dobString;
        this.accountType = accountType;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        circularProgressDialog = new CircularProgressDialog(this.mContext);
        circularProgressDialog = CircularProgressDialog.show(this.mContext,"","");
    }

    @Override
    protected String doInBackground(Void... params) {
        StringBuilder builder = null;
        try {
            String urlString = new Connections().getExternalAccountLoginOrSignUpURL(name, accountId,emailId, genderCode, dobString, accountType);
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
    protected void onPostExecute(String returnValue) {
        super.onPostExecute(returnValue);
        try {
            JSONObject jsonObject = new JSONObject(returnValue);
            Log.i("Signup", "JSON :" + jsonObject.toString());

            // If jsonArray.length is 0, user does not exist. So show Toast that user/password combination is invalid.
            // If jsonArray.length is 1, extract and add to user entity. Save userid and Go to MainActivity.
            // TODO: If jsonArray.length is more than 1, do as previous , but there is an error


            if(jsonObject==null ){
                Toast.makeText(mContext, "Connection Error.", Toast.LENGTH_LONG).show();
            }
            else{
                if(jsonObject.getString("responseState").equals("error")){
                    Toast.makeText(mContext, "Connection Error.", Toast.LENGTH_LONG).show();
                }
                else if(jsonObject.getString("responseState").equals("login")){
                    JSONObject userObject = jsonObject.getJSONObject("responseDetails");
                    Connections.performInitialLoginFormalities(userObject,mContext);
                }
                else if(jsonObject.getString("responseState").equals("signup")) {
                    JSONObject userObject = jsonObject.getJSONObject("responseDetails");
                    Connections.performInitialSignupFormalities(userObject,mContext);
                }
            }
            circularProgressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
