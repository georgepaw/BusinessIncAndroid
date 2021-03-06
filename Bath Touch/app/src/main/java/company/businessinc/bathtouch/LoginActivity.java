package company.businessinc.bathtouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;

import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.DeviceRegister;
import company.businessinc.endpoints.DeviceRegisterInterface;
import company.businessinc.endpoints.UserLogin;
import company.businessinc.endpoints.UserLoginInterface;
import company.businessinc.networking.APICall;
import company.businessinc.networking.CheckNetworkConnection;


public class LoginActivity extends ActionBarActivity implements UserLoginInterface, DeviceRegisterInterface {

    private SharedPreferences mSharedPreferences;
    private static final String USERLOGGEDIN = "login";
    private static final String COOKIE = "cookie";
    private static final String USER = "user";
    public static final String PROPERTY_REG_ID = "registration_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        if(savedInstanceState == null)
            mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts and uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Get an Analytics tracker to report app starts and uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    public void login_as_user(View view) {
        if (CheckNetworkConnection.check(this)) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.login_boxes);
            String username = ((EditText)linearLayout.getChildAt(0)).getText().toString();
            String password = ((EditText)linearLayout.getChildAt(1)).getText().toString();
            Log.d("Login", "Network is working, let's log in");
            new UserLogin(this,username,password).execute();
        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
            Log.d("Login", "Network is not working");
        }
    }

    public void login_as_anonymous(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        mSharedPreferences.edit().putBoolean(USERLOGGEDIN, false).commit();
        DataStore.getInstance(this).setUser(new User());
        sendRegistrationIdToBackend();
        finish();
    }

    public void create_account(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }

    //After the login button is pressed
    public void userLoginCallback(User data) {
        if (data != null) {
            DataStore.getInstance(this).setUser(data);
            if (DataStore.getInstance(this).isUserLoggedIn()) { //User has logged in
                Log.d("Login", "Logged in");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mSharedPreferences.edit().putBoolean(USERLOGGEDIN, true).commit();
                mSharedPreferences.edit().putString(COOKIE, APICall.getCookie()).commit();
                mSharedPreferences.edit().putString(USER, DataStore.getInstance(this).userToJSON()).commit();
                sendRegistrationIdToBackend();
                finish();
            } else {
                Log.d("Login", "Invalid credentials");
                Toast toast = Toast.makeText(LoginActivity.this, "Bad Details", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Log.d("Login", "Error connecting and parsing");
            Toast.makeText(LoginActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
        }
    }



    private void sendRegistrationIdToBackend() {
        String regId = mSharedPreferences.getString(PROPERTY_REG_ID, "null");
        if (!regId.equals("null") && DataStore.getInstance(getApplicationContext()).isUserLoggedIn()) {
            new DeviceRegister(this, getApplicationContext(), regId).execute();
        } else {
            Log.d("WTF", "Reg id for this device isn't here!");
        }
    }

    @Override
    public void deviceRegisterCallback(ResponseStatus responseStatus) {
        if(responseStatus.getStatus()){
            Log.d("RegGCMOnServer", "Call was successful");
        } else {
            Log.d("RegGCMOnServer", "Call was not successful");
        }
    }
}
