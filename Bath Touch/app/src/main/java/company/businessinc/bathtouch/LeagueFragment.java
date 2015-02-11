package company.businessinc.bathtouch;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import company.businessinc.bathtouch.adapters.LeagueTableAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Team;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.LeagueTableFragment.LeagueTableCallbacks} interface
 * to handle interaction events.
 * Use the {@link LeagueTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeagueFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "LeagueTableFragment";
    public static final String ARG_OBJECT = "object";

    private LeagueCallbacks mCallbacks;
    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<LeagueTeam> mLeagueTeams;
    private List<Team> mAllTeams = new ArrayList<Team>();
    private Integer mLeagueID;

    public static LeagueFragment newInstance(int leagueID) {
        LeagueFragment fragment = new LeagueFragment();
        Bundle args = new Bundle();
        args.putInt("leagueID", leagueID);
        fragment.setArguments(args);
        return fragment;
    }

    public LeagueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        getLoaderManager().initLoader(DBProviderContract.LEAGUESSTANDINGS_URL_QUERY, null, this);
//        getLoaderManager().initLoader(DBProviderContract.ALLTEAMS_URL_QUERY, null, this);

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
        mLayout = inflater.inflate(R.layout.fragment_league, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.fragment_league_recycle);

//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        int userTeamId = DataStore.getInstance(getActivity().getApplicationContext()).getUserTeamID();


//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
//                .getBaseContext(),
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        selectItem(position);
//                    }
//                }));

        //Adapter loads the data fror the leagues
        mAdapter = new LeagueTableAdapter(getActivity(), userTeamId);
        mRecyclerView.setAdapter(mAdapter);

        Cursor rCursor = getActivity().getContentResolver()
                .query(DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI, null, DBProviderContract
                        .SELECTION_LEAGUEID, new String[]{Integer.toString(mLeagueID)}, null);
        if (rCursor.getCount() > 0) {
            loadStandings(rCursor);
        }
        rCursor.close();

        getLoaderManager().initLoader(DBProviderContract.ALLTEAMS_URL_QUERY, null, this);



        return mLayout;
    }

    public void selectItem(int position) {
        if (mCallbacks != null) {
            mCallbacks.onLeagueItemSelected(position);
        }
//        Intent intent = new Intent(getActivity(), MatchActivity.class);
//        Bundle args = new Bundle();
//        args.putString("teamOneName", mLeagueTeams.get(position).);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (LeagueCallbacks) activity;
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
    public interface LeagueCallbacks {

        public void onLeagueItemSelected(int position);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI, null, null, null, null);
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
        switch (loader.getId()) {
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                loadStandings(data);
                break;
            case DBProviderContract.ALLTEAMS_URL_QUERY:

                loadAllTeams(data);

        }
    }

    private void loadAllTeams(Cursor data) {
        Log.d("LEAGUEFRAG", "load finished for all teams");
        if (data.moveToFirst()){
            while(!data.isAfterLast()){
                mAllTeams.add(new Team(data));
                data.moveToNext();
            }
            ((LeagueTableAdapter) mAdapter).setAllTeams(mAllTeams);
        }
        else{
            Log.d("LEAGUEFRAG", "table was empty of teams");

        }

    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void loadStandings(Cursor data) {
        if (data.moveToFirst()) {
            mLeagueTeams = new ArrayList<>();
            while (!data.isAfterLast()) {
                if (data.getInt(0) == mLeagueID) {
                    mLeagueTeams.add(new LeagueTeam(data));
                }
                data.moveToNext();
            }
            if (mLeagueTeams.size() > 0) {
                Collections.sort(mLeagueTeams, new Comparator<LeagueTeam>() {
                    @Override
                    public int compare(LeagueTeam T1, LeagueTeam T2) {
                        return T1.getPosition() - T2.getPosition();
                    }
                });
                ((LeagueTableAdapter) mAdapter).setLeagueTeams(mLeagueTeams);
            }
        }
    }

}
