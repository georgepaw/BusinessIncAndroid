package company.businessinc.bathtouch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import org.json.JSONException;
import org.json.JSONObject;

import company.businessinc.bathtouch.adapters.HomePageAdapter;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.UserReset;
import company.businessinc.networking.APICall;


public class MainActivity extends ActionBarActivity
        implements HomePageFragment.HomePageCallbacks,
        TeamResultsFragment.TeamResultsCallbacks,
        LeagueTableFragment.LeagueTableCallbacks,
        HomePageAdapter.homePageAdapterCallbacks{

    private SharedPreferences mSharedPreferences;
    private static final String USERLOGGEDIN = "login";
    private static final String COOKIE = "cookie";
    private static final String USER = "user";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerFrameLayout mNavigationDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager mFragmentManager;

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
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, HomePageFragment.newInstance(), "fragmentTag")
                    .commit();
        }

        mNavigationDrawerLayout = (DrawerFrameLayout) findViewById(R.id.drawer_layout);
//        DrawerView navigationDrawer = (DrawerView) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);

        if(!DataStore.getInstance(this).isUserLoggedIn()) {
            mNavigationDrawerLayout.setProfile(
                    new DrawerProfile()
                            .setAvatar(getResources().getDrawable(R.color.accent_material_light))
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
            mNavigationDrawerLayout.setProfile(
                    new DrawerProfile()
                            .setAvatar(getResources().getDrawable(R.color.accent_material_light))
                            .setBackground(getResources().getDrawable(R.color.primary))
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
                        .setImage(getResources().getDrawable(R.drawable.ic_thumb_up))
                        .setTextPrimary("Home")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                changeFragments("HOMEPAGETAG");
                            }
                        })
        );
        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_thumbs_up_down))
                        .setTextPrimary("League Tables")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                Toast.makeText(MainActivity.this, "No league tables fragment yet", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_thumb_up))
                        .setTextPrimary("Previous Results")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                changeFragments("TEAMRESULTSTAG");
                            }
                        })
        );
        if(DataStore.getInstance(this).isUserCaptain()) { //TODO This call is hardcoded, should work later
            mNavigationDrawerLayout.addItem(
                    new DrawerItem()
                            .setImage(getResources().getDrawable(R.drawable.ic_thumbs_up_down))
                            .setTextPrimary("Organise Team")
                            .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                                @Override
                                public void onClick(DrawerItem drawerItem, int id, int position) {
                                    Toast.makeText(MainActivity.this, "No team organise fragment yet", Toast.LENGTH_SHORT).show();
                                }
                            })
            );
        }
        mNavigationDrawerLayout.addDivider();
        mNavigationDrawerLayout.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_thumb_down))
                        .setTextPrimary("Settings")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, int id, int position) {
                                Toast.makeText(MainActivity.this, "No settings activity yet", Toast.LENGTH_SHORT).show();
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
                invalidateOptionsMenu();
            }
        };

        setSupportActionBar(toolbar);

        mNavigationDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        mNavigationDrawerLayout.setDrawerListener(mDrawerToggle);
//        mNavigationDrawerLayout.closeDrawer(navigationDrawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_log_out) {
//            logOut();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onNavigationDrawerItemSelected(int position, String name) {
////        update the main content by replacing fragments
//        switch (position) {
//            case 0:
//                changeFragments("HOMEPAGETAG");
//                break;
//            case 1:
//                changeFragments("LEAGUETABLETAG");
//                break;
//            case 2:
//                Log.d("CALLLBK", "in callback on main acitivy");
//
//                changeFragments("TEAMRESULTSTAG");
//                break;
//            case 3:
//                logOut();
//            default:
//                break;
//        }
//    }

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
                //TODO Add a check to make sure nextMatch exists
                Match nextMatch = null;//DataStore.getInstance(this).getNextRefMatch();
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
            case HomePageAdapter.NEXTGAME:
                Log.d("CARDS", "Next game card selected");
                break;
            case HomePageAdapter.TABLE:
                Log.d("MATCH", "starting leage table activity");
                intent = new Intent(this, LeagueTableActivity.class);
                startActivity(intent);
//                changeFragments("LEAGUETABLETAG");
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
}
