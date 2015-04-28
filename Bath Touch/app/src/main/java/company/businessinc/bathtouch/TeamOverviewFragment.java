package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import company.businessinc.bathtouch.adapters.TeamOverviewAdapter;
import company.businessinc.bathtouch.adapters.TeamResultsAdapter;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.CachedRequest;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.endpoints.ScoreSubmit;
import company.businessinc.endpoints.ScoreSubmitInterface;
import company.businessinc.networking.CheckNetworkConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TeamOverviewFragment extends Fragment{

    private TeamOverviewCallbacks mCallbacks;
    private View mLayout;
    private ViewPager mPager;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeamOverviewAdapter mAdapter;
    private int mTeamID;
    private int mLeagueID;
    private League league;
    private SwipeRefreshLayout mSwipeRefresh;


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
        mCallbacks = (TeamOverviewCallbacks) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TeamOverviewFragment", "onCreateView called");

        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_team_overview, container, false);

        mPager = (ViewPager) getActivity().findViewById(R.id.fragment_my_team_view_pager);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_overview_recycle);

        mSwipeRefresh = (SwipeRefreshLayout) (mLayout.findViewById(R.id.swipeRefreshOverview));
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataStore.getInstance(getActivity()).refreshData();
//                        mLayout.setVisibility(View.GONE);
//                        mSwipeRefresh.setRefreshing(false);
//                        mPager.getAdapter().notifyDataSetChanged();
                    }

                },3000);
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TeamOverviewAdapter(this, mTeamID, mLeagueID, mSwipeRefresh, mPager);

        mRecyclerView.setAdapter(mAdapter);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public static interface TeamOverviewCallbacks {
    }
}
