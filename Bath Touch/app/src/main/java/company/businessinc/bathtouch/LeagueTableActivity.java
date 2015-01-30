package company.businessinc.bathtouch;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.League;


/**
 * Created by user on 27/01/15.
 */


public class LeagueTableActivity extends FragmentActivity implements LeagueTableFragment.LeagueTableCallbacks, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
    private static List<League> leagueNames = new LinkedList<League>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leagues);

        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        //
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());

        // Set up action bar.

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        getSupportLoaderManager().initLoader(DBProviderContract.ALLLEAGUES_URL_QUERY, null, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onLeagueTableItemSelected(int position) {
        Log.d("LEAGUE", Integer.toString(position));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                return new CursorLoader(this, DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI, null, null, null, null);
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
                        leagueNames.add(new League(data));
                        data.moveToNext();
                    }
                    mDemoCollectionPagerAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new LeagueTableFragment();
            Bundle args = new Bundle();
            args.putInt("LEAGUEID", i + 1);
            //find the league with this ID
            for(League l : leagueNames){
                if(l.getLeagueID() == i+1){
                    args.putString("LEAGUENAME", l.getLeagueName());
                }
            }
            args.putInt(LeagueTableFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        //Number of leagues in system
        public int getCount() {
            return leagueNames.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "LEAGUE " + (position + 1);
        }
    }
}


