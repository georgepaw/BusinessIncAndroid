package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;

import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;

import java.util.List;


public class MyTeamFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ANON_PRIMARY = "#ff0000";
    private static final String ANON_SECONDARY = "#ffffff";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String[] headersLoggedIn = new String[]{"Activity","Today's Games", "Matches", "League"};
    private static final String[] headersAnon = new String[]{"Today's Games","Matches", "League"};

    private MyTeamFragmentCallbacks mListener;
    private View mLayout;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    public static MyTeamFragment newInstance() {
        MyTeamFragment fragment = new MyTeamFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_my_team, container, false);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        int primaryColour;
        int secondaryColour;
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
            actionBar.setTitle("My Team");
            primaryColour = DataStore.getInstance(getActivity()).getUserTeamColorPrimary();
            secondaryColour = DataStore.getInstance(getActivity()).getUserTeamColorSecondary();
        } else {
            actionBar.setTitle("Overview");
            primaryColour = Color.parseColor(ANON_PRIMARY);
            secondaryColour = Color.parseColor(ANON_SECONDARY);
        }
        actionBar.setBackgroundDrawable(new ColorDrawable(primaryColour));

        actionBar.setElevation(0f);

        DrawerFrameLayout drawerFrameLayout = (DrawerFrameLayout) (getActivity().findViewById(R.id.drawer_layout));
        int colour = darker(primaryColour, 0.7f);
        drawerFrameLayout.setStatusBarBackgroundColor(colour);

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(0);
        
        mViewPager = (ViewPager) mLayout.findViewById(R.id.fragment_my_team_view_pager);


        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) mLayout.findViewById(R.id.fragment_my_team_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setBackgroundColor(primaryColour);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(secondaryColour);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setEnablePadding(false);
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
        mViewPagerAdapter.notifyDataSetChanged();
        return mLayout;
    }

    public int darker (int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MyTeamFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter  implements DBObserver  {

        int mLeagueID = -1;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mLeagueID = getLeagueID();
            notifyDataSetChanged();
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            String switchString;
            if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                switchString = position < headersLoggedIn.length ? headersLoggedIn[position] : "null";
            } else {
                switchString = position < headersAnon.length ? headersAnon[position] : "null";
            }
            switch (switchString) {
                case "Activity":
                    return TeamOverviewFragment.newInstance(DataStore.getInstance(getActivity()).getUserTeamID(), mLeagueID);
                case "Today's Games":
                    return TodayFragment.newInstance(mLeagueID);
                case "Matches":
                    if(DataStore.getInstance(getActivity()).isUserLoggedIn())
                        return ResultsListFragment.newInstance(mLeagueID, DataStore.getInstance(getActivity()).getUserTeamID(), false);
                    else
                        return ResultsListFragment.newInstance(mLeagueID, -1, true);
                case "League":
                default:
                    return LeagueFragment.newInstance(mLeagueID);
            }

        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                return headersLoggedIn.length;
            } else{
                return headersAnon.length;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(DataStore.getInstance(getActivity()).isUserLoggedIn()){
                return position < headersLoggedIn.length ? headersLoggedIn[position] : "null";
            } else {
                return position < headersAnon.length ? headersAnon[position] : "null";
            }
        }

        @Override
        public void notify(String tableName, Object data) {
            switch(tableName){
                case DBProviderContract.LIVELEAGUE_TABLE_NAME:
                    mLeagueID = getLeagueID();
                    notifyDataSetChanged();
                    break;
            }
        }

        private int getLeagueID(){
            List<League> leagues = DataStore.getInstance(getActivity()).getAllLiveLeagues();
            if(leagues.size() > 0){
                return leagues.get(0).getLeagueID(); //TODO what's the point of live league if there is more than one?
            }
            return  -1;
        }
    }

    public interface MyTeamFragmentCallbacks {
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView(){
        //Un register to prevent memory leak
        DataStore.getInstance(getActivity()).unregisterLiveLeagueDBObserver(mViewPagerAdapter);
        super.onDestroyView();
    }

}
