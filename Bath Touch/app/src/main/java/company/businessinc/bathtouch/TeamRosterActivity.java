package company.businessinc.bathtouch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import company.businessinc.bathtouch.adapters.AvailablePlayersAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Player;


/**
 * Created by user on 29/01/15.
 * http://www.android4devs.com/2015/01/how-to-make-material-design-sliding-tabs.html
 */
public class TeamRosterActivity extends FragmentActivity implements ActionBar.TabListener,
        AvailablePlayersAdapter.AvailablePlayerCallbacks, LoaderManager.LoaderCallbacks<Cursor>, AvailablePlayersFragment.AvailablePlayersListener {

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;

    private ArrayList<Player> playerListAvail = new ArrayList<Player>();
    private ArrayList<Player> playerListUnavail = new ArrayList<Player>();
//    private int NUM_PEOPLE = 10;

    TextView mLeagueName, mMatchDate, mMatchTime, mMatchPlace, mTeamOneName, mTeamTwoName;
    Button mGhostPlayer;

    private static final String TAG = "TeamRosterActivity";

    private int leagueID;
    private int matchID;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team_roster);

        String teamTwo = "NULL";
        String place = "NULL";
        Date date = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                teamTwo = DataStore.getInstance(this).getUserTeam().equals(extras.getString(Match.KEY_TEAMTWO)) ? extras.getString(Match.KEY_TEAMONE) : extras.getString(Match.KEY_TEAMTWO);
                matchID = extras.getInt(Match.KEY_MATCHID);
                place = extras.getString(Match.KEY_PLACE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                date = sdf.parse(extras.getString(Match.KEY_DATETIME));
                leagueID = extras.getInt(League.KEY_LEAGUEID);
            } catch (Exception e) {
                Log.d(TAG, "Couldn't parse the bundle");
            }
        }

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.team_roster_pager);
        mPagerAdapter = new SamplePagerAdapter(getSupportFragmentManager(), playerListAvail, playerListUnavail);
        mViewPager.setAdapter(mPagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setEnablePadding(false);
        mSlidingTabLayout.setViewPager(mViewPager);

        Display disp = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        mLeagueName = (TextView) findViewById(R.id.team_roster_league);
        mMatchDate = (TextView) findViewById(R.id.team_roster_match_date);
        mMatchTime = (TextView) findViewById(R.id.team_roster_match_time);
        mMatchPlace = (TextView) findViewById(R.id.team_roster_match_place);
        mTeamTwoName = (TextView) findViewById(R.id.team_roster_team_1_name);

        mGhostPlayer = (Button) findViewById(R.id.team_roster_create_ghost);
        mGhostPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateGhostPlayerIntent();
            }
        });

        mLeagueName.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        mMatchDate.setText(sdf.format(date));
        sdf = new SimpleDateFormat("HH:mm");
        mMatchTime.setText(sdf.format(date));
        mMatchPlace.setText(place);
//        mTeamOneName.setText(teamOne);
        mTeamTwoName.setText(teamTwo);

//        getSupportLoaderManager().initLoader(DBProviderContract.ALLLEAGUES_URL_QUERY, null, this);

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onPlayerAvailableChecked(boolean available, int playerID) {


    }

    /*
    Fired when a player icon is selected in the recyclerview
    Shows the player info
     */
    @Override
    public void onPlayerSelected(Player player) {
        showPlayerInfo(player);
    }

    /*
    Creates a new dialog Fragment showing the details of the player selected
     */
    public void showPlayerInfo(Player player) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance(player);
        newFragment.show(ft, "dialog");
    }

    /*
    Called when the create new ghost player is selected
    Starts a new create player flow intent
     */
    public void startCreateGhostPlayerIntent(){
        Intent intent = new Intent(TeamRosterActivity.this, CreateAccountActivity.class);
        Bundle args = new Bundle();
        args.putBoolean("ghost", true);
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }

    @Override
    public void createGhostPlayerEvent() {
        Log.d("TEAMROSTERACTIVITY", "creating new intent");
        startCreateGhostPlayerIntent();
    }

//    private ArrayList<Integer> populatePeople(int start) {
//        ArrayList<Integer> list = new ArrayList<Integer>();
//        for (int i = start; i < NUM_PEOPLE + start; i++) {
//            list.add(i);
//        }
//        return list;
//    }

    public static class MyDialogFragment extends DialogFragment {
        TextView mName, mEmail, mIsGhost;

        /**
         * Create a new instance of MyDialogFragment, providing "num"
         * as an argument.
         */
        static MyDialogFragment newInstance(Player player) {
            MyDialogFragment f = new MyDialogFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putString("name", player.getName());
            args.putString("email", player.getEmail());
            args.putBoolean("ghost", player.getIsGhostPlayer());
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            int style = DialogFragment.STYLE_NO_TITLE;
            int theme = android.R.style.Theme_Holo_Light_Dialog;
            setStyle(style, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dialog, container, false);
            mName = (TextView) v.findViewById(R.id.user_dialog_username);
            mEmail = (TextView) v.findViewById(R.id.user_dialog_user_emailtext);
            mIsGhost = (TextView) v.findViewById(R.id.user_dialog_isGhost);
            Bundle b = getArguments();
            if(b!=null){
                mName.setText(b.getString("name"));
                mEmail.setText(b.getString("email"));
                if(b.getBoolean("ghost")){
                    mIsGhost.setText("Yes");
                } else {
                    mIsGhost.setText("No");
                }
            }
            return v;
        }
    }

    class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm, ArrayList<Player> availP, ArrayList<Player> unavailP) {
            super(fm);

        }

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }


        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p/>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Match Team";
            } else if (position == 1) {
                return "Player List";
            }
            return "Item " + (position + 1);
        }

        @Override
        public Fragment getItem(int position) {
            return AvailablePlayersFragment.newInstance(position, matchID);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                return new CursorLoader(this, DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI, null, DBProviderContract.SELECTION_LEAGUEID, new String[]{Integer.toString(leagueID)}, null);
//            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY:
//                return new CursorLoader(this, DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_CONTENTURI, null, DBProviderContract.SELECTION_MATCHID, new String[]{Integer.toString(matchID)}, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                if (data.moveToFirst()) {
                    while (!data.isAfterLast()) {
                        mLeagueName.setText(data.getString(1));
                        data.moveToNext();
                    }
                }
                break;
//            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY:
//                if (data.moveToFirst()) {
//                    while (!data.isAfterLast()) {
//                        Player player = new Player(data);
//                        if(player.getIsPlaying()){
//                            playerListAvail.add(player);
//                        } else {
//                            playerListUnavail.add(player);
//                        }
//                        data.moveToNext();
//                    }
//                }
//                break;
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
