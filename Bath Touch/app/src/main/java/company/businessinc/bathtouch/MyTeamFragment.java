package company.businessinc.bathtouch;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;

import company.businessinc.bathtouch.data.DataStore;


public class MyTeamFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mLeagueID;

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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_my_team, container, false);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(DataStore.getInstance(getActivity()).getUserTeamColorPrimary()));
        actionBar.setTitle("My Team");
        actionBar.setElevation(0f);

        DrawerFrameLayout drawerFrameLayout = (DrawerFrameLayout) (getActivity().findViewById(R.id.drawer_layout));
        int color = DataStore.getInstance(getActivity()).getUserTeamColorPrimary();
        color = darker(color, 0.7f);
        drawerFrameLayout.setStatusBarBackgroundColor(color);

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(0);
        
        mViewPager = (ViewPager) mLayout.findViewById(R.id.fragment_my_team_view_pager);

        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) mLayout.findViewById(R.id.fragment_my_team_sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setBackgroundColor(DataStore.getInstance(getActivity()).getUserTeamColorPrimary());

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(DataStore.getInstance(getActivity()).getUserTeamColorSecondary());
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

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
//                    HomePageFragment homePageFragment = HomePageFragment.newInstance();
//                    return homePageFragment;
                    TeamOverviewFragment teamOverviewFragment = TeamOverviewFragment
                            .newInstance(DataStore.getInstance(getActivity()).getUserTeamID(), mLeagueID);
                    return teamOverviewFragment;
                case 1:
                    ResultsListFragment resultsListFragment = ResultsListFragment.newInstance(mLeagueID);
                    return resultsListFragment;
                default:
                    LeagueFragment leagueFragment = LeagueFragment.newInstance(mLeagueID);
                    return leagueFragment;
            }

        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Activity";
                case 1:
                    return "Results";
                case 2:
                    return "League";
                default:
                    return "null";
            }
        }
    }

    public void setLeagueID(int leagueID) {
        mLeagueID = leagueID;
        if(mViewPager != null)
            mViewPager.getAdapter().notifyDataSetChanged();
    }

    public interface MyTeamFragmentCallbacks {
        public void onFragmentInteraction(Uri uri);
    }

}
