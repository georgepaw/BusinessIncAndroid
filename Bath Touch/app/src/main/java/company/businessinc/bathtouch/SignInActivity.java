package company.businessinc.bathtouch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.*;
import company.businessinc.networking.APICall;
import company.businessinc.networking.CheckNetworkConnection;


public class SignInActivity extends ActionBarActivity {

    private SharedPreferences mSharedPreferences;
    private static final String USERLOGGEDIN = "login";
    private static final String COOKIE = "cookie";
    private static final String USER = "user";
    public static final String PROPERTY_REG_ID = "registration_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (savedInstanceState == null) {
            mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sign_in_container, new SignInStart())
                    .commit();

        }

    }

    public static class SignInStart extends Fragment{

        private SharedPreferences mSharedPreferences;
        private AppCompatActivity mActivity;
        private EditText mUsernameEditText;
        private Button mSkipNext, mNewAccount, mAnonymous;

        public SignInStart() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mActivity = (ActionBarActivity) getActivity();
            mSharedPreferences = mActivity.getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            View rootView = inflater.inflate(R.layout.fragment_sign_in_start, container, false);
            mUsernameEditText = (EditText) rootView.findViewById(R.id.fragment_sign_in_start_username_edit_text);
            mUsernameEditText.clearFocus();
            mSkipNext = (Button) rootView.findViewById(R.id.fragment_sign_in_start_button_skip_next);
            mNewAccount = (Button) rootView.findViewById(R.id.fragment_sign_in_start_button_no_account);
            mAnonymous = (Button) rootView.findViewById(R.id.fragment_sign_in_start_button_anonymous);
            mSkipNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    next(v);
                }
            });
            mNewAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, CreateAccountActivity.class);
                    Bundle args = new Bundle();
                    args.putBoolean("ghost", false);
                    intent.putExtras(args);
                    startActivity(intent);
                }
            });
            mAnonymous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_as_anonymous(v);
                }
            });
            return rootView;
        }

        public void next(View view) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            SignInPassword pw = new SignInPassword();
            Bundle args = new Bundle();
            if(mUsernameEditText.getText().length() == 0) {
                mUsernameEditText.setError("Please enter your username");
            } else {
                args.putString("username", mUsernameEditText.getText().toString());
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                pw.setArguments(args);
                ft.replace(R.id.activity_sign_in_container, pw);
                ft.addToBackStack(null);
                ft.commit();
            }
        }

        public void login_as_anonymous(View view) {
            Intent intent = new Intent(mActivity, MainActivity.class);
            startActivity(intent);
            mSharedPreferences.edit().putBoolean(USERLOGGEDIN, false).commit();
            DataStore.getInstance(mActivity).setUser(new User());
            mActivity.finish();
        }

        public void create_account(View view) {
            Intent intent = new Intent(mActivity, CreateAccountActivity.class);
            startActivity(intent);
            DataStore.getInstance(mActivity).loadAllTeams();
        }
    }
    public static class SignInPassword extends Fragment implements UserLoginInterface, UserResetInterface, DeviceRegisterInterface {

        private SharedPreferences mSharedPreferences;
        private String mUsername;
        private AppCompatActivity mActivity;
        private EditText mPasswordEditText;
        private Button mNext, mForgotPassword;

        public SignInPassword() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mUsername = getArguments().getString("username");
            mActivity = (AppCompatActivity) getActivity();
            mSharedPreferences = mActivity.getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            View rootView = inflater.inflate(R.layout.fragment_sign_in_password, container, false);
            mPasswordEditText = (EditText) rootView.findViewById(R.id.fragment_sign_in_password_password_edit_text);
            mNext = (Button) rootView.findViewById(R.id.fragment_sign_in_password_button_next);
            mForgotPassword = (Button) rootView.findViewById(R.id.fragment_sign_in_password_forgot_password);
            mNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    next(v);
                }
            });
            mForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forgot_password(v);
                }
            });
            return rootView;
        }

        public void next(View view) {
            if(mPasswordEditText.getText().length() == 0) {
                mPasswordEditText.setError("Please enter your password");
            } else {
                if (CheckNetworkConnection.check(mActivity)) {
                    String password = mPasswordEditText.getText().toString();
                    Log.d("Login", "Network is working, let's log in");
                    new UserLogin(this, mUsername, password).execute();
                } else {
                    Toast.makeText(mActivity, "No connection", Toast.LENGTH_SHORT).show();
                    Log.d("Login", "Network is not working");
                }
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        public void forgot_password(View view) {
            AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
            alertDialog.setMessage("Send a reset token to the email address" +
                    " registered to this username?");

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new UserReset(SignInPassword.this, mUsername, true).execute();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
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
                    sendRegistrationIdToBackend();
                    mActivity.finish();
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

        @Override
        public void userResetCallback(ResponseStatus data) {
            if(data != null) {
                if(data.getStatus()) {
                    Log.d("Reset", "Reset email sent");
                    Intent intent = new Intent(mActivity, ForgotPasswordActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("Reset", "Reset returned false");
                    Toast.makeText(mActivity,
                            "Error, something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("Reset", "Error connecting and parsing");
                Toast.makeText(mActivity, "Cannot connect", Toast.LENGTH_SHORT).show();
            }
        }

        private void sendRegistrationIdToBackend() {
            String regId = mSharedPreferences.getString(PROPERTY_REG_ID, "null");
            if (!regId.equals("null") && DataStore.getInstance(getActivity()).isUserLoggedIn()) {
                new DeviceRegister(this, getActivity(), regId).execute();
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
}
