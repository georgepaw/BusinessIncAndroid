package company.businessinc.bathtouch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import company.businessinc.bathtouch.adapters.HomePageAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.dataModels.User;
import company.businessinc.networking.APICall;


public class MainActivity extends ActionBarActivity
        implements HomePageFragment.HomePageCallbacks,
        TeamOverviewFragment.TeamOverviewCallbacks,
        MyTeamFragment.MyTeamFragmentCallbacks,
        TeamResultsFragment.TeamResultsCallbacks,
        LeagueTableFragment.LeagueTableCallbacks,
        LeagueFragment.LeagueCallbacks,
        HomePageAdapter.homePageAdapterCallbacks,
        ResultsListFragment.ResultsListCallbacks,
        LoaderManager.LoaderCallbacks<Cursor>{

    private SharedPreferences mSharedPreferences;
    private static final String USERLOGGEDIN = "login";
    private static final String COOKIE = "cookie";
    private static final String USER = "user";
    private static final String CURRENT_FRAGMENT = "cur_frag";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerFrameLayout mNavigationDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager mFragmentManager;

    private Match nextRefMatch;
    private Match nextPlayingMatch;
    private int nextPlayingMatchLeagueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        if(!mSharedPreferences.contains(USERLOGGEDIN)) {
            Log.d("MAIN", "HAS NOT LOGGED IN");
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        if(mSharedPreferences.getBoolean(USERLOGGEDIN,false) && !mSharedPreferences.contains(COOKIE)) { //user logged in but no cookie string, kick him out
            Log.d("MAIN", "USER LOGGED IN BUT NO COOKIE");
            mSharedPreferences.edit().remove(USERLOGGEDIN).commit();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else if(mSharedPreferences.contains(COOKIE)){
            APICall.setCookie(mSharedPreferences.getString(COOKIE, ""));
        }
        if(mSharedPreferences.getBoolean(USERLOGGEDIN,false) != DataStore.getInstance(this).isUserLoggedIn()){
            try{
                DataStore.getInstance(this).setUser(new User(new JSONObject(mSharedPreferences.getString(USER, ""))));
            } catch (JSONException e){
                Log.d("MAIN LOGIN", "CAN'T PARSE THE USER");
            }
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            if(DataStore.getInstance(this).isUserLoggedIn())
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, MyTeamFragment.newInstance(), "HOMEPAGETAG")
                        .commit();
            else
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, HomePageFragment.newInstance(), "HOMEPAGETAG")
                        .commit();
        }

        mNavigationDrawerLayout = (DrawerFrameLayout) findViewById(R.id.drawer_layout);
//        DrawerView navigationDrawer = (DrawerView) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(!DataStore.getInstance(this).isUserLoggedIn()) {
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
                    .buildRound(DataStore.getInstance(MainActivity.this).getUserTeam().substring(0,1)
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

        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_home_grey600_48dp))
                        .setTextPrimary("My Team")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                changeFragments("HOMEPAGETAG");
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
                                changeFragments("LEAGUETABLETAG");
                                mNavigationDrawerLayout.closeDrawer();
                            }
                        })
        );
        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_event_grey600_48dp))
                        .setTextPrimary("Results")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                changeFragments("TEAMRESULTSTAG");
                                mNavigationDrawerLayout.closeDrawer();
                            }
                        })
        );
        if(DataStore.getInstance(this).isUserCaptain()) {
            mNavigationDrawerLayout.addItem(
                    new DrawerItem()
                            .setImage(getResources().getDrawable(R.drawable.ic_supervisor_account_grey600_48dp))
                            .setTextPrimary("Organise Team")
                            .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                                @Override
                                public void onClick(DrawerItem drawerItem, int id, int position) {
                                    Toast.makeText(MainActivity.this, "No team organise fragment yet", Toast.LENGTH_SHORT).show();
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
        ){

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
//                mNavigationDrawerLayout.selectItem(mFragmentManager.get);
                invalidateOptionsMenu();
            }
        };

        setSupportActionBar(toolbar);

        mNavigationDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        mNavigationDrawerLayout.setDrawerListener(mDrawerToggle);

        if(DataStore.getInstance(this).isUserLoggedIn()) {
            if(DataStore.getInstance(this).isReferee()) {
                getSupportLoaderManager().initLoader(DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY, null, this);
            }
            getSupportLoaderManager().initLoader(DBProviderContract.MYUPCOMINGGAMES_URL_QUERY, null, this);
        }
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHomePageCardSelected(int position) {
        switch(position) {
            case HomePageAdapter.NEXTREFGAME:
                if(nextRefMatch != null){
                    Intent intent = new Intent(this, SubmitScoreActivity.class);
                    intent.putExtra(Match.KEY_MATCHID, nextRefMatch.getMatchID());
                    intent.putExtra(Match.KEY_TEAMONE, nextRefMatch.getTeamOne());
                    intent.putExtra(Match.KEY_TEAMTWO, nextRefMatch.getTeamTwo());
                    startActivity(intent);
                } else {
                    //This card should be gone
                }
                break;
            case HomePageAdapter.NEXTGAME:
                Log.d("CARDS", "Next game card selected");
                //Only allow the card to open if there is a next game
                if(nextPlayingMatch != null && DataStore.getInstance(this).isUserCaptain()) {
                    Intent intent = new Intent(this, TeamRosterActivity.class);
                    intent.putExtra(Match.KEY_MATCHID, nextPlayingMatch.getMatchID());
                    intent.putExtra(Match.KEY_TEAMONE, nextPlayingMatch.getTeamOne());
                    intent.putExtra(Match.KEY_TEAMTWO, nextPlayingMatch.getTeamTwo());
                    intent.putExtra(Match.KEY_PLACE, nextPlayingMatch.getPlace());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    intent.putExtra(Match.KEY_DATETIME, sdf.format(nextPlayingMatch.getDateTime()));
                    intent.putExtra(League.KEY_LEAGUEID, nextPlayingMatchLeagueID);
                    startActivity(intent);
                } else {
                    Log.d("Main", "Players is not a captain, can't go here");
                }
                break;
            case HomePageAdapter.TABLE:
                Log.d("MATCH", "starting leage table activity");
//                Intent intent = new Intent(this, LeagueTableActivity.class);
//                startActivity(intent);
                changeFragments("LEAGUETABLETAG");
                break;
            case HomePageAdapter.TEAMRESULTS:
                Log.d("MATCH", "starting team results fragment");

                changeFragments("TEAMRESULTSTAG");
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

    public void changeFragments(String tag) {
        if(mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if(fragment == null) {
            if(tag.equals("HOMEPAGETAG"))
                ft.replace(R.id.container, MyTeamFragment.newInstance(), tag);
            if(tag.equals("LEAGUETABLETAG"))
                ft.replace(R.id.container, LeagueTableFragment.newInstance(), tag);
            if(tag.equals("TEAMRESULTSTAG"))
                ft.replace(R.id.container, TeamResultsFragment.newInstance(), tag);
            ft.addToBackStack(tag);
            ft.commit();
        }
        else if(!fragment.isVisible()) {
            ft.replace(R.id.container, fragment, tag);
            ft.addToBackStack(tag);
            ft.commit();
        }
    }

    private void logOut() {
        Log.d("MAIN", "LOGGING OUT");
        Intent intent = new Intent(this, SignInActivity.class);
        APICall.clearCookies();
        mSharedPreferences.edit().remove(USERLOGGEDIN).commit();
        if(mSharedPreferences.contains(COOKIE)){
            mSharedPreferences.edit().remove(COOKIE).commit();
        }
        if(mSharedPreferences.contains(USER)){
            mSharedPreferences.edit().remove(USER).commit();
        }
        DataStore.getInstance(this).clearUserData();
        startActivity(intent);
        finish();
    }

    //Invoked when the cursor loader is created
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(this, DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(this, DBProviderContract.MYUPCOMINGGAMES_TABLE_CONTENTURI, null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()){
            return;
        }
        switch(loader.getId()) {
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY:
                nextRefMatch = getNextMatch(data);
                break;
            case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                nextPlayingMatch = getNextMatch(data);
                getLeague();
                break;
        }
    }

    private Match getNextMatch(Cursor data){
        List<Match> matchList = new ArrayList<>();
        while(!data.isAfterLast()){
            matchList.add(new Match(data));
            data.moveToNext();
        }
        if(matchList.size() > 0){
            matchList = Match.sortList(matchList, Match.SortType.ASCENDING);
            return matchList.get(0);
        }
        return null;
    }

    private void getLeague(){
        Cursor cursor = getContentResolver().query(DBProviderContract.MYUPCOMINGGAMES_TABLE_CONTENTURI,null, DBProviderContract.SELECTION_MATCHID, new String[] {Integer.toString(nextPlayingMatch.getMatchID())},null);
        if(cursor.moveToFirst() && cursor.getCount() > 0){
            nextPlayingMatchLeagueID = cursor.getInt(0);
            MyTeamFragment fragment = (MyTeamFragment) mFragmentManager.findFragmentByTag("HOMEPAGETAG");
            fragment.setLeagueID(nextPlayingMatchLeagueID);
        }
        cursor.close();
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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
    public void onResultsItemSelected(int position) {

    }

    @Override
    public void onLeagueItemSelected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
