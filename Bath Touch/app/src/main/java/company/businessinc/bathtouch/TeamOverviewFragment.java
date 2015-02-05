package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;


public class TeamOverviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener{

    private TeamOverviewCallbacks mCallbacks;
    private View mLayout;
    private TextView mNextMatchHeader, mNextMatchName, mNextMatchPlace, mNextMatchDate,
            mNextRefMatchName, mNextRefMatchPlace, mNextRefMatchDate, mPastMatchesHeader,
            mLeagueHeader, mLeaguePoints, mLeagueWins, mLeagueDraws, mLeagueLosses;
    private FrameLayout mNextMatchDivider, mNextRefMatchDivider, mPastMatchesDivider;
    private ImageView mNextMatchImage;
    private List<ImageView> mPastMatchesImages;
    private CheckBox mNextMatchCheckBox;
    private LinearLayout mNextMatchCheckBoxContainer;
    private RelativeLayout mNextMatchContainer, mNextRefMatchContainer, mPastMatchesContainer,
            mLeagueContainer;
    private Button mNextMatchManage, mNextRefMatchSubmit;

    private League league;
    private int mTeamID;
    private int mLeagueID;
    private Team myTeam;
    private LeagueTeam thisLeagueTeam;
    private List<Match> leagueFixtures;
    private List<LeagueTeam> leagueStandings;
    private List<Match> leagueScores;
    private SwipeRefreshLayout mSwipeRefresh;
    private Boolean isPlaying = null;
    private Match nextPlayingMatch;

    private Match nextMatch;
    private Match nextRefMatch;
    private List<Match> pastMatches;
    private List<LeagueTeam> leagueTeam;
    private League leagueViewLeague;
    private League pastMatchesLeague;

    private List<Match> teamOverviewLeagueFixtures;
    private Team teamOverviewTeam;
    private LeagueTeam teamOverviewLeagueTeam;
    private League teamOverviewLeague;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TeamOverviewFragment", "onCreateView called");
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()) {
            if(DataStore.getInstance(getActivity()).isReferee()) {
                getLoaderManager().initLoader(DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY, null, this);
            }
            if(!DataStore.getInstance(getActivity()).isUserCaptain()){
                getLoaderManager().initLoader(DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_URL_QUERY, null, this);
            }
            getLoaderManager().initLoader(DBProviderContract.MYUPCOMINGGAMES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.MYLEAGUES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.TEAMSSCORES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.TEAMSFIXTURES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.LEAGUETEAMS_URL_QUERY, null, this);
        } else {
            getLoaderManager().initLoader(DBProviderContract.ALLLEAGUES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.LEAGUESSCORE_URL_QUERY, null, this);
        }
        getLoaderManager().initLoader(DBProviderContract.LEAGUESSTANDINGS_URL_QUERY, null, this);
        mLayout = inflater.inflate(R.layout.fragment_team_overview, container, false);

        mSwipeRefresh = (SwipeRefreshLayout) (mLayout.findViewById(R.id.fragment_team_overview_swiperefresh));
        mSwipeRefresh.setOnRefreshListener(this);

        mNextMatchContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_nextmatch);
        mNextMatchDivider = (FrameLayout) mLayout.findViewById(R.id.fragment_team_overview_divider1);
        mNextMatchHeader = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_header);
        mNextMatchName = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_name);
        mNextMatchPlace = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_place);
        mNextMatchDate = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_date);

        mPastMatchesContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_pastmatches);
        mPastMatchesDivider = (FrameLayout) mLayout.findViewById(R.id.fragment_team_overview_divider3);
        mPastMatchesHeader = (TextView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_header);

        mNextRefMatchContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch);
        mNextRefMatchDivider = (FrameLayout) mLayout.findViewById(R.id.fragment_team_overview_divider2);
        if(!DataStore.getInstance(getActivity()).isReferee()) {
            mNextRefMatchContainer.setVisibility(View.GONE);
            mNextRefMatchDivider.setVisibility(View.GONE);
        }
        mNextRefMatchName = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_name);
        mNextRefMatchPlace = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_place);
        mNextRefMatchDate = (TextView) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_date);
        mNextRefMatchSubmit = (Button) mLayout.findViewById(R.id.fragment_team_overview_nextrefmatch_submit);

        mNextMatchCheckBoxContainer = (LinearLayout) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_checkbox_container);
        mNextMatchCheckBox = (CheckBox) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_checkbox);
        mNextMatchManage = (Button) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_manage);

        mNextMatchImage = (ImageView) mLayout.findViewById(R.id.fragment_team_overview_nextmatch_image);
        mPastMatchesImages = new ArrayList<>();
        mPastMatchesImages.add(0,(ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_1));
        mPastMatchesImages.add(1,(ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_2));
        mPastMatchesImages.add(2,(ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_3));
        mPastMatchesImages.add(3,(ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_4));
        mPastMatchesImages.add(4,(ImageView) mLayout.findViewById(R.id.fragment_team_overview_pastmatches_image_5));

        mLeagueContainer = (RelativeLayout) mLayout.findViewById(R.id.fragment_team_overview_league);
        mLeagueHeader = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_header);
        mLeaguePoints = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_points);
        mLeagueWins = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_wins);
        mLeagueDraws = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_draws);
        mLeagueLosses = (TextView) mLayout.findViewById(R.id.fragment_team_overview_league_losses);

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

    //Invoked when the cursor loader is created
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.MYUPCOMINGGAMES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.MYLEAGUES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.MYLEAGUES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSCORE_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.TEAMSFIXTURES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.LEAGUETEAMS_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUETEAMS_TABLE_CONTENTURI, null, null, null, null);
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
                List<Match> nextRefMatch = new ArrayList<>();
                while(!data.isAfterLast()){
                    nextRefMatch.add(new Match(data));
                    data.moveToNext();
                }
                if(nextRefMatch.size() > 0){
                    nextRefMatch = Match.sortList(nextRefMatch, Match.SortType.ASCENDING);
                    setNextMatch(nextRefMatch.get(0), true, false);
                }
                break;
            case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                List<Match> nextMatch = new ArrayList<>();
                while(!data.isAfterLast()){
                    nextMatch.add(new Match(data));
                    data.moveToNext();
                }
                if(nextMatch.size() > 0){
                    nextMatch = Match.sortList(nextMatch, Match.SortType.ASCENDING);
                    nextPlayingMatch = nextMatch.get(0);
                }
                if(nextPlayingMatch!=null){
                    if(DataStore.getInstance(getActivity()).isUserCaptain()){
                        setNextMatch(nextPlayingMatch, false, false);
                    } else {
                        loadNextMatchCard();
                    }
                }
                break;
            case DBProviderContract.MYLEAGUES_URL_QUERY:
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                if(league == null){
                    while(!data.isAfterLast()){
                        league = new League(data);
                        data.moveToNext();
                    }
                    if(league != null){
                        Cursor rCursor = getActivity().getContentResolver().query(DBProviderContract
                                .LEAGUESSTANDINGS_TABLE_CONTENTURI,null,DBProviderContract.SELECTION_LEAGUEID,
                                new String[]{Integer.toString(mLeagueID)},null);
                        if(rCursor.getCount() > 0){
                            leagueStandings = loadLeagueTeams(rCursor);
                            setLeague(leagueStandings, league);
                        }
                        rCursor.close();
                        //try loading the scores
                        if(DataStore.getInstance(getActivity()).isUserLoggedIn()) {
                            rCursor = getActivity().getContentResolver().query(DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI,
                                    null,
                                    DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                                    new String[]{Integer.toString(mLeagueID), Integer.toString(DataStore.getInstance(getActivity()).getUserTeamID())},
                                    null);
                        } else {
                            rCursor = getActivity().getContentResolver().query(DBProviderContract.LEAGUESSCORE_TABLE_CONTENTURI,
                                    null,
                                    DBProviderContract.SELECTION_LEAGUEID,
                                    new String[]{Integer.toString(mLeagueID)},
                                    null);
                        }
                        if(rCursor.getCount() > 0){
                            leagueScores = loadLeagueMatches(rCursor);
                            setLeagueStandings(leagueScores, league);
                        }
                        rCursor.close();
                        //Load the fixtures
                        if(DataStore.getInstance(getActivity()).isUserLoggedIn()) {
                            loadTeamOverview();
                        }
                    }
                }
                break;
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                if(league != null){
                    leagueStandings = loadLeagueTeams(data);
                    if(leagueStandings.size() > 0){
                        setLeague(leagueStandings, league);
                    }
                }
                break;
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                if(league != null){
                    leagueScores = loadLeagueMatches(data);
                    if(leagueScores.size() > 0){
                        setLeagueStandings(leagueScores, league);
                    }
                }
                break;
            case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                if(league != null){
                    leagueFixtures = loadLeagueMatches(data);
                    if(leagueFixtures.size() > 0){
                        loadTeamOverview();
                    }
                }
                break;
            case DBProviderContract.LEAGUETEAMS_URL_QUERY:
                if(league != null && myTeam == null){
                    myTeam = loadTeam(data);
                    if(myTeam!=null){
                        loadTeamOverview();
                    }
                }
                break;
            case DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_URL_QUERY:
                if(isPlaying!=null && nextPlayingMatch != null){
                    isPlaying = isPlaying(nextPlayingMatch.getMatchID(), data);
                    loadNextMatchCard();
                }
                break;
        }
    }

    private void loadTeamOverview(){
        Cursor rCursor;
        if(leagueFixtures == null){
            rCursor = getActivity().getContentResolver().query(DBProviderContract.TEAMSFIXTURES_TABLE_CONTENTURI,
                    null,
                    DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                    new String[]{Integer.toString(mLeagueID), Integer.toString(DataStore.getInstance(getActivity()).getUserTeamID())},
                    null);
            leagueFixtures = loadLeagueMatches(rCursor);
            rCursor.close();
        }
        if(myTeam == null){
            rCursor = getActivity().getContentResolver().query(DBProviderContract.LEAGUETEAMS_TABLE_CONTENTURI,
                    null,
                    DBProviderContract.SELECTION_LEAGUEID,
                    new String[]{Integer.toString(mLeagueID)},
                    null);
            myTeam = loadTeam(rCursor);
            rCursor.close();
        }
        if(leagueFixtures != null && myTeam != null && thisLeagueTeam != null && league != null){
            setLeagueOverview(leagueFixtures, myTeam,thisLeagueTeam,league);
        }
    }

    public List<LeagueTeam> loadLeagueTeams(Cursor data){
        List<LeagueTeam> leagueTeams = new ArrayList<>();
        if(data.moveToFirst()) {
            while (!data.isAfterLast()) {
                if (data.getInt(0) == mLeagueID) {
                    LeagueTeam leagueTeam = new LeagueTeam(data);
                    if (DataStore.getInstance(getActivity()).isUserLoggedIn()) {
                        if (DataStore.getInstance(getActivity()).getUserTeamID() == leagueTeam.getTeamID()) {
                            thisLeagueTeam = leagueTeam;
                            loadTeamOverview();
                        }
                    }
                    leagueTeams.add(leagueTeam);
                }
                data.moveToNext();
            }
        }
        return leagueTeams;
    }

    public List<Match> loadLeagueMatches(Cursor data){
        List<Match> matchList = new ArrayList<>();
        if(data.moveToFirst()) {
            while (!data.isAfterLast()) {
                if (data.getInt(0) == mLeagueID) {
                    matchList.add(new Match(data));
                }
                data.moveToNext();
            }
        }
        return matchList;
    }

    private void loadNextMatchCard() {
        if (isPlaying == null){
            Cursor rCursor = getActivity().getContentResolver().query(DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_CONTENTURI,
                    null,
                    DBProviderContract.SELECTION_MATCHID,
                    new String[]{Integer.toString(nextPlayingMatch.getMatchID())},
                    null);
            if (rCursor.getCount() > 0) {
                isPlaying = isPlaying(nextPlayingMatch.getMatchID(), rCursor);
            }
            rCursor.close();
        }
        if(nextPlayingMatch != null && isPlaying != null){
            setNextMatch(nextPlayingMatch, false, isPlaying);
        }
    }

    private Boolean isPlaying(int matchID, Cursor cursor){
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (matchID == cursor.getInt(0)) {
                    return cursor.getInt(1) == 1;
                }
            }
        }
        return null;
    }

    public Team loadTeam(Cursor data){
        Team team = null;
        if(data.moveToFirst()) {
            while (!data.isAfterLast()) {
                team = new Team(data);
                if (data.getInt(0) == mLeagueID && team.getTeamID() == DataStore.getInstance(getActivity()).getUserTeamID()) {
                    return team;
                }
                data.moveToNext();
            }
        }
        return null;
    }

    public void setLeague(List<LeagueTeam> data, League league){
        if(data!=null && league != null) {
            this.leagueViewLeague = league;
            this.leagueTeam = data;
            mLeagueContainer.setVisibility(View.VISIBLE);
            String header = league.getLeagueName();
            for(LeagueTeam team : data) {
                if(team.getTeamID() == mTeamID) {
                    int position = team.getPosition();
                    switch(position % 10){
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
            refreshPage();
        }
    }

    public void setNextMatch(final Match nextMatch, boolean isRefMatch, boolean isPlaying){
        if(isRefMatch) {
            mNextRefMatchContainer.setVisibility(View.VISIBLE);
            mNextRefMatchDivider.setVisibility(View.VISIBLE);
            this.nextRefMatch = nextMatch;
            this.isPlaying  = isPlaying;
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
        } else {
            this.nextMatch = nextMatch;
            this.isPlaying  = isPlaying;
            mNextMatchContainer.setVisibility(View.VISIBLE);
            mNextMatchDivider.setVisibility(View.VISIBLE);
            if(nextMatch.getTeamOneID() == DataStore.getInstance(getActivity()).getUserTeamID()) {
                mNextMatchName.setText(nextMatch.getTeamTwo());
                TextDrawable avatar = TextDrawable.builder()
                        .buildRound(nextMatch.getTeamTwo().substring(0,1), getActivity()
                                .getResources().getColor(R.color.dark_divider));
                mNextMatchImage.setImageDrawable(avatar);
            } else {
                mNextMatchName.setText(nextMatch.getTeamOne());
                TextDrawable avatar = TextDrawable.builder()
                        .buildRound(nextMatch.getTeamOne().substring(0,1), getActivity()
                                .getResources().getColor(R.color.dark_divider));
                mNextMatchImage.setImageDrawable(avatar);
            }
            mNextMatchPlace.setText(nextMatch.getPlace());
            DateFormatter sdf = new DateFormatter();
            mNextMatchDate.setText(sdf.format(nextMatch.getDateTime()));
            final int nxtmtchID = nextMatch.getMatchID();
            if(!DataStore.getInstance(getActivity()).isUserCaptain()) {
                mNextMatchCheckBoxContainer.setVisibility(View.VISIBLE);
//                mNextMatchCheckBox.setButtonTintList(ColorStateList.valueOf(DataStore.getInstance(getActivity()).getUserTeamColorPrimary()));
                mNextMatchCheckBox.setChecked(isPlaying);
                mNextMatchCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DataStore.getInstance(getActivity()).setMyAvailability(isChecked, nxtmtchID);
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

    public void setLeagueStandings(List<Match> data, League league){
        mPastMatchesContainer.setVisibility(View.VISIBLE);
        mPastMatchesDivider.setVisibility(View.VISIBLE);
        pastMatches = data;
        this.pastMatchesLeague = league;
        TextDrawable win = TextDrawable.builder()
                .buildRound("W", getActivity().getResources().getColor(R.color.darkgreen));
        TextDrawable draw = TextDrawable.builder()
                .buildRound("D", getActivity().getResources().getColor(R.color.darkorange));
        TextDrawable lose = TextDrawable.builder()
                .buildRound("L", getActivity().getResources().getColor(R.color.darkred));
        int max = Math.min(pastMatches.size(), 5);
        mPastMatchesHeader.setText("Last " + max + " matches");
        for(int i = 0; i < max; i++) {
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
            if(match.getTeamOnePoints() == match.getTeamTwoPoints()){
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

    public void setLeagueOverview(List<Match> teamOverviewLeagueFixtures, Team teamOverviewTeam, LeagueTeam teamOverviewLeagueTeam, League teamOverviewLeague){
        this.teamOverviewLeagueFixtures = teamOverviewLeagueFixtures;
        this.teamOverviewTeam = teamOverviewTeam;
        this.teamOverviewLeagueTeam = teamOverviewLeagueTeam;
        this.teamOverviewLeague = teamOverviewLeague;
        refreshPage();
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onRefresh() {
        Log.d("TeamOverviewFragment", "Refreshing data");
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
