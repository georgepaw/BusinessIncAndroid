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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import company.businessinc.bathtouch.ApdaterData.TeamResultsData;
import company.businessinc.bathtouch.adapters.TeamResultsAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.TeamResultsFragment.TeamResultsCallbacks} interface
 * to handle interaction events.
 * Use the {@link TeamResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamResultsFragment extends Fragment {


    private TeamResultsCallbacks mCallbacks;
    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public static TeamResultsFragment newInstance() {
        TeamResultsFragment fragment = new TeamResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TeamResultsFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_team_results, container, false);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Past Results");

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_results_recycle);

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
                        selectItem(position);
                    }
                }));

        TeamResultsData mTeamResultsData = new TeamResultsData();
        mAdapter = new TeamResultsAdapter(mTeamResultsData);
        mRecyclerView.setAdapter(mAdapter);

        return mLayout;
    }

    public void selectItem(int position) {
        if (mCallbacks != null) {
            mCallbacks.onTeamResultsItemSelected(position);
        }
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

    public interface TeamResultsCallbacks {

        public void onTeamResultsItemSelected(int position);
    }

}
