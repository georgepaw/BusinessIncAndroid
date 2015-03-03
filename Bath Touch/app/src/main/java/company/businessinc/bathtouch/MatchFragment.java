package company.businessinc.bathtouch;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;


public class MatchFragment extends Fragment implements LeagueFragment.LeagueCallbacks,
        MatchFactsFragment.OnFragmentInteractionListener, AvailablePlayersFragment.AvailablePlayersListener {

    private static final String TAG = "MatchActivty";
    private String mTeamOneName,mTeamTwoName;
    private Integer mLeagueID, mMatchID, mTeamOneScore, mTeamTwoScore;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private String mPlace;
    private Date mDate;
    private boolean mHasBeenPlayed = false;
    private View mLayout;

    private static final String ANON_PRIMARY = "#ff0000";
    private static final String ANON_SECONDARY = "#ffffff";

    private static final String[] headersLoggedIn = new String[]{"Players", "Table"};
    private static final String[] headersAnon = new String[]{"Table"};

    public static MatchFragment newInstance(Bundle args){
        MatchFragment frag = new MatchFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.activity_match, container, false);

        Bundle extras = getArguments();
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
                mHasBeenPlayed = extras.getBoolean("hasBeenPlayed");
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse the bundle");
            }
        }

        RelativeLayout headerBox;
        ImageView teamOneImage, teamTwoImage;
        TextView teamOneText, teamTwoText, scoreText, dateText;

//        Toolbar toolbar = (Toolbar) mLayout.findViewById(R.id.toolbar);
//        toolbar.setTitle("Match");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(DataStore.getInstance(getBaseContext()).getUserTeamColorPrimary()));

        int primaryColour;
        int secondaryColour;
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            primaryColour = DataStore.getInstance(getActivity()).getUserTeamColorPrimary();
            secondaryColour = DataStore.getInstance(getActivity()).getUserTeamColorSecondary();
        } else {
            primaryColour = Color.parseColor(ANON_PRIMARY);
            secondaryColour = Color.parseColor(ANON_SECONDARY);
        }

        headerBox = (RelativeLayout) mLayout.findViewById(R.id.activity_match_header);
        headerBox.setBackgroundColor(primaryColour);

        TextDrawable teamOneDrawable = TextDrawable.builder()
                .buildRound(mTeamOneName.substring(0,1).toUpperCase(), Color.RED); //TODO remove this hardcoding

        TextDrawable teamTwoDrawable = TextDrawable.builder()
                .buildRound(mTeamTwoName.substring(0,1).toUpperCase(), Color.BLUE);

        teamOneImage = (ImageView) mLayout.findViewById(R.id.activity_match_header_team_one_image);
        teamOneImage.setImageDrawable(teamOneDrawable);

        teamTwoImage = (ImageView) mLayout.findViewById(R.id.activity_match_header_team_two_image);
        teamTwoImage.setImageDrawable(teamTwoDrawable);

        teamOneText = (TextView) mLayout.findViewById(R.id.activity_match_header_team_one_text);
        teamOneText.setText(mTeamOneName);
        teamTwoText = (TextView) mLayout.findViewById(R.id.activity_match_header_team_two_text);
        teamTwoText.setText(mTeamTwoName);


        scoreText = (TextView) mLayout.findViewById(R.id.activity_match_header_score);
        scoreText.setText(mTeamOneScore + " - " + mTeamTwoScore);
        dateText = (TextView) mLayout.findViewById(R.id.activity_match_header_date);
        DateFormatter sdf = new DateFormatter();
        dateText.setText(sdf.format(mDate));

        mViewPager = (ViewPager) mLayout.findViewById(R.id.activity_match_view_pager);

        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) mLayout.findViewById(R.id.activity_match_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setBackgroundColor(primaryColour);
        mSlidingTabLayout.setSelectedIndicatorColors(secondaryColour);

        Resources res = getResources();
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setEnablePadding(false);
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
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

        mViewPagerAdapter.notifyDataSetChanged();
        return mLayout;
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
        Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
        Bundle args = new Bundle();
        args.putBoolean("ghost", true);
        intent.putExtras(args);
        getActivity().startActivity(intent);
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
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            String switchString;
            if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                switchString = position < headersLoggedIn.length ? headersLoggedIn[position] : "null";
            } else {
                switchString = position < headersAnon.length ? headersAnon[position] : "null";
            }
            switch (switchString) {
                case "Players":
                    return AvailablePlayersFragment.newInstance(mMatchID, mHasBeenPlayed);
                case "Table":
                    return LeagueFragment.newInstance(mLeagueID);
                default:
                    return MatchFactsFragment.newInstance("a", "b");
            }

        }

        @Override
        public int getCount() {
            if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                return headersLoggedIn.length;
            } else{
                return headersAnon.length;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                return position < headersLoggedIn.length ? headersLoggedIn[position] : "null";
            } else {
                return position < headersAnon.length ? headersAnon[position] : "null";
            }
        }
    }
}
