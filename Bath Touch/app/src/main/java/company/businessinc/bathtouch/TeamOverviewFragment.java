package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amulyakhare.textdrawable.TextDrawable;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TeamOverviewFragment extends Fragment implements DBObserver, SwipeRefreshLayout.OnRefreshListener{

    private TeamOverviewCallbacks mCallbacks;
    private View mLayout;
    private TextView mNextMatchHeader, mNextMatchName, mNextMatchPlace, mNextMatchDate,
            mNextRefMatchName, mNextRefMatchPlace, mNextRefMatchDate, mPastMatchesHeader,
            mLeagueHeader, mLeaguePoints, mLeagueWins, mLeagueDraws, mLeagueLosses;
    private FrameLayout mNextMatchDivider, mNextRefMatchDivider, mPastMatchesDivider;
    private ImageView mNextMatchImage, mNextRefMatchImage;
    private List<ImageView> mPastMatchesImages;
    private CheckBox mNextMatchCheckBox;
    private LinearLayout mNextMatchCheckBoxContainer;
    private RelativeLayout mNextMatchContainer, mNextRefMatchContainer, mPastMatchesContainer,
            mLeagueContainer;
    private Button mNextMatchManage, mNextRefMatchSubmit;

    private int mTeamID;
    private int mLeagueID;
    private League league;
    private SwipeRefreshLayout mSwipeRefresh;

    private CardView mNextMatchCard, mNextRefMatchCard, mPastMatchesCard, mLeagueCard;


    public static TeamOverviewFragment newInstance(int teamID, int leagueID) {
        TeamOverviewFragment fragment = new TeamOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(Team.KEY_TEAMID, teamID);
        args.putInt(League.KEY_LEAGUEID, leagueID);
        fragment.setArguments(args);
        return fragment;
    }

    public TeamOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TeamOverviewFragment", "onCreate called");
        if (getArguments() != null) {
            mTeamID = getArguments().getInt(Team.KEY_TEAMID);
            mLeagueID = getArguments().getInt(League.KEY_LEAGUEID);
        }
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()) {
            if(DataStore.getInstance(getActivity()).isReferee()) {
                DataStore.getInstance(getActivity()).registerMyUpcomingRefereeDBObserver(this);
            }
            if(!DataStore.getInstance(getActivity()).isUserCaptain()){
                DataStore.getInstance(getActivity()).registerMyUpcomingGameAvailabilitysDBObserver(this);
            }
            DataStore.getInstance(getActivity()).registerMyUpcomingGamesDBObserver(this);
            DataStore.getInstance(getActivity()).registerTeamsScoresDBObserver(this);
            DataStore.getInstance(getActivity()).registerTeamsFixturesDBObserver(this);
            DataStore.getInstance(getActivity()).registerLeagueTeamsDBObserver(this);
        } else {
            DataStore.getInstance(getActivity()).registerLeagueScoreDBObserver(this);
        }
        DataStore.getInstance(getActivity()).registerLeaguesStandingsDBObserver(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TeamOverviewFragment", "onCreateView called");

        mLayout = inflater.inflate(R.layout.fragment_team_overview, container, false);

        mSwipeRefresh = (SwipeRefreshLayout) (mLayout.findViewById(R.id.fragment_team_overview_swiperefresh));
        mSwipeRefresh.setOnRefreshListener(this);

//        mNextMatchContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_nextmatch);
        mNextMatchCard = (CardView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch);

//        mNextMatchDivider = (FrameLayout) mLayout.findViewById(R.id.fragment_team_overview_divider1);
        mNextMatchHeader = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_header);
        mNextMatchName = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_name);
        mNextMatchPlace = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_place);
        mNextMatchDate = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_date);

//        mPastMatchesContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_pastmatches);
        mPastMatchesCard = (CardView)  mLayout.findViewById(R.id.fragment_team_overview_pastmatches);
//        mPastMatchesDivider = (FrameLayout) mLayout.findViewById(R.id.fragment_team_overview_divider3);
        mPastMatchesHeader = (TextView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_header);

//        mNextRefMatchContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch);
        mNextRefMatchCard = (CardView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch);

//        mNextRefMatchDivider = (FrameLayout) mLayout.findViewById(R.id.fragment_team_overview_divider2);
//        if (!DataStore.getInstance(getActivity()).isReferee()) {
//            mNextRefMatchContainer.setVisibility(View.GONE);
//            mNextRefMatchDivider.setVisibility(View.GONE);
//        }
        mNextRefMatchName = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_name);
        mNextRefMatchPlace = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_place);
        mNextRefMatchDate = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_date);
        mNextRefMatchSubmit = (Button) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_submit);
        mNextRefMatchImage = (ImageView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_image);

        mNextMatchCheckBoxContainer = (LinearLayout) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_checkbox_container);
        mNextMatchCheckBox = (CheckBox) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_checkbox);
        mNextMatchManage = (Button) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_manage);

        mNextMatchImage = (ImageView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_image);
        mPastMatchesImages = new ArrayList<>();
        mPastMatchesImages.add(0, (ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_1));
        mPastMatchesImages.add(1, (ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_2));
        mPastMatchesImages.add(2, (ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_3));
        mPastMatchesImages.add(3, (ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_4));
        mPastMatchesImages.add(4, (ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_5));

//        mLeagueContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_league);
        mLeagueCard = (CardView) mLayout.findViewById(R.id.fragment_team_overview_league);
        mLeagueHeader = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_header);
        mLeaguePoints = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_points);
        mLeagueWins = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_wins);
        mLeagueDraws = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_draws);
        mLeagueLosses = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_losses);


        mNextRefMatchCard.setVisibility(View.GONE);
        mNextMatchCard.setVisibility(View.GONE);
        mNextRefMatchCard.setVisibility(View.GONE);
        mPastMatchesCard.setVisibility(View.GONE);
        mLeagueCard.setVisibility(View.GONE);

        leagueChanged();

        return mLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (TeamOverviewCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TeamOverviewCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onDestroy(){
        //prevent mem leaks, unregister
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()) {
            if(DataStore.getInstance(getActivity()).isReferee()) {
                DataStore.getInstance(getActivity()).unregisterMyUpcomingRefereeDBObserver(this);
            }
            if(!DataStore.getInstance(getActivity()).isUserCaptain()){
                DataStore.getInstance(getActivity()).unregisterMyUpcomingGameAvailabilitysDBObserver(this);
            }
            DataStore.getInstance(getActivity()).unregisterMyUpcomingGamesDBObserver(this);
            DataStore.getInstance(getActivity()).unregisterTeamsScoresDBObserver(this);
            DataStore.getInstance(getActivity()).unregisterLeagueTeamsDBObserver(this);
        }
        DataStore.getInstance(getActivity()).unregisterLeaguesStandingsDBObserver(this);
        super.onDestroy();
    }

    private void notifyUIThread(){
        if(getActivity()!=null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ViewPager pager = (ViewPager) getActivity().findViewById(R.id.fragment_my_team_view_pager);
                    if(pager!=null) {
                        pager.getAdapter().notifyDataSetChanged();
                    }
                }
            });
        }
    }

    //query has finished
    @Override
    public void notify(String tableName, Object data) {
        switch(tableName) {
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME:
                if(DataStore.getInstance(getActivity()).getNextRefGame() != null) {
                    notifyUIThread();
                }
                break;
            case DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME:
                if(DataStore.getInstance(getActivity()).getNextGame()!=null){
                    notifyUIThread();
                }
                break;
            case DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME:
            case DBProviderContract.LEAGUETEAMS_TABLE_NAME:
            case DBProviderContract.LEAGUESSCORE_TABLE_NAME:
            case DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME:
                notifyUIThread();
                break;
        }
    }

    private void leagueChanged(){
        if (DataStore.getInstance(getActivity()).getNextGame() != null) {
            league = DataStore.getInstance(getActivity()).getLeague(mLeagueID);
        }
        if (league != null) {
            setLeague();
            setLeagueScores();
            setNextMatch(false);
        }
        setNextMatch(true);
    }

    public void setLeague(){
//        mLeagueContainer.setVisibility(View.VISIBLE);

        mLeagueCard.setVisibility(View.VISIBLE);
        String header = league.getLeagueName();
        List<LeagueTeam> leagueTeams = DataStore.getInstance(getActivity()).getLeagueStandings(league.getLeagueID());
        for(LeagueTeam team : leagueTeams) {
            if (team.getTeamID() == mTeamID) {
                int position = team.getPosition();
                switch (position % 10) {
                    case 1:
                        mLeagueHeader.setText(position + "st in " + header);
                        break;
                    case 2:
                        mLeagueHeader.setText(position + "nd in " + header);
                        break;
                    case 3:
                        mLeagueHeader.setText(position + "rd in " + header);
                        break;
                    default:
                        mLeagueHeader.setText(position + "th in " + header);
                        break;
                }
                mLeaguePoints.setText(team.getLeaguePoints().toString());
                int color = DataStore.getInstance(getActivity()).getUserTeamColorPrimary();
                mLeaguePoints.setTextColor(color);
                mLeagueWins.setText(team.getWin().toString());
                mLeagueWins.setTextColor(color);
                mLeagueDraws.setText(team.getDraw().toString());
                mLeagueDraws.setTextColor(color);
                mLeagueLosses.setText(team.getLose().toString());
                mLeagueLosses.setTextColor(color);
            }
        }
    }

    public void setNextMatch(boolean isRefMatch){
        if(isRefMatch && DataStore.getInstance(getActivity()).getNextRefGame() != null) {
            //            mNextRefMatchContainer.setVisibility(View.VISIBLE);
            mNextRefMatchCard.setVisibility(View.VISIBLE);

            final Match nextMatch = DataStore.getInstance(getActivity()).getNextRefGame();

            mNextRefMatchName.setText(nextMatch.getTeamOne() + " vs " + nextMatch.getTeamTwo());
            mNextRefMatchPlace.setText(nextMatch.getPlace());
            DateFormatter sdf = new DateFormatter();
            mNextRefMatchDate.setText(sdf.format(nextMatch.getDateTime()));
            mNextRefMatchSubmit.setVisibility(View.VISIBLE);
            mNextRefMatchSubmit.setTextColor(DataStore.getInstance(getActivity()).getUserTeamColorPrimary());
            mNextRefMatchSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SubmitScoreActivity.class);
                    intent.putExtra(Match.KEY_MATCHID, nextMatch.getMatchID());
                    intent.putExtra(Match.KEY_TEAMONE, nextMatch.getTeamOne());
                    intent.putExtra(Match.KEY_TEAMTWO, nextMatch.getTeamTwo());
                    startActivity(intent);
                }
            });

            //TODO show next team and color
            Drawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .toUpperCase()
                    .endConfig()
                    .buildRound("N", Color.RED);
            mNextRefMatchImage.setImageDrawable(drawable);
        } else if(!isRefMatch && DataStore.getInstance(getActivity()).getNextGame() != null) {
            final Match nextMatch = DataStore.getInstance(getActivity()).getNextGame();
            //            mNextMatchContainer.setVisibility(View.VISIBLE);
            mNextMatchCard.setVisibility(View.VISIBLE);

//            mNextMatchDivider.setVisibility(View.VISIBLE);
            if (nextMatch.getTeamOneID() == DataStore.getInstance(getActivity()).getUserTeamID()) {
                mNextMatchName.setText(nextMatch.getTeamTwo());
                TextDrawable avatar = TextDrawable.builder()
                        .buildRound(nextMatch.getTeamTwo().substring(0, 1), getActivity()
                                .getResources().getColor(R.color.dark_divider));
                mNextMatchImage.setImageDrawable(avatar);
            } else {
                mNextMatchName.setText(nextMatch.getTeamOne());
                TextDrawable avatar = TextDrawable.builder()
                        .buildRound(nextMatch.getTeamOne().substring(0, 1), getActivity()
                                .getResources().getColor(R.color.dark_divider));
                mNextMatchImage.setImageDrawable(avatar);
            }
            mNextMatchPlace.setText(nextMatch.getPlace());
            DateFormatter sdf = new DateFormatter();
            mNextMatchDate.setText(sdf.format(nextMatch.getDateTime()));
            final int nxtmtchID = nextMatch.getMatchID();
            if (!DataStore.getInstance(getActivity()).isUserCaptain()) {
                mNextMatchCheckBoxContainer.setVisibility(View.VISIBLE);
                mNextMatchCheckBox.setChecked(DataStore.getInstance(getActivity()).amIPlaying(nxtmtchID));
                mNextMatchCheckBox.setEnabled(true);
                mNextMatchCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DataStore.getInstance(getActivity()).setMyAvailability(isChecked, nxtmtchID);
                        mNextMatchCheckBox.setEnabled(false);
                    }
                });
            } else {
                mNextMatchCheckBoxContainer.setVisibility(View.GONE);
                mNextMatchManage.setVisibility(View.VISIBLE);
                mNextMatchManage.setTextColor(DataStore.getInstance(getActivity()).getUserTeamColorPrimary());
                mNextMatchManage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TeamRosterActivity.class);
                        intent.putExtra(Match.KEY_MATCHID, nextMatch.getMatchID());
                        intent.putExtra(Match.KEY_TEAMONE, nextMatch.getTeamOne());
                        intent.putExtra(Match.KEY_TEAMTWO, nextMatch.getTeamTwo());
                        intent.putExtra(Match.KEY_PLACE, nextMatch.getPlace());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                        intent.putExtra(Match.KEY_DATETIME, sdf.format(nextMatch.getDateTime()));
                        intent.putExtra(League.KEY_LEAGUEID, mLeagueID);
                        startActivity(intent);
                    }
                });
            }
        }

        refreshPage();
    }

    public void setLeagueScores(){
//        mPastMatchesContainer.setVisibility(View.VISIBLE);
        mPastMatchesCard.setVisibility(View.VISIBLE);
//        mPastMatchesDivider.setVisibility(View.VISIBLE);
        TextDrawable win = TextDrawable.builder()
                .buildRound("W", getActivity().getResources().getColor(R.color.darkgreen));
        TextDrawable draw = TextDrawable.builder()
                .buildRound("D", getActivity().getResources().getColor(R.color.darkorange));
        TextDrawable lose = TextDrawable.builder()
                .buildRound("L", getActivity().getResources().getColor(R.color.darkred));
        List<Match> pastMatches = DataStore.getInstance(getActivity()).getTeamScores(league.getLeagueID(),mTeamID);
        int max = Math.min(pastMatches.size(), 5);
        mPastMatchesHeader.setText("Last " + max + " matches");
        for (int i = 0; i < max; i++) {
            final Match match = pastMatches.get(i);
            if (match.getTeamOneID() == mTeamID) {
                if (match.getTeamOnePoints() > match.getTeamTwoPoints()) {
                    mPastMatchesImages.get(i).setImageDrawable(win);
                } else {
                    mPastMatchesImages.get(i).setImageDrawable(lose);
                }
            } else {
                if (match.getTeamOnePoints() < match.getTeamTwoPoints()) {
                    mPastMatchesImages.get(i).setImageDrawable(win);
                } else {
                    mPastMatchesImages.get(i).setImageDrawable(lose);
                }
            }
            if (match.getTeamOnePoints() == match.getTeamTwoPoints()) {
                mPastMatchesImages.get(i).setImageDrawable(draw);
            }
            final int lgId = mLeagueID;
            mPastMatchesImages.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MatchActivity.class);
                    Bundle args = new Bundle();
                    args.putString(Match.KEY_TEAMONE, match.getTeamOne());
                    args.putString(Match.KEY_TEAMTWO, match.getTeamTwo());
                    args.putInt(Match.KEY_TEAMONEPOINTS, match.getTeamOnePoints());
                    args.putInt(Match.KEY_TEAMTWOPOINTS, match.getTeamTwoPoints());
                    args.putInt("leagueID", lgId);
                    args.putInt(Match.KEY_MATCHID, match.getMatchID());
                    args.putString(Match.KEY_PLACE, match.getPlace());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    args.putString(Match.KEY_DATETIME, sdf.format(match.getDateTime()));
                    intent.putExtras(args);
                    startActivity(intent);
                }
            });
        }
        refreshPage();
    }

    public void setAvailability(){
        mNextMatchCheckBox.setChecked(DataStore.getInstance(getActivity()).amIPlaying(DataStore.getInstance(getActivity()).getNextGame().getMatchID()));
    }

    @Override
    public void onRefresh() {
        Log.d("TeamOverviewFragment", "Refreshing data");
        league = null;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DataStore.getInstance(getActivity()).refreshData();
                mLayout.setVisibility(View.GONE);
                ViewPager pager = (ViewPager) getActivity().findViewById(R.id.fragment_my_team_view_pager);
                mSwipeRefresh.setRefreshing(false);
                pager.getAdapter().notifyDataSetChanged();
            }

        },3000);
    }

    public static interface TeamOverviewCallbacks {
    }

    public void refreshPage() {
//        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.fragment_my_team_view_pager);
//        pager.getAdapter().notifyDataSetChanged();
    }

}
