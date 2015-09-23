package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.asynctasks.LoginAsyncTask;
import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;
import com.brandstore1.interfaces.LoginAsyncResponse;

public class LoginActivity extends ActionBarActivity implements LoginAsyncResponse{


    EditText emailEditText;
    EditText passwordEditText ;
    Button signInButton;
    TextView forgotPasswordTextView;
    TextView signUpTextView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        emailEditText = (EditText) findViewById(R.id.login_email);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        signInButton = (Button) findViewById(R.id.login_signin_button);
        forgotPasswordTextView = (TextView) findViewById(R.id.login_forgot_password_text);
        signUpTextView = (TextView) findViewById(R.id.signup_text);

        // 1. In splash screen , Check if SharedPreference has userid variable.
        //    If yes , then go to MainActivity
        //    If no, then no user set for the app. Show this login screen
        // Fetch android device details before screen is loaded,

        // Handle clickListener for :

        // 1. SignIn button click
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Login","signInButton");
                String emailString = emailEditText.getText().toString();
                String passwordString = passwordEditText .getText().toString();
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask(emailString , passwordString ,mContext);
                loginAsyncTask.loginAsyncResponseDelegate = LoginActivity.this;
                loginAsyncTask.execute();
            }
        });


        // 2. Forgot password click
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Login","forgotPasswordTextView");
            }
        });

        // 3. Sign Up click
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Login","signUpTextView");
                goToSignUpActivityScreen();
            }
        });

        //
        //
        //
        // .




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        Update suggestions in SQLite
     */
    public void updateSuggestionInSQLite(){
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("brandstoreDB",MODE_PRIVATE,null);
        UpdateSuggestionsAsyncTask updateSuggestionsAsyncTask=new UpdateSuggestionsAsyncTask(sqLiteDatabase);
        updateSuggestionsAsyncTask.execute();
    }

    /*
        Go To Screen methods
     */

    public void goToMainActivityScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void goToSignUpActivityScreen(){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }
}
