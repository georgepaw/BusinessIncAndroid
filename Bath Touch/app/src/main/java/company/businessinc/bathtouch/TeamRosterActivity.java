package company.businessinc.bathtouch;

import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

import company.businessinc.bathtouch.adapters.AvailablePlayersAdapter;


/**
 * Created by user on 29/01/15.
 * http://www.android4devs.com/2015/01/how-to-make-material-design-sliding-tabs.html
 */
public class TeamRosterActivity extends FragmentActivity implements ActionBar.TabListener,
        AvailablePlayersAdapter.AvailablePlayerCallbacks {

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;

    private ArrayList<Integer> playerListAvail = new ArrayList<Integer>();
    private ArrayList<Integer> playerListUnavail = new ArrayList<Integer>();
    private int NUM_PEOPLE = 10;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team_roster);

        playerListAvail = populatePeople();
        playerListUnavail = populatePeople();


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.team_roster_pager);
        mPagerAdapter = new SamplePagerAdapter(getSupportFragmentManager(), playerListAvail, playerListUnavail);
        mViewPager.setAdapter(mPagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        Display disp = getWindowManager().getDefaultDisplay();
        Point size = new Point();


    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onPlayerAvailableChecked(boolean available, int playerID) {

//        Log.d("CALLBACK", "fired");
//        //Move from available to unavailable
//        if(available){
//            playerListAvail.remove(playerID);
//            playerListUnavail.add(playerID);
//        }else{
//            playerListAvail.add(playerID);
//            playerListUnavail.remove(playerID);
//        }
//        mPagerAdapter.notifyDataSetChanged();
    }

    private ArrayList<Integer> populatePeople(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < NUM_PEOPLE; i++){
            list.add(i);
        }
        return list;
    }


    class SamplePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Integer> playerListAvail = new ArrayList<Integer>();
        private ArrayList<Integer> playerListUnavail = new ArrayList<Integer>();
        private ArrayList<ArrayList<Integer>> playerList = new ArrayList<ArrayList<Integer>>();

        public SamplePagerAdapter(FragmentManager fm, ArrayList<Integer> availP, ArrayList<Integer> unavailP) {
            super(fm);
            playerListAvail = availP;
            playerListUnavail = unavailP;
            playerList.add(playerListAvail);
            playerList.add(playerListUnavail);

        }

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }


        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Available Players";
            }
            else if(position == 1){
                return "Unavailable Players";
            }
            return "Item " + (position + 1);
        }

        @Override
        public Fragment getItem(int position) {

            return AvailablePlayersFragment.newInstance(position, playerList.get(position));
        }





    }


}
