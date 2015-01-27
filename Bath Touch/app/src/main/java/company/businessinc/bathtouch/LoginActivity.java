package company.businessinc.bathtouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import company.businessinc.dataModels.User;
import company.businessinc.endpoints.UserLogin;
import company.businessinc.endpoints.UserLoginInterface;
import company.businessinc.networking.APICall;
import company.businessinc.networking.CheckNetworkConnection;


public class LoginActivity extends ActionBarActivity implements UserLoginInterface {

    private SharedPreferences mSharedPreferences;
    private String userLoggedIn = "login";
    private static final String cookie = "Cookie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(savedInstanceState == null)
            mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
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
        mSharedPreferences.edit().putBoolean(userLoggedIn, false).commit();
        DataStore.newInstance();
        finish();
    }

    public void create_account(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        DataStore.newInstance();
        finish();
    }

    //After the login button is pressed
    public void userLoginCallback(User data) {
        if (data != null) {
            if (data.isLoggedIn()) { //User has logged in
                Log.d("Login", "Logged in");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mSharedPreferences.edit().putBoolean(userLoggedIn, true).commit();
                mSharedPreferences.edit().putString(cookie, APICall.getCookie()).commit();
                mSharedPreferences.edit().putString("user", data.toString()).commit();
                DataStore.newInstance();
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


}
