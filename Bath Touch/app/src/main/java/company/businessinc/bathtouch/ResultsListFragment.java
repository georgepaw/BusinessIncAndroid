package company.businessinc.bathtouch;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import company.businessinc.bathtouch.ApdaterData.TeamResultsData;
import company.businessinc.bathtouch.adapters.TeamResultsAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link company.businessinc.bathtouch.ResultsListFragment.ResultsListCallbacks} interface
 * to handle interaction events.
 * Use the {@link ResultsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsListFragment extends Fragment {

    private View mLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeamResultsAdapter mAdapter;
    private ResultsListCallbacks mCallbacks;
    private Integer mLeagueID;


    public static ResultsListFragment newInstance(Integer leagueID) {
        ResultsListFragment fragment = new ResultsListFragment();
        Bundle args = new Bundle();
        args.putInt("leagueID", leagueID);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLeagueID = getArguments().getInt("leagueID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_results_list, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_results_recycle);

        mRecyclerView.setHasFixedSize(true);

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
            mCallbacks.onResultsItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (ResultsListCallbacks) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ResultsListCallbacks {
        public void onResultsItemSelected(int position);
    }

}
