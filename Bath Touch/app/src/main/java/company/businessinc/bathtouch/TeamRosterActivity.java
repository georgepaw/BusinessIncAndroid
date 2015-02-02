package company.businessinc.bathtouch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import company.businessinc.bathtouch.adapters.AvailablePlayersAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;


/**
 * Created by user on 29/01/15.
 * http://www.android4devs.com/2015/01/how-to-make-material-design-sliding-tabs.html
 */
public class TeamRosterActivity extends FragmentActivity implements ActionBar.TabListener,
        AvailablePlayersAdapter.AvailablePlayerCallbacks, LoaderManager.LoaderCallbacks<Cursor> {

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

    private ArrayList<Integer> playerListAvail = new ArrayList<Integer>();
    private ArrayList<Integer> playerListUnavail = new ArrayList<Integer>();
    private int NUM_PEOPLE = 10;

    TextView mLeagueName, mMatchDate, mMatchTime, mMatchPlace, mTeamOneName, mTeamTwoName;

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

        playerListAvail = populatePeople();
        playerListUnavail = populatePeople();

        String teamOne = "NULL";
        String teamTwo = "NULL";
        String place = "NULL";
        Date date = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                teamOne = extras.getString(Match.KEY_TEAMONE);
                teamTwo = extras.getString(Match.KEY_TEAMTWO);
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
        mTeamOneName = (TextView) findViewById(R.id.team_roster_team1_name);
        mTeamTwoName = (TextView) findViewById(R.id.team_roster_team2_name);

        mLeagueName.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        mMatchDate.setText(sdf.format(date));
        sdf = new SimpleDateFormat("HH:mm");
        mMatchTime.setText(sdf.format(date));
        mMatchPlace.setText(place);
        mTeamOneName.setText(teamOne);
        mTeamTwoName.setText(teamTwo);

        getSupportLoaderManager().initLoader(DBProviderContract.ALLLEAGUES_URL_QUERY, null, this);

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
    public void onPlayerSelected(int playerID) {
        showPlayerInfo(playerID);
    }

    public void showPlayerInfo(int playerID) {

//        DialogFragment newFragment = PlayerInfoDialog
//                .newInstance(R.string.alert_dialog_two_buttons_title);
//        newFragment.show(getFragmentManager(), "dialog");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance(1);
        newFragment.show(ft, "dialog");
    }

    private ArrayList<Integer> populatePeople() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < NUM_PEOPLE; i++) {
            list.add(i);
        }
        return list;
    }

    public static class MyDialogFragment extends DialogFragment {
        int mNum;

        /**
         * Create a new instance of MyDialogFragment, providing "num"
         * as an argument.
         */
        static MyDialogFragment newInstance(int num) {
            MyDialogFragment f = new MyDialogFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments().getInt("num");

            // Pick a style based on the num.
            int style = DialogFragment.STYLE_NORMAL, theme = 0;
            switch ((mNum-1)%6) {
                case 1: style = DialogFragment.STYLE_NO_TITLE; break;
                case 2: style = DialogFragment.STYLE_NO_FRAME; break;
                case 3: style = DialogFragment.STYLE_NO_INPUT; break;
                case 4: style = DialogFragment.STYLE_NORMAL; break;
                case 5: style = DialogFragment.STYLE_NORMAL; break;
                case 6: style = DialogFragment.STYLE_NO_TITLE; break;
                case 7: style = DialogFragment.STYLE_NO_FRAME; break;
                case 8: style = DialogFragment.STYLE_NORMAL; break;
            }
            switch ((mNum-1)%6) {
                case 4: theme = android.R.style.Theme_Holo; break;
                case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
                case 6: theme = android.R.style.Theme_Holo_Light; break;
                case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
                case 8: theme = android.R.style.Theme_Holo_Light; break;
            }
            setStyle(style, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dialog, container, false);
            View tv = v.findViewById(R.id.text);
            ((TextView)tv).setText("Dialog #" + mNum + ": using style ");

//            // Watch for button clicks.
//            Button button = (Button)v.findViewById(R.id.show);
//            button.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    // When button is clicked, call up to owning activity.
//                    ((FragmentDialog)getActivity()).showDialog();
//                }
//            });

            return v;
        }
    }

    class SamplePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Integer> playerListAvail = new ArrayList<Integer>();
        private ArrayList<Integer> playerListUnavail = new ArrayList<Integer>();
        private ArrayList<ArrayList<Integer>> playerList = new ArrayList<ArrayList<Integer>>();

        public SamplePagerAdapter(FragmentManager fm, ArrayList<Integer> availP, ArrayList<Integer> unavailP) {
            super(fm);
            playerListAvail = availP;
            playerListUnavail = unavailP;
            playerList.add(playerListAvail);
            playerList.add(playerListUnavail);

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
                return "Available Players";
            } else if (position == 1) {
                return "Unavailable Players";
            }
            return "Item " + (position + 1);
        }

        @Override
        public Fragment getItem(int position) {

            return AvailablePlayersFragment.newInstance(position, playerList.get(position));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                return new CursorLoader(this, DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI, null, DBProviderContract.SELECTION_LEAGUEID, new String[]{Integer.toString(leagueID)}, null);
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
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
