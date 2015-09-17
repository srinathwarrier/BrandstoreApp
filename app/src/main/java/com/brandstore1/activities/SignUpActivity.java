package com.brandstore1.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.asynctasks.LoginAsyncTask;
import com.brandstore1.asynctasks.SignupAsyncTask;
import com.brandstore1.interfaces.SignupAsyncResponse;

public class SignUpActivity extends ActionBarActivity implements SignupAsyncResponse{

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText ;
    EditText confirmPasswordEditText;
    Button signUpButton;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = this;

        firstNameEditText = (EditText) findViewById(R.id.signup_first_name);
        lastNameEditText = (EditText) findViewById(R.id.signup_last_name);
        emailEditText = (EditText) findViewById(R.id.signup_email);
        passwordEditText = (EditText) findViewById(R.id.signup_password);
        confirmPasswordEditText = (EditText) findViewById(R.id.signup_confirm_password);
        signUpButton = (Button) findViewById(R.id.signup_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameString = firstNameEditText.getText().toString();
                String lastNameString = lastNameEditText.getText().toString();
                String emailString = emailEditText.getText().toString();
                String passwordString = passwordEditText .getText().toString();
                String genderCode = "M";
                SignupAsyncTask signupAsyncTask = new SignupAsyncTask( firstNameString, lastNameString, emailString , passwordString ,genderCode, mContext);
                signupAsyncTask.signupAsyncResponseDelegate = SignUpActivity.this;
                signupAsyncTask.execute();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void goToMainActivityScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
