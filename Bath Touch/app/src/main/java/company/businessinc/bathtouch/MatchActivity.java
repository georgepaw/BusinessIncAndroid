package company.businessinc.bathtouch;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;


public class MatchActivity extends ActionBarActivity implements LeagueFragment.LeagueCallbacks,
        MatchFactsFragment.OnFragmentInteractionListener, AvailablePlayersFragment.AvailablePlayersListener {

    private static final String TAG = "MatchActivty";
    private String mTeamOneName,mTeamTwoName;
    private Integer mLeagueID, mMatchID, mTeamOneScore, mTeamTwoScore;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private String mPlace;
    private Date mDate;

    private int NUMTABS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            try {
                mTeamOneName = extras.getString(Match.KEY_TEAMONE);
                mTeamOneScore = extras.getInt(Match.KEY_TEAMONEPOINTS);
                mTeamTwoName = extras.getString(Match.KEY_TEAMTWO);
                mTeamTwoScore = extras.getInt(Match.KEY_TEAMTWOPOINTS);
                mMatchID = extras.getInt(Match.KEY_MATCHID);
                mPlace = extras.getString(Match.KEY_PLACE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                mDate = sdf.parse(extras.getString(Match.KEY_DATETIME));
                mLeagueID = extras.getInt(League.KEY_LEAGUEID);
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse the bundle");
            }
        }

        RelativeLayout headerBox;
        ImageView teamOneImage, teamTwoImage;
        TextView teamOneText, teamTwoText, scoreText, dateText;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Match");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(DataStore.getInstance(getBaseContext()).getUserTeamColorPrimary()));


        headerBox = (RelativeLayout) findViewById(R.id.activity_match_header);
        headerBox.setBackgroundColor(DataStore.getInstance(getBaseContext()).getUserTeamColorPrimary());

        TextDrawable teamOneDrawable = TextDrawable.builder()
                .buildRound(mTeamOneName.substring(0,1).toUpperCase(), Color.RED);

        TextDrawable teamTwoDrawable = TextDrawable.builder()
                .buildRound(mTeamTwoName.substring(0,1).toUpperCase(), Color.BLUE);

        teamOneImage = (ImageView) findViewById(R.id.activity_match_header_team_one_image);
        teamOneImage.setImageDrawable(teamOneDrawable);

        teamTwoImage = (ImageView) findViewById(R.id.activity_match_header_team_two_image);
        teamTwoImage.setImageDrawable(teamTwoDrawable);

        teamOneText = (TextView) findViewById(R.id.activity_match_header_team_one_text);
        teamOneText.setText(mTeamOneName);
        teamTwoText = (TextView) findViewById(R.id.activity_match_header_team_two_text);
        teamTwoText.setText(mTeamTwoName);


        scoreText = (TextView) findViewById(R.id.activity_match_header_score);
        scoreText.setText(mTeamOneScore + " - " + mTeamTwoScore);
        dateText = (TextView) findViewById(R.id.activity_match_header_date);
        DateFormatter sdf = new DateFormatter();
        dateText.setText(sdf.format(mDate));

        mViewPager = (ViewPager) findViewById(R.id.activity_match_view_pager);

        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.activity_match_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setBackgroundColor(DataStore.getInstance(this).getUserTeamColorPrimary());
        mSlidingTabLayout.setSelectedIndicatorColors(DataStore.getInstance(this).getUserTeamColorSecondary());

        Resources res = getResources();
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setEnablePadding(false);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);

        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLeagueItemSelected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /*
    Called when the create new ghost player is selected
    Starts a new create player flow intent
     */
    public void startCreateGhostPlayerIntent(){
        Intent intent = new Intent(MatchActivity.this, CreateAccountActivity.class);
        Bundle args = new Bundle();
        args.putBoolean("ghost", true);
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }

    /*
    Implemented interface from available players fragment
    when create ghost player button is pressed
     */
    @Override
    public void createGhostPlayerEvent() {
        Log.d("TEAMROSTERACTIVITY", "creating new intent");
        startCreateGhostPlayerIntent();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    AvailablePlayersFragment frag = AvailablePlayersFragment.newInstance(mMatchID);
                    return frag;
                case 1:
                    LeagueFragment leagueFragment = LeagueFragment.newInstance(mLeagueID);
                    return leagueFragment;
                default:
                    MatchFactsFragment matchFactsFragment = MatchFactsFragment.newInstance("a", "b");
                    return matchFactsFragment;
            }

        }

        @Override
        public int getCount() {
            return NUMTABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Players";
                case 1:
                    return "Table";
                case 2:
                    return "Head-to-head";
                case 3:
                    return "Match Facts";
                default:
                    return "null";
            }
        }
    }
}
