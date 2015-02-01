package company.businessinc.bathtouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class CreateAccountActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_create_account_container, new CreateAccountStart())
                    .commit();
        }
    }


    public static class CreateAccountStart extends Fragment {

        private EditText mFirstNameEditText, mSecondnameEditText;
        private Button mNext;

        public CreateAccountStart() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_account_start, container, false);
            mFirstNameEditText = (EditText) rootView.findViewById(R.id.fragment_create_account_start_name_first_edit_text);
            mSecondnameEditText = (EditText) rootView.findViewById(R.id.fragment_create_account_start_name_second_edit_text);
            mNext = (Button) rootView.findViewById(R.id.fragment_create_account_start_button_skip_next);
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putString("name", mFirstNameEditText.getText().toString() + " "
                    + mSecondnameEditText.getText().toString());
                    CreateAccountEmail cau = new CreateAccountEmail();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    cau.setArguments(args);
                    ft.replace(R.id.activity_create_account_container, cau);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            return rootView;
        }
    }

    public static class CreateAccountEmail extends Fragment {

        private EditText mEmailEditText;
        private Button mNext;

        public CreateAccountEmail() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_account_email, container, false);
            mEmailEditText = (EditText) rootView.findViewById(R.id.fragment_create_account_email_email_edit_text);
            mNext = (Button) rootView.findViewById(R.id.fragment_create_account_email_button_skip_next);
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = getArguments();
                    args.putString("email", mEmailEditText.getText().toString());
                    CreateAccountUsername cau = new CreateAccountUsername();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    cau.setArguments(args);
                    ft.replace(R.id.activity_create_account_container, cau);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            return rootView;
        }
    }

    public static class CreateAccountUsername extends Fragment {

        private EditText mUsernameEditText;
        private Button mNext;

        public CreateAccountUsername() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_account_username, container, false);
            mUsernameEditText = (EditText) rootView.findViewById(R.id.fragment_create_account_username_username_edit_text);
            mNext = (Button) rootView.findViewById(R.id.fragment_create_account_username_button_skip_next);
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = getArguments();
                    args.putString("username", mUsernameEditText.getText().toString());
                    CreateAccountPassword cap = new CreateAccountPassword();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    cap.setArguments(args);
                    ft.replace(R.id.activity_create_account_container, cap);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            return rootView;
        }
    }

    public static class CreateAccountPassword extends Fragment {

        private EditText mPasswordEditText, mRePasswordEditText;
        private Button mNext;

        public CreateAccountPassword() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_account_password, container, false);
            mPasswordEditText = (EditText) rootView.findViewById(R.id.fragment_create_account_password_password_edit_text);
            mRePasswordEditText = (EditText) rootView.findViewById(R.id.fragment_create_account_password_repassword_edit_text);
            mNext = (Button) rootView.findViewById(R.id.fragment_create_account_password_button_skip_next);
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = getArguments();
                    args.putString("password", mPasswordEditText.getText().toString());
                    CreateAccountTeam cat = new CreateAccountTeam();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    cat.setArguments(args);
                    ft.replace(R.id.activity_create_account_container, cat);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            return rootView;
        }
    }

    public static class CreateAccountTeam extends Fragment implements UserNewInterface, UserLoginInterface, LoaderManager.LoaderCallbacks<Cursor>{

        private SharedPreferences mSharedPreferences;
        private String userLoggedIn = "login";
        private static final String cookie = "Cookie";
        private Spinner mTeamSpinner;
        private Button mNext;
        private ArrayList<Team> mLeagueTeams;
        private String mSelectedTeam, mUsername, mPassword;

        public CreateAccountTeam() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mSharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            View rootView = inflater.inflate(R.layout.fragment_create_account_team, container, false);
            mTeamSpinner = (Spinner) rootView.findViewById(R.id.fragment_create_account_team_team_spinner);
            mNext = (Button) rootView.findViewById(R.id.fragment_create_account_team_button_skip_next);

            mLeagueTeams = new ArrayList<>();
            getLoaderManager().initLoader(DBProviderContract.ALLTEAMS_URL_QUERY, null, this);
             return rootView;
        }

        public void create_account(View view) {
            if (CheckNetworkConnection.check(getActivity())) {
                Bundle args = getArguments();
                String name = args.getString("name");
                String email = args.getString("email");
                mUsername = args.getString("username");
                mPassword = args.getString("password");
                Log.d("Create User", name + email + mUsername + mPassword);
                int teamID = -1;
                for(Team t : mLeagueTeams){
                    if(t.getTeamName().equals(mTeamSpinner.getSelectedItem().toString())){
                        teamID = t.getTeamID();
                    }
                }
                Log.d("Create User", "Network is working, let's create a user");
                new UserNew(this,mUsername,mPassword,email,name,teamID).execute();
            } else {
                Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
                Log.d("Create User", "Network is not working");
            }
        }

        @Override
        public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
            switch (loaderID) {
                case DBProviderContract.ALLTEAMS_URL_QUERY:
                    // Returns a new CursorLoader
                    return new CursorLoader(getActivity(), DBProviderContract.ALLTEAMS_TABLE_CONTENTURI, null, null, null, null);
                default:
                    // An invalid id was passed in
                    return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mLeagueTeams = new ArrayList<>();
            if (data.moveToFirst()){
                while(!data.isAfterLast()){
                    mLeagueTeams.add(new Team(data));
                    data.moveToNext();
                }
            }

            final ArrayList<String> teamNames = new ArrayList<>();
            for(Team t : mLeagueTeams) {
                teamNames.add(t.getTeamName());
            }
            Log.d("Create User",teamNames.toString());
            ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, teamNames);
            mTeamSpinner.setAdapter(teamAdapter);
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    create_account(v);
                }
            });
            mTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

        @Override
        public void userNewCallback(Status data) {
            if(data != null){
                if(data.getStatus().equals(true)) {
                    Log.d("Create User", "User successfully created");

                    if (CheckNetworkConnection.check(getActivity())) {
                        Log.d("Create User", "Network is working, let's log in");
                        new UserLogin(this,mUsername,mPassword).execute();
                    } else {
                        Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
                        Log.d("Create User", "Network is not working");
                    }
                } else {
                    Log.d("Create User", "Invalid credentials");
                    Toast.makeText(getActivity(), "Bad Details", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("Create User", "Error connecting and parsing");
                Toast.makeText(getActivity(), "Cannot connect", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void userLoginCallback(User data) {
            if (data != null) {
                if (data.isLoggedIn()) { //User has logged in
                    Log.d("Create User", "Logged in");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    mSharedPreferences.edit().putBoolean(userLoggedIn, true).commit();
                    mSharedPreferences.edit().putString(cookie, APICall.getCookie()).commit();
                    mSharedPreferences.edit().putString("user", data.toString()).commit();
                    getActivity().finish();
                } else {
                    Log.d("Create User", "Invalid credentials");
                    Toast.makeText(getActivity(), "Bad Details", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("Login", "Error connecting and parsing");
                Toast.makeText(getActivity(), "Cannot connect", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

}
