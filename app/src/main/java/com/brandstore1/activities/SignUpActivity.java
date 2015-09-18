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
import android.widget.Toast;

import com.brandstore1.R;
import com.brandstore1.asynctasks.LoginAsyncTask;
import com.brandstore1.asynctasks.SignupAsyncTask;
import com.brandstore1.interfaces.SignupAsyncResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends ActionBarActivity implements SignupAsyncResponse{

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText ;
    EditText confirmPasswordEditText;
    Button signUpButton;
    Context mContext;
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


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
                String passwordString = passwordEditText.getText().toString();
                String confirmPasswordString = confirmPasswordEditText.getText().toString();
                String genderCode = "M";
                String dobString = "08091990"; // MMDDYYYY
                String checkValidFormData = isValidFormData(firstNameString, lastNameString, emailString, passwordString, confirmPasswordString, genderCode, dobString);
                if (checkValidFormData.equals("VALID")) {
                    SignupAsyncTask signupAsyncTask = new SignupAsyncTask(firstNameString, lastNameString, emailString, passwordString, genderCode, dobString, mContext);
                    signupAsyncTask.signupAsyncResponseDelegate = SignUpActivity.this;
                    signupAsyncTask.execute();
                } else {
                    Toast.makeText(mContext, checkValidFormData, Toast.LENGTH_LONG).show();
                }
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public String isValidFormData(String firstNameString ,
                                   String lastNameString ,
                                   String emailString ,
                                   String passwordString ,
                                   String confirmPasswordString ,
                                   String genderCode ,
                                   String dobString){
        if(! validateEmail(emailString)){
            return "Enter a valid Email address.";
        }
        if(!firstNameString.equals("")) {

        }
        if(passwordString.equals("")){
            return "Password cannot be empty.";
        }
        if(!passwordString.equals(confirmPasswordString)){
            return "Passwords do not match.";
        }
        return "VALID";
    }

    public boolean validateEmail(String emailString){
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(emailString);
        return matcher.matches();
    }

}
