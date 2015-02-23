package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import company.businessinc.bathtouch.adapters.TeamResultsAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.ResultsListFragment.ResultsListCallbacks} interface
 * to handle interaction events.
 * Use the {@link ResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TeamResultsAdapter.OnResultSelectedCallbacks{

    private View mLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeamResultsAdapter mAdapter;
    private ResultsListCallbacks mCallbacks;
    private Integer mLeagueID;
    private String mLeagueName;
    private League mLeague;
    private List<Match> leagueScores;
    private ArrayList<Team> allTeams = new ArrayList<Team>();
    private String teamName;


    public static ResultsListFragment newInstance(Integer leagueID) {
        ResultsListFragment fragment = new ResultsListFragment();
        Bundle args = new Bundle();
        args.putInt("leagueID", leagueID);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLeagueID = getArguments().getInt("leagueID");
        }

        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            getLoaderManager().initLoader(DBProviderContract.TEAMSSCORES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.ALLTEAMS_URL_QUERY, null, this);

        } else {
            getLoaderManager().initLoader(DBProviderContract.LEAGUESSCORE_URL_QUERY, null, this);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_results_list, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_results_recycle);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        selectItem(position);
//                    }
//                }));

        mAdapter = new TeamResultsAdapter(this);

        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            getLoaderManager().restartLoader(DBProviderContract.TEAMSSCORES_URL_QUERY, null, this);
            getLoaderManager().restartLoader(DBProviderContract.ALLLEAGUES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.ALLTEAMS_URL_QUERY, null, this);

        } else {
            getLoaderManager().restartLoader(DBProviderContract.LEAGUESSCORE_URL_QUERY, null, this);
        }

        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    //Invoked when the cursor loader is created
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSCORE_TABLE_CONTENTURI, null,
                                        DBProviderContract.SELECTION_LEAGUEID,
                                        new String[]{Integer.toString(mLeagueID)}
                                        , null);
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI, null,
                                        DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                                        new String[]{Integer.toString(mLeagueID), Integer.toString(DataStore.getInstance(getActivity()).getUserTeamID())}
                                        , null);
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                return new CursorLoader(getActivity(), DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI, null,
                        DBProviderContract.SELECTION_LEAGUEID, new String[]{Integer.toString(mLeagueID)}, null);
            case DBProviderContract.ALLTEAMS_URL_QUERY:
                return new CursorLoader(getActivity(), DBProviderContract.ALLTEAMS_TABLE_CONTENTURI, null, null, null, null);
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
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                    leagueScores = loadLeagueMatches(data);

                    if(leagueScores.size() > 0){
                        teamName = "";
                        //TODO change this to teamID
                        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                            teamName = DataStore.getInstance(getActivity()).getUserTeam();
                        }
                        mAdapter.setData(leagueScores, teamName);
                    }
                break;
            case DBProviderContract.ALLLEAGUES_URL_QUERY: //TODO talk about formatting this to show all past results
                if(mLeague == null) {
                    while (!data.isAfterLast()) {
                        League l = new League(data);
                        if (l.getLeagueID() == mLeagueID)
                            mLeague = l;
                        data.moveToNext();
                        mAdapter.setLeagueName(mLeague.getLeagueName());
                        break;
                    }
                }
                break;
            case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                break;
            case DBProviderContract.ALLTEAMS_URL_QUERY:
                loadAllTeams(data);
                mAdapter.addAllTeams(allTeams);
        }
    }

    public void loadAllTeams(Cursor data){
        if(data.moveToFirst()){
            while(!data.isAfterLast()){
                allTeams.add(new Team(data));
                data.moveToNext();
            }
        }
    }

    public List<Match> loadLeagueMatches(Cursor data){
        List<Match> matchList = new ArrayList<>();
        data.moveToFirst();
        while(!data.isAfterLast()){
            matchList.add(new Match(data));
            data.moveToNext();
        }
        return matchList;
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /*
    On a Result item selected in the recycler view, this is called
    Starts a new activity that shows the overview of a match
     */
    public void selectItem(int position) {
        if (mCallbacks != null) {
            mCallbacks.onResultsItemSelected(position);
        }

        Intent intent = new Intent(getActivity(), MatchFragment.class);
        Bundle args = new Bundle();
        args.putString(Match.KEY_TEAMONE, leagueScores.get(position).getTeamOne());
        args.putString(Match.KEY_TEAMTWO, leagueScores.get(position).getTeamTwo());
        args.putInt(Match.KEY_TEAMONEPOINTS, leagueScores.get(position).getTeamOnePoints());
        args.putInt(Match.KEY_TEAMTWOPOINTS, leagueScores.get(position).getTeamTwoPoints());
        args.putInt("leagueID", mLeagueID);
        args.putInt(Match.KEY_MATCHID, leagueScores.get(position).getMatchID());
        args.putString(Match.KEY_PLACE, leagueScores.get(position).getPlace());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        args.putString(Match.KEY_DATETIME, sdf.format(leagueScores.get(position).getDateTime()));
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (ResultsListCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void showMatchOverview(int position) {
        selectItem(position);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ResultsListCallbacks {
        public void onResultsItemSelected(int position);
    }

}
