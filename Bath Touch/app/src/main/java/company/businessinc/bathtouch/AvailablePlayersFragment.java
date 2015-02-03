package company.businessinc.bathtouch;


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

import java.util.ArrayList;

import company.businessinc.bathtouch.adapters.AvailablePlayersAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.Player;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mRecyclerView;
    private View mLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private boolean available_toggle;
    private int matchID;


    public static AvailablePlayersFragment newInstance(int position, int matchID) {
        AvailablePlayersFragment fragment = new AvailablePlayersFragment();
        Bundle args = new Bundle();

        if(position == 0){
            args.putBoolean("AVAIL", true);
        }
        else{
            args.putBoolean("AVAIL", false);
        }
        args.putInt("matchID", matchID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle  = getArguments();
            available_toggle = bundle.getBoolean("AVAIL");
            matchID = bundle.getInt("matchID");
            getLoaderManager().initLoader(DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY, null, this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_team_roster, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_roster_recycle );

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Adapter loads the data fror the leagues
        mAdapter = new AvailablePlayersAdapter(available_toggle, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY:
                return new CursorLoader(getActivity(), DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_CONTENTURI, null, DBProviderContract.SELECTION_MATCHID, new String[]{Integer.toString(matchID)}, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY:
                if (data.moveToFirst()) {
                    while (!data.isAfterLast()) {
                        Player player = new Player(data);
                        if(player.getIsPlaying() == available_toggle){
                            ((AvailablePlayersAdapter)mAdapter).addToPlayerList(player);
                        }
                        data.moveToNext();
                    }
                }
                break;
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


}
