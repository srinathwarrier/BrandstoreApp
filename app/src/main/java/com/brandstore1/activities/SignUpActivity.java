package com.brandstore1.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brandstore1.R;
import com.brandstore1.asynctasks.LoginAsyncTask;
import com.brandstore1.asynctasks.SignupAsyncTask;
import com.brandstore1.interfaces.SignupAsyncResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends ActionBarActivity implements SignupAsyncResponse{

    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText ;
    EditText confirmPasswordEditText;
    TextView dobTextView;
    RadioGroup genderRadioGroup;
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

        nameEditText = (EditText) findViewById(R.id.signup_name);
        emailEditText = (EditText) findViewById(R.id.signup_email);
        passwordEditText = (EditText) findViewById(R.id.signup_password);
        //confirmPasswordEditText = (EditText) findViewById(R.id.signup_confirm_password);
        dobTextView = (TextView) findViewById(R.id.signup_dob);
        genderRadioGroup = (RadioGroup)findViewById(R.id.signup_gender_radioGroup);
        signUpButton = (Button) findViewById(R.id.signup_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = nameEditText.getText().toString();
                String emailString = emailEditText.getText().toString();
                String passwordString = passwordEditText.getText().toString();
                //String confirmPasswordString = confirmPasswordEditText.getText().toString();
                String genderCode = "";
                if(genderRadioGroup.getCheckedRadioButtonId() == R.id.signup_gender_male){
                    genderCode = "M";
                }else if(genderRadioGroup.getCheckedRadioButtonId() == R.id.signup_gender_female){
                    genderCode = "F";
                }
                String dobString = dobTextView.getTag().toString();
                //String dobString = "08091990"; // MMDDYYYY
                String checkValidFormData = isValidFormData(nameString, emailString, passwordString, genderCode, dobString);
                if (checkValidFormData.equals("VALID")) {
                    SignupAsyncTask signupAsyncTask = new SignupAsyncTask(nameString, emailString, passwordString, genderCode, dobString, mContext);
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

    public String isValidFormData(String nameString ,
                                   String emailString ,
                                   String passwordString ,
                                   String genderCode ,
                                   String dobString){
        if(nameString.equals("")) {
            return "Name cannot be empty.";
        }
        if(! validateEmail(emailString)){
            return "Enter a valid Email address.";
        }
        if(passwordString.equals("")){
            return "Password cannot be empty.";
        }
        /*if(!passwordString.equals(confirmPasswordString)){
            return "Passwords do not match.";
        }*/
        return "VALID";
    }

    public boolean validateEmail(String emailString){
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(emailString);
        return matcher.matches();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        TextView textView;
        public DatePickerFragment(){
            super();
        }
        public DatePickerFragment(TextView v){
            super();
            textView = v;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Date date = new Date(year-1900,month,day);
            DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
            String reportDate = df.format(date);
            textView.setText(reportDate);

            df = new SimpleDateFormat("yyyy-MM-dd");
            textView.setTag(df.format(date));

        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment((TextView)v);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



}
