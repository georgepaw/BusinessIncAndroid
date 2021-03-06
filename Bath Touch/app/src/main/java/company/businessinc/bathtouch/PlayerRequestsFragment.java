package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import company.businessinc.bathtouch.adapters.PlayerRequestAdapter;
import company.businessinc.bathtouch.adapters.TeamResultsAdapter;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;

import java.util.ArrayList;
import java.util.List;

public class PlayerRequestsFragment extends Fragment {


    private View mLayout;
    private RecyclerView mRecyclerView;
    private PlayerRequestAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private static final String ANON_PRIMARY = "#ff0000";


    public static PlayerRequestsFragment newInstance() {
        PlayerRequestsFragment fragment = new PlayerRequestsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerRequestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_team_results, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_player_requests, container, false);
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.player_requests_recycle);
        mRecyclerView.setHasFixedSize(true);

        int userColor;
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            userColor = DataStore.getInstance(getActivity().getBaseContext()).getUserTeamColorPrimary();
        } else {
            userColor = Color.parseColor(ANON_PRIMARY);
        }

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(userColor));
        actionBar.setTitle("Player requests");
        actionBar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics()));

        DrawerFrameLayout drawerFrameLayout = (DrawerFrameLayout) (getActivity().findViewById(R.id.drawer_layout));
        int color = userColor;
        color = darker(color, 0.7f);
        drawerFrameLayout.setStatusBarBackgroundColor(color);

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(4);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        selectItem(position);
//                    }
//                }));

        mAdapter = new PlayerRequestAdapter(getActivity());

        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
                MyApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Player Requests Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public int darker (int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

}
