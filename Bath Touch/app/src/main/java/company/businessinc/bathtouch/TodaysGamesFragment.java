package company.businessinc.bathtouch;

import android.app.Activity;
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
import android.util.Log;
import android.view.*;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;

import java.util.LinkedList;
import java.util.List;

public class TodaysGamesFragment extends Fragment{

    private View mLayout;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;

    private static final String ANON_PRIMARY = "#ff0000";


    public static TodaysGamesFragment newInstance() {
        TodaysGamesFragment fragment = new TodaysGamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TodaysGamesFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_todays_games, container, false);

        int userColor;
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            userColor = DataStore.getInstance(getActivity().getBaseContext()).getUserTeamColorPrimary();
        } else {
            userColor = Color.parseColor(ANON_PRIMARY);
        }

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(userColor));
        actionBar.setTitle("Today's Games");
        actionBar.setElevation(0f);

        DrawerFrameLayout drawerFrameLayout = (DrawerFrameLayout) (getActivity().findViewById(R.id.drawer_layout));
        int color = userColor;
        color = darker(color, 0.7f);
        drawerFrameLayout.setStatusBarBackgroundColor(color);

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(1);

        mViewPager = (ViewPager) mLayout.findViewById(R.id.fragment_todays_games_view_pager);

        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) mLayout.findViewById(R.id.fragment_todays_games_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setBackgroundColor(userColor);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.accent_material_light));
        mSlidingTabLayout.setDistributeEvenly(false);
        mSlidingTabLayout.setEnablePadding(true);
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mSlidingTabLayout.setViewPager(mViewPager);

        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
        DataStore.getInstance(getActivity()).registerLiveLeagueDBObserver(mViewPagerAdapter);
        mViewPagerAdapter.setLeagues();
        return mLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
                MyApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Today's Games Fragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onDestroyView(){
        DataStore.getInstance(getActivity()).unregisterLiveLeagueDBObserver(mViewPagerAdapter);
        super.onDestroyView();
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

    private class ViewPagerAdapter extends FragmentPagerAdapter implements DBObserver {


        private List<League> leagueNames = new LinkedList<League>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            leagueNames = DataStore.getInstance(getActivity()).getAllLiveLeagues();
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Team Results", "Creating fragment");
            return TodayFragment.newInstance(leagueNames.get(position).getLeagueID());
        }

        @Override
        public int getCount() {
            return leagueNames.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return leagueNames.get(position).getLeagueName();
        }

        @Override
        public void notify(String tableName, Object data) {
            switch (tableName){
                case DBProviderContract.LIVELEAGUE_TABLE_NAME:
                    setLeagues();
                    break;
            }
        }

        public void setLeagues(){
            leagueNames = DataStore.getInstance(getActivity()).getAllLeagues();
            notifyDataSetChanged();
        }
    }

}
