package company.businessinc.bathtouch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.ApdaterData.TeamResultsData;
import company.businessinc.bathtouch.adapters.HomePageAdapter;
import company.businessinc.bathtouch.adapters.TeamResultsAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.ResultsListFragment.ResultsListCallbacks} interface
 * to handle interaction events.
 * Use the {@link ResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private View mLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeamResultsAdapter mAdapter;
    private ResultsListCallbacks mCallbacks;
    private Integer mLeagueID;
    private List<Match> leagueScores;
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
        } else {
            getLoaderManager().initLoader(DBProviderContract.LEAGUESSCORE_URL_QUERY, null, this);
        }
    }

    //Invoked when the cursor loader is created
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSCORE_TABLE_CONTENTURI, null,
                                        DBProviderContract.SELECTION_LEAGUEID,
                                        new String[]{Integer.toString(mLeagueID + 1)}
                                        , null);
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI, null,
                                        DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                                        new String[]{Integer.toString(mLeagueID + 1), Integer.toString(DataStore.getInstance(getActivity()).getUserTeamID())}
                                        , null);
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
                        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                            teamName = DataStore.getInstance(getActivity()).getUserTeam();
                        }
                        mAdapter.setData(leagueScores, teamName);
                    }
                break;
            case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                break;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_results_list, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_results_recycle);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        selectItem(position);
                    }
                }));

        mAdapter = new TeamResultsAdapter();

        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            getLoaderManager().restartLoader(DBProviderContract.TEAMSSCORES_URL_QUERY, null, this);
        } else {
            getLoaderManager().restartLoader(DBProviderContract.LEAGUESSCORE_URL_QUERY, null, this);
        }

        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    public void selectItem(int position) {
        if (mCallbacks != null) {
            mCallbacks.onResultsItemSelected(position);
        }
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
