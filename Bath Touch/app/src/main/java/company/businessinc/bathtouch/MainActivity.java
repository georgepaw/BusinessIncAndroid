package company.businessinc.bathtouch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.endpoints.DeviceRegister;
import company.businessinc.endpoints.DeviceUnregister;
import company.businessinc.endpoints.DeviceUnregisterInterface;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import company.businessinc.bathtouch.adapters.HomePageAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.ScoreSubmitInterface;
import company.businessinc.networking.APICall;


public class MainActivity extends ActionBarActivity
        implements HomePageFragment.HomePageCallbacks,
        MyTeamFragment.MyTeamFragmentCallbacks,
        TeamResultsFragment.TeamResultsCallbacks,
        LeagueTableFragment.LeagueTableCallbacks,
        LeagueFragment.LeagueCallbacks,
        HomePageAdapter.homePageAdapterCallbacks,
        ResultsListFragment.ResultsListCallbacks,
        TeamOverviewFragment.TeamOverviewCallbacks,
        RefGamesFragment.RefGamesCallbacks,
        ScoreSubmitInterface,
        DeviceUnregisterInterface{

    private SharedPreferences mSharedPreferences;
    private static final String USERLOGGEDIN = "login";
    private static final String COOKIE = "cookie";
    private static final String USER = "user";
    private static final String CURRENT_FRAGMENT = "cur_frag";
    private static final String TAG = "MainActivity";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "388248839516"; //got this from Google
    private GoogleCloudMessaging gcm =null;
    private String regid = null;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerFrameLayout mNavigationDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        if (!mSharedPreferences.contains(USERLOGGEDIN)) {
            Log.d("MAIN", "HAS NOT LOGGED IN");
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        if (mSharedPreferences.getBoolean(USERLOGGEDIN, false) && !mSharedPreferences.contains(COOKIE)) { //user logged in but no cookie string, kick him out
            Log.d("MAIN", "USER LOGGED IN BUT NO COOKIE");
            mSharedPreferences.edit().remove(USERLOGGEDIN).commit();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else if (mSharedPreferences.contains(COOKIE)) {
            APICall.setCookie(mSharedPreferences.getString(COOKIE, ""));
        }

        try {
            DataStore.getInstance(this).setUser(new User(new JSONObject(mSharedPreferences.getString(USER, ""))));
        } catch (JSONException e) {
            Log.d("MAIN LOGIN", "CAN'T PARSE THE USER");
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, MyTeamFragment.newInstance(), "HOMEPAGETAG")
                    .commit();
        }

        mNavigationDrawerLayout = (DrawerFrameLayout) findViewById(R.id.drawer_layout);
//        DrawerView navigationDrawer = (DrawerView) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (!DataStore.getInstance(this).isUserLoggedIn()) {
            mNavigationDrawerLayout.setProfile(
                    new DrawerProfile()
                            .setAvatar(getResources().getDrawable(R.drawable.ic_account_circle_grey600_48dp))
                            .setBackground(getResources().getDrawable(R.color.primary))
                            .setName("Anonymous User")
                            .setDescription("Not signed in")
                            .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                                @Override
                                public void onClick(DrawerProfile drawerProfile) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setMessage("Do you want to sign in?");
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sign in", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            logOut();
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
                            })
            );
        } else {
            TextDrawable avatar = TextDrawable.builder()
                    .buildRound(DataStore.getInstance(MainActivity.this).getUserTeam().substring(0, 1)
                            .toUpperCase(), DataStore.getInstance(MainActivity.this).getUserTeamColorPrimary());
            mNavigationDrawerLayout.setProfile(
                    new DrawerProfile()
                            .setAvatar(avatar)
                            .setBackground(new ColorDrawable(DataStore.getInstance(MainActivity.this).getUserTeamColorSecondary()))
                            .setName(DataStore.getInstance(MainActivity.this).getUserName())
                            .setDescription(DataStore.getInstance(MainActivity.this).getUserTeam())
                            .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                                @Override
                                public void onClick(DrawerProfile drawerProfile) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setMessage("Do you want to sign out?");
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sign out", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            logOut();
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
                            })
            );
        }

        String myTeamFragmentName;
        if(DataStore.getInstance(this).isUserLoggedIn()){
            myTeamFragmentName = "My Team";
        } else {
            myTeamFragmentName = "Overview";
        }

        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_home_grey600_48dp))
                        .setTextPrimary(myTeamFragmentName)
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                changeFragments("HOMEPAGETAG", null);
                                mNavigationDrawerLayout.closeDrawer();
                            }
                        })
        );
        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_assessment_grey600_48dp))
                        .setTextPrimary("Leagues")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                changeFragments("LEAGUETABLETAG", null);
                                mNavigationDrawerLayout.closeDrawer();
                            }
                        })
        );
        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_event_grey600_48dp))
                        .setTextPrimary("Matches")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                changeFragments("TEAMRESULTSTAG", null);
                                mNavigationDrawerLayout.closeDrawer();
                            }
                        })
        );
        if(DataStore.getInstance(this).isReferee()) {
            mNavigationDrawerLayout.addItem(
                    new DrawerItem()
                            .setImage(getResources().getDrawable(R.drawable.ic_whistle_grey600_48dp))
                            .setTextPrimary("Referee Matches")
                            .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                                @Override
                                public void onClick(DrawerItem drawerItem, int i, int i2) {
                                    changeFragments("REFGAMESTAG", null);
                                    mNavigationDrawerLayout.closeDrawer();
                                }
                            })
            );
        }
        if(DataStore.getInstance(this).getNotifications()){
            mNavigationDrawerLayout.addItem(
                    new DrawerItem()
                            .setImage(getResources().getDrawable(R.drawable.ic_group_add_grey600_48dp))
                            .setTextPrimary("Player requests")
                            .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                                @Override
                                public void onClick(DrawerItem drawerItem, int id, int position) {
                                    changeFragments("PLAYERREQUESTS", null);
                                    mNavigationDrawerLayout.closeDrawer();
                                }
                            })
            );
        }
        mNavigationDrawerLayout.addDivider();
        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_settings_grey600_48dp))
                        .setTextPrimary("Settings")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                Toast.makeText(MainActivity.this, "No settings activity yet", Toast.LENGTH_SHORT).show();
                                mNavigationDrawerLayout.closeDrawer();
                            }
                        })
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mNavigationDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
//                mNavigationDrawerLayout.selectItem(mFragmentManager.get);
                invalidateOptionsMenu();
            }
        };

        setSupportActionBar(toolbar);

        int colour;
        if(DataStore.getInstance(this).isUserLoggedIn()){
            colour = darker(DataStore.getInstance(this).getUserTeamColorPrimary(), 0.7f);
        } else {
            colour = darker(getResources().getColor(R.color.primary), 0.7f);
        }
        mNavigationDrawerLayout.setStatusBarBackgroundColor(colour);
        mNavigationDrawerLayout.setDrawerListener(mDrawerToggle);


        //Google play services + notifications
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            if (regid.isEmpty()) {
                registerInBackground();
            }
            else {
                Log.d(TAG, "No valid Google Play Services APK found.");
            }
        }

        //FAB STUFF JAMES

//        final FloatingActionButton nextMatchButton = new FloatingActionButton(getApplicationContext());
//        nextMatchButton.setTitle("Next Match");
//        nextMatchButton.setIconDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_black));
//        nextMatchButton.setColorNormal(Color.WHITE);
//        nextMatchButton.setColorPressed(getResources().getColor(R.color.primary));
//        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(nextMatchButton);
//
//        final FloatingActionButton exampleLeagueA = new FloatingActionButton(getApplicationContext());
//        exampleLeagueA.setTitle("Your Account");
//        exampleLeagueA.setIconDrawable(getResources().getDrawable(R.drawable.ic_person_big_black));
//        exampleLeagueA.setColorNormal(Color.WHITE);
//        exampleLeagueA.setColorPressed(getResources().getColor(R.color.primary));
//        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(exampleLeagueA);
//
////        final FloatingActionButton actionC = new FloatingActionButton(getApplicationContext());
////        actionC.setColorNormal(Color.WHITE);
////        actionC.setTitle("Show all Leagues");
////        actionC.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
////                exampleLeagueA.setVisibility(exampleLeagueA.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
////                //Toggle the title of action C
////                if (actionB.getVisibility() == View.VISIBLE) {
////                    actionC.setTitle("Hide all Leagues");
////                } else {
////                    actionC.setTitle("Show all Leagues");
////                }
////            }
////        });
////        //Added last to bottom
////        ((FloatingActionsMenu) findViewById(R.id.multiple_actions)).addButton(actionC);
//
//
//        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
//        actionA.setIconDrawable(getResources().getDrawable(R.drawable.ic_send_black));

        //END FAB STUFF JAMES
    }

    public int darker (int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

    @Override protected void onResume()
    {
        super.onResume();
        if (mSharedPreferences.contains(COOKIE)) { //restore cookie
            APICall.setCookie(mSharedPreferences.getString(COOKIE, ""));
        }
        try { //restore the user
            DataStore.getInstance(this).setUser(new User(new JSONObject(mSharedPreferences.getString(USER, ""))));
        } catch (JSONException e) {
            Log.d("MAIN LOGIN", "CAN'T PARSE THE USER");
        }
        checkPlayServices();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onHomePageCardSelected(int position) {
        switch (position) {
            case HomePageAdapter.NEXTREFGAME:
                if (DataStore.getInstance(this).getNextGame() != null) {
                    Intent intent = new Intent(this, SubmitScoreActivity.class);
                    Match nextRefMatch = DataStore.getInstance(this).getNextGame();
                    intent.putExtra(Match.KEY_MATCHID, nextRefMatch.getMatchID());
                    intent.putExtra(Match.KEY_TEAMONE, nextRefMatch.getTeamOne());
                    intent.putExtra(Match.KEY_TEAMTWO, nextRefMatch.getTeamTwo());
                    startActivity(intent);
                }
                break;
            case HomePageAdapter.NEXTGAME:
                Log.d("CARDS", "Next game card selected");
                //Only allow the card to open if there is a next game
                if (DataStore.getInstance(this).getNextGame() != null && DataStore.getInstance(this).isUserCaptain()) {
                    Match nextPlayingMatch = DataStore.getInstance(this).getNextGame();
                    Intent intent = new Intent(this, TeamRosterActivity.class);
                    intent.putExtra(Match.KEY_MATCHID, nextPlayingMatch.getMatchID());
                    intent.putExtra(Match.KEY_TEAMONE, nextPlayingMatch.getTeamOne());
                    intent.putExtra(Match.KEY_TEAMTWO, nextPlayingMatch.getTeamTwo());
                    intent.putExtra(Match.KEY_PLACE, nextPlayingMatch.getPlace());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    intent.putExtra(Match.KEY_DATETIME, sdf.format(nextPlayingMatch.getDateTime()));
                    intent.putExtra(Match.KEY_LEAGUEID, nextPlayingMatch.getLeagueID());
                    //setLeagueID(nextPlayingMatch.getLeagueID());
                    startActivity(intent);
                } else {
                    Log.d("Main", "Players is not a captain, can't go here");
                }
                break;
            case HomePageAdapter.TABLE:
                Log.d("MATCH", "starting leage table activity");
//                Intent intent = new Intent(this, LeagueTableActivity.class);
//                startActivity(intent);
                changeFragments("LEAGUETABLETAG", null);
                break;
            case HomePageAdapter.TEAMRESULTS:
                Log.d("MATCH", "starting team results fragment");

                changeFragments("TEAMRESULTSTAG", null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLeagueTableItemSelected(int position) {
        Log.d("LEAGUE", Integer.toString(position));
    }

    @Override
    public void onTeamResultsItemSelected(int position) {
        Log.d("TEAM", Integer.toString(position));
    }

    public void changeFragments(String tag, Bundle args) {
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals("HOMEPAGETAG"))
                ft.replace(R.id.container, MyTeamFragment.newInstance(), tag);
            if (tag.equals("LEAGUETABLETAG"))
                ft.replace(R.id.container, LeagueTableFragment.newInstance(), tag);
            if (tag.equals("TEAMRESULTSTAG"))
                ft.replace(R.id.container, TeamResultsFragment.newInstance(), tag);
            if (tag.equals("REFGAMESTAG"))
                ft.replace(R.id.container, RefGamesFragment.newInstance(), tag);
            if (tag.equals("MATCHDETAILSFRAG")) {
                ft.replace(R.id.container, MatchFragment.newInstance(args), tag);
            }
            if (tag.equals("PLAYERREQUESTS")) {
                ft.replace(R.id.container, PlayerRequestsFragment.newInstance(), tag);
            }

            ft.addToBackStack(tag);
            ft.commit();
        } else if (!fragment.isVisible()) {
            if (tag.equals("MATCHDETAILSFRAG")) {
                ft.replace(R.id.container, MatchFragment.newInstance(args), tag);
            } else {
                ft.replace(R.id.container, fragment, tag);
            }
            ft.addToBackStack(tag);
            ft.commit();
        }
    }

    private void logOut() {
        String regId = mSharedPreferences.getString(PROPERTY_REG_ID, "null"); //unregister this device from GCM if possible
        if (!regId.equals("null") && DataStore.getInstance(getApplicationContext()).isUserLoggedIn()) {
            new DeviceUnregister(this, getApplicationContext(), regId).execute();
        } else {
            Log.d("WTF", "Reg id for this device isn't here!");
            performLogOut();
        }

    }

    @Override
    public void deviceUnregisterCallback(ResponseStatus responseStatus) {
        if(responseStatus.getStatus()){
            Log.d(TAG, "Unregistered the device successfuly");
        } else {
            Log.d(TAG, "Problem unregistering the device");
        }
        performLogOut();
    }

    private void performLogOut(){
        Log.d("MAIN", "LOGGING OUT");
        Intent intent = new Intent(this, SignInActivity.class);
        APICall.clearCookies();
        mSharedPreferences.edit().remove(USERLOGGEDIN).commit();
        if (mSharedPreferences.contains(COOKIE)) {
            mSharedPreferences.edit().remove(COOKIE).commit();
        }
        if (mSharedPreferences.contains(USER)) {
            mSharedPreferences.edit().remove(USER).commit();
        }
        DataStore.getInstance(this).clearUserData();
        startActivity(intent);
        finish();
    }

    @Override
    public void onNextMatchCardSelected() {
        onHomePageCardSelected(HomePageAdapter.NEXTGAME);
    }

    @Override
    public void onNextRefMatchCardSelected() {
        onHomePageCardSelected(HomePageAdapter.NEXTREFGAME);
    }

    @Override
    public void onTeamResultsCardSelected() {
        onHomePageCardSelected(HomePageAdapter.TEAMRESULTS);
    }

    @Override
    public void onLeagueCardSelected() {
        onHomePageCardSelected(HomePageAdapter.TABLE);
    }

    @Override
    public void onResultsItemSelected(Bundle args) {
        changeFragments("MATCHDETAILSFRAG", args);
    }

    @Override
    public void onLeagueItemSelected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void matchDetailsSelectedCallback(Bundle args) {
        changeFragments("MATCHDETAILSFRAG", args);
    }

    //GCM methods

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(TAG, "This device is not supported - Google Play Services.");
                //finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context){
        String registrationId = mSharedPreferences.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.d(TAG, "Registration ID not found.");
            return "";
        }
        int registeredVersion = mSharedPreferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.d(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void registerInBackground(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    //store this in prefrences
                    storeRegistrationId(getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    @Override
    public void scoreSubmitCallback(ResponseStatus data) {
        if(data != null){
            if(data.getStatus()) {
                Toast.makeText(this, "Score submitted successfully", Toast.LENGTH_SHORT).show();
                DataStore.getInstance(getApplicationContext()).refreshData(); //update all scores
            } else {
                Toast.makeText(this, "Score could not be submitted", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Score could not be submitted", Toast.LENGTH_LONG).show();
        }
    }
}
