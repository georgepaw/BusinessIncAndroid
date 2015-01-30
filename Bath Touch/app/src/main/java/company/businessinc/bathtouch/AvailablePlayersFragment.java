package company.businessinc.bathtouch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import company.businessinc.bathtouch.adapters.AvailablePlayersAdapter;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private View mLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private boolean available_toggle;
    private ArrayList<Integer> playerList = new ArrayList<Integer>();


    public static AvailablePlayersFragment newInstance(int position, ArrayList<Integer> list) {
        AvailablePlayersFragment fragment = new AvailablePlayersFragment();
        Bundle args = new Bundle();

        if(position == 0){
            args.putBoolean("AVAIL", true);

        }
        else{
            args.putBoolean("AVAIL", false);
        }

        args.putIntegerArrayList("PLAYERS", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle  = getArguments();
            available_toggle = bundle.getBoolean("AVAIL");
            playerList = bundle.getIntegerArrayList("PLAYERS");
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
        mAdapter = new AvailablePlayersAdapter(available_toggle, playerList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    /*
    Adapter to dipslay the available players in the recycler view
     */


}
