package company.businessinc.bathtouch;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import company.businessinc.bathtouch.adapters.LicenceAdapter;
import company.businessinc.bathtouch.adapters.TodaysGameAdapter;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Licence;
import company.businessinc.dataModels.Match;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class LicenceFragment extends Fragment {

    private static final String TAG = "LicenceFragment";
    public static final String ARG_OBJECT = "object";

    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String ANON_PRIMARY = "#ff0000";
    private static final String ANON_SECONDARY = "#ffffff";

    public static LicenceFragment newInstance() {
        return new LicenceFragment();
    }

    public LicenceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ANON_PRIMARY)));
        actionBar.setTitle("About");
        actionBar.setElevation(0f);

        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_licences, container, false);

        //set up the recycler view
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.fragment_licence_recycle);

//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LicenceAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        return mLayout;
    }
}
