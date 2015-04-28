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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Team;

public class TeamResultsFragment extends Fragment {


    private TeamResultsCallbacks mCallbacks;
    private View mLayout;
    private ViewPager mViewPager;
    private View mSpinnerContainer;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private int mTeamID;
    private boolean mAllTeams = false;

    private static final String ANON_PRIMARY = "#ff0000";


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

        setEnterTransition(new Fade());
        setExitTransition(new Fade());

        int userColor;
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            userColor = DataStore.getInstance(getActivity().getBaseContext()).getUserTeamColorPrimary();
            mTeamID = DataStore.getInstance(getActivity()).getUserTeamID();
        } else {
            userColor = Color.parseColor(ANON_PRIMARY);
            mTeamID = 0;
        }

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(userColor));
//        actionBar.setTitle("Past Results");
        actionBar.setElevation(0f);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mSpinnerContainer = LayoutInflater.from(getActivity()).inflate(R.layout.toolbar_spinner,
                toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(mSpinnerContainer, lp);

        TeamSpinnerAdapter spinnerAdapter = new TeamSpinnerAdapter(getActivity());

        Spinner spinner = (Spinner) mSpinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);

        DrawerFrameLayout drawerFrameLayout = (DrawerFrameLayout) (getActivity().findViewById(R.id.drawer_layout));
        int color = userColor;
        color = darker(color, 0.7f);
        drawerFrameLayout.setStatusBarBackgroundColor(color);

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(2);

        mViewPager = (ViewPager) mLayout.findViewById(R.id.fragment_team_results_view_pager);

        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) mLayout.findViewById(R.id.fragment_team_results_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setBackgroundColor(userColor);

        setSlidingTabLayoutContentDescriptions();

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.accent_material_light));
        mSlidingTabLayout.setDistributeEvenly(false);
        mSlidingTabLayout.setEnablePadding(true);
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity());
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mAllTeams = true;
                    mTeamID = -1;
                    mViewPager.getAdapter().notifyDataSetChanged();
                } else {
                    mAllTeams = false;
                    Team team = (Team) parent.getItemAtPosition(position);
                    mTeamID = team.getTeamID();
                    mViewPager.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return mLayout;
    }

    @Override
    public  void onDestroyView(){
        DataStore.getInstance(getActivity()).unregisterAllLeagueDBObserver(mViewPagerAdapter);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(toolbar != null && mSpinnerContainer != null)
            toolbar.removeView(mSpinnerContainer);
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

    private void setSlidingTabLayoutContentDescriptions() {
        mSlidingTabLayout.setContentDescription(0, "Winter 2015");
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
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter implements DBObserver {

        private List<League> leagues = new ArrayList<>();
        private Context context;

        public ViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            leagues = DataStore.getInstance(context).getAllLeagues();
        }


        @Override
        public Fragment getItem(int position) {
            Log.d("Team Results", "Creating fragment");
            ResultsListFragment frag = ResultsListFragment.newInstance(leagues.get(position).getLeagueID(), mTeamID, mAllTeams);
            return frag;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return leagues.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return leagues.get(position).getLeagueName();
        }

        @Override
        public void notify(String tableName, Object data) {
            switch(tableName){
                case DBProviderContract.ALLLEAGUES_TABLE_NAME:
                    leagues = DataStore.getInstance(context).getAllLeagues();
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    public interface TeamResultsCallbacks {

        public void onTeamResultsItemSelected(int position);
    }

    private class TeamSpinnerAdapter  extends BaseAdapter implements DBObserver{
        private List<Team> teams = new ArrayList<>();
        private Context context;

        public TeamSpinnerAdapter(Context context) {
            this.context = context;
            teams = DataStore.getInstance(context).getAllTeams();
        }

        @Override
        public int getCount() {
            return teams.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return position > 0 && position <= teams.size() ? teams.get(position-1) : "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                view = getActivity().getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = getActivity().getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item_actionbar, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position > 0 && position <= teams.size() ? teams.get(position-1).getTeamName() : "All";
        }

        @Override
        public void notify(String tableName, Object data) {
            switch(tableName){
                case DBProviderContract.ALLTEAMS_TABLE_NAME:
                    teams = DataStore.getInstance(context).getAllTeams();
                    notifyDataSetChanged();
                    break;
            }
        }
    }

}
