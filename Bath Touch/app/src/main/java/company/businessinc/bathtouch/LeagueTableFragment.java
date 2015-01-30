package company.businessinc.bathtouch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import company.businessinc.bathtouch.ApdaterData.HomeCardData;
import company.businessinc.bathtouch.ApdaterData.LeagueTableData;
import company.businessinc.bathtouch.adapters.HomePageAdapter;
import company.businessinc.bathtouch.adapters.LeagueTableAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.endpoints.LeagueList;
import company.businessinc.endpoints.LeagueListInterface;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.LeagueTableFragment.LeagueTableCallbacks} interface
 * to handle interaction events.
 * Use the {@link LeagueTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeagueTableFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "LeagueTableFragment";
    public static final String ARG_OBJECT = "object";

    private LeagueTableCallbacks mCallbacks;
    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int leagueID;

    public static LeagueTableFragment newInstance() {
        LeagueTableFragment fragment = new LeagueTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LeagueTableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        getLoaderManager().initLoader(DBProviderContract.LEAGUESSTANDINGS_URL_QUERY, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //get the data from the league activity on what league to display
        Bundle bundle = this.getArguments();
        leagueID = 0;
        String leagueName = "";
        if(bundle == null){
            Log.d("League fragment", "no bundle");
        }
        else{
            leagueID = bundle.getInt("LEAGUEID");
            leagueName = bundle.getString("LEAGUENAME");
        }


        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_league_table, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.league_table_recycle );

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        selectItem(position);
                    }
                }));

        //Adapter loads the data fror the leagues
        mAdapter = new LeagueTableAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Cursor rCursor = getActivity().getContentResolver().query(DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI,null,DBProviderContract.SELECTION_LEAGUEID,new String[]{Integer.toString(leagueID)},null);
        if(rCursor.getCount() > 0){
            loadStandings(rCursor);
        }
        rCursor.close();

        TextView mLeagueName = (TextView) mLayout.findViewById(R.id.league_table_name);
        mLeagueName.setText(leagueName);

        return mLayout;
    }

    public void selectItem(int position) {
        if (mCallbacks != null) {
            mCallbacks.onLeagueTableItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (LeagueTableCallbacks) activity;
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
    public interface LeagueTableCallbacks {

        public void onLeagueTableItemSelected(int position);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI, null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                loadStandings(data);
                break;
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void loadStandings(Cursor data){
        if (data.moveToFirst()){
            List<LeagueTeam> leagueTeams = new ArrayList<>();
            while(!data.isAfterLast()){
                if(data.getInt(0) == leagueID){
                        leagueTeams.add(new LeagueTeam(data));
                }
                data.moveToNext();
            }
            if(leagueTeams.size() >0) {
                Collections.sort(leagueTeams, new Comparator<LeagueTeam>() {
                    @Override
                    public int compare(LeagueTeam T1, LeagueTeam T2) {
                        return T1.getPosition() - T2.getPosition();
                    }
                });
                ((LeagueTableAdapter) mAdapter).setLeagueTeams(leagueTeams);
            }
        }
    }

}
