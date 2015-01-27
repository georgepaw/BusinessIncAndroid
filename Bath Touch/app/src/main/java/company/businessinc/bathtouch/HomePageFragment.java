package company.businessinc.bathtouch;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.businessinc.bathtouch.ApdaterData.HomeCardData;
import company.businessinc.bathtouch.adapters.HomePageAdapter;
import company.businessinc.dataModels.User;


public class HomePageFragment extends Fragment {

    private HomePageCallbacks mCallbacks;
    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.alternative_home_page, container, false);

        Toolbar toolbar = (Toolbar) mLayout.findViewById(R.id.alt_toolbar_home_screen);
         ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.alt_home_page_cards_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    //the card clicked decides which fragment to start
                    case(R.id.home_page_card_team_container):
                        Log.d("fragment", "changing on id for team container");
                        selectCard(1);
                        break;
                    case(R.id.home_page_card_table):
                        Log.d("fragment", "changing on id for table container");
                        selectCard(2);
                        break;
                    case(R.id.home_page_card_next_match_container):
                        selectCard(0);
                        break;
                    default:
                        Log.d("FRAGMENT", "doing defalt");
                        selectCard(position);
                        break;
                }
                selectCard(position);
            }
        }));

        mAdapter = new HomePageAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //set the onclick events for the home cards


        return mLayout;
    }

    private void selectCard(int position) {
        if (mCallbacks != null) {
            mCallbacks.onHomePageCardSelected(position );
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (HomePageCallbacks) activity;
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

    public static interface HomePageCallbacks {

        void onHomePageCardSelected(int position);
    }
}
