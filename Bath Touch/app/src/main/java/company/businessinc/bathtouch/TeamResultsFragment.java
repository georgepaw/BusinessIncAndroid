package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.League;

public class TeamResultsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private TeamResultsCallbacks mCallbacks;
    private View mLayout;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<League> leagueNames = new LinkedList<League>();


    public static TeamResultsFragment newInstance() {
        TeamResultsFragment fragment = new TeamResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TeamResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        getLoaderManager().initLoader(DBProviderContract.ALLLEAGUES_URL_QUERY, null, this);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_team_results, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_team_results, container, false);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Past Results");
        actionBar.setElevation(0f);

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(2);

        mViewPager = (ViewPager) mLayout.findViewById(R.id.fragment_team_results_view_pager);

        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) mLayout.findViewById(R.id.fragment_team_results_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

        setSlidingTabLayoutContentDescriptions();

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

        return mLayout;
    }

    private void setSlidingTabLayoutContentDescriptions() {
        mSlidingTabLayout.setContentDescription(0,"Winter 2015");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (TeamResultsCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                return new CursorLoader(getActivity(), DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI, null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                if (data.moveToFirst()){
                    leagueNames = new ArrayList<>();
                    while(!data.isAfterLast()){
                        League league = new League(data);
                        leagueNames.add(league);
                        data.moveToNext();
                    }
                    if(leagueNames.size()>0) {
                        mSlidingTabLayout.setViewPager(mViewPager);
                        mViewPagerAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Team Results", "Creating fragment");
            ResultsListFragment frag = ResultsListFragment.newInstance(leagueNames.get(position).getLeagueID());
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
    }

    public interface TeamResultsCallbacks {

        public void onTeamResultsItemSelected(int position);
    }

}
