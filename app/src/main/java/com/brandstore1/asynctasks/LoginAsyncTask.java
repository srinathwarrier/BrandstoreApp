package com.brandstore1.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.brandstore1.R;
import com.brandstore1.activities.LoginActivity;
import com.brandstore1.activities.MainActivity;
import com.brandstore1.entities.User;
import com.brandstore1.interfaces.LoginAsyncResponse;
import com.brandstore1.utils.CircularProgressDialog;
import com.brandstore1.utils.Connections;
import com.brandstore1.utils.MySharedPreferences;
import com.google.gson.Gson;

import org.json.JSONArray;
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
public class LoginAsyncTask extends AsyncTask<Void,Void,String> {


    String emailId;
    String password;
    Context mContext;
    User user ;
    CircularProgressDialog circularProgressDialog;
    public LoginAsyncResponse loginAsyncResponseDelegate =null;

    public LoginAsyncTask(String emailId, String password, Context mContext)
    {
        this.emailId=emailId;
        this.password=password;
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
            String urlString = new Connections().getLoginURL(emailId, password);
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
            JSONArray jsonArray = new JSONArray(returnValue);
            Log.i("Login", "JSON :" + jsonArray.toString());

            // If jsonArray.length is 0, user does not exist. So show Toast that user/password combination is invalid.
            // If jsonArray.length is 1, extract and add to user entity. Save userid and Go to MainActivity.
            // TODO: If jsonArray.length is more than 1, do as previous , but there is an error


            if(jsonArray.length()==0){
                Toast.makeText(mContext, "Invalid username/password combination", Toast.LENGTH_LONG).show();
                circularProgressDialog.dismiss();


            }
            else{
                user = new User();
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                user.setUserId(jsonObject.getString("userID"));
                user.setName(jsonObject.getString("name"));
                //user.setEmailId(jsonObject.getString("emailid"));
                //user.setPassword(jsonObject.getString("password"));

                // Convert user object to json object
                Gson gson = new Gson();
                String userJsonObject = gson.toJson(user);

                // Save userid and userJsonObject to SharedPreferences
                /*
                SharedPreferences sharedPref = mContext.getSharedPreferences("BrandstoreApp",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("userid", user.getUserId());
                editor.putString("userJsonObject", userJsonObject);
                editor.putBoolean("hasLoggedIn", true);
                editor.apply();
                */
                MySharedPreferences.setUserId(mContext , user.getUserId());
                MySharedPreferences.setUserJsonObjectString(mContext, userJsonObject);
                MySharedPreferences.setHasLoggedIn(mContext, true);

                Connections.setUserIdFromSharedPreferences(mContext);

                circularProgressDialog.dismiss();

                loginAsyncResponseDelegate.updateSuggestionInSQLite();
                loginAsyncResponseDelegate.goToMainActivityScreen();

            }










        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
}
