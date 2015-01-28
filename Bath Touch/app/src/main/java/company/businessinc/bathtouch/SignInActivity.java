package company.businessinc.bathtouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.UserLogin;
import company.businessinc.endpoints.UserLoginInterface;
import company.businessinc.networking.APICall;
import company.businessinc.networking.CheckNetworkConnection;


public class SignInActivity extends ActionBarActivity {

    private SharedPreferences mSharedPreferences;
    private static final String USERLOGGEDIN = "login";
    private static final String COOKIE = "cookie";
    private static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (savedInstanceState == null) {
            mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SignInStart())
                    .commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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

    public static class SignInStart extends Fragment{

        private SharedPreferences mSharedPreferences;
        private ActionBarActivity mActivity;
        private EditText mUsernameEditText;
        private Button mSkipNext;

        public SignInStart() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mActivity = (ActionBarActivity) getActivity();
            mSharedPreferences = mActivity.getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            View rootView = inflater.inflate(R.layout.fragment_sign_in_start, container, false);
            mUsernameEditText = (EditText) rootView.findViewById(R.id.fragment_sign_in_start_username_edit_text);
            mSkipNext = (Button) rootView.findViewById(R.id.fragment_sign_in_start_button_skip_next);
            mUsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        mSkipNext.setText("Next");
                        mSkipNext.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                next(v);
                            }
                        });
                    } else if(mUsernameEditText.getText().length() == 0){
                        mSkipNext.setText("Skip");
                        mSkipNext.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                skip(v);
                            }
                        });
                    }
                }
            });
            return rootView;
        }

        public void next(View view) {
            FragmentManager fm = mActivity.getSupportFragmentManager();
            SignInPassword pw = new SignInPassword();
            Bundle args = new Bundle();
            args.putString("username", mUsernameEditText.getText().toString());
            pw.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.container, pw)
                    .commit();
        }
        public void skip(View view) {
            Intent intent = new Intent(mActivity, MainActivity.class);
            startActivity(intent);
            mSharedPreferences.edit().putBoolean(USERLOGGEDIN, false).commit();
            DataStore.getInstance(mActivity).setUser(new User());
        }

        public void create_account(View view) {
            Intent intent = new Intent(mActivity, CreateAccountActivity.class);
            startActivity(intent);
            DataStore.getInstance(mActivity).loadAllTeams();
        }
    }
    public static class SignInPassword extends Fragment implements UserLoginInterface{

        private SharedPreferences mSharedPreferences;
        private String mUsername;
        private ActionBarActivity mActivity;
        private EditText mPasswordEditText;
        private Button mNext;

        public SignInPassword() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mUsername = getArguments().getString("username");
            mActivity = (ActionBarActivity) getActivity();
            mSharedPreferences = mActivity.getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            View rootView = inflater.inflate(R.layout.fragment_sign_in_password, container, false);
            mPasswordEditText = (EditText) rootView.findViewById(R.id.fragment_sign_in_password_password_edit_text);
            mNext = (Button) rootView.findViewById(R.id.fragment_sign_in_password_button_next);
            return rootView;
        }

        public void next(View view) {
            if (CheckNetworkConnection.check(mActivity)) {
                String password = mPasswordEditText.getText().toString();
                Log.d("Login", "Network is working, let's log in");
                new UserLogin(this,mUsername,password).execute();
            } else {
                Toast.makeText(mActivity, "No connection", Toast.LENGTH_SHORT).show();
                Log.d("Login", "Network is not working");
            }
        }

        @Override
        public void userLoginCallback(User data) {
            if (data != null) {
                DataStore.getInstance(mActivity).setUser(data);
                if (DataStore.getInstance(mActivity).isUserLoggedIn()) { //User has logged in
                    Log.d("Login", "Logged in");
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    startActivity(intent);
                    mSharedPreferences.edit().putBoolean(USERLOGGEDIN, true).commit();
                    mSharedPreferences.edit().putString(COOKIE, APICall.getCookie()).commit();
                    mSharedPreferences.edit().putString(USER, DataStore.getInstance(mActivity).userToJSON()).commit();
                } else {
                    Log.d("Login", "Invalid credentials");
                    Toast toast = Toast.makeText(mActivity, "Bad Details", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                Log.d("Login", "Error connecting and parsing");
                Toast.makeText(mActivity, "Cannot connect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
