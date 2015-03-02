package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.SimpleDateFormat;
import java.util.Locale;

import company.businessinc.bathtouch.adapters.TeamResultsAdapter;
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
public class ResultsListFragment extends Fragment implements TeamResultsAdapter.OnResultSelectedCallbacks{

    private View mLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeamResultsAdapter mAdapter;
    private ResultsListCallbacks mCallbacks;
    private Integer mLeagueID;
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

        mAdapter = new TeamResultsAdapter(this, mLeagueID);

        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    /*
    On a Result item selected in the recycler view, this is called
    Starts a new activity that shows the overview of a match
     */
    public void selectItem(int position, int matchID) {

        Bundle args = new Bundle();
        Match selectedMatch = DataStore.getInstance(getActivity()).getPastLeagueMatch(matchID);
        args.putString(Match.KEY_TEAMONE, selectedMatch.getTeamOne());
        args.putString(Match.KEY_TEAMTWO, selectedMatch.getTeamTwo());
        args.putInt(Match.KEY_TEAMONEPOINTS, selectedMatch.getTeamOnePoints());
        args.putInt(Match.KEY_TEAMTWOPOINTS, selectedMatch.getTeamTwoPoints());
        args.putInt("leagueID", mLeagueID);
        args.putInt(Match.KEY_MATCHID, selectedMatch.getMatchID());
        args.putString(Match.KEY_PLACE, selectedMatch.getPlace());
        args.putBoolean("hasBeenPlayed", true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        args.putString(Match.KEY_DATETIME, sdf.format(selectedMatch.getDateTime()));

        if (mCallbacks != null) {
            mCallbacks.onResultsItemSelected(args);
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

    @Override
    public void showMatchOverview(int position, int matchID) {
        selectItem(position, matchID);
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
