package company.businessinc.bathtouch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.User;
import company.businessinc.networking.APICall;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        HomePageFragment.HomePageCallbacks,
        TeamResultsFragment.TeamResultsCallbacks,
        LeagueTableFragment.LeagueTableCallbacks{

    private SharedPreferences mSharedPreferences;
    private String userLoggedIn = "login";
    private static final String cookie = "Cookie";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        if(!mSharedPreferences.contains(userLoggedIn)) {
            Log.d("MAIN", "HAS NOT LOGGED IN");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if(mSharedPreferences.getBoolean(userLoggedIn,false) && !mSharedPreferences.contains(cookie)) { //user logged in but no cookie string, kick him out
            Log.d("MAIN", "USER LOGGED IN BUT NO COOKIE");
            mSharedPreferences.edit().remove(userLoggedIn).commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if(mSharedPreferences.contains(cookie)){
            APICall.setCookie(mSharedPreferences.getString(cookie, ""));
        }
        if(mSharedPreferences.getBoolean(userLoggedIn,false) != User.isLoggedIn()){
            try{
                new User(new JSONObject(mSharedPreferences.getString("user", "")));
            } catch (JSONException e){
                Log.d("MAIN LOGIN", "CAN'T PARSE THE USER");
            }
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, HomePageFragment.newInstance())
                    .commit();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer_fragment,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String name) {
//        update the main content by replacing fragments
        switch (position) {
            case 0:
                changeFragments("HOMEPAGETAG");
                break;
            case 1:
                changeFragments("LEAGUETABLETAG");
                break;
            case 2:
                Log.d("CALLLBK", "in callback on main acitivy");

                changeFragments("TEAMRESULTSTAG");
                break;
            case 3:
                logOut();
            default:
                break;
        }
    }

    @Override
    public void onHomePageCardSelected(int position) {
        switch(position) {
            case 0:
                //TODO Add a check to make sure nextMatch exists
                Match nextMatch = DataStore.getNextMatch();
                if(nextMatch == null){
                    Log.d("ERROR", "match is null, not opening fragment");
                    break; //TODO fix this happeneing

                }
                Intent intent = new Intent(this, SubmitScoreActivity.class);
                intent.putExtra("matchId", nextMatch.getMatchID());
                intent.putExtra("teamOneName", nextMatch.getTeamOne());
                intent.putExtra("teamTwoName", nextMatch.getTeamTwo());
                startActivity(intent);
                break;
            case 1:
                changeFragments("TEAMRESULTSTAG");
                break;
            case 2:
                intent = new Intent(this, LeagueTableActivity.class);
                startActivity(intent);
//                changeFragments("LEAGUETABLETAG");
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
                ft.replace(R.id.container, HomePageFragment.newInstance(), tag);
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
        Intent intent = new Intent(this, LoginActivity.class);
        APICall.clearCookies();
        mSharedPreferences.edit().remove(userLoggedIn).commit();
        if(mSharedPreferences.contains(cookie)){
            mSharedPreferences.edit().remove(cookie).commit();
        }
        if(mSharedPreferences.contains("user")){
            mSharedPreferences.edit().remove("user").commit();
        }
        new User();
        DataStore.clearData();
        startActivity(intent);
        finish();
    }
}
