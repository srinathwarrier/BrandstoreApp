package com.brandstore1.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brandstore1.R;
import com.brandstore1.asynctasks.ExternalAccountLoginOrSignupAsyncTask;
import com.brandstore1.asynctasks.LoginAsyncTask;
import com.brandstore1.asynctasks.SignupAsyncTask;
import com.brandstore1.asynctasks.UpdateSuggestionsAsyncTask;
import com.brandstore1.gcm.GCMConnection;
import com.brandstore1.interfaces.LoginAsyncResponse;
import com.brandstore1.interfaces.SignupAsyncResponse;
import com.brandstore1.utils.Connections;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.facebook.FacebookSdk;

import org.json.JSONObject;

public class LoginActivity extends ActionBarActivity implements
        SignupAsyncResponse,
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    private static final String TAG = "LoginActivity";

    EditText emailEditText;
    EditText passwordEditText ;
    boolean isPasswordVisible;
    Button signInButton;
    TextView forgotPasswordTextView;
    TextView signUpTextView;
    Context mContext;

    LoginButton facebookLoginButton;
    CallbackManager callbackManager;
    SignInButton googleplusSignInButton;

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 9001;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        mContext = this;

        // Restore from saved instance state
        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        // [END restore_saved_instance_state]

        emailEditText = (EditText) findViewById(R.id.login_email);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        signInButton = (Button) findViewById(R.id.login_signin_button);
        forgotPasswordTextView = (TextView) findViewById(R.id.login_forgot_password_text);
        signUpTextView = (TextView) findViewById(R.id.signup_text);
        isPasswordVisible = false;

        // Set up Login button click listeners
        googleplusSignInButton = (SignInButton)findViewById(R.id.login_googleplusbutton);
        googleplusSignInButton.setOnClickListener(this);


        /*facebookLoginButton = (LoginButton)findViewById(R.id.login_facebookbutton);
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG,"");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.i(TAG,"");
                            }
                        }
                );
            }

            @Override
            public void onCancel() {
                Log.i(TAG,"");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG,"");
            }
        });*/

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_black_24dp, 0);
            // Implement showPassword
            passwordEditText.setOnTouchListener(new View.OnTouchListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if (isPasswordVisible) {
                                passwordEditText.setInputType(129);
                                passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_black_24dp, 0);
                            } else {
                                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_white_24dp, 0);
                            }
                            isPasswordVisible = !(isPasswordVisible);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        // [START create_google_api_client]
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        // [END create_google_api_client]


        // Handle clickListener for :

        // 1. SignIn button click
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Login","signInButton");
                String emailString = emailEditText.getText().toString();
                String passwordString = passwordEditText .getText().toString();
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask(emailString , passwordString ,mContext);
                loginAsyncTask.execute();
            }
        });


        // 2. Forgot password click
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Login","forgotPasswordTextView");
                Toast.makeText(mContext, "Forgot password not implemented", Toast.LENGTH_LONG).show();
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

    // [START on_start_on_stop]
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    // [END on_start_on_stop]


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

    @Override
    public void onEmailAlreadyExists() {
        Toast.makeText(mContext, "Email already exists", Toast.LENGTH_LONG).show();
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    /*
        Go To Screen methods
     */

    public void goToSignUpActivityScreen(){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            // get all Data
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson != null) {
                String emailString = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String name = currentPerson.getDisplayName();
                String dobString = currentPerson.getBirthday();
                if(dobString==null)dobString="";

                String genderCode = ""+currentPerson.getGender();

                ExternalAccountLoginOrSignupAsyncTask externalAccountLoginOrSignupAsyncTask= new ExternalAccountLoginOrSignupAsyncTask(name, emailString, genderCode,dobString ,Connections.AccountType.GOOGLE_ACCOUNT, mContext);
                externalAccountLoginOrSignupAsyncTask.execute();

                //Toast.makeText(this,"name:"+name+" lang:"+language+" birthday:"+test,Toast.LENGTH_LONG);
            } else {
                Log.w(TAG, "invalid");
            }
        } else {
            Log.i(TAG,"Not signed in");
        }
    }


    // [START on_save_instance_state]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }
    // [END on_save_instance_state]

    // [START on_activity_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);


        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further errors.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }
    // [END on_activity_result]


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected:" + bundle);

        // Show the signed-in UI
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended:" + i);
    }

    @Override
    public void onClick(View v) {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.

        // [START sign_in_clicked]
        mShouldResolve = true;
        mGoogleApiClient.connect();
        // [END sign_in_clicked]
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            updateUI(false);
        }
    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Show the default Google Play services error dialog which may still start an intent
            // on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;
                            updateUI(false);
                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a message to the user.
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();

            mShouldResolve = false;
            updateUI(false);
        }
    }
}
