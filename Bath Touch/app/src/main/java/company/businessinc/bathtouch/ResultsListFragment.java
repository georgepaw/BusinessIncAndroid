package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Locale;

import company.businessinc.bathtouch.adapters.TeamResultsAdapter;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.adapters.TeamResultsAdapter;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Match;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsListFragment.ResultsListCallbacks} interface
 * to handle interaction events.
 * Use the {@link ResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsListFragment extends Fragment implements TeamResultsAdapter.OnResultSelectedCallbacks, DBObserver{

    private View mLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeamResultsAdapter mAdapter;
    private ResultsListCallbacks mCallbacks;
    private Integer mLeagueID, mTeamID;
    private Boolean mAllTeams;

    public static ResultsListFragment newInstance(Integer leagueID, Integer teamID, Boolean allTeams) {
        ResultsListFragment fragment = new ResultsListFragment();
        Bundle args = new Bundle();
        args.putInt("leagueID", leagueID);
        args.putInt("teamID", teamID);
        args.putBoolean("allTeams", allTeams);
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
            mTeamID = getArguments().getInt("teamID");
            mAllTeams = getArguments().getBoolean("allTeams");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_results_list, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_results_recycle);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        selectItem(position);
//                    }
//                }));

        mAdapter = new TeamResultsAdapter(this, mLeagueID, mTeamID, mAllTeams);

        mRecyclerView.setAdapter(mAdapter);
        return mLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
                MyApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Matches Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /*
    On a Result item selected in the recycler view, this is called
    Starts a new activity that shows the overview of a match
     */
    public void selectItem(int matchID, boolean hasBeenPlayed) {

        Bundle args = new Bundle();
        Match selectedMatch = hasBeenPlayed ? DataStore.getInstance(getActivity()).getPastLeagueMatch(matchID) : DataStore.getInstance(getActivity()).getFutureLeagueMatch(matchID);
        if(selectedMatch!=null) {
            args.putString(Match.KEY_TEAMONE, selectedMatch.getTeamOne());
            args.putString(Match.KEY_TEAMTWO, selectedMatch.getTeamTwo());
            args.putInt(Match.KEY_TEAMONEID, selectedMatch.getTeamOneID());
            args.putInt(Match.KEY_TEAMTWOID, selectedMatch.getTeamTwoID());
            args.putInt(Match.KEY_TEAMONEPOINTS, selectedMatch.getTeamOnePoints());
            args.putInt(Match.KEY_TEAMTWOPOINTS, selectedMatch.getTeamTwoPoints());
            args.putInt("leagueID", mLeagueID);
            args.putInt(Match.KEY_MATCHID, selectedMatch.getMatchID());
            args.putString(Match.KEY_PLACE, selectedMatch.getPlace());
            args.putBoolean("hasBeenPlayed", hasBeenPlayed);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
            args.putString(Match.KEY_DATETIME, sdf.format(selectedMatch.getDateTime()));
            args.putFloat(Match.KEY_LONGITUDE, selectedMatch.getLongitude());
            args.putFloat(Match.KEY_LATITUDE, selectedMatch.getLatitude());
            args.putString(Match.KEY_POSTCODE, selectedMatch.getPostCode());
            args.putString(Match.KEY_ADDRESS, selectedMatch.getAddress());

            if (mCallbacks != null) {
                mCallbacks.onResultsItemSelected(args);
            }
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

        DataStore.getInstance(activity).registerLeagueScoreDBObserver(this);
        DataStore.getInstance(activity).registerLeaguesFixturesDBObserver(this);
    }

    @Override
    public void onDetach() {
        DataStore.getInstance(getActivity()).unregisterLeagueScoreDBObserver(this);
        DataStore.getInstance(getActivity()).unregisterLeaguesFixturesDBObserver(this);
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void showMatchOverview(int matchID, boolean hasBeenPlayed) {
        selectItem(matchID, hasBeenPlayed);
    }

    @Override
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.LEAGUESSCORE_TABLE_NAME:
            case DBProviderContract.LEAGUESFIXTURES_TABLE_NAME:
                int position = 0;
                if(mAdapter.getScoresCount() <= mAdapter.getItemCount() - 1)
                    position = mAdapter.getScoresCount();
                else
                    position = mAdapter.getScoresCount() - 1;
                mLayoutManager.scrollToPositionWithOffset(position, 60);
                break;
        }
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
        public void onResultsItemSelected(Bundle args);
    }

}
