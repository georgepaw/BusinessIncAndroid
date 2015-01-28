package company.businessinc.bathtouch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.Status;
import company.businessinc.dataModels.Team;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.UserLogin;
import company.businessinc.endpoints.UserLoginInterface;
import company.businessinc.endpoints.UserNew;
import company.businessinc.endpoints.UserNewInterface;
import company.businessinc.networking.APICall;
import company.businessinc.networking.CheckNetworkConnection;


public class CreateAccountActivity extends ActionBarActivity implements UserNewInterface, UserLoginInterface, LoaderManager.LoaderCallbacks<Cursor> {

    public static String name, email, username, password, teamName;
    private SharedPreferences mSharedPreferences;
    private String userLoggedIn = "login";
    private static final String cookie = "Cookie";
    private ArrayList<Team> leagueTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        leagueTeams = new ArrayList<>();
        getLoaderManager().initLoader(DBProviderContract.GETALLTEAMS_URL_QUERY, null, this);
        if(savedInstanceState == null)
            mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
    }

    public void create_account(View view) {
        if (CheckNetworkConnection.check(this)) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.create_account_boxes);
            name = ((EditText)linearLayout.getChildAt(0)).getText().toString();
            email = ((EditText)linearLayout.getChildAt(1)).getText().toString();
            username = ((EditText)linearLayout.getChildAt(2)).getText().toString();
            password = ((EditText)linearLayout.getChildAt(3)).getText().toString();
            teamName = ((EditText)linearLayout.getChildAt(4)).getText().toString();
            int teamID = -1;
            for(Team t : leagueTeams){
                if(t.getTeamName().equals(teamName)){
                    teamID = t.getTeamID();
                }
            }
            Log.d("Create User", "Network is working, let's create a user");
            new UserNew(this,username,password,email,name,teamID).execute();
        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
            Log.d("Create User", "Network is not working");
        }
    }

    @Override
    public void userNewCallback(Status data) {
        if(data != null){
            if(data.getStatus().equals(true)) {
                Log.d("Create User", "User successfully created");

                if (CheckNetworkConnection.check(this)) {
                    Log.d("Create User", "Network is working, let's log in");
                    new UserLogin(this,username,password).execute();
                } else {
                    Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
                    Log.d("Create User", "Network is not working");
                }
            } else {
                Log.d("Create User", "Invalid credentials");
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Bad Details", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Log.d("Create User", "Error connecting and parsing");
            Toast.makeText(CreateAccountActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void userLoginCallback(User data) {
        if (data != null) {
            if (data.isLoggedIn()) { //User has logged in
                Log.d("Create User", "Logged in");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mSharedPreferences.edit().putBoolean(userLoggedIn, true).commit();
                mSharedPreferences.edit().putString(cookie, APICall.getCookie()).commit();
                mSharedPreferences.edit().putString("user", data.toString()).commit();
                finish();
            } else {
                Log.d("Create User", "Invalid credentials");
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Bad Details", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Log.d("Login", "Error connecting and parsing");
            Toast.makeText(CreateAccountActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Invoked when the cursor loader is created
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.GETALLTEAMS_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(this, DBProviderContract.ALLTEAMS_TABLE_CONTENTURI, null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        leagueTeams = new ArrayList<>();
        if (data.moveToFirst()){
            while(!data.isAfterLast()){
                leagueTeams.add(new Team(data));
                data.moveToNext();
            }
        }
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.create_account_boxes); //TODO data is loaded into an arrayList, display it nicely wizards
        if(leagueTeams.size()>0) {
            ((EditText) linearLayout.getChildAt(4)).setText(leagueTeams.get(0).getTeamName());
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }



}
