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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;

import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;

public class LeagueTableFragment extends Fragment{


    private LeagueTableCallbacks mCallbacks;
    private View mLayout;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<League> leagueNames = new LinkedList<League>();


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
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_league_table, container, false);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        actionBar.setTitle("League Tables");
        actionBar.setElevation(0f);

        DrawerFrameLayout drawerFrameLayout = (DrawerFrameLayout) (getActivity().findViewById(R.id.drawer_layout));
        int color = getResources().getColor(R.color.primary);
        color = darker(color, 0.7f);
        drawerFrameLayout.setStatusBarBackgroundColor(color);

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(1);

        mViewPager = (ViewPager) mLayout.findViewById(R.id.fragment_league_table_view_pager);

        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) mLayout.findViewById(R.id.fragment_league_table_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

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
        DataStore.getInstance(getActivity()).registerAllLeagueDBObserver(mViewPagerAdapter);
        mViewPagerAdapter.setLeagues();
        return mLayout;
    }

    @Override
    public void onDestroyView(){
        DataStore.getInstance(getActivity()).unregisterAllLeagueDBObserver(mViewPagerAdapter);
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

    private class ViewPagerAdapter extends FragmentPagerAdapter implements DBObserver {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Team Results", "Creating fragment");
            LeagueFragment frag = LeagueFragment.newInstance(leagueNames.get(position).getLeagueID());
            return frag;
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
                case DBProviderContract.ALLLEAGUES_TABLE_NAME:
                    setLeagues();
                    break;
            }
        }

        public void setLeagues(){
            leagueNames = DataStore.getInstance(getActivity()).getAllLeagues();
            notifyDataSetChanged();
        }
    }

    public interface LeagueTableCallbacks {

        public void onLeagueTableItemSelected(int position);
    }

}
