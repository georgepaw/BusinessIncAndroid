package company.businessinc.bathtouch;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.melnykov.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.CachedRequest;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.endpoints.ScoreSubmit;
import company.businessinc.endpoints.ScoreSubmitInterface;
import company.businessinc.networking.CheckNetworkConnection;


public class MatchFragment extends Fragment implements LeagueFragment.LeagueCallbacks,
        MatchFactsFragment.OnFragmentInteractionListener, DBObserver {

    private static final String TAG = "MatchActivty";
    private String mTeamOneName,mTeamTwoName, mPostCode, mAddress;
    private Integer mLeagueID, mMatchID, mTeamOneScore, mTeamTwoScore, mTeamOneID, mTeamTwoID;
    private Float mLongitude, mLatitude;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private TextDrawable teamOneDrawable, teamTwoDrawable;
    private ImageView teamOneImage, teamTwoImage;
    private String mPlace;
    private Date mDate;
    private boolean mHasBeenPlayed = false;
    private View mLayout;

    private static final String ANON_PRIMARY = "#ff0000";
    private static final String ANON_SECONDARY = "#ffffff";

    private static final String[] headersLoggedIn = new String[]{"Players", "Table", "Map"};
    private static final String[] headersAnon = new String[]{"Table", "Map"};

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
                mTeamOneID = extras.getInt(Match.KEY_TEAMONEID);
                mTeamTwoID = extras.getInt(Match.KEY_TEAMTWOID);
                mTeamTwoName = extras.getString(Match.KEY_TEAMTWO);
                mTeamTwoScore = extras.getInt(Match.KEY_TEAMTWOPOINTS);
                mMatchID = extras.getInt(Match.KEY_MATCHID);
                mPlace = extras.getString(Match.KEY_PLACE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                mDate = sdf.parse(extras.getString(Match.KEY_DATETIME));
                mLeagueID = extras.getInt(League.KEY_LEAGUEID);
                mHasBeenPlayed = extras.getBoolean("hasBeenPlayed");
                mLatitude = extras.getFloat(Match.KEY_LATITUDE);
                mLongitude = extras.getFloat(Match.KEY_LONGITUDE);
                mPostCode = extras.getString(Match.KEY_POSTCODE);
                mAddress = extras.getString(Match.KEY_ADDRESS);
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse the bundle");
            }
        }

        RelativeLayout headerBox;
        ImageView teamOneImage, teamTwoImage;
        TextView teamOneText, teamTwoText, scoreText, dateText;

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("");
        int primaryColour;
        int secondaryColour;
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            primaryColour = DataStore.getInstance(getActivity()).getUserTeamColorPrimary();
            secondaryColour = DataStore.getInstance(getActivity()).getUserTeamColorSecondary();
        } else {
            primaryColour = Color.parseColor(ANON_PRIMARY);
            secondaryColour = Color.parseColor(ANON_SECONDARY);
        }
        actionBar.setBackgroundDrawable(new ColorDrawable(primaryColour));

        actionBar.setElevation(0f);

        setEnterTransition(new Fade());
        setReturnTransition(new Fade());

        headerBox = (RelativeLayout) mLayout.findViewById(R.id.activity_match_header);
        headerBox.setBackgroundColor(primaryColour);
        FloatingActionButton fab = (FloatingActionButton) mLayout.findViewById(R.id.submit_score_fab);
        if(DataStore.getInstance(getActivity()).amIRefing(mMatchID)) {
            fab.setImageResource(R.drawable.ic_whistle_white600_24dp);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSubmitScoreDialog();
                }
            });
        } else if(DataStore.getInstance(getActivity()).isUserLoggedIn() && DataStore.getInstance(getActivity()).isUserCaptain() && !mHasBeenPlayed) {
            fab.setImageResource(R.drawable.ic_add_small_24dp);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCreateGhostPlayerIntent();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        setCircles();

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

//        ImageView googleMaps = (ImageView) mLayout.findViewById(R.id.google_maps_click);
//        googleMaps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f (%s)", mLatitude, mLongitude, "Where the party is at");
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("geo:" + mLatitude + "," + mLongitude
//                                + "?q=" + mLatitude + "," + mLongitude + "(" + mAddress + ")"));
//                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                try {
//                    startActivity(intent); //first try opening google maps
//                } catch(ActivityNotFoundException e) {
//                    try { //no google apps, try any maps app
//                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                        startActivity(unrestrictedIntent);
//                    } catch(ActivityNotFoundException inE){
//                        Toast.makeText(getActivity(), "Please install a maps application.", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });

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
        DataStore.getInstance(getActivity()).registerLeagueTeamsDBObserver(this);
        DataStore.getInstance(getActivity()).registerMyUpcomingRefereeDBObserver(this);
        mViewPagerAdapter.notifyDataSetChanged();
        return mLayout;
    }

    @Override
    public void onDestroyView(){
        DataStore.getInstance(getActivity()).unregisterLeagueTeamsDBObserver(this);
        DataStore.getInstance(getActivity()).unregisterMyUpcomingRefereeDBObserver(this);
        super.onDestroyView();
    }

    @Override
    public void onLeagueItemSelected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showSubmitScoreDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Submit Scores")
                .customView(R.layout.dialog_submit_score, true)
                .positiveText("Submit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        CheckBox mTeamCaptainConfirm = (CheckBox) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_confirm);
                        CheckBox mTeamOneForfeit = (CheckBox) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_forfeit);
                        CheckBox mTeamTwoForfeit = (CheckBox) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_forfeit);
                        MaterialEditText mTeamOneEditText = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_name);
                        MaterialEditText mTeamTwoEditText = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_name);

                        if(!mTeamCaptainConfirm.isChecked()){
                            Toast.makeText(getActivity(), "Captains need to approve scores", Toast.LENGTH_SHORT).show();
                        } else if(mTeamOneForfeit.isChecked() && mTeamTwoForfeit.isChecked()) {
                            Toast.makeText(getActivity(), "Both teams can't forfeit", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean mIsForfeit = false;
                            if(mTeamOneForfeit.isChecked()){
                                mTeamOneScore = 0;
                                mTeamTwoScore = 10;
                                mIsForfeit = true;
                            } else if(mTeamTwoForfeit.isChecked()){
                                mTeamOneScore = 10;
                                mTeamTwoScore = 0;
                                mIsForfeit = true;
                            } else {
                                try{
                                    mTeamOneScore = Integer.valueOf(mTeamOneEditText.getText().toString());
                                } catch(NumberFormatException e){
                                    Toast.makeText(getActivity(), "Enter score for " + mTeamOneName, Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                try{
                                    mTeamTwoScore = Integer.valueOf(mTeamTwoEditText.getText().toString());
                                } catch(NumberFormatException e){
                                    Toast.makeText(getActivity(), "Enter score for " + mTeamTwoName, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(CheckNetworkConnection.check(getActivity())) {
                                ScoreSubmitInterface activity = null;
                                try {
                                    activity = (ScoreSubmitInterface) getActivity();
                                } catch (ClassCastException e) {
                                    throw new ClassCastException(activity.toString()
                                            + " must implement ScoreSubmitInteface");
                                }
                                new ScoreSubmit((ScoreSubmitInterface) getActivity(), getActivity(), mMatchID, mTeamOneScore, mTeamTwoScore, mIsForfeit).execute();
                                TextView scoreText = (TextView) mLayout.findViewById(R.id.activity_match_header_score);
                                scoreText.setText(mTeamOneScore + " - " + mTeamTwoScore);
                            } else {
                                Toast.makeText(getActivity(), "No network connection, the score will be submitted automatically", Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("matchID", Integer.toString(mMatchID));
                                    jsonObject.put("teamOneScore", Integer.toString(mTeamOneScore));
                                    jsonObject.put("teamTwoScore", Integer.toString(mTeamTwoScore));
                                    jsonObject.put("isForfeit", Boolean.toString(mIsForfeit));
                                    DataStore.getInstance(getActivity()).cacheRequest(new CachedRequest(CachedRequest.RequestType.SUBMITSCORE, jsonObject));
                                } catch (JSONException e){

                                }
                            }
                        }
                    }
                })
                .build();

        Team teamOne = DataStore.getInstance(getActivity()).getTeam(mLeagueID,mTeamOneID);
        Team teamTwo = DataStore.getInstance(getActivity()).getTeam(mLeagueID,mTeamTwoID);

        TextDrawable teamOneAvatar = TextDrawable.builder()
                .buildRound(mTeamOneName.substring(0,1),
                        Color.parseColor(teamOne.getTeamColorPrimary()));
        TextDrawable teamTwoAvatar = TextDrawable.builder()
                .buildRound(mTeamTwoName.substring(0,1),
                        Color.parseColor(teamTwo.getTeamColorPrimary()));
        ImageView teamOneImage = (ImageView) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_image);
        ImageView teamTwoImage = (ImageView) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_image);
        teamOneImage.setImageDrawable(teamOneAvatar);
        teamTwoImage.setImageDrawable(teamTwoAvatar);

        MaterialEditText teamOneInput = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_name);
        MaterialEditText teamTwoInput = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_name);
        teamOneInput.setHint(mTeamOneName + "'s score");
        teamOneInput.setFloatingLabelText(mTeamOneName + "'s score");
        teamTwoInput.setHint(mTeamTwoName + "'s score");
        teamTwoInput.setFloatingLabelText(mTeamTwoName + "'s score");
        dialog.show();
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
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.LEAGUETEAMS_TABLE_NAME:
                    if(mViewPager!=null) {
                        mViewPager.getAdapter().notifyDataSetChanged();
                        setCircles();
                    }
                break;
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME:
                if(DataStore.getInstance(getActivity()).amIRefing(mMatchID)) {
                    ((FloatingActionButton) mLayout.findViewById(R.id.submit_score_fab)).setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            String switchString;
            if(displayLoggedInHeaders()){
                switchString = position < headersLoggedIn.length ? headersLoggedIn[position] : "null";
            } else {
                switchString = position < headersAnon.length ? headersAnon[position] : "null";
            }
            switch (switchString) {
                case "Players":
                    return AvailablePlayersFragment.newInstance(mMatchID, mHasBeenPlayed);
                case "Table":
                    return LeagueFragment.newInstance(mLeagueID);
                case "Map":
                    return MyMapFragment.newInstance(mLongitude, mLatitude, mAddress, mPostCode, mPlace);
                default:
                    return MatchFactsFragment.newInstance("a", "b");
            }

        }

        @Override
        public int getCount() {
            if(displayLoggedInHeaders()){
                return headersLoggedIn.length;
            } else{
                return headersAnon.length;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(displayLoggedInHeaders()){
                return position < headersLoggedIn.length ? headersLoggedIn[position] : "null";
            } else {
                return position < headersAnon.length ? headersAnon[position] : "null";
            }
        }

        private boolean displayLoggedInHeaders(){
            return DataStore.getInstance(getActivity()).isUserLoggedIn() && (DataStore.getInstance(getActivity()).getUserTeamID() == mTeamOneID || DataStore.getInstance(getActivity()).getUserTeamID() == mTeamTwoID);
        }
    }

    private void setCircles(){
        int colourTeamOne;
        int colourTeamTwo;
        Team teamOne = DataStore.getInstance(getActivity()).getTeam(mLeagueID,mTeamOneID);
        Team teamTwo = DataStore.getInstance(getActivity()).getTeam(mLeagueID,mTeamTwoID);
        if(teamOne != null){
            colourTeamOne = Color.parseColor(teamOne.getTeamColorPrimary());
        } else {
            colourTeamOne = getActivity()
                    .getResources().getColor(R.color.dark_divider);
        }

        if(teamTwo != null){
            colourTeamTwo = Color.parseColor(teamTwo.getTeamColorPrimary());
        } else {
            colourTeamTwo = getActivity()
                    .getResources().getColor(R.color.dark_divider);
        }

        teamOneDrawable = TextDrawable.builder()
                .buildRound(mTeamOneName.substring(0,1).toUpperCase(), colourTeamOne);

        teamTwoDrawable = TextDrawable.builder()
                .buildRound(mTeamTwoName.substring(0,1).toUpperCase(), colourTeamTwo);

        teamOneImage = (ImageView) mLayout.findViewById(R.id.activity_match_header_team_one_image);
        teamOneImage.setImageDrawable(teamOneDrawable);

        teamTwoImage = (ImageView) mLayout.findViewById(R.id.activity_match_header_team_two_image);
        teamTwoImage.setImageDrawable(teamTwoDrawable);
    }
}
