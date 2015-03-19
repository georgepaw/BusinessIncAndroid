package company.businessinc.bathtouch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import company.businessinc.bathtouch.adapters.LeagueTableAdapter;
import company.businessinc.bathtouch.adapters.TodaysGameAdapter;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TodayFragment extends Fragment implements TodaysGameAdapter.MatchSelectedCallback{

    private static final String TAG = "TodayFragment";
    public static final String ARG_OBJECT = "object";

    private TodaysCallbacks mCallbacks;
    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<LeagueTeam> mLeagueTeams;
    private Integer mLeagueID;

    private static final String ANON_PRIMARY = "#ff0000";
    private static final String ANON_SECONDARY = "#ffffff";

    public static TodayFragment newInstance(int leagueID) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putInt("leagueID", leagueID);
        fragment.setArguments(args);
        return fragment;
    }

    public TodayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //get the data from the league activity on what league to display
        mLeagueID = 0;
        if (getArguments() != null) {
            mLeagueID = getArguments().getInt("leagueID");
        }


        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_today, container, false);

        //set up the recycler view
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.fragment_today_recycle);

//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TodaysGameAdapter(this, mLeagueID);
        mRecyclerView.setAdapter(mAdapter);
        return mLayout;
    }

    public void selectItem(int matchID, boolean hasBeenPlayed) {

        Bundle args = new Bundle();
        Match selectedMatch = DataStore.getInstance(getActivity()).getTodaysMatch(mLeagueID,matchID);
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

            if (mCallbacks != null) {
                mCallbacks.onTodaysItemSelected(args);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (TodaysCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onTodaysItemSelected");
        }
    }

    @Override
    public void showMatchOverview(int matchID, boolean hasBeenPlayed) {
        selectItem(matchID, hasBeenPlayed);
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
    public interface TodaysCallbacks {

        public void onTodaysItemSelected(Bundle args);
    }
}
