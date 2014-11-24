package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class NavigationActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        HomePageFragment.HomePageCallbacks,
        TeamResultsFragment.TeamResultsCallbacks,
        LeagueTableFragment.LeagueTableCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        if(savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, TeamResultsFragment.newInstance())
                    .commit();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.navigation, menu);
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        switch (position) {
//            case 0:
//                view_home_page();
//                break;
//            case 1:
//                view_league_table();
//                break;
//            case 2:
//                view_team_results();
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onHomePageCardSelected(int position) {
        switch(position) {
            case 1:
                view_league_table();
                break;
            case 2:
                view_team_results();
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

    public void view_home_page() {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, HomePageFragment.newInstance())
                .commit();
    }

    public void view_team_results(){
        mFragmentManager.beginTransaction()
                .replace(R.id.container, TeamResultsFragment.newInstance())
                .commit();

    }

    public void view_league_table(){
        mFragmentManager.beginTransaction()
                .replace(R.id.container, LeagueTableFragment.newInstance())
                .commit();
    }
}
