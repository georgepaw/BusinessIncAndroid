package company.businessinc.bathtouch;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import company.businessinc.bathtouch.ApdaterData.HomeCardData;
import company.businessinc.bathtouch.adapters.HomePageAdapter;


public class HomePageActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home_screen);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mRecyclerView = (RecyclerView) findViewById(R.id.home_page_cards_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] myDataset = createDataset();

        HomeCardData mCardData = new HomeCardData();

        mAdapter = new HomePageAdapter(mCardData);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return false;

    }

    public String[] createDataset(){
        String[] dataset = {"HOME PAGE", "adam"};
        return dataset;
    }

    public void view_team_results(View v){
        Intent intent = new Intent(this, TeamResultsActivity.class);
        startActivity(intent);

    }

    public void view_league_table(View v){
        Intent intent = new Intent(this, LeagueTableActivity.class);
        startActivity(intent);
    }


}


// The data to show






